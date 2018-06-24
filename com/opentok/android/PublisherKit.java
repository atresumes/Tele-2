package com.opentok.android;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import com.opentok.android.OpentokError.Domain;
import com.opentok.android.OtLog.LogToken;
import com.opentok.android.Stream.StreamVideoType;
import com.opentok.impl.ConnectionImpl;
import com.opentok.impl.OpentokErrorImpl;
import com.opentok.impl.StreamImpl;
import java.nio.ByteBuffer;
import java.util.Date;

public class PublisherKit {
    private static final LogToken log = new LogToken();
    private boolean audioFallbackEnabled;
    protected AudioLevelListener audioLevelListener;
    private boolean audioTrack;
    protected BaseVideoCapturer capturer;
    protected Context context;
    protected Handler handler;
    private String name;
    private long nativeInstanceId;
    private boolean publishAudio;
    private boolean publishVideo;
    protected PublisherListener publisherListener;
    private PublisherKitVideoType publisherVideoType;
    protected BaseVideoRenderer renderer;
    protected Session session;
    private Stream stream;
    private boolean videoTrack;
    private boolean videoTrackStateCache;

    public static class Builder {
        int audioBitrate;
        boolean audioTrack = true;
        BaseVideoCapturer capturer = null;
        Context context;
        String name;
        BaseVideoRenderer renderer = null;
        boolean videoTrack = true;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder audioTrack(boolean enabled) {
            this.audioTrack = enabled;
            return this;
        }

        public Builder videoTrack(boolean enabled) {
            this.videoTrack = enabled;
            return this;
        }

        public Builder capturer(BaseVideoCapturer capturer) {
            this.capturer = capturer;
            return this;
        }

        public Builder renderer(BaseVideoRenderer renderer) {
            this.renderer = renderer;
            return this;
        }

        public Builder audioBitrate(int bitsPerSecond) {
            this.audioBitrate = bitsPerSecond;
            return this;
        }

        public PublisherKit build() {
            return new PublisherKit(this.context, this.name, this.audioTrack, this.audioBitrate, this.videoTrack, this.capturer, this.renderer);
        }
    }

    public interface AudioLevelListener {
        void onAudioLevelUpdated(PublisherKit publisherKit, float f);
    }

    public enum PublisherKitVideoType {
        PublisherKitVideoTypeCamera(1),
        PublisherKitVideoTypeScreen(2);
        
        private int videoType;

        private PublisherKitVideoType(int type) {
            this.videoType = type;
        }

        public int getVideoType() {
            return this.videoType;
        }

        static PublisherKitVideoType fromType(int typeId) {
            for (PublisherKitVideoType type : values()) {
                if (type.getVideoType() == typeId) {
                    return type;
                }
            }
            throw new IllegalArgumentException("unknown type " + typeId);
        }
    }

    public interface PublisherListener {
        void onError(PublisherKit publisherKit, OpentokError opentokError);

        void onStreamCreated(PublisherKit publisherKit, Stream stream);

        void onStreamDestroyed(PublisherKit publisherKit, Stream stream);
    }

    private native int destroyPublisherKitNative();

    private native void finalizeNative();

    private native boolean getAudioFallbackEnabledNative();

    private native int getCameraIdNative();

    private native String getNameNative();

    private native boolean getPublishAudioNative();

    private native boolean getPublishVideoNative();

    private native Session getSessionNative();

    private native String getStreamIdNative();

    private native Stream getStreamNative();

    private native int getVideoTypeNative();

    private native boolean init(Context context);

    private native int setAudioFallbackEnabledNative(boolean z);

    private native void setAudioLevelListenerNative(AudioLevelListener audioLevelListener);

    private native void setAudioOnlyImageNative(boolean z);

    private native void setCameraRotation(int i);

    private native void setPublishAudioNative(boolean z);

    private native void setPublishVideoNative(boolean z);

    private native void setPublisherKitAudioOnlyNative(int i, int i2, ByteBuffer byteBuffer);

    private native void setPublisherMaxAudioBitrateKbpsNative(int i);

    private native void setVideoTypeNative(int i);

    protected native int initCapturerNative(BaseVideoCapturer baseVideoCapturer);

    protected native int initRendererNative(BaseVideoRenderer baseVideoRenderer);

    static {
        System.loadLibrary("opentok");
    }

    protected void attachToSession(Session session) {
        this.session = session;
    }

    protected void detachFromSession(Session session) {
        this.session = null;
    }

    @Deprecated
    protected PublisherKit(Context context, String name, boolean audioTrack, boolean videoTrack, BaseVideoCapturer capturer, BaseVideoRenderer renderer) {
        this(context, name, audioTrack, 0, videoTrack, capturer, renderer);
    }

