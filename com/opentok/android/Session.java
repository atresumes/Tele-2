package com.opentok.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaCodecInfo;
import android.media.MediaCodecInfo.CodecCapabilities;
import android.media.MediaCodecList;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Handler;
import android.view.OrientationEventListener;
import com.opentok.android.OpentokError.Domain;
import com.opentok.android.OpentokError.ErrorCode;
import com.opentok.android.OtLog.LogToken;
import com.opentok.android.Stream.StreamVideoType;
import com.opentok.client.DeviceInfo;
import com.opentok.impl.ConnectionImpl;
import com.opentok.impl.OpentokErrorImpl;
import com.opentok.impl.StreamImpl;
import com.opentok.jni.ProxyDetector;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import org.json.JSONArray;
import org.json.JSONObject;

public class Session {
    private static final int AUDIO_TYPE = 1;
    private static final int DEVICE_MODEL_GENERIC = 0;
    private static final int DEVICE_MODEL_NEXUS5 = 1;
    private static final String INTENT_ACTION = (Session.class.getPackage().getName() + ".log.event");
    private static final int VIDEO_TYPE = 0;
    private static final LogToken log = new LogToken();
    protected Set<PublisherKit> activePublishers;
    protected Set<Stream> activeStreams;
    protected ConcurrentHashMap<Stream, SubscriberKit> activeSubscribers;
    protected String apiKey;
    protected ArchiveListener archiveListener;
    protected Connection connection;
    protected boolean connectionEventsSuppressed;
    protected ConnectionListener connectionListener;
    private long constructorTime;
    private Context context;
    Handler handler;
    private boolean isLoggingReceiverRegistered;
    private LoggingDetails loggingDetails;
    private BroadcastReceiver loggingReceiver;
    private long nativeInstanceId;
    private OrientationEventListener orientationListener;
    protected ReconnectionListener reconnectionListener;
    protected String sessionId;
    protected SessionListener sessionListener;
    private boolean shouldRegisterLoggingReceiver;
    protected SignalListener signalListener;
    protected StreamPropertiesListener streamPropertiesListener;

    public static abstract class SessionOptions {
        private final Map<String, Boolean> cam2EnableList = new C09901();

        class C09901 extends HashMap<String, Boolean> {
            C09901() {
                put("nexus 4", Boolean.valueOf(true));
                put("nexus 5", Boolean.valueOf(true));
                put("nexus 5x", Boolean.valueOf(true));
                put("nexus 6", Boolean.valueOf(true));
                put("nexus 6p", Boolean.valueOf(true));
                put("nexus 7", Boolean.valueOf(true));
                put("nexus 10", Boolean.valueOf(true));
                put("pixel", Boolean.valueOf(true));
                put("gt-i9300", Boolean.valueOf(true));
                put("samsung-sm-g925a", Boolean.valueOf(true));
                put("samsung-sm-g935a", Boolean.valueOf(true));
                put("samsung-sm-t817a", Boolean.valueOf(true));
                put("sm-g900h", Boolean.valueOf(true));
                put("lgus991", Boolean.valueOf(true));
                put("lg-h810", Boolean.valueOf(true));
                put("xt1058", Boolean.valueOf(true));
                put("aquaris e5", Boolean.valueOf(true));
                put("c6602", Boolean.valueOf(true));
            }
        }

        @Deprecated
        public boolean isHwDecodingSupported() {
            return false;
        }

        public boolean isCamera2Capable() {
            return this.cam2EnableList.containsKey(Build.MODEL.toLowerCase());
        }

        public boolean useTextureViews() {
            return false;
        }
    }

    class C09801 extends SessionOptions {
        C09801() {
        }
    }

    class C09812 implements Runnable {
        C09812() {
        }

