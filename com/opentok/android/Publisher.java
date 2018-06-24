package com.opentok.android;

import android.content.Context;
import com.opentok.android.BaseVideoCapturer.CaptureSwitch;
import com.opentok.android.Camera2VideoCapturer.Camera2Exception;
import com.opentok.android.OpentokError.Domain;
import com.opentok.android.OpentokError.ErrorCode;
import com.opentok.android.OtLog.LogToken;
import com.opentok.impl.OpentokErrorImpl;

public class Publisher extends PublisherKit {
    private static final LogToken log = new LogToken();
    protected CameraCaptureFrameRate cameraFrameRate;
    protected CameraListener cameraListener;
    protected CameraCaptureResolution cameraResolution;

    class C09752 implements Runnable {
        C09752() {
        }

        public void run() {
            Publisher.this.onCameraError(new OpentokErrorImpl(Domain.PublisherErrorDomain, ErrorCode.CameraFailed.getErrorCode()));
        }
    }

    public static class Builder extends com.opentok.android.PublisherKit.Builder {
        CameraCaptureFrameRate frameRate = null;
        CameraCaptureResolution resolution = null;

        public Builder(Context context) {
            super(context);
        }

        public Builder resolution(CameraCaptureResolution resolution) {
            this.resolution = resolution;
            return this;
        }

        public Builder frameRate(CameraCaptureFrameRate frameRate) {
            this.frameRate = frameRate;
            return this;
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
            return (Builder) super.audioBitrate(bitsPerSecond);
        }

        public Publisher build() {
            return new Publisher(this.context, this.name, this.audioTrack, this.audioBitrate, this.videoTrack, this.capturer, this.resolution, this.frameRate, this.renderer);
        }
    }

    public enum CameraCaptureFrameRate {
        FPS_30(0),
        FPS_15(1),
        FPS_7(2),
        FPS_1(3);
        
        private int captureFramerate;

        private CameraCaptureFrameRate(int framerate) {
            this.captureFramerate = framerate;
        }

        int getCaptureFrameRate() {
            return this.captureFramerate;
        }

        static CameraCaptureFrameRate fromFramerate(int captureFramerateId) {
            for (CameraCaptureFrameRate fps : values()) {
                if (fps.getCaptureFrameRate() == captureFramerateId) {
                    return fps;
                }
            }
            throw new IllegalArgumentException("unknown capture framerate " + captureFramerateId);
        }

        static CameraCaptureFrameRate defaultFrameRate() {
            return FPS_30;
        }
    }

    public enum CameraCaptureResolution {
        LOW(0),
        MEDIUM(1),
        HIGH(2);
        
        private int captureResolution;

        private CameraCaptureResolution(int resolution) {
            this.captureResolution = resolution;
        }

        int getCaptureResolution() {
            return this.captureResolution;
        }

        static CameraCaptureResolution fromResolution(int captureResolutionId) {
            for (CameraCaptureResolution resolution : values()) {
                if (resolution.getCaptureResolution() == captureResolutionId) {
                    return resolution;
                }
            }
            throw new IllegalArgumentException("unknown capture resolution " + captureResolutionId);
        }

        static CameraCaptureResolution defaultResolution() {
            return MEDIUM;
        }
    }

    public interface CameraListener {
        void onCameraChanged(Publisher publisher, int i);

        void onCameraError(Publisher publisher, OpentokError opentokError);
    }

    private native int createOtkitPublisher();

    public void setCameraListener(CameraListener listener) {
        this.cameraListener = listener;
    }

    @Deprecated
    public Publisher(Context context) {
        this(context, null, true, 0, true, null, null, null, null);
    }

    @Deprecated
    public Publisher(Context context, String name) {
        this(context, name, true, 0, true, null, null, null, null);
    }

    @Deprecated
    public Publisher(Context context, String name, BaseVideoCapturer capturer) {
        this(context, name, true, 0, true, capturer, null, null, null);
    }

    @Deprecated
    public Publisher(Context context, String name, boolean audioTrack, boolean videoTrack) {
        this(context, name, audioTrack, 0, videoTrack, null, null, null, null);
    }

    @Deprecated
    public Publisher(Context context, String name, CameraCaptureResolution resolution, CameraCaptureFrameRate frameRate) {
        this(context, name, true, 0, true, null, resolution, frameRate, null);
    }

    @Deprecated
    protected Publisher(Context context, String name, boolean audioTrack, boolean videoTrack, BaseVideoCapturer capturer, CameraCaptureResolution resolution, CameraCaptureFrameRate frameRate, BaseVideoRenderer renderer) {
        this(context, name, audioTrack, 0, videoTrack, capturer, resolution, frameRate, renderer);
    }

