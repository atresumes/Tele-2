package com.opentok.android;

import android.view.View;
import java.nio.ByteBuffer;
import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class BaseVideoRenderer {
    public static final String STYLE_VIDEO_FILL = "STYLE_VIDEO_FILL";
    public static final String STYLE_VIDEO_FIT = "STYLE_VIDEO_FIT";
    public static final String STYLE_VIDEO_SCALE = "STYLE_VIDEO_SCALE";
    private ConcurrentLinkedQueue<Frame> reuseFrameStack = new ConcurrentLinkedQueue();

    public final class Frame {
        protected ByteBuffer buffer;
        protected int format;
        protected int height;
        protected long internalBuffer;
        protected boolean mirrored;
        protected int uvStride;
        protected int width;
        protected int yStride;

        private native void nativeDispose();

        protected Frame() {
        }

        public void recycle() {
            nativeDispose();
            this.internalBuffer = 0;
            this.buffer = null;
            this.height = 0;
            this.width = 0;
            this.format = 0;
            BaseVideoRenderer.this.reuseFrameStack.add(this);
        }

        protected void finalize() throws Throwable {
            nativeDispose();
            super.finalize();
        }

        public ByteBuffer getBuffer() {
            return this.buffer;
        }

        public int getWidth() {
            return this.width;
        }

        public int getHeight() {
            return this.height;
        }

        public boolean isMirroredX() {
            return this.mirrored;
        }

        public int getYstride() {
            return this.yStride;
        }

        public int getUvStride() {
            return this.uvStride;
        }
    }

    public abstract View getView();

    public abstract void onFrame(Frame frame);

    public abstract void onPause();

    public abstract void onResume();

    public abstract void onVideoPropertiesChanged(boolean z);

    public abstract void setStyle(String str, String str2);

    static {
        System.loadLibrary("opentok");
    }

    private void createFrameAndDispatch(long instance, ByteBuffer buffer, int width, int height, int yStride, int uvStride, int format, boolean mirrored) {
        Frame frame;
        if (this.reuseFrameStack.isEmpty()) {
            frame = new Frame();
        } else {
            frame = (Frame) this.reuseFrameStack.remove();
        }
        frame.internalBuffer = instance;
        frame.buffer = buffer;
        frame.width = width;
        frame.height = height;
        frame.format = format;
        frame.mirrored = mirrored;
        frame.yStride = yStride;
        frame.uvStride = uvStride;
        onFrame(frame);
    }

    private int[] getViewDimensions() {
        int width;
        int i = 0;
        View view = getView();
        int[] ret = new int[2];
        if (view != null) {
            width = view.getWidth();
        } else {
            width = 0;
        }
        ret[0] = width;
        if (view != null) {
            i = view.getHeight();
        }
        ret[1] = i;
        return ret;
    }
}