        public void run() {
            if (VERSION.SDK_INT >= 19) {
                try {
                    JSONObject logMsg = new JSONObject();
                    JSONArray encoderMsg = new JSONArray();
                    JSONArray decoderMSg = new JSONArray();
                    logMsg.put("encoders", encoderMsg);
                    logMsg.put("decoders", decoderMSg);
                    for (int i = 0; i < MediaCodecList.getCodecCount(); i++) {
                        MediaCodecInfo info = MediaCodecList.getCodecInfoAt(i);
                        CodecCapabilities capabilities;
                        JSONObject codecInfoMsg;
                        if (info != null && info.isEncoder()) {
                            for (String mimeType : info.getSupportedTypes()) {
                                if (mimeType.equals("video/avc")) {
                                    capabilities = info.getCapabilitiesForType(mimeType);
                                    codecInfoMsg = new JSONObject();
                                    codecInfoMsg.put("codec", info.getName());
                                    codecInfoMsg.put("color_format", Arrays.toString(capabilities.colorFormats));
                                    encoderMsg.put(codecInfoMsg);
                                }
                            }
                        } else if (info != null) {
                            for (String mimeType2 : info.getSupportedTypes()) {
                                if (mimeType2.equals("video/avc")) {
                                    capabilities = info.getCapabilitiesForType(mimeType2);
                                    codecInfoMsg = new JSONObject();
                                    codecInfoMsg.put("codec", info.getName());
                                    codecInfoMsg.put("color_format", Arrays.toString(capabilities.colorFormats));
                                    decoderMSg.put(codecInfoMsg);
                                }
                            }
                        }
                    }
                    Session.this.logCustomMsg("codec-avail", logMsg.toString());
                    Session.log.m19i(logMsg.toString(), new Object[0]);
                } catch (Exception e) {
                    Session.log.m23w("Failed to analyze codec list: " + e.getMessage(), new Object[0]);
                    e.printStackTrace();
                }
            }
        }
    }

    class C09823 implements Runnable {
        C09823() {
        }

        public void run() {
            Session.this.onConnected();
        }
    }

    class C09834 implements Runnable {
        C09834() {
        }

        public void run() {
            for (SubscriberKit s : Session.this.activeSubscribers.values()) {
                s.destroy();
            }
            Session.this.activeSubscribers.clear();
            Session.this.activePublishers.clear();
        }
    }

    class C09845 implements Runnable {
        C09845() {
        }

        public void run() {
            for (SubscriberKit s : Session.this.activeSubscribers.values()) {
                s.destroy();
            }
            for (PublisherKit p : Session.this.activePublishers) {
                Session.this.unpublish(p);
            }
            Session.this.activeSubscribers.clear();
            Session.this.activePublishers.clear();
            Session.this.onDisconnected();
        }
    }

    class C09856 implements Runnable {
        C09856() {
        }

        public void run() {
            Session.this.onReconnecting();
        }
    }

    class C09867 implements Runnable {
        C09867() {
        }

        public void run() {
            Session.this.onReconnected();
        }
    }

    public interface ArchiveListener {
        void onArchiveStarted(Session session, String str, String str2);

        void onArchiveStopped(Session session, String str);
    }

    public static class Builder {
        String apiKey;
        boolean connectionEventsSuppressed = false;
        Context context;
        String sessionId;
        SessionOptions sessionOptions = new C09891();

        class C09891 extends SessionOptions {
            C09891() {
            }
        }

        public Builder(Context context, String apiKey, String sessionId) {
            this.context = context;
            this.apiKey = apiKey;
            this.sessionId = sessionId;
        }

        public Builder connectionEventsSuppressed(Boolean enabled) {
            this.connectionEventsSuppressed = enabled.booleanValue();
            return this;
        }

        public Builder sessionOptions(SessionOptions sessionOptions) {
            this.sessionOptions = sessionOptions;
            return this;
        }

        public Session build() {
            return new Session(this.context, this.apiKey, this.sessionId, this.connectionEventsSuppressed, this.sessionOptions);
        }
    }

    public static class Capabilities {
        public boolean canPublish;
        public boolean canSubscribe;

        public String toString() {
            StringBuilder str = new StringBuilder("[\n");
            for (Field f : getClass().getFields()) {
                try {
                    str.append(String.format("\t%s = %b\n", new Object[]{f.getName(), f.get(this)}));
                } catch (IllegalAccessException e) {
                    Session.log.m17e("Error converting Capabilities to String", new Object[0]);
                }
            }
            str.append(']');
            return str.toString();
        }
    }

    protected static class ConfigurableSessionOptions extends SessionOptions {
        private boolean hwDecCapable = false;

        ConfigurableSessionOptions(boolean hwDecCapable) {
            this.hwDecCapable = hwDecCapable;
        }

        public boolean isHwDecodingSupported() {
            return this.hwDecCapable;
        }
    }

    public interface ConnectionListener {
        void onConnectionCreated(Session session, Connection connection);

        void onConnectionDestroyed(Session session, Connection connection);
    }

    private class LoggingDetails {
        String appId;
        String appVersion;
        String carrierName;
        String deviceModel;
        String deviceUuid;
        String libOpentokVersion;
        String networkStatus;
        String systemName;
        String systemVersion;

        private LoggingDetails() {
        }
    }

    public interface ReconnectionListener {
        void onReconnected(Session session);

        void onReconnecting(Session session);
    }

    public interface SessionListener {
        void onConnected(Session session);

        void onDisconnected(Session session);

        void onError(Session session, OpentokError opentokError);

