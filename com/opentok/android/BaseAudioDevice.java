package com.opentok.android;

import com.opentok.android.OpentokError.Domain;
import com.opentok.android.OpentokError.ErrorCode;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;
import java.util.WeakHashMap;

public abstract class BaseAudioDevice {
    private static WeakHashMap<Integer, PublisherKit> activePublisherLst = new WeakHashMap();
    private static WeakHashMap<Integer, SubscriberKit> activeSubscriberLst = new WeakHashMap();
    private AudioBus audioBus;
    private long nativeInstace = 0;
    private OutputMode outputMode = OutputMode.SpeakerPhone;

    public static class AudioBus {
        private BaseAudioDevice device;

        private native int readRenderDataNative(BaseAudioDevice baseAudioDevice, Buffer buffer, int i);

        private native void writeCaptureDataNative(BaseAudioDevice baseAudioDevice, Buffer buffer, int i);

        AudioBus(BaseAudioDevice device) {
            this.device = device;
        }

        public void writeCaptureData(ByteBuffer data, int numberOfSamples) {
            writeCaptureDataNative(this.device, data, numberOfSamples);
        }

        public void writeCaptureData(ShortBuffer data, int numberOfSamples) {
            writeCaptureDataNative(this.device, data, numberOfSamples);
        }

        public int readRenderData(ByteBuffer data, int numberOfSamples) {
            return readRenderDataNative(this.device, data, numberOfSamples);
        }

        public int readRenderData(ShortBuffer data, int numberOfSamples) {
            return readRenderDataNative(this.device, data, numberOfSamples);
        }
    }

    public static class AudioSettings {
        int numChannels;
        int sampleRate;

        public AudioSettings(int sampleRate, int numChannels) {
            this.sampleRate = sampleRate;
            this.numChannels = numChannels;
        }

        public int getSampleRate() {
            return this.sampleRate;
        }

        public int getNumChannels() {
            return this.numChannels;
        }

        public void setSampleRate(int sampleRate) {
            this.sampleRate = sampleRate;
        }

        public void setNumChannels(int numChannels) {
            this.numChannels = numChannels;
        }
    }

    public enum OutputMode {
        SpeakerPhone,
        Handset
    }

    public abstract boolean destroyCapturer();

    native boolean destroyNative();

    public abstract boolean destroyRenderer();

    native boolean finalizeNative();

    public abstract AudioSettings getCaptureSettings();

    public abstract int getEstimatedCaptureDelay();

    public abstract int getEstimatedRenderDelay();

    public abstract AudioSettings getRenderSettings();

    public abstract boolean initCapturer();

    native boolean initNative();

    public abstract boolean initRenderer();

    native boolean isActive();

    public abstract void onPause();

    public abstract void onResume();

    public abstract boolean startCapturer();

    public abstract boolean startRenderer();

    public abstract boolean stopCapturer();

    public abstract boolean stopRenderer();

    static {
        System.loadLibrary("opentok");
    }

    protected void finalize() throws Throwable {
        activeSubscriberLst.clear();
        activePublisherLst.clear();
        finalizeNative();
        super.finalize();
    }

    public boolean setOutputMode(OutputMode mode) {
        this.outputMode = mode;
        return true;
    }

    public OutputMode getOutputMode() {
        return this.outputMode;
    }

    private void setNativeInstanceId(long instance) {
        this.nativeInstace = instance;
    }

    private long getNativeInstanceId() {
        return this.nativeInstace;
    }

    void setAudioBus(AudioBus audioBus) {
        this.audioBus = audioBus;
    }

    public AudioBus getAudioBus() {
        return this.audioBus;
    }

    static void addPublisher(PublisherKit publisherkit) {
        activePublisherLst.put(Integer.valueOf(publisherkit.hashCode()), publisherkit);
    }

    static void addSubsciber(SubscriberKit subscriberKit) {
        activeSubscriberLst.put(Integer.valueOf(subscriberKit.hashCode()), subscriberKit);
    }

    static void publisherError(Exception exp) {
        for (PublisherKit sub : activePublisherLst.values()) {
            if (sub != null) {
                sub.throwError(new OpentokError(Domain.PublisherErrorDomain, ErrorCode.PublisherInternalError.getErrorCode(), exp.getMessage()));
            }
        }
    }

    static void subscriberError(Exception exp) {
        for (SubscriberKit sub : activeSubscriberLst.values()) {
            if (sub != null) {
                sub.throwError(new OpentokError(Domain.SubscriberErrorDomain, ErrorCode.SubscriberInternalError.getErrorCode(), exp.getMessage()));
            }
        }
    }
}