    protected PublisherKit(Context context, String name, boolean audioTrack, int maxAudioBitRate, boolean videoTrack, BaseVideoCapturer capturer, BaseVideoRenderer renderer) {
        this.publishVideo = true;
        this.publishAudio = true;
        this.audioTrack = true;
        this.videoTrack = true;
        this.videoTrackStateCache = true;
        this.nativeInstanceId = 0;
        this.audioFallbackEnabled = true;
        if (context == null) {
            throw new IllegalArgumentException("(Context context) is null, cannot create publisher object!");
        }
        this.audioTrack = audioTrack;
        this.videoTrack = videoTrack;
        this.context = context;
        this.name = name;
        this.publisherVideoType = PublisherKitVideoType.PublisherKitVideoTypeCamera;
        this.handler = new Handler(context.getMainLooper());
        if (init(context)) {
            setCapturerRenderer(capturer, renderer);
            BaseAudioDevice.addPublisher(this);
            if (maxAudioBitRate > 0) {
                setPublisherMaxAudioBitrateKbpsNative(maxAudioBitRate / 1000);
                return;
            }
            return;
        }
        throw new RuntimeException("Error intializing Publisher object!");
    }

    @Deprecated
    public PublisherKit(Context context, String name, boolean audioTrack, boolean videoTrack) {
        this(context, name, audioTrack, 0, videoTrack, null, null);
    }

    @Deprecated
    public PublisherKit(Context context) {
        this(context, null, true, 0, true, null, null);
    }

    @Deprecated
    public PublisherKit(Context context, String name) {
        this(context, name, true, 0, true, null, null);
    }

    public void onPause() {
        this.videoTrackStateCache = getPublishVideo();
        setPublishVideo(false);
        if (getRenderer() != null) {
            getRenderer().onPause();
        }
        if (getCapturer() != null) {
            getCapturer().onPause();
        }
    }

    public void onResume() {
        if (getRenderer() != null) {
            getRenderer().onResume();
        }
        if (getCapturer() != null) {
            getCapturer().onResume();
        }
        setPublishVideo(this.videoTrackStateCache);
    }

    public void destroy() {
        log.m19i("Destroying publisher", new Object[0]);
        int retCode = destroyPublisherKitNative();
        if (this.session != null) {
            this.session.safeRemovePublisher(this);
        }
        if (retCode > 0) {
            throwError(new OpentokErrorImpl(Domain.PublisherErrorDomain, retCode));
        }
    }

    protected void finalize() throws Throwable {
        finalizeNative();
        super.finalize();
    }

    public void setPublishVideo(boolean publishVideo) {
        log.m19i("Setting publisher video property to %b", Boolean.valueOf(publishVideo));
        this.publishVideo = publishVideo;
        setPublishVideoNative(publishVideo);
        if (this.renderer != null) {
            this.renderer.onVideoPropertiesChanged(publishVideo);
        }
    }

    public void setPublishAudio(boolean publishAudio) {
        log.m19i("Setting publisher audio property to %b", Boolean.valueOf(publishAudio));
        this.publishAudio = publishAudio;
        setPublishAudioNative(publishAudio);
    }

    public void setName(String name) {
        log.m19i("Setting name: %s for publisher", name);
        this.name = name;
    }

    public void setPublisherVideoType(PublisherKitVideoType type) {
        log.m19i("Setting videoType: %d for publisher", Integer.valueOf(type.videoType));
        this.publisherVideoType = type;
        setVideoTypeNative(type.getVideoType());
    }

    public String getName() {
        return this.name;
    }

    public boolean getPublishVideo() {
        return getPublishVideoNative();
    }

    public boolean getPublishAudio() {
        return getPublishAudioNative();
    }

    public Session getSession() {
        return this.session;
    }

    public Stream getStream() {
        return this.stream;
    }

    public PublisherKitVideoType getPublisherVideoType() {
        return PublisherKitVideoType.fromType(getVideoTypeNative());
    }

    public void setPublisherListener(PublisherListener listener) {
        this.publisherListener = listener;
    }

    public void setAudioLevelListener(AudioLevelListener listener) {
        this.audioLevelListener = listener;
        setAudioLevelListenerNative(listener);
    }

    public void setStyle(String key, String value) {
        if (getRenderer() != null) {
            getRenderer().setStyle(key, value);
        }
    }

    private void setCapturerRenderer(BaseVideoCapturer capturer, BaseVideoRenderer renderer) {
        if (this.capturer == null || !this.capturer.isCaptureStarted()) {
            this.capturer = capturer;
            if (capturer != null) {
                capturer.setPublisherKit(this);
                initCapturerNative(capturer);
            }
        } else {
            log.m17e("You cannot call this method after starting the capturer", new Object[0]);
        }
        this.renderer = renderer;
        if (renderer != null) {
            initRendererNative(renderer);
        }
    }