    protected Publisher(Context context, String name, boolean audioTrack, int maxAudioBitrate, boolean videoTrack, BaseVideoCapturer capturer, CameraCaptureResolution resolution, CameraCaptureFrameRate frameRate, BaseVideoRenderer renderer) {
        BaseVideoCapturer baseVideoCapturer;
        BaseVideoRenderer baseVideoRenderer;
        if (capturer != null || context == null) {
            baseVideoCapturer = capturer;
        } else {
            if (resolution == null) {
                resolution = CameraCaptureResolution.defaultResolution();
            }
            if (frameRate == null) {
                frameRate = CameraCaptureFrameRate.defaultFrameRate();
            }
            baseVideoCapturer = VideoCaptureFactory.constructCapturer(context, resolution, frameRate);
        }
        if (renderer != null || context == null) {
            baseVideoRenderer = renderer;
        } else {
            baseVideoRenderer = VideoRenderFactory.constructRenderer(context);
        }
        super(context, name, audioTrack, maxAudioBitrate, videoTrack, baseVideoCapturer, baseVideoRenderer);
        AudioDeviceManager.initializeDefaultDevice(context);
    }

    protected void finalize() throws Throwable {
        log.m15d("Publisher finalizing", new Object[0]);
        super.finalize();
    }

    public void destroy() {
        super.destroy();
    }

    @Deprecated
    public void setCameraId(int cameraId) {
        log.m19i("Setting cameraId to %d", Integer.valueOf(cameraId));
        if (this.capturer == null) {
            log.m17e("Capturer is not yet initialized. Call startPreview() or publish into a session", new Object[0]);
            return;
        }
        this.capturer = getVideoCapturer();
        try {
            this.capturer.swapCamera(cameraId);
            onPublisherCameraPositionChanged(this, cameraId);
        } catch (ClassCastException e) {
            log.m17e("This capturer cannot change cameras since it does not implement BaseVideoCapturer.CaptureSwitch interface", new Object[0]);
        }
    }

    public void cycleCamera() {
        log.m19i("cycle camera", new Object[0]);
        if (this.capturer == null) {
            log.m17e("Capturer is not yet initialized. Call startPreview() or publish into a session", new Object[0]);
            return;
        }
        this.capturer = getVideoCapturer();
        try {
            CaptureSwitch captureSwitch = this.capturer;
            captureSwitch.cycleCamera();
            onPublisherCameraPositionChanged(this, captureSwitch.getCameraIndex());
        } catch (Camera2Exception e) {
            onCameraFailed();
        } catch (ClassCastException e2) {
            log.m17e("This capturer cannot change cameras since it does not implement BaseVideoCapturer.CaptureSwitch interface", new Object[0]);
        } catch (RuntimeException e3) {
            log.m17e(e3.getMessage(), new Object[0]);
            onCameraFailed();
        }
    }

    @Deprecated
    public void swapCamera() {
        log.m19i("swap camera", new Object[0]);
        if (this.capturer == null) {
            log.m17e("Capturer is not yet initialized. Call startPreview() or publish into a session", new Object[0]);
        } else {
            cycleCamera();
        }
    }

    @Deprecated
    public int getCameraId() {
        this.capturer = getVideoCapturer();
        try {
            return this.capturer.getCameraIndex();
        } catch (ClassCastException e) {
            return -1;
        }
    }

    void onPublisherCameraPositionChanged(Publisher publisher, final int newCameraId) {
        log.m19i("Publisher has changed the camera position to: %d", Integer.valueOf(newCameraId));
        this.handler.post(new Runnable() {
            public void run() {
                Publisher.this.onCameraChanged(newCameraId);
            }
        });
    }

    protected void onCameraChanged(int newCameraId) {
        if (this.cameraListener != null) {
            this.cameraListener.onCameraChanged(this, newCameraId);
        }
    }

    void onCameraFailed() {
        log.m19i("Camera device has failed ", new Object[0]);
        this.handler.post(new C09752());
    }

    protected void onCameraError(OpentokError error) {
        if (this.cameraListener != null) {
            this.cameraListener.onCameraError(this, error);
        }
    }

    private BaseVideoCapturer getVideoCapturer() {
        if (this.capturer != null) {
            return this.capturer;
        }
        BaseVideoCapturer cap = VideoCaptureFactory.constructCapturer(this.context, this.cameraResolution, this.cameraFrameRate);
        cap.setPublisherKit(this);
        if (!(cap instanceof DefaultVideoCapturer)) {
            return cap;
        }
        ((DefaultVideoCapturer) cap).setPublisher(this);
        return cap;
    }

    public void startPreview() {
        if (this.capturer == null) {
            this.capturer = VideoCaptureFactory.constructCapturer(this.context, CameraCaptureResolution.defaultResolution(), CameraCaptureFrameRate.defaultFrameRate());
            this.capturer.setPublisherKit(this);
            initCapturerNative(this.capturer);
            initRendererNative(this.renderer);
        }
        createOtkitPublisher();
    }
}
