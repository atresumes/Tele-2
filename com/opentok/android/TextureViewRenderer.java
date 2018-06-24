package com.opentok.android;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.opengl.GLES20;
import android.view.TextureView;
import android.view.TextureView.SurfaceTextureListener;
import android.view.View;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.opentok.android.BaseVideoRenderer.Frame;
import com.opentok.android.grafika.gles.EglCore;
import com.opentok.android.grafika.gles.WindowSurface;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.concurrent.locks.ReentrantLock;

public class TextureViewRenderer extends BaseVideoRenderer {
    Context ctx;
    Renderer renderer = new Renderer();
    boolean videoLastStatus;
    TextureView view;

    private static class Renderer extends Thread implements SurfaceTextureListener {
        static final int COORDS_PER_VERTEX = 3;
        static final int TEXTURECOORDS_PER_VERTEX = 2;
        static float[] uvCoords = new float[]{0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f};
        static float[] xyzCoords = new float[]{GroundOverlayOptions.NO_DIMENSION, 1.0f, 0.0f, GroundOverlayOptions.NO_DIMENSION, GroundOverlayOptions.NO_DIMENSION, 0.0f, 1.0f, GroundOverlayOptions.NO_DIMENSION, 0.0f, 1.0f, 1.0f, 0.0f};
        Frame currentFrame;
        private ShortBuffer drawListBuffer;
        EglCore eglCore;
        private final String fragmentShaderCode;
        ReentrantLock frameLock;
        int glProgram;
        Object lock;
        float[] scaleMatrix;
        SurfaceTexture surfaceTexture;
        private FloatBuffer textureBuffer;
        private int textureHeight;
        int[] textureIds;
        private int textureWidth;
        private FloatBuffer vertexBuffer;
        private short[] vertexIndex;
        private final String vertexShaderCode;
        private boolean videoEnabled;
        private boolean videoFitEnabled;
        private int viewportHeight;
        private int viewportWidth;

        private Renderer() {
            this.vertexShaderCode = "uniform mat4 uMVPMatrix;attribute vec4 aPosition;\nattribute vec2 aTextureCoord;\nvarying vec2 vTextureCoord;\nvoid main() {\n  gl_Position = uMVPMatrix * aPosition;\n  vTextureCoord = aTextureCoord;\n}\n";
            this.fragmentShaderCode = "precision mediump float;\nuniform sampler2D Ytex;\nuniform sampler2D Utex,Vtex;\nvarying vec2 vTextureCoord;\nvoid main(void) {\n  float nx,ny,r,g,b,y,u,v;\n  mediump vec4 txl,ux,vx;  nx=vTextureCoord[0];\n  ny=vTextureCoord[1];\n  y=texture2D(Ytex,vec2(nx,ny)).r;\n  u=texture2D(Utex,vec2(nx,ny)).r;\n  v=texture2D(Vtex,vec2(nx,ny)).r;\n  y=1.1643*(y-0.0625);\n  u=u-0.5;\n  v=v-0.5;\n  r=y+1.5958*v;\n  g=y-0.39173*u-0.81290*v;\n  b=y+2.017*u;\n  gl_FragColor=vec4(r,g,b,1.0);\n}\n";
            this.lock = new Object();
            this.videoEnabled = true;
            this.videoFitEnabled = false;
            this.textureIds = new int[3];
            this.scaleMatrix = new float[16];
            this.vertexIndex = new short[]{(short) 0, (short) 1, (short) 2, (short) 0, (short) 2, (short) 3};
            this.frameLock = new ReentrantLock();
            ByteBuffer bb = ByteBuffer.allocateDirect(xyzCoords.length * 4);
            bb.order(ByteOrder.nativeOrder());
            this.vertexBuffer = bb.asFloatBuffer();
            this.vertexBuffer.put(xyzCoords);
            this.vertexBuffer.position(0);
            ByteBuffer tb = ByteBuffer.allocateDirect(uvCoords.length * 4);
            tb.order(ByteOrder.nativeOrder());
            this.textureBuffer = tb.asFloatBuffer();
            this.textureBuffer.put(uvCoords);
            this.textureBuffer.position(0);
            ByteBuffer dlb = ByteBuffer.allocateDirect(this.vertexIndex.length * 2);
            dlb.order(ByteOrder.nativeOrder());
            this.drawListBuffer = dlb.asShortBuffer();
            this.drawListBuffer.put(this.vertexIndex);
            this.drawListBuffer.position(0);
        }

