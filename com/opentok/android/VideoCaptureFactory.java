package com.opentok.android;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build.VERSION;
import android.util.Log;
import com.opentok.android.Publisher.CameraCaptureFrameRate;
import com.opentok.android.Publisher.CameraCaptureResolution;
import com.opentok.android.Session.SessionOptions;

class VideoCaptureFactory {
    private static String TAG = VideoCaptureFactory.class.getSimpleName();
    private static boolean camera2Enabled;

    static class C10011 extends SessionOptions {
        C10011() {
        }
    }

    VideoCaptureFactory() {
    }

    static {
        boolean z = false;
        camera2Enabled = false;
        SessionOptions options = new C10011();
        if (VERSION.SDK_INT >= 21 && options.isCamera2Capable()) {
            z = true;
        }
        camera2Enabled = z;
    }

    @SuppressLint({"LogNotTimber"})
    public static BaseVideoCapturer constructCapturer(Context context, CameraCaptureResolution resolution, CameraCaptureFrameRate frameRate) {
        if (isCamera2Capable()) {
            Log.i(TAG, "Using Camera2 Capturer");
            return new Camera2VideoCapturer(context, resolution, frameRate);
        }
        Log.i(TAG, "Using Camera Capturer");
        return new DefaultVideoCapturer(context, resolution, frameRate);
    }

    public static BaseVideoCapturer constructCapturer(Context context) {
        return constructCapturer(context, CameraCaptureResolution.defaultResolution(), CameraCaptureFrameRate.defaultFrameRate());
    }

    public static void enableCamera2api(boolean enable) {
        camera2Enabled = enable;
    }

    private static boolean isCamera2Capable() {
        return camera2Enabled;
    }
}
