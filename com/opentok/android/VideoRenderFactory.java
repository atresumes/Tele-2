package com.opentok.android;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build.VERSION;
import android.util.Log;

public class VideoRenderFactory {
    private static String TAG = VideoCaptureFactory.class.getSimpleName();
    private static boolean useTextureViews = false;

    @SuppressLint({"LogNotTimber"})
    public static BaseVideoRenderer constructRenderer(Context context) {
        if (!useTextureViews || VERSION.SDK_INT < 19) {
            Log.i(TAG, "Using GLSurfaceViews for render");
            return new DefaultVideoRenderer(context);
        }
        Log.i(TAG, "Using TextureViews for render");
        return new TextureViewRenderer(context);
    }

    public static void useTextureViews(boolean useTextureViews) {
        useTextureViews = useTextureViews;
    }
}