        void onStreamDropped(Session session, Stream stream);

        void onStreamReceived(Session session, Stream stream);
    }

    @Deprecated
    public interface SessionOptionsProvider {
        @Deprecated
        boolean isHwDecodingSupported();
    }

    public interface SignalListener {
        void onSignalReceived(Session session, String str, String str2, Connection connection);
    }

    public interface StreamPropertiesListener {
        void onStreamHasAudioChanged(Session session, Stream stream, boolean z);

        void onStreamHasVideoChanged(Session session, Stream stream, boolean z);

        void onStreamVideoDimensionsChanged(Session session, Stream stream, int i, int i2);

        void onStreamVideoTypeChanged(Session session, Stream stream, StreamVideoType streamVideoType);
    }

    private native int connectSessionNative(String str, String str2, String str3);

    private native int connectionCountNative();

    private native int disconnectNative();

    private native void finalizeNative();

    private native int init(String str, Context context, String str2, String str3, int i, int i2);

    private native int initSessionNative();

    private native void logAdHocActionNative(String str);

    private native void logCustomNative(String str, String str2);

    private native int nativeGetCapabilities(Capabilities capabilities);

    private native String nativeReportIssue();

    private native int nativeSendSignal(String str, String str2, String str3, boolean z);

    private native int publishNative(PublisherKit publisherKit, BaseVideoCapturer baseVideoCapturer, BaseVideoRenderer baseVideoRenderer);

    private native void reportDriverUsage(int i);

    private native int subscribeNative(SubscriberKit subscriberKit, long j, BaseVideoRenderer baseVideoRenderer);

    private native int unpublishNative(PublisherKit publisherKit);

    private native int unsubscribeNative(SubscriberKit subscriberKit);

    static {
        System.loadLibrary("opentok");
    }

    @Deprecated
    public Session(Context context, String apiKey, String sessionId) {
        this(context, apiKey, sessionId, new C09801());
    }

    @Deprecated
    public Session(Context context, String apiKey, String sessionId, SessionOptionsProvider optionsProvider) {
        this(context, apiKey, sessionId, new ConfigurableSessionOptions(optionsProvider.isHwDecodingSupported()));
    }

    @Deprecated
    public Session(Context context, String apiKey, String sessionId, SessionOptions sessionOptions) {
        this(context, apiKey, sessionId, false, sessionOptions);
    }

