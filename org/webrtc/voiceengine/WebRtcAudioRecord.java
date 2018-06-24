package org.webrtc.voiceengine;

import android.content.Context;
import android.media.AudioRecord;
import android.os.Process;
import java.nio.ByteBuffer;
import org.webrtc.Logging;
import org.webrtc.ThreadUtils;

public class WebRtcAudioRecord {
    private static final long AUDIO_RECORD_THREAD_JOIN_TIMEOUT_MS = 2000;
    private static final int BITS_PER_SAMPLE = 16;
    private static final int BUFFERS_PER_SECOND = 100;
    private static final int BUFFER_SIZE_FACTOR = 2;
    private static final int CALLBACK_BUFFER_SIZE_MS = 10;
    private static final boolean DEBUG = false;
    private static final String TAG = "WebRtcAudioRecord";
    private static volatile boolean microphoneMute = false;
    private AudioRecord audioRecord = null;
    private AudioRecordThread audioThread = null;
    private ByteBuffer byteBuffer;
    private final Context context;
    private WebRtcAudioEffects effects = null;
    private byte[] emptyBytes;
    private final long nativeAudioRecord;

    private class AudioRecordThread extends Thread {
        private volatile boolean keepAlive = true;

        public AudioRecordThread(String name) {
            super(name);
        }

        public void run() {
            boolean z;
            Process.setThreadPriority(-19);
            Logging.m172d(WebRtcAudioRecord.TAG, "AudioRecordThread" + WebRtcAudioUtils.getThreadInfo());
            if (WebRtcAudioRecord.this.audioRecord.getRecordingState() == 3) {
                z = true;
            } else {
                z = false;
            }
            WebRtcAudioRecord.assertTrue(z);
            long lastTime = System.nanoTime();
            while (this.keepAlive) {
                int bytesRead = WebRtcAudioRecord.this.audioRecord.read(WebRtcAudioRecord.this.byteBuffer, WebRtcAudioRecord.this.byteBuffer.capacity());
                if (bytesRead == WebRtcAudioRecord.this.byteBuffer.capacity()) {
                    if (WebRtcAudioRecord.microphoneMute) {
                        WebRtcAudioRecord.this.byteBuffer.clear();
                        WebRtcAudioRecord.this.byteBuffer.put(WebRtcAudioRecord.this.emptyBytes);
                    }
                    WebRtcAudioRecord.this.nativeDataIsRecorded(bytesRead, WebRtcAudioRecord.this.nativeAudioRecord);
                } else {
                    Logging.m173e(WebRtcAudioRecord.TAG, "AudioRecord.read failed: " + bytesRead);
                    if (bytesRead == -3) {
                        this.keepAlive = false;
                    }
                }
            }
            try {
                if (WebRtcAudioRecord.this.audioRecord != null) {
                    WebRtcAudioRecord.this.audioRecord.stop();
                }
            } catch (IllegalStateException e) {
                Logging.m173e(WebRtcAudioRecord.TAG, "AudioRecord.stop failed: " + e.getMessage());
            }
        }

        public void stopThread() {
            Logging.m172d(WebRtcAudioRecord.TAG, "stopThread");
            this.keepAlive = false;
        }
    }

    private native void nativeCacheDirectBufferAddress(ByteBuffer byteBuffer, long j);

    private native void nativeDataIsRecorded(int i, long j);

    WebRtcAudioRecord(Context context, long nativeAudioRecord) {
        Logging.m172d(TAG, "ctor" + WebRtcAudioUtils.getThreadInfo());
        this.context = context;
        this.nativeAudioRecord = nativeAudioRecord;
        this.effects = WebRtcAudioEffects.create();
    }

    private boolean enableBuiltInAEC(boolean enable) {
        Logging.m172d(TAG, "enableBuiltInAEC(" + enable + ')');
        if (this.effects != null) {
            return this.effects.setAEC(enable);
        }
        Logging.m173e(TAG, "Built-in AEC is not supported on this platform");
        return false;
    }

    private boolean enableBuiltInNS(boolean enable) {
        Logging.m172d(TAG, "enableBuiltInNS(" + enable + ')');
        if (this.effects != null) {
            return this.effects.setNS(enable);
        }
        Logging.m173e(TAG, "Built-in NS is not supported on this platform");
        return false;
    }

