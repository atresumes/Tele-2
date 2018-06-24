package org.webrtc.voiceengine;

import android.content.Context;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Process;
import java.nio.ByteBuffer;
import org.webrtc.Logging;

public class WebRtcAudioTrack {
    private static final int BITS_PER_SAMPLE = 16;
    private static final int BUFFERS_PER_SECOND = 100;
    private static final int CALLBACK_BUFFER_SIZE_MS = 10;
    private static final boolean DEBUG = false;
    private static final String TAG = "WebRtcAudioTrack";
    private static volatile boolean speakerMute = false;
    private final AudioManager audioManager;
    private AudioTrackThread audioThread = null;
    private AudioTrack audioTrack = null;
    private ByteBuffer byteBuffer;
    private final Context context;
    private byte[] emptyBytes;
    private final long nativeAudioTrack;

    private class AudioTrackThread extends Thread {
        private volatile boolean keepAlive = true;

        public AudioTrackThread(String name) {
            super(name);
        }

        public void run() {
            boolean z = true;
            Process.setThreadPriority(-19);
            Logging.m172d(WebRtcAudioTrack.TAG, "AudioTrackThread" + WebRtcAudioUtils.getThreadInfo());
            try {
                boolean z2;
                WebRtcAudioTrack.this.audioTrack.play();
                if (WebRtcAudioTrack.this.audioTrack.getPlayState() == 3) {
                    z2 = true;
                } else {
                    z2 = false;
                }
                WebRtcAudioTrack.assertTrue(z2);
                int sizeInBytes = WebRtcAudioTrack.this.byteBuffer.capacity();
                while (this.keepAlive) {
                    int bytesWritten;
                    WebRtcAudioTrack.this.nativeGetPlayoutData(sizeInBytes, WebRtcAudioTrack.this.nativeAudioTrack);
                    if (sizeInBytes <= WebRtcAudioTrack.this.byteBuffer.remaining()) {
                        z2 = true;
                    } else {
                        z2 = false;
                    }
                    WebRtcAudioTrack.assertTrue(z2);
                    if (WebRtcAudioTrack.speakerMute) {
                        WebRtcAudioTrack.this.byteBuffer.clear();
                        WebRtcAudioTrack.this.byteBuffer.put(WebRtcAudioTrack.this.emptyBytes);
                        WebRtcAudioTrack.this.byteBuffer.position(0);
                    }
                    if (WebRtcAudioUtils.runningOnLollipopOrHigher()) {
                        bytesWritten = writeOnLollipop(WebRtcAudioTrack.this.audioTrack, WebRtcAudioTrack.this.byteBuffer, sizeInBytes);
                    } else {
                        bytesWritten = writePreLollipop(WebRtcAudioTrack.this.audioTrack, WebRtcAudioTrack.this.byteBuffer, sizeInBytes);
                    }
                    if (bytesWritten != sizeInBytes) {
                        Logging.m173e(WebRtcAudioTrack.TAG, "AudioTrack.write failed: " + bytesWritten);
                        if (bytesWritten == -3) {
                            this.keepAlive = false;
                        }
                    }
                    WebRtcAudioTrack.this.byteBuffer.rewind();
                }
                try {
                    WebRtcAudioTrack.this.audioTrack.stop();
                } catch (IllegalStateException e) {
                    Logging.m173e(WebRtcAudioTrack.TAG, "AudioTrack.stop failed: " + e.getMessage());
                }
                if (WebRtcAudioTrack.this.audioTrack.getPlayState() != 1) {
                    z = false;
                }
                WebRtcAudioTrack.assertTrue(z);
                WebRtcAudioTrack.this.audioTrack.flush();
            } catch (IllegalStateException e2) {
                Logging.m173e(WebRtcAudioTrack.TAG, "AudioTrack.play failed: " + e2.getMessage());
            }
        }

        private int writeOnLollipop(AudioTrack audioTrack, ByteBuffer byteBuffer, int sizeInBytes) {
            return audioTrack.write(byteBuffer, sizeInBytes, 0);
        }

        private int writePreLollipop(AudioTrack audioTrack, ByteBuffer byteBuffer, int sizeInBytes) {
            return audioTrack.write(byteBuffer.array(), byteBuffer.arrayOffset(), sizeInBytes);
        }

        public void joinThread() {
            this.keepAlive = false;
            while (isAlive()) {
                try {
                    join();
                } catch (InterruptedException e) {
                }
            }
        }
    }

    private native void nativeCacheDirectBufferAddress(ByteBuffer byteBuffer, long j);

    private native void nativeGetPlayoutData(int i, long j);

    WebRtcAudioTrack(Context context, long nativeAudioTrack) {
        Logging.m172d(TAG, "ctor" + WebRtcAudioUtils.getThreadInfo());
        this.context = context;
        this.nativeAudioTrack = nativeAudioTrack;
        this.audioManager = (AudioManager) context.getSystemService("audio");
    }