    protected Session(Context context, String apiKey, String sessionId, boolean connectionEventsSuppressed, SessionOptions sessionOptions) {
        boolean z = false;
        this.shouldRegisterLoggingReceiver = false;
        this.isLoggingReceiverRegistered = false;
        this.nativeInstanceId = -1;
        this.loggingReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                if (intent.getExtras() != null && intent.hasExtra("event")) {
                    String logParam = intent.getExtras().getString("event");
                    if (logParam != null) {
                        Session.this.logAdHocAction(logParam);
                    }
                }
            }
        };
        this.context = context;
        this.constructorTime = System.currentTimeMillis();
        this.sessionId = sessionId;
        this.apiKey = apiKey;
        this.connectionEventsSuppressed = connectionEventsSuppressed;
        this.activePublishers = new CopyOnWriteArraySet();
        this.activeSubscribers = new ConcurrentHashMap();
        this.activeStreams = new CopyOnWriteArraySet();
        this.loggingDetails = new LoggingDetails();
        DeviceInfo deviceInfo = new DeviceInfo(context);
        this.loggingDetails.appId = deviceInfo.getApplicationIdentifier();
        this.loggingDetails.appVersion = deviceInfo.getApplicationVersion();
        this.loggingDetails.systemVersion = deviceInfo.getSystemVersion();
        this.loggingDetails.systemName = deviceInfo.getSystemName();
        this.loggingDetails.deviceModel = deviceInfo.getDeviceModel();
        this.loggingDetails.libOpentokVersion = deviceInfo.getSdkVersion();
        this.loggingDetails.networkStatus = deviceInfo.getNetworkStatus();
        this.loggingDetails.carrierName = deviceInfo.getCarrierName();
        this.loggingDetails.deviceUuid = deviceInfo.getOpenTokDeviceIdentifier();
        if (VERSION.SDK_INT >= 21 && sessionOptions.isCamera2Capable()) {
            z = true;
        }
        VideoCaptureFactory.enableCamera2api(z);
        VideoRenderFactory.useTextureViews(sessionOptions.useTextureViews());
        this.handler = new Handler(context.getMainLooper());
        AudioDeviceManager.initializeDefaultDevice(this.context);
        ProxyDetector.registerProxyDetector(this.context);
        int model = 0;
        int apiLevel = VERSION.SDK_INT;
        if (Build.MODEL.equals("Nexus 5")) {
            model = 1;
        }
        init(context.getCacheDir().getAbsolutePath() + "/.ca-cert.pem", context, apiKey, sessionId, model, apiLevel);
    }

    public Connection getConnection() {
        return this.connection;
    }

    private int getConnectionCount() {
        return connectionCountNative();
    }

    public String getSessionId() {
        return this.sessionId;
    }

    public void setSessionListener(SessionListener listener) {
        this.sessionListener = listener;
    }

    public void setConnectionListener(ConnectionListener listener) {
        this.connectionListener = listener;
    }

    public void setStreamPropertiesListener(StreamPropertiesListener listener) {
        this.streamPropertiesListener = listener;
    }

    public void setSignalListener(SignalListener listener) {
        this.signalListener = listener;
    }

    public void setArchiveListener(ArchiveListener listener) {
        this.archiveListener = listener;
    }

    public void setReconnectionListener(ReconnectionListener listener) {
        this.reconnectionListener = listener;
    }

    public void connect(String token) {
        log.m19i("Connecting to the session. SessionID: %s Token: %s ApiKey: %s", this.sessionId, token, this.apiKey);
        if (this.sessionId == null || this.sessionId.isEmpty()) {
            throwError(this, new OpentokErrorImpl(Domain.SessionErrorDomain, ErrorCode.InvalidSessionId.getErrorCode()));
            return;
        }
        int retCode = connectSessionNative(this.apiKey, this.sessionId, token);
        log.m19i("connectSessionNative returned %d", Integer.valueOf(retCode));
        if (retCode > 0) {
            throwError(this, new OpentokErrorImpl(Domain.SessionErrorDomain, retCode));
        }
        if (!(AudioDeviceManager.defaultAudioDevice instanceof DefaultAudioDevice)) {
            reportDriverUsage(1);
        }
    }

    public void disconnect() {
        log.m19i("Disconnecting to the session", new Object[0]);
        for (SubscriberKit s : this.activeSubscribers.values()) {
            log.m19i("Unsubcribing the active subscribers", new Object[0]);
            unsubscribe(s);
        }
        for (PublisherKit p : this.activePublishers) {
            log.m19i("Unpublishing the active publisher", new Object[0]);
            unpublish(p);
        }
        this.activePublishers.clear();
        this.activeSubscribers.clear();
        int retCode = disconnectNative();
        if (retCode > 0) {
            throwError(this, new OpentokErrorImpl(Domain.SessionErrorDomain, retCode));
        }
    }

    public void onPause() {
        log.m19i("Session - onPause", new Object[0]);
        for (PublisherKit p : this.activePublishers) {
            Publisher publisher = (Publisher) p;
            p.onPause();
        }
        for (SubscriberKit s : this.activeSubscribers.values()) {
            ((Subscriber) s).getRenderer().onPause();
        }
        if (AudioDeviceManager.getAudioDevice() != null) {
            AudioDeviceManager.getAudioDevice().onPause();
        }
        unregisterLoggingEventsReceiver();
    }

    public void onResume() {
        log.m19i("Session - onResume", new Object[0]);
        for (PublisherKit p : this.activePublishers) {
            Publisher publisher = (Publisher) p;
            p.onResume();
        }
        for (SubscriberKit s : this.activeSubscribers.values()) {
            ((Subscriber) s).getRenderer().onResume();
        }
        if (AudioDeviceManager.getAudioDevice() != null) {
            AudioDeviceManager.getAudioDevice().onResume();
        }
        registerLoggingEventsReceiver();
    }

    public void publish(PublisherKit publisher) {
        log.m19i("Starting a Publisher streaming to the session", new Object[0]);
        if (publisher != null) {
            if (publisher.getCapturer() == null) {
                publisher.setCapturer(VideoCaptureFactory.constructCapturer(this.context));
            }
            if (publisher.getCapturer() instanceof DefaultVideoCapturer) {
                ((DefaultVideoCapturer) publisher.getCapturer()).setPublisher((Publisher) publisher);
            }
            if (this.activePublishers.contains(publisher) || publisher.getSession() != null) {
                throwError(this, new OpentokErrorImpl(Domain.PublisherErrorDomain, ErrorCode.SessionNullOrInvalidParameter.getErrorCode()));
                return;
            }
            int retCode = publishNative(publisher, publisher.getCapturer(), publisher.getRenderer());
            if (retCode > 0) {
                publisher.destroy();
                publisher.detachFromSession(this);
                publisher.session = null;
                publisher.throwError(new OpentokErrorImpl(Domain.PublisherErrorDomain, retCode));
            } else {
                this.activePublishers.add(publisher);
                publisher.attachToSession(this);
            }
            if (!(publisher.getCapturer() instanceof DefaultVideoCapturer) || !(publisher.getRenderer() instanceof DefaultVideoRenderer)) {
                reportDriverUsage(0);
            }
        }
    }

    public void unpublish(PublisherKit publisher) {
        log.m19i("Disconnecting the Publisher from the session", new Object[0]);
        if (this.activePublishers.remove(publisher)) {
            int retCode = unpublishNative(publisher);
            if (retCode > 0) {
                throwError(this, new OpentokErrorImpl(Domain.SessionErrorDomain, retCode));
                return;
            } else {
                publisher.detachFromSession(this);
                return;
            }
        }
        throwError(this, new OpentokErrorImpl(Domain.SessionErrorDomain, ErrorCode.UnknownPublisherInstance.getErrorCode()));
    }

    void safeRemovePublisher(PublisherKit publisher) {
        if (this.activePublishers.remove(publisher)) {
            publisher.detachFromSession(this);
        }
    }

    public void subscribe(SubscriberKit subscriber) {
        log.m19i("Start subscribing to streamId: %s in the session", subscriber.getStream().getStreamId());
        Stream stream = subscriber.getStream();
        this.activeSubscribers.put(stream, subscriber);
        if (subscriber.getStream().getStreamVideoType() == StreamVideoType.StreamVideoTypeScreen) {
            log.m19i("Start subscribing VIDEO STYLE TO FIT ", new Object[0]);
            subscriber.getRenderer().setStyle(BaseVideoRenderer.STYLE_VIDEO_SCALE, BaseVideoRenderer.STYLE_VIDEO_FIT);
        }
        if (stream instanceof StreamImpl) {
            int errorCode = subscribeNative(subscriber, ((StreamImpl) stream).getPStream(), subscriber.getRenderer());
            if (errorCode > 0) {
                this.activeSubscribers.remove(stream);
                subscriber.throwError(new OpentokErrorImpl(Domain.SubscriberErrorDomain, errorCode));
            } else {
                subscriber.attachToSession(this);
            }
        }
        if (!(subscriber.getRenderer() instanceof DefaultVideoRenderer)) {
            reportDriverUsage(0);
        }
    }

    public void unsubscribe(SubscriberKit subscriber) {
        log.m19i("Stop subscribing to streamId: %s in the session", subscriber.getStream().getStreamId());
        if (this.activeSubscribers.remove(subscriber.getStream()) == null) {
            throwError(this, new OpentokErrorImpl(Domain.SessionErrorDomain, ErrorCode.UnknownSubscriberInstance.getErrorCode()));
            return;
        }
        int retCode = unsubscribeNative(subscriber);
        if (retCode > 0) {
            throwError(this, new OpentokErrorImpl(Domain.SessionErrorDomain, retCode));
        } else {
            subscriber.detachFromSession(this);
        }
    }

    void safeRemoveSubscriber(SubscriberKit subscriber) {
        if (this.activeSubscribers.remove(subscriber.getStream()) != null) {
            subscriber.detachFromSession(this);
        }
    }

    protected void finalize() throws Throwable {
        ProxyDetector.unregisterProxyDetector(this.context);
        unregisterLoggingEventsReceiver();
        finalizeNative();
        super.finalize();
    }

    private void setNativeInstanceId(long id) {
        this.nativeInstanceId = id;
    }

    private long getNativeInstanceId() {
        return this.nativeInstanceId;
    }

    void sessionConnectionCreated(long creationTime, String connectionId, String connectionData) {
        this.connection = new ConnectionImpl(connectionId, creationTime, connectionData);
    }

    public void sendSignal(String type, String data) {
        int retCode = nativeSendSignal(type, data, null, true);
        log.m19i("retCode: %d", Integer.valueOf(retCode));
        if (retCode > 0) {
            throwError(this, new OpentokErrorImpl(Domain.SessionErrorDomain, retCode));
        }
    }

    public void sendSignal(String type, String data, boolean retryAfterReconnect) {
        int retCode = nativeSendSignal(type, data, null, retryAfterReconnect);
        log.m19i("retCode: %d", Integer.valueOf(retCode));
        if (retCode > 0) {
            throwError(this, new OpentokErrorImpl(Domain.SessionErrorDomain, retCode));
        }
    }

    public void sendSignal(String type, String data, Connection connection) {
        int retCode = nativeSendSignal(type, data, connection.getConnectionId(), true);
        if (retCode > 0) {
            throwError(this, new OpentokErrorImpl(Domain.SessionErrorDomain, retCode));
        }
    }

    public void sendSignal(String type, String data, Connection connection, boolean retryAfterReconnect) {
        if (connection == null) {
            throwError(this, new OpentokErrorImpl(Domain.SessionErrorDomain, ErrorCode.ConnectionFailed.getErrorCode()));
            return;
        }
        int retCode = nativeSendSignal(type, data, connection.getConnectionId(), retryAfterReconnect);
        if (retCode > 0) {
            throwError(this, new OpentokErrorImpl(Domain.SessionErrorDomain, retCode));
        }
    }

    public Capabilities getCapabilities() {
        Capabilities capabilities = new Capabilities();
        try {
            if (nativeGetCapabilities(capabilities) > 0) {
                return null;
            }
            return capabilities;
        } catch (Throwable th) {
            log.m17e("Error while trying to get the session capabilities", new Object[0]);
            return null;
        }
    }

    public String reportIssue() {
        return nativeReportIssue();
    }

    void sessionConnected(Session session) {
        log.m19i("Session is connected", new Object[0]);
        this.handler.post(new C09812());
        this.handler.post(new C09823());
    }

    protected void onConnected() {
        enableLoggingEventsReceiver();
        if (this.sessionListener != null) {
            this.sessionListener.onConnected(this);
        }
    }

    protected void onReconnecting() {
        if (this.reconnectionListener != null) {
            this.reconnectionListener.onReconnecting(this);
        }
    }

    protected void onReconnected() {
        if (this.reconnectionListener != null) {
            this.reconnectionListener.onReconnected(this);
        }
    }

    void sessionDestroyed() {
        log.m19i("Session is destroyed", new Object[0]);
        this.handler.post(new C09834());
    }

    void sessionDisconnected(Session session) {
        log.m19i("Session is disconnected", new Object[0]);
        this.handler.post(new C09845());
    }

    void sessionReconnecting(Session session) {
        log.m19i("Session is reconnecting", new Object[0]);
        this.handler.post(new C09856());
    }

    void sessionReconnected(Session session) {
        log.m19i("Session is reconnected", new Object[0]);
        this.handler.post(new C09867());
    }

    protected void onDisconnected() {
        disableLoggingEventsReceiver();
        if (this.sessionListener != null) {
            this.sessionListener.onDisconnected(this);
        }
    }

    void error(Session session, int errorCode, String msg) {
        log.m19i("Session error: %s", msg);
        throwError(session, new OpentokError(Domain.SessionErrorDomain, errorCode, msg));
    }

    void throwError(Session session, final OpentokError error) {
        this.handler.post(new Runnable() {
            public void run() {
                Session.log.m19i("Session calling onError %s", error.getMessage());
                Session.this.onError(error);
            }
        });
    }

    protected void onError(OpentokError error) {
        if (this.sessionListener != null) {
            this.sessionListener.onError(this, error);
        }
    }

    void streamCreated(Session session, long pStream, String streamId, String streamName, long creationTime, int videoWidth, int videoHeight, boolean hasAudio, boolean hasVideo, String connectionId, long creationTimeConnection, String connectionData, int videoType) {
        log.m19i("New session stream is created. StreamId: %s", streamId);
        long j = pStream;
        String str = streamId;
        String str2 = streamName;
        final Stream stream = findReusableStream(new StreamImpl(j, str, str2, new Date(creationTime), videoWidth, videoHeight, hasAudio, hasVideo, new ConnectionImpl(connectionId, creationTimeConnection, connectionData), session, videoType));
        this.activeStreams.add(stream);
        this.handler.post(new Runnable() {
            public void run() {
                Session.this.onStreamReceived(stream);
            }
        });
    }

    protected void onStreamReceived(Stream stream) {
        if (this.sessionListener != null) {
            this.sessionListener.onStreamReceived(this, stream);
        }
    }

    private Stream findReusableStream(Stream stream) {
        synchronized (this.activeStreams) {
            if (this.activeStreams.contains(stream)) {
                Stream currentStream = null;
                for (Stream s : this.activeStreams) {
                    if (s.equals(stream)) {
                        currentStream = s;
                        break;
                    }
                }
                if (currentStream != null) {
                    currentStream.name = stream.name;
                    currentStream.hasAudio = stream.hasAudio;
                    currentStream.hasVideo = stream.hasVideo;
                    currentStream.videoWidth = stream.videoWidth;
                    currentStream.videoHeight = stream.videoHeight;
                    stream = currentStream;
                }
            }
        }
        return stream;
    }

    void streamDropped(Session session, long pStream, String streamId, String streamName, long creationTime, int videoWidth, int videoHeight, boolean hasAudio, boolean hasVideo, int videoType, String reason) {
        final Stream stream = findReusableStream(new StreamImpl(pStream, streamId, streamName, new Date(creationTime), videoWidth, videoHeight, hasAudio, hasVideo, session, videoType));
        removeStream(stream);
        this.handler.post(new Runnable() {
            public void run() {
                Session.this.onStreamDropped(stream);
            }
        });
    }

    void addStream(Stream stream) {
        this.activeStreams.add(stream);
    }

    void removeStream(Stream stream) {
        this.activeStreams.remove(stream);
    }

    protected void onStreamDropped(Stream stream) {
        if (this.sessionListener != null) {
            this.sessionListener.onStreamDropped(this, stream);
        }
    }

    void signalReceived(Session session, final String type, final String data, String connectionId, String connectionData, long creationTimeConnection) {
        log.m19i("New signal with data: %s is received", data);
        final Connection connection = connectionId != null ? new ConnectionImpl(connectionId, creationTimeConnection, connectionData) : null;
        this.handler.post(new Runnable() {
            public void run() {
                Session.this.onSignalReceived(type, data, connection);
            }
        });
    }

    protected void onSignalReceived(String type, String data, Connection connection) {
        if (this.signalListener != null) {
            this.signalListener.onSignalReceived(this, type, data, connection);
        }
    }

    void connectionCreated(Session session, long creationTime, String connectionId, String connectionData) {
        log.m19i("New session connection is created. ConnectionId: %s", connectionId);
        final Connection receivedConnection = new ConnectionImpl(connectionId, creationTime, connectionData);
        this.handler.post(new Runnable() {
            public void run() {
                Session.this.onConnectionCreated(receivedConnection);
            }
        });
    }

    protected void onConnectionCreated(Connection connection) {
        if (this.connectionListener != null) {
            this.connectionListener.onConnectionCreated(this, connection);
        }
    }

    void connectionDropped(Session session, long creationTime, String connectionId, String connectionData) {
        log.m19i("Session connection is dropped. ConnectionId: %s", connectionId);
        final Connection receivedConnection = new ConnectionImpl(connectionId, creationTime, connectionData);
        this.handler.post(new Runnable() {
            public void run() {
                Session.this.onConnectionDestroyed(receivedConnection);
            }
        });
    }

    protected void onConnectionDestroyed(Connection connection) {
        if (this.connectionListener != null) {
            this.connectionListener.onConnectionDestroyed(this, connection);
        }
    }

    void streamHasAudioChanged(Session session, long pStream, String streamId, String streamName, long creationTime, int videoWidth, int videoHeight, boolean hasAudio, boolean hasVideo, String connectionId, long creationTimeConnection, String connectionData, int newHasAudio, int videoType) {
        log.m19i("Stream with streamId: %s has changed the audio value to: %d", streamId, Integer.valueOf(newHasAudio));
        long j = pStream;
        String str = streamId;
        String str2 = streamName;
        final Stream stream = findReusableStream(new StreamImpl(j, str, str2, new Date(creationTime), videoWidth, videoHeight, hasAudio, hasVideo, new ConnectionImpl(connectionId, creationTimeConnection, connectionData), session, videoType));
        final int i = newHasAudio;
        this.handler.post(new Runnable() {
            public void run() {
                Session.this.onStreamHasAudioChanged(stream, i);
            }
        });
    }

    protected void onStreamHasAudioChanged(Stream stream, int hasAudio) {
        if (this.streamPropertiesListener != null) {
            this.streamPropertiesListener.onStreamHasAudioChanged(this, stream, hasAudio != 0);
        }
    }

    void streamHasVideoChanged(Session session, long pStream, String streamId, String streamName, long createTime, int videoWidth, int videoHeight, boolean hasAudio, boolean hasVideo, String connectionId, long creationTimeConnection, String connectionData, int newHasVideo, int videoType) {
        log.m19i("Stream with streamId: %s has changed the video value to: %d", streamId, Integer.valueOf(newHasVideo));
        final Stream stream = findReusableStream(new StreamImpl(pStream, streamId, streamName, new Date(createTime), videoWidth, videoHeight, hasAudio, hasVideo, new ConnectionImpl(connectionId, creationTimeConnection, connectionData), session, videoType));
        SubscriberKit subscriber = (SubscriberKit) session.activeSubscribers.get(stream);
        if (!(subscriber == null || subscriber.getRenderer() == null)) {
            subscriber.getRenderer().onVideoPropertiesChanged(newHasVideo == 1);
        }
        final int i = newHasVideo;
        this.handler.post(new Runnable() {
            public void run() {
                Session.this.onStreamHasVideoChanged(stream, i);
            }
        });
    }

    protected void onStreamHasVideoChanged(Stream stream, int hasVideo) {
        if (this.streamPropertiesListener != null) {
            this.streamPropertiesListener.onStreamHasVideoChanged(this, stream, hasVideo != 0);
        }
    }

    void streamVideoDimensionsChanged(Session session, long pStream, String streamId, String streamName, long creationTime, int videoWidth, int videoHeight, boolean hasAudio, boolean hasVideo, String connectionId, long creationTimeConnection, String connectionData, int newWidth, int newHeight, int videoType) {
        log.m19i("Stream with streamId: %s has changed the dimensions to w:%d, h:%d", streamId, Integer.valueOf(newWidth), Integer.valueOf(newHeight));
        long j = pStream;
        String str = streamId;
        String str2 = streamName;
        final Stream stream = findReusableStream(new StreamImpl(j, str, str2, new Date(creationTime), videoWidth, videoHeight, hasAudio, hasVideo, new ConnectionImpl(connectionId, creationTimeConnection, connectionData), session, videoType));
        final int i = newWidth;
        final int i2 = newHeight;
        this.handler.post(new Runnable() {
            public void run() {
                Session.this.onStreamVideoDimensionsChanged(stream, i, i2);
            }
        });
    }

    protected void onStreamVideoDimensionsChanged(Stream stream, int width, int height) {
        if (this.streamPropertiesListener != null) {
            this.streamPropertiesListener.onStreamVideoDimensionsChanged(this, stream, width, height);
        }
    }

    void streamVideoTypeChanged(Session session, long pStream, String streamId, String streamName, long creationTime, int videoWidth, int videoHeight, boolean hasAudio, boolean hasVideo, String connectionId, long creationTimeConnection, String connectionData, int videoType) {
        log.m19i("Stream with streamId: %s has changed the videoType value to: %d", streamId, Integer.valueOf(videoType));
        long j = pStream;
        String str = streamId;
        String str2 = streamName;
        final Stream stream = findReusableStream(new StreamImpl(j, str, str2, new Date(creationTime), videoWidth, videoHeight, hasAudio, hasVideo, new ConnectionImpl(connectionId, creationTimeConnection, connectionData), session, videoType));
        final int i = videoType;
        this.handler.post(new Runnable() {
            public void run() {
                Session.this.onStreamVideoTypeChanged(stream, StreamVideoType.fromType(i));
            }
        });
    }

    protected void onStreamVideoTypeChanged(Stream stream, StreamVideoType videoType) {
        if (this.streamPropertiesListener != null) {
            this.streamPropertiesListener.onStreamVideoTypeChanged(this, stream, videoType);
        }
    }

    void archiveStarted(Session session, final String archiveId, final String archiveName) {
        log.m19i("Archive is started. ArchiveId: %s archive name: %s", archiveId, archiveName);
        this.handler.post(new Runnable() {
            public void run() {
                Session.this.onArchiveStarted(archiveId, archiveName);
            }
        });
    }

    void archiveStopped(Session session, final String archiveId) {
        log.m19i("Archive is stopped. ArchiveId: %s", archiveId);
        this.handler.post(new Runnable() {
            public void run() {
                Session.this.onArchiveStopped(archiveId);
            }
        });
    }

    protected void onArchiveStarted(String id, String name) {
        if (this.archiveListener != null) {
            this.archiveListener.onArchiveStarted(this, id, name);
        }
    }

    protected void onArchiveStopped(String id) {
        if (this.archiveListener != null) {
            this.archiveListener.onArchiveStopped(this, id);
        }
    }

    private void logAdHocAction(String action) {
        logAdHocActionNative(action);
    }

    private void logCustomMsg(String event, String payload) {
        logCustomNative(event, payload);
    }

    private void enableLoggingEventsReceiver() {
        this.shouldRegisterLoggingReceiver = true;
        registerLoggingEventsReceiver();
    }

    private void disableLoggingEventsReceiver() {
        this.shouldRegisterLoggingReceiver = false;
        unregisterLoggingEventsReceiver();
    }

    private void registerLoggingEventsReceiver() {
        if (!this.isLoggingReceiverRegistered && this.shouldRegisterLoggingReceiver) {
            this.context.registerReceiver(this.loggingReceiver, new IntentFilter(INTENT_ACTION));
            this.isLoggingReceiverRegistered = true;
        }
    }

    private void unregisterLoggingEventsReceiver() {
        if (this.isLoggingReceiverRegistered) {
            try {
                this.context.unregisterReceiver(this.loggingReceiver);
                this.isLoggingReceiverRegistered = false;
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
    }
}