    private int initRecording(int sampleRate, int channels) {
        Logging.m172d(TAG, "initRecording(sampleRate=" + sampleRate + ", channels=" + channels + ")");
        if (!WebRtcAudioUtils.hasPermission(this.context, "android.permission.RECORD_AUDIO")) {
            Logging.m173e(TAG, "RECORD_AUDIO permission is missing");
            return -1;
        } else if (this.audioRecord != null) {
            Logging.m173e(TAG, "InitRecording() called twice without StopRecording()");
            return -1;
        } else {
            int framesPerBuffer = sampleRate / 100;
            this.byteBuffer = ByteBuffer.allocateDirect((channels * 2) * framesPerBuffer);
            Logging.m172d(TAG, "byteBuffer.capacity: " + this.byteBuffer.capacity());
            this.emptyBytes = new byte[this.byteBuffer.capacity()];
            nativeCacheDirectBufferAddress(this.byteBuffer, this.nativeAudioRecord);
            int minBufferSize = AudioRecord.getMinBufferSize(sampleRate, 16, 2);
            if (minBufferSize == -1 || minBufferSize == -2) {
                Logging.m173e(TAG, "AudioRecord.getMinBufferSize failed: " + minBufferSize);
                return -1;
            }
            Logging.m172d(TAG, "AudioRecord.getMinBufferSize: " + minBufferSize);
            int bufferSizeInBytes = Math.max(minBufferSize * 2, this.byteBuffer.capacity());
            Logging.m172d(TAG, "bufferSizeInBytes: " + bufferSizeInBytes);
            try {
                this.audioRecord = new AudioRecord(7, sampleRate, 16, 2, bufferSizeInBytes);
                if (this.audioRecord == null || this.audioRecord.getState() != 1) {
                    Logging.m173e(TAG, "Failed to create a new AudioRecord instance");
                    return -1;
                }
                if (this.effects != null) {
                    this.effects.enable(this.audioRecord.getAudioSessionId());
                }
                if (areParametersValid(sampleRate, channels)) {
                    logMainParameters();
                    logMainParametersExtended();
                    return framesPerBuffer;
                }
                Logging.m173e(TAG, "At least one audio record parameter is invalid.");
                return -1;
            } catch (IllegalArgumentException e) {
                Logging.m173e(TAG, e.getMessage());
                return -1;
            }
        }
    }

    private boolean startRecording() {
        boolean z;
        Logging.m172d(TAG, "startRecording");
        assertTrue(this.audioRecord != null);
        if (this.audioThread == null) {
            z = true;
        } else {
            z = false;
        }
        assertTrue(z);
        try {
            this.audioRecord.startRecording();
            if (this.audioRecord.getRecordingState() != 3) {
                Logging.m173e(TAG, "AudioRecord.startRecording failed");
                return false;
            }
            this.audioThread = new AudioRecordThread("AudioRecordJavaThread");
            this.audioThread.start();
            return true;
        } catch (IllegalStateException e) {
            Logging.m173e(TAG, "AudioRecord.startRecording failed: " + e.getMessage());
            return false;
        }
    }

    private boolean stopRecording() {
        Logging.m172d(TAG, "stopRecording");
        assertTrue(this.audioThread != null);
        this.audioThread.stopThread();
        if (!ThreadUtils.joinUninterruptibly(this.audioThread, AUDIO_RECORD_THREAD_JOIN_TIMEOUT_MS)) {
            Logging.m173e(TAG, "Join of AudioRecordJavaThread timed out");
        }
        this.audioThread = null;
        if (this.effects != null) {
            this.effects.release();
        }
        this.audioRecord.release();
        this.audioRecord = null;
        return true;
    }

    private boolean areParametersValid(int sampleRate, int channels) {
        return this.audioRecord.getAudioFormat() == 2 && this.audioRecord.getChannelConfiguration() == 16 && this.audioRecord.getAudioSource() == 7 && this.audioRecord.getSampleRate() == sampleRate && this.audioRecord.getChannelCount() == channels;
    }

    private void logMainParameters() {
        Logging.m172d(TAG, "AudioRecord: session ID: " + this.audioRecord.getAudioSessionId() + ", " + "channels: " + this.audioRecord.getChannelCount() + ", " + "sample rate: " + this.audioRecord.getSampleRate());
    }

    private void logMainParametersExtended() {
        if (WebRtcAudioUtils.runningOnMarshmallowOrHigher()) {
            Logging.m172d(TAG, "AudioRecord: buffer size in frames: " + this.audioRecord.getBufferSizeInFrames());
        }
    }

    private static void assertTrue(boolean condition) {
        if (!condition) {
            throw new AssertionError("Expected condition to be true");
        }
    }

    public static void setMicrophoneMute(boolean mute) {
        Logging.m176w(TAG, "setMicrophoneMute(" + mute + ")");
        microphoneMute = mute;
    }
}