        public void run() {
            waitUntilSurfaceIsReady();
            this.eglCore = new EglCore(null, 2);
            WindowSurface windowSurface = new WindowSurface(this.eglCore, this.surfaceTexture);
            windowSurface.makeCurrent();
            setupgl();
            renderFrameLoop(windowSurface);
            windowSurface.release();
            this.eglCore.release();
        }

        public void onSurfaceTextureAvailable(SurfaceTexture st, int width, int height) {
            synchronized (this.lock) {
                this.surfaceTexture = st;
                this.viewportWidth = width;
                this.viewportHeight = height;
                this.lock.notify();
            }
        }

        public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int width, int height) {
            GLES20.glViewport(0, 0, width, height);
            this.viewportWidth = width;
            this.viewportHeight = height;
        }

        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            synchronized (this.lock) {
                this.surfaceTexture = null;
            }
            return true;
        }

        public void onSurfaceTextureUpdated(SurfaceTexture surface) {
            synchronized (this.lock) {
                this.surfaceTexture = surface;
            }
        }

        private void enableVideoFit(boolean videoFit) {
            this.videoFitEnabled = videoFit;
        }

        private void setEnableVideo(boolean video) {
            this.videoEnabled = video;
        }

        private boolean isEnableVideo() {
            return this.videoEnabled;
        }

        private void displayFrame(Frame frame) {
            this.frameLock.lock();
            if (this.currentFrame != null) {
                this.currentFrame.recycle();
            }
            this.currentFrame = frame;
            this.frameLock.unlock();
        }

        private void waitUntilSurfaceIsReady() {
            synchronized (this.lock) {
                while (this.surfaceTexture == null) {
                    try {
                        this.lock.wait();
                    } catch (InterruptedException e) {
                        OtLog.m25d("Waiting for surface ready was interrupted", new Object[0]);
                    }
                }
            }
        }

        private void initializeTexture(int name, int id, int width, int height) {
            GLES20.glActiveTexture(name);
            GLES20.glBindTexture(3553, id);
            GLES20.glTexParameterf(3553, 10241, 9728.0f);
            GLES20.glTexParameterf(3553, 10240, 9729.0f);
            GLES20.glTexParameterf(3553, 10242, 33071.0f);
            GLES20.glTexParameterf(3553, 10243, 33071.0f);
            GLES20.glTexImage2D(3553, 0, 6409, width, height, 0, 6409, 5121, null);
        }

        private void setupTextures(Frame frame) {
            if (this.textureIds[0] != 0) {
                GLES20.glDeleteTextures(3, this.textureIds, 0);
            }
            GLES20.glGenTextures(3, this.textureIds, 0);
            int width = frame.getWidth();
            int height = frame.getHeight();
            int paddedWidth = (width + 1) >> 1;
            int paddedHeight = (height + 1) >> 1;
            initializeTexture(33984, this.textureIds[0], width, height);
            initializeTexture(33985, this.textureIds[1], paddedWidth, paddedHeight);
            initializeTexture(33986, this.textureIds[2], paddedWidth, paddedHeight);
            this.textureWidth = frame.getWidth();
            this.textureHeight = frame.getHeight();
        }

        private void updateTextures(Frame frame) {
            int width = frame.getWidth();
            int height = frame.getHeight();
            int halfWidth = (width + 1) >> 1;
            int halfHeight = (height + 1) >> 1;
            int ySize = width * height;
            int uvSize = halfWidth * halfHeight;
            Buffer bb = frame.getBuffer();
            bb.clear();
            if (bb.remaining() == (uvSize * 2) + ySize) {
                bb.position(0);
                GLES20.glPixelStorei(3317, 1);
                GLES20.glPixelStorei(3333, 1);
                GLES20.glActiveTexture(33984);
                GLES20.glBindTexture(3553, this.textureIds[0]);
                GLES20.glTexSubImage2D(3553, 0, 0, 0, width, height, 6409, 5121, bb);
                bb.position(ySize);
                GLES20.glActiveTexture(33985);
                GLES20.glBindTexture(3553, this.textureIds[1]);
                GLES20.glTexSubImage2D(3553, 0, 0, 0, halfWidth, halfHeight, 6409, 5121, bb);
                bb.position(ySize + uvSize);
                GLES20.glActiveTexture(33986);
                GLES20.glBindTexture(3553, this.textureIds[2]);
                GLES20.glTexSubImage2D(3553, 0, 0, 0, halfWidth, halfHeight, 6409, 5121, bb);
                return;
            }
            this.textureWidth = 0;
            this.textureHeight = 0;
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private void renderFrameLoop(com.opentok.android.grafika.gles.WindowSurface r13) {
            /*
            r12 = this;
            r6 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
            r11 = 0;
            r10 = 0;
        L_0x0004:
            r7 = r12.lock;
            monitor-enter(r7);
            r5 = r12.surfaceTexture;	 Catch:{ all -> 0x009c }
            if (r5 != 0) goto L_0x000d;
        L_0x000b:
            monitor-exit(r7);	 Catch:{ all -> 0x009c }
            return;
        L_0x000d:
            monitor-exit(r7);	 Catch:{ all -> 0x009c }
            r5 = r12.frameLock;
            r5.lock();
            r5 = r12.currentFrame;
            if (r5 == 0) goto L_0x00ae;
        L_0x0017:
            r5 = r12.videoEnabled;
            if (r5 == 0) goto L_0x00ae;
        L_0x001b:
            r5 = r12.glProgram;
            android.opengl.GLES20.glUseProgram(r5);
            r5 = r12.textureWidth;
            r7 = r12.currentFrame;
            r7 = r7.getWidth();
            if (r5 != r7) goto L_0x0034;
        L_0x002a:
            r5 = r12.textureHeight;
            r7 = r12.currentFrame;
            r7 = r7.getHeight();
            if (r5 == r7) goto L_0x0039;
        L_0x0034:
            r5 = r12.currentFrame;
            r12.setupTextures(r5);
        L_0x0039:
            r5 = r12.currentFrame;
            r12.updateTextures(r5);
            r5 = r12.scaleMatrix;
            android.opengl.Matrix.setIdentityM(r5, r10);
            r2 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
            r3 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
            r5 = r12.currentFrame;
            r5 = r5.getWidth();
            r5 = (float) r5;
            r7 = r12.currentFrame;
            r7 = r7.getHeight();
            r7 = (float) r7;
            r1 = r5 / r7;
            r5 = r12.viewportWidth;
            r5 = (float) r5;
            r7 = r12.viewportHeight;
            r7 = (float) r7;
            r4 = r5 / r7;
            r5 = r12.videoFitEnabled;
            if (r5 == 0) goto L_0x00a2;
        L_0x0063:
            r5 = (r1 > r4 ? 1 : (r1 == r4 ? 0 : -1));
            if (r5 <= 0) goto L_0x009f;
        L_0x0067:
            r3 = r4 / r1;
        L_0x0069:
            r7 = r12.scaleMatrix;
            r5 = r12.currentFrame;
            r5 = r5.isMirroredX();
            if (r5 == 0) goto L_0x00ac;
        L_0x0073:
            r5 = -1082130432; // 0xffffffffbf800000 float:-1.0 double:NaN;
        L_0x0075:
            r5 = r5 * r2;
            android.opengl.Matrix.scaleM(r7, r10, r5, r3, r6);
            r5 = r12.glProgram;
            r7 = "uMVPMatrix";
            r0 = android.opengl.GLES20.glGetUniformLocation(r5, r7);
            r5 = 1;
            r7 = r12.scaleMatrix;
            android.opengl.GLES20.glUniformMatrix4fv(r0, r5, r10, r7, r10);
            r5 = 4;
            r7 = r12.vertexIndex;
            r7 = r7.length;
            r8 = 5123; // 0x1403 float:7.179E-42 double:2.531E-320;
            r9 = r12.drawListBuffer;
            android.opengl.GLES20.glDrawElements(r5, r7, r8, r9);
        L_0x0092:
            r5 = r12.frameLock;
            r5.unlock();
            r13.swapBuffers();
            goto L_0x0004;
        L_0x009c:
            r5 = move-exception;
            monitor-exit(r7);	 Catch:{ all -> 0x009c }
            throw r5;
        L_0x009f:
            r2 = r1 / r4;
            goto L_0x0069;
        L_0x00a2:
            r5 = (r1 > r4 ? 1 : (r1 == r4 ? 0 : -1));
            if (r5 >= 0) goto L_0x00a9;
        L_0x00a6:
            r3 = r4 / r1;
            goto L_0x0069;
        L_0x00a9:
            r2 = r1 / r4;
            goto L_0x0069;
        L_0x00ac:
            r5 = r6;
            goto L_0x0075;
        L_0x00ae:
            android.opengl.GLES20.glClearColor(r11, r11, r11, r6);
            r5 = 16384; // 0x4000 float:2.2959E-41 double:8.0948E-320;
            android.opengl.GLES20.glClear(r5);
            goto L_0x0092;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.opentok.android.TextureViewRenderer.Renderer.renderFrameLoop(com.opentok.android.grafika.gles.WindowSurface):void");
        }

        private int loadShader(int type, String shaderCode) {
            int shader = GLES20.glCreateShader(type);
            GLES20.glShaderSource(shader, shaderCode);
            GLES20.glCompileShader(shader);
            return shader;
        }

        private void setupgl() {
            GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
            GLES20.glClear(16384);
            int vertexShader = loadShader(35633, "uniform mat4 uMVPMatrix;attribute vec4 aPosition;\nattribute vec2 aTextureCoord;\nvarying vec2 vTextureCoord;\nvoid main() {\n  gl_Position = uMVPMatrix * aPosition;\n  vTextureCoord = aTextureCoord;\n}\n");
            int fragmentShader = loadShader(35632, "precision mediump float;\nuniform sampler2D Ytex;\nuniform sampler2D Utex,Vtex;\nvarying vec2 vTextureCoord;\nvoid main(void) {\n  float nx,ny,r,g,b,y,u,v;\n  mediump vec4 txl,ux,vx;  nx=vTextureCoord[0];\n  ny=vTextureCoord[1];\n  y=texture2D(Ytex,vec2(nx,ny)).r;\n  u=texture2D(Utex,vec2(nx,ny)).r;\n  v=texture2D(Vtex,vec2(nx,ny)).r;\n  y=1.1643*(y-0.0625);\n  u=u-0.5;\n  v=v-0.5;\n  r=y+1.5958*v;\n  g=y-0.39173*u-0.81290*v;\n  b=y+2.017*u;\n  gl_FragColor=vec4(r,g,b,1.0);\n}\n");
            this.glProgram = GLES20.glCreateProgram();
            GLES20.glAttachShader(this.glProgram, vertexShader);
            GLES20.glAttachShader(this.glProgram, fragmentShader);
            GLES20.glLinkProgram(this.glProgram);
            int positionHandle = GLES20.glGetAttribLocation(this.glProgram, "aPosition");
            int textureHandle = GLES20.glGetAttribLocation(this.glProgram, "aTextureCoord");
            GLES20.glVertexAttribPointer(positionHandle, 3, 5126, false, 12, this.vertexBuffer);
            GLES20.glEnableVertexAttribArray(positionHandle);
            GLES20.glVertexAttribPointer(textureHandle, 2, 5126, false, 8, this.textureBuffer);
            GLES20.glEnableVertexAttribArray(textureHandle);
            GLES20.glUseProgram(this.glProgram);
            GLES20.glUniform1i(GLES20.glGetUniformLocation(this.glProgram, "Ytex"), 0);
            GLES20.glUniform1i(GLES20.glGetUniformLocation(this.glProgram, "Utex"), 1);
            GLES20.glUniform1i(GLES20.glGetUniformLocation(this.glProgram, "Vtex"), 2);
            this.textureWidth = 0;
            this.textureHeight = 0;
        }
    }

    public TextureViewRenderer(Context context) {
        this.ctx = context;
        this.view = new TextureView(context);
        this.view.setSurfaceTextureListener(this.renderer);
        this.renderer.start();
    }

    public void onFrame(Frame frame) {
        this.renderer.displayFrame(frame);
    }

    public void setStyle(String key, String value) {
        if (!BaseVideoRenderer.STYLE_VIDEO_SCALE.equals(key)) {
            return;
        }
        if (BaseVideoRenderer.STYLE_VIDEO_FIT.equals(value)) {
            this.renderer.enableVideoFit(true);
        } else if (BaseVideoRenderer.STYLE_VIDEO_FILL.equals(value)) {
            this.renderer.enableVideoFit(false);
        }
    }

    public void onVideoPropertiesChanged(boolean videoEnabled) {
        this.renderer.setEnableVideo(videoEnabled);
    }

    public View getView() {
        return this.view;
    }

    public void onPause() {
        this.videoLastStatus = this.renderer.isEnableVideo();
        this.renderer.setEnableVideo(false);
    }

    public void onResume() {
        this.renderer.setEnableVideo(this.videoLastStatus);
    }
}