    private boolean initPlayout(int sampleRate, int channels) {
        Logging.m172d(TAG, "initPlayout(sampleRate=" + sampleRate + ", channels=" + channels + ")");
        int bytesPerFrame = channels * 2;
        ByteBuffer byteBuffer = this.byteBuffer;
        this.byteBuffer = ByteBuffer.allocateDirect((sampleRate / 100) * bytesPerFrame);
        Logging.m172d(TAG, "byteBuffer.capacity: " + this.byteBuffer.capacity());
        this.emptyBytes = new byte[this.byteBuffer.capacity()];
        nativeCacheDirectBufferAddress(this.byteBuffer, this.nativeAudioTrack);
        int minBufferSizeInBytes = AudioTrack.getMinBufferSize(sampleRate, 4, 2);
        Logging.m172d(TAG, "AudioTrack.getMinBufferSize: " + minBufferSizeInBytes);
        if (minBufferSizeInBytes < this.byteBuffer.capacity()) {
            Logging.m173e(TAG, "AudioTrack.getMinBufferSize returns an invalid value.");
            return false;
        } else if (this.audioTrack != null) {
            Logging.m173e(TAG, "Conflict with existing AudioTrack.");
            return false;
        } else {
            try {
                this.audioTrack = new AudioTrack(0, sampleRate, 4, 2, minBufferSizeInBytes, 1);
                if (this.audioTrack.getState() != 1) {
                    Logging.m173e(TAG, "Initialization of audio track failed.");
                    return false;
                } else if (areParametersValid(sampleRate, channels)) {
                    logMainParameters();
                    logMainParametersExtended();
                    return true;
                } else {
                    Logging.m173e(TAG, "At least one audio track parameter is invalid.");
                    return false;
                }
            } catch (IllegalArgumentException e) {
                Logging.m172d(TAG, e.getMessage());
                return false;
            }
        }
    }

    private boolean startPlayout() {
        boolean z;
        Logging.m172d(TAG, "startPlayout");
        assertTrue(this.audioTrack != null);
        if (this.audioThread == null) {
            z = true;
        } else {
            z = false;
        }
        assertTrue(z);
        if (this.audioTrack.getState() != 1) {
            Logging.m173e(TAG, "Audio track is not successfully initialized.");
            return false;
        }
        this.audioThread = new AudioTrackThread("AudioTrackJavaThread");
        this.audioThread.start();
        return true;
    }

    private boolean stopPlayout() {
        Logging.m172d(TAG, "stopPlayout");
        assertTrue(this.audioThread != null);
        logUnderrunCount();
        this.audioThread.joinThread();
        this.audioThread = null;
        if (this.audioTrack != null) {
            this.audioTrack.release();
            this.audioTrack = null;
        }
        return true;
    }

    private int getStreamMaxVolume() {
        boolean z;
        Logging.m172d(TAG, "getStreamMaxVolume");
        if (this.audioManager != null) {
            z = true;
        } else {
            z = false;
        }
        assertTrue(z);
        return this.audioManager.getStreamMaxVolume(0);
    }

    private boolean setStreamVolume(int volume) {
        Logging.m172d(TAG, "setStreamVolume(" + volume + ")");
        assertTrue(this.audioManager != null);
        if (isVolumeFixed()) {
            Logging.m173e(TAG, "The device implements a fixed volume policy.");
            return false;
        }
        this.audioManager.setStreamVolume(0, volume, 0);
        return true;
    }

    private boolean isVolumeFixed() {
        if (WebRtcAudioUtils.runningOnLollipopOrHigher()) {
            return this.audioManager.isVolumeFixed();
        }
        return false;
    }

    private int getStreamVolume() {
        boolean z;
        Logging.m172d(TAG, "getStreamVolume");
        if (this.audioManager != null) {
            z = true;
        } else {
            z = false;
        }
        assertTrue(z);
        return this.audioManager.getStreamVolume(0);
    }

    private boolean areParametersValid(int sampleRate, int channels) {
        int streamType = this.audioTrack.getStreamType();
        if (this.audioTrack.getAudioFormat() == 2 && this.audioTrack.getChannelConfiguration() == 4 && streamType == 0 && this.audioTrack.getSampleRate() == sampleRate) {
            AudioTrack audioTrack = this.audioTrack;
            if (sampleRate == AudioTrack.getNativeOutputSampleRate(streamType) && this.audioTrack.getChannelCount() == channels) {
                return true;
            }
        }
        return false;
    }

    private void logMainParameters() {
        String str = TAG;
        StringBuilder append = new StringBuilder().append("AudioTrack: session ID: ").append(this.audioTrack.getAudioSessionId()).append(", ").append("channels: ").append(this.audioTrack.getChannelCount()).append(", ").append("sample rate: ").append(this.audioTrack.getSampleRate()).append(", ").append("max gain: ");
        AudioTrack audioTrack = this.audioTrack;
        Logging.m172d(str, append.append(AudioTrack.getMaxVolume()).toString());
    }

    private void logMainParametersExtended() {
        if (WebRtcAudioUtils.runningOnMarshmallowOrHigher()) {
            Logging.m172d(TAG, "AudioTrack: buffer size in frames: " + this.audioTrack.getBufferSizeInFrames());
        }
        if (WebRtcAudioUtils.runningOnNougatOrHigher()) {
            Logging.m172d(TAG, "AudioTrack: buffer capacity in frames: " + this.audioTrack.getBufferCapacityInFrames());
        }
    }

    private void logUnderrunCount() {
        if (WebRtcAudioUtils.runningOnNougatOrHigher()) {
            Logging.m172d(TAG, "underrun count: " + this.audioTrack.getUnderrunCount());
        }
    }

    private static void assertTrue(boolean condition) {
        if (!condition) {
            throw new AssertionError("Expected condition to be true");
        }
    }

    public static void setSpeakerMute(boolean mute) {
        Logging.m176w(TAG, "setSpeakerMute(" + mute + ")");
        speakerMute = mute;
    }
}