    @Deprecated
    public void setRenderer(BaseVideoRenderer renderer) {
        if (this.renderer != renderer) {
            this.renderer = renderer;
            initRendererNative(this.renderer);
        }
    }

    public BaseVideoRenderer getRenderer() {
        return this.renderer;
    }

    public BaseVideoCapturer getCapturer() {
        return this.capturer;
    }

    @Deprecated
    public void setCapturer(BaseVideoCapturer newCapturer) {
        boolean needToInitializeCapturerNative = false;
        if (this.capturer == null || newCapturer != this.capturer) {
            needToInitializeCapturerNative = true;
        }
        if (this.capturer.isCaptureStarted()) {
            log.m17e("The capturer cannot be changed after publishing or starting a preview", new Object[0]);
            return;
        }
        this.capturer = newCapturer;
        if (needToInitializeCapturerNative) {
            this.capturer.setPublisherKit(this);
            initCapturerNative(this.capturer);
        }
    }

    private void setNativeInstanceId(long id) {
        this.nativeInstanceId = id;
    }

    private long getNativeInstanceId() {
        return this.nativeInstanceId;
    }

    public View getView() {
        if (getRenderer() != null) {
            return getRenderer().getView();
        }
        return null;
    }

    void publisherStreamCreated(PublisherKit publisher, long pstream, String streamId, String streamName, long streamCreationTime, int videoWidth, int videoHeight, boolean hasAudio, boolean hasVideo, String connectionId, long creationTimeConnection, String connectionData, int videoType) {
        log.m19i("Publisher with streamId: %s is created", streamId);
        long j = pstream;
        String str = streamId;
        String str2 = streamName;
        int i = videoWidth;
        int i2 = videoHeight;
        boolean z = hasAudio;
        boolean z2 = hasVideo;
        this.stream = new StreamImpl(j, str, str2, new Date(streamCreationTime), i, i2, z, z2, new ConnectionImpl(connectionId, creationTimeConnection, connectionData), getSession(), videoType);
        getSession().addStream(this.stream);
        final Stream str3 = this.stream;
        this.handler.post(new Runnable() {
            public void run() {
                PublisherKit.this.onStreamCreated(str3);
            }
        });
    }

    protected void onStreamCreated(Stream stream) {
        if (this.publisherListener != null) {
            this.publisherListener.onStreamCreated(this, stream);
        }
    }

    void publisherStreamDestroyed(PublisherKit publisher, long pstream, String streamId, String streamName, int videoWidth, int videoHeight, boolean hasAudio, boolean hasVideo, int videoType) {
        log.m19i("Publisher with streamId: %s is removed", streamId);
        if (this.stream != null) {
            this.stream.name = streamName;
            this.stream.videoWidth = videoWidth;
            this.stream.videoHeight = videoHeight;
            this.stream.hasAudio = hasAudio;
            this.stream.hasVideo = hasVideo;
            this.stream.videoType = StreamVideoType.fromType(videoType);
            final Stream str = this.stream;
            this.handler.post(new Runnable() {
                public void run() {
                    PublisherKit.this.onStreamDestroyed(str);
                }
            });
            getSession().removeStream(this.stream);
        }
        this.stream = null;
    }

    protected void onStreamDestroyed(Stream stream) {
        if (this.publisherListener != null) {
            this.publisherListener.onStreamDestroyed(this, stream);
        }
    }

    void publisherAudioStats(PublisherKit publisher, final float audioLevel) {
        this.handler.post(new Runnable() {
            public void run() {
                PublisherKit.this.onAudioLevelUpdated(audioLevel);
            }
        });
    }

    void onAudioLevelUpdated(float audioLevel) {
        if (this.audioLevelListener != null) {
            this.audioLevelListener.onAudioLevelUpdated(this, audioLevel);
        }
    }

    void error(PublisherKit publisher, int errorCode, String msg) {
        throwError(new OpentokError(Domain.PublisherErrorDomain, errorCode, msg));
    }

    void throwError(final OpentokError error) {
        this.handler.post(new Runnable() {
            public void run() {
                PublisherKit.this.onError(error);
            }
        });
    }

    protected void onError(OpentokError error) {
        if (this.publisherListener != null) {
            this.publisherListener.onError(this, error);
        }
    }

    public void setAudioFallbackEnabled(boolean enabled) {
        this.audioFallbackEnabled = enabled;
        int retCode = setAudioFallbackEnabledNative(enabled);
        if (retCode > 0 && retCode > 0) {
            throwError(new OpentokErrorImpl(Domain.PublisherErrorDomain, retCode));
        }
    }

    public boolean getAudioFallbackEnabled() {
        return getAudioFallbackEnabledNative();
    }
}
