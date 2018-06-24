package org.webrtc;

import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.opengl.GLES20;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.Surface;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;
import org.webrtc.EglBase.Context;
import org.webrtc.RendererCommon.GlDrawer;
import org.webrtc.RendererCommon.YuvUploader;
import org.webrtc.VideoRenderer.Callbacks;
import org.webrtc.VideoRenderer.I420Frame;

public class EglRenderer implements Callbacks {
    private static final long LOG_INTERVAL_SEC = 4;
    private static final int MAX_SURFACE_CLEAR_COUNT = 3;
    private static final String TAG = "EglRenderer";
    private GlTextureFrameBuffer bitmapTextureFramebuffer;
    private GlDrawer drawer;
    private EglBase eglBase;
    private final EglSurfaceCreation eglSurfaceCreationRunnable = new EglSurfaceCreation();
    private final Object fpsReductionLock = new Object();
    private final Object frameListenerLock = new Object();
    private final ArrayList<ScaleAndFrameListener> frameListeners = new ArrayList();
    private final Object frameLock = new Object();
    private int framesDropped;
    private int framesReceived;
    private int framesRendered;
    private final Object handlerLock = new Object();
    private float layoutAspectRatio;
    private final Object layoutLock = new Object();
    private final Runnable logStatisticsRunnable = new C16162();
    private long minRenderPeriodNs;
    private boolean mirror;
    private final String name;
    private long nextFrameTimeNs;
    private I420Frame pendingFrame;
    private final Runnable renderFrameRunnable = new C16151();
    private long renderSwapBufferTimeNs;
    private Handler renderThreadHandler;
    private long renderTimeNs;
    private final Object statisticsLock = new Object();
    private long statisticsStartTimeNs;
    private int surfaceHeight;
    private int surfaceWidth;
    private int[] yuvTextures = null;
    private final YuvUploader yuvUploader = new YuvUploader();

    class C16151 implements Runnable {
        C16151() {
        }

        public void run() {
            EglRenderer.this.renderFrameOnRenderThread();
        }
    }

    class C16162 implements Runnable {
        C16162() {
        }

        public void run() {
            EglRenderer.this.logStatistics();
            synchronized (EglRenderer.this.handlerLock) {
                if (EglRenderer.this.renderThreadHandler != null) {
                    EglRenderer.this.renderThreadHandler.removeCallbacks(EglRenderer.this.logStatisticsRunnable);
                    EglRenderer.this.renderThreadHandler.postDelayed(EglRenderer.this.logStatisticsRunnable, TimeUnit.SECONDS.toMillis(4));
                }
            }
        }
    }

    class C16217 implements Runnable {
        C16217() {
        }

        public void run() {
            EglRenderer.this.clearSurfaceOnRenderThread();
        }
    }

    private class EglSurfaceCreation implements Runnable {
        private Object surface;

        private EglSurfaceCreation() {
        }

        public synchronized void setSurface(Object surface) {
            this.surface = surface;
        }

        public synchronized void run() {
            if (!(this.surface == null || EglRenderer.this.eglBase == null || EglRenderer.this.eglBase.hasSurface())) {
                if (this.surface instanceof Surface) {
                    EglRenderer.this.eglBase.createSurface((Surface) this.surface);
                } else if (this.surface instanceof SurfaceTexture) {
                    EglRenderer.this.eglBase.createSurface((SurfaceTexture) this.surface);
                } else {
                    throw new IllegalStateException("Invalid surface: " + this.surface);
                }
                EglRenderer.this.eglBase.makeCurrent();
                GLES20.glPixelStorei(3317, 1);
            }
        }
    }

    public interface FrameListener {
        void onFrame(Bitmap bitmap);
    }

    private static class ScaleAndFrameListener {
        public final FrameListener listener;
        public final float scale;

        public ScaleAndFrameListener(float scale, FrameListener listener) {
            this.scale = scale;
            this.listener = listener;
        }
    }

    public EglRenderer(String name) {
        this.name = name;
    }

    public void init(final Context sharedContext, final int[] configAttributes, GlDrawer drawer) {
        synchronized (this.handlerLock) {
            if (this.renderThreadHandler != null) {
                throw new IllegalStateException(this.name + "Already initialized");
            }
            logD("Initializing EglRenderer");
            this.drawer = drawer;
            HandlerThread renderThread = new HandlerThread(this.name + TAG);
            renderThread.start();
            this.renderThreadHandler = new Handler(renderThread.getLooper());
            ThreadUtils.invokeAtFrontUninterruptibly(this.renderThreadHandler, new Runnable() {
                public void run() {
                    if (sharedContext == null) {
                        EglRenderer.this.logD("EglBase10.create context");
                        EglRenderer.this.eglBase = new EglBase10(null, configAttributes);
                        return;
                    }
                    EglRenderer.this.logD("EglBase.create shared context");
                    EglRenderer.this.eglBase = EglBase.create(sharedContext, configAttributes);
                }
            });
            this.renderThreadHandler.post(this.eglSurfaceCreationRunnable);
            resetStatistics(System.nanoTime());
            this.renderThreadHandler.postDelayed(this.logStatisticsRunnable, TimeUnit.SECONDS.toMillis(4));
        }
    }

    public void createEglSurface(Surface surface) {
        createEglSurfaceInternal(surface);
    }

    public void createEglSurface(SurfaceTexture surfaceTexture) {
        createEglSurfaceInternal(surfaceTexture);
    }

    private void createEglSurfaceInternal(Object surface) {
        this.eglSurfaceCreationRunnable.setSurface(surface);
        postToRenderThread(this.eglSurfaceCreationRunnable);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void release() {
        /*
        r5 = this;
        r2 = "Releasing.";
        r5.logD(r2);
        r0 = new java.util.concurrent.CountDownLatch;
        r2 = 1;
        r0.<init>(r2);
        r3 = r5.handlerLock;
        monitor-enter(r3);
        r2 = r5.renderThreadHandler;	 Catch:{ all -> 0x0057 }
        if (r2 != 0) goto L_0x0019;
    L_0x0012:
        r2 = "Already released";
        r5.logD(r2);	 Catch:{ all -> 0x0057 }
        monitor-exit(r3);	 Catch:{ all -> 0x0057 }
    L_0x0018:
        return;
    L_0x0019:
        r2 = r5.renderThreadHandler;	 Catch:{ all -> 0x0057 }
        r4 = r5.logStatisticsRunnable;	 Catch:{ all -> 0x0057 }
        r2.removeCallbacks(r4);	 Catch:{ all -> 0x0057 }
        r2 = r5.renderThreadHandler;	 Catch:{ all -> 0x0057 }
        r4 = new org.webrtc.EglRenderer$4;	 Catch:{ all -> 0x0057 }
        r4.<init>(r0);	 Catch:{ all -> 0x0057 }
        r2.postAtFrontOfQueue(r4);	 Catch:{ all -> 0x0057 }
        r2 = r5.renderThreadHandler;	 Catch:{ all -> 0x0057 }
        r1 = r2.getLooper();	 Catch:{ all -> 0x0057 }
        r2 = r5.renderThreadHandler;	 Catch:{ all -> 0x0057 }
        r4 = new org.webrtc.EglRenderer$5;	 Catch:{ all -> 0x0057 }
        r4.<init>(r1);	 Catch:{ all -> 0x0057 }
        r2.post(r4);	 Catch:{ all -> 0x0057 }
        r2 = 0;
        r5.renderThreadHandler = r2;	 Catch:{ all -> 0x0057 }
        monitor-exit(r3);	 Catch:{ all -> 0x0057 }
        org.webrtc.ThreadUtils.awaitUninterruptibly(r0);
        r3 = r5.frameLock;
        monitor-enter(r3);
        r2 = r5.pendingFrame;	 Catch:{ all -> 0x005a }
        if (r2 == 0) goto L_0x0050;
    L_0x0048:
        r2 = r5.pendingFrame;	 Catch:{ all -> 0x005a }
        org.webrtc.VideoRenderer.renderFrameDone(r2);	 Catch:{ all -> 0x005a }
        r2 = 0;
        r5.pendingFrame = r2;	 Catch:{ all -> 0x005a }
    L_0x0050:
        monitor-exit(r3);	 Catch:{ all -> 0x005a }
        r2 = "Releasing done.";
        r5.logD(r2);
        goto L_0x0018;
    L_0x0057:
        r2 = move-exception;
        monitor-exit(r3);	 Catch:{ all -> 0x0057 }
        throw r2;
    L_0x005a:
        r2 = move-exception;
        monitor-exit(r3);	 Catch:{ all -> 0x005a }
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.webrtc.EglRenderer.release():void");
    }

    private void resetStatistics(long currentTimeNs) {
        synchronized (this.statisticsLock) {
            this.statisticsStartTimeNs = currentTimeNs;
            this.framesReceived = 0;
            this.framesDropped = 0;
            this.framesRendered = 0;
            this.renderTimeNs = 0;
            this.renderSwapBufferTimeNs = 0;
        }
    }

    public void printStackTrace() {
        synchronized (this.handlerLock) {
            Thread renderThread;
            if (this.renderThreadHandler == null) {
                renderThread = null;
            } else {
                renderThread = this.renderThreadHandler.getLooper().getThread();
            }
            if (renderThread != null) {
                StackTraceElement[] renderStackTrace = renderThread.getStackTrace();
                if (renderStackTrace.length > 0) {
                    logD("EglRenderer stack trace:");
                    for (StackTraceElement traceElem : renderStackTrace) {
                        logD(traceElem.toString());
                    }
                }
            }
        }
    }

    public void setMirror(boolean mirror) {
        logD("setMirror: " + mirror);
        synchronized (this.layoutLock) {
            this.mirror = mirror;
        }
    }

    public void setLayoutAspectRatio(float layoutAspectRatio) {
        logD("setLayoutAspectRatio: " + layoutAspectRatio);
        synchronized (this.layoutLock) {
            this.layoutAspectRatio = layoutAspectRatio;
        }
    }

    public void setFpsReduction(float fps) {
        logD("setFpsReduction: " + fps);
        synchronized (this.fpsReductionLock) {
            long previousRenderPeriodNs = this.minRenderPeriodNs;
            if (fps <= 0.0f) {
                this.minRenderPeriodNs = Long.MAX_VALUE;
            } else {
                this.minRenderPeriodNs = (long) (((float) TimeUnit.SECONDS.toNanos(1)) / fps);
            }
            if (this.minRenderPeriodNs != previousRenderPeriodNs) {
                this.nextFrameTimeNs = System.nanoTime();
            }
        }
    }

    public void disableFpsReduction() {
        setFpsReduction(Float.POSITIVE_INFINITY);
    }

    public void pauseVideo() {
        setFpsReduction(0.0f);
    }

    public void addFrameListener(FrameListener listener, float scale) {
        synchronized (this.frameListenerLock) {
            this.frameListeners.add(new ScaleAndFrameListener(scale, listener));
        }
    }

    public void removeFrameListener(FrameListener listener) {
        synchronized (this.frameListenerLock) {
            Iterator<ScaleAndFrameListener> iter = this.frameListeners.iterator();
            while (iter.hasNext()) {
                if (((ScaleAndFrameListener) iter.next()).listener == listener) {
                    iter.remove();
                }
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void renderFrame(org.webrtc.VideoRenderer.I420Frame r11) {
        /*
        r10 = this;
        r4 = r10.statisticsLock;
        monitor-enter(r4);
        r3 = r10.framesReceived;	 Catch:{ all -> 0x001b }
        r3 = r3 + 1;
        r10.framesReceived = r3;	 Catch:{ all -> 0x001b }
        monitor-exit(r4);	 Catch:{ all -> 0x001b }
        r4 = r10.handlerLock;
        monitor-enter(r4);
        r3 = r10.renderThreadHandler;	 Catch:{ all -> 0x003e }
        if (r3 != 0) goto L_0x001e;
    L_0x0011:
        r3 = "Dropping frame - Not initialized or already released.";
        r10.logD(r3);	 Catch:{ all -> 0x003e }
        org.webrtc.VideoRenderer.renderFrameDone(r11);	 Catch:{ all -> 0x003e }
        monitor-exit(r4);	 Catch:{ all -> 0x003e }
    L_0x001a:
        return;
    L_0x001b:
        r3 = move-exception;
        monitor-exit(r4);	 Catch:{ all -> 0x001b }
        throw r3;
    L_0x001e:
        r5 = r10.fpsReductionLock;	 Catch:{ all -> 0x003e }
        monitor-enter(r5);	 Catch:{ all -> 0x003e }
        r6 = r10.minRenderPeriodNs;	 Catch:{ all -> 0x007b }
        r8 = 0;
        r3 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1));
        if (r3 <= 0) goto L_0x0050;
    L_0x0029:
        r0 = java.lang.System.nanoTime();	 Catch:{ all -> 0x007b }
        r6 = r10.nextFrameTimeNs;	 Catch:{ all -> 0x007b }
        r3 = (r0 > r6 ? 1 : (r0 == r6 ? 0 : -1));
        if (r3 >= 0) goto L_0x0041;
    L_0x0033:
        r3 = "Dropping frame - fps reduction is active.";
        r10.logD(r3);	 Catch:{ all -> 0x007b }
        org.webrtc.VideoRenderer.renderFrameDone(r11);	 Catch:{ all -> 0x007b }
        monitor-exit(r5);	 Catch:{ all -> 0x007b }
        monitor-exit(r4);	 Catch:{ all -> 0x003e }
        goto L_0x001a;
    L_0x003e:
        r3 = move-exception;
        monitor-exit(r4);	 Catch:{ all -> 0x003e }
        throw r3;
    L_0x0041:
        r6 = r10.nextFrameTimeNs;	 Catch:{ all -> 0x007b }
        r8 = r10.minRenderPeriodNs;	 Catch:{ all -> 0x007b }
        r6 = r6 + r8;
        r10.nextFrameTimeNs = r6;	 Catch:{ all -> 0x007b }
        r6 = r10.nextFrameTimeNs;	 Catch:{ all -> 0x007b }
        r6 = java.lang.Math.max(r6, r0);	 Catch:{ all -> 0x007b }
        r10.nextFrameTimeNs = r6;	 Catch:{ all -> 0x007b }
    L_0x0050:
        monitor-exit(r5);	 Catch:{ all -> 0x007b }
        r5 = r10.frameLock;	 Catch:{ all -> 0x003e }
        monitor-enter(r5);	 Catch:{ all -> 0x003e }
        r3 = r10.pendingFrame;	 Catch:{ all -> 0x0080 }
        if (r3 == 0) goto L_0x007e;
    L_0x0058:
        r2 = 1;
    L_0x0059:
        if (r2 == 0) goto L_0x0060;
    L_0x005b:
        r3 = r10.pendingFrame;	 Catch:{ all -> 0x0080 }
        org.webrtc.VideoRenderer.renderFrameDone(r3);	 Catch:{ all -> 0x0080 }
    L_0x0060:
        r10.pendingFrame = r11;	 Catch:{ all -> 0x0080 }
        r3 = r10.renderThreadHandler;	 Catch:{ all -> 0x0080 }
        r6 = r10.renderFrameRunnable;	 Catch:{ all -> 0x0080 }
        r3.post(r6);	 Catch:{ all -> 0x0080 }
        monitor-exit(r5);	 Catch:{ all -> 0x0080 }
        monitor-exit(r4);	 Catch:{ all -> 0x003e }
        if (r2 == 0) goto L_0x001a;
    L_0x006d:
        r4 = r10.statisticsLock;
        monitor-enter(r4);
        r3 = r10.framesDropped;	 Catch:{ all -> 0x0078 }
        r3 = r3 + 1;
        r10.framesDropped = r3;	 Catch:{ all -> 0x0078 }
        monitor-exit(r4);	 Catch:{ all -> 0x0078 }
        goto L_0x001a;
    L_0x0078:
        r3 = move-exception;
        monitor-exit(r4);	 Catch:{ all -> 0x0078 }
        throw r3;
    L_0x007b:
        r3 = move-exception;
        monitor-exit(r5);	 Catch:{ all -> 0x007b }
        throw r3;	 Catch:{ all -> 0x003e }
    L_0x007e:
        r2 = 0;
        goto L_0x0059;
    L_0x0080:
        r3 = move-exception;
        monitor-exit(r5);	 Catch:{ all -> 0x0080 }
        throw r3;	 Catch:{ all -> 0x003e }
        */
        throw new UnsupportedOperationException("Method not decompiled: org.webrtc.EglRenderer.renderFrame(org.webrtc.VideoRenderer$I420Frame):void");
    }

    public void releaseEglSurface(final Runnable completionCallback) {
        this.eglSurfaceCreationRunnable.setSurface(null);
        synchronized (this.handlerLock) {
            if (this.renderThreadHandler != null) {
                this.renderThreadHandler.removeCallbacks(this.eglSurfaceCreationRunnable);
                this.renderThreadHandler.postAtFrontOfQueue(new Runnable() {
                    public void run() {
                        if (EglRenderer.this.eglBase != null) {
                            EglRenderer.this.eglBase.detachCurrent();
                            EglRenderer.this.eglBase.releaseSurface();
                        }
                        completionCallback.run();
                    }
                });
                return;
            }
            completionCallback.run();
        }
    }

    public void surfaceSizeChanged(int surfaceWidth, int surfaceHeight) {
        logD("Surface size changed: " + surfaceWidth + "x" + surfaceHeight);
        synchronized (this.layoutLock) {
            this.surfaceWidth = surfaceWidth;
            this.surfaceHeight = surfaceHeight;
        }
    }

    private void postToRenderThread(Runnable runnable) {
        synchronized (this.handlerLock) {
            if (this.renderThreadHandler != null) {
                this.renderThreadHandler.post(runnable);
            }
        }
    }

    private void clearSurfaceOnRenderThread() {
        if (this.eglBase != null && this.eglBase.hasSurface()) {
            logD("clearSurface");
            GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
            GLES20.glClear(16384);
            this.eglBase.swapBuffers();
        }
    }

    public void clearImage() {
        synchronized (this.handlerLock) {
            if (this.renderThreadHandler == null) {
                return;
            }
            this.renderThreadHandler.postAtFrontOfQueue(new C16217());
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void renderFrameOnRenderThread() {
        /*
        r23 = this;
        r0 = r23;
        r3 = r0.frameLock;
        monitor-enter(r3);
        r0 = r23;
        r2 = r0.pendingFrame;	 Catch:{ all -> 0x0032 }
        if (r2 != 0) goto L_0x000d;
    L_0x000b:
        monitor-exit(r3);	 Catch:{ all -> 0x0032 }
    L_0x000c:
        return;
    L_0x000d:
        r0 = r23;
        r14 = r0.pendingFrame;	 Catch:{ all -> 0x0032 }
        r2 = 0;
        r0 = r23;
        r0.pendingFrame = r2;	 Catch:{ all -> 0x0032 }
        monitor-exit(r3);	 Catch:{ all -> 0x0032 }
        r0 = r23;
        r2 = r0.eglBase;
        if (r2 == 0) goto L_0x0027;
    L_0x001d:
        r0 = r23;
        r2 = r0.eglBase;
        r2 = r2.hasSurface();
        if (r2 != 0) goto L_0x0035;
    L_0x0027:
        r2 = "Dropping frame - No surface";
        r0 = r23;
        r0.logD(r2);
        org.webrtc.VideoRenderer.renderFrameDone(r14);
        goto L_0x000c;
    L_0x0032:
        r2 = move-exception;
        monitor-exit(r3);	 Catch:{ all -> 0x0032 }
        throw r2;
    L_0x0035:
        r18 = java.lang.System.nanoTime();
        r2 = r14.samplingMatrix;
        r3 = r14.rotationDegree;
        r3 = (float) r3;
        r22 = org.webrtc.RendererCommon.rotateTextureMatrix(r2, r3);
        r0 = r23;
        r3 = r0.layoutLock;
        monitor-enter(r3);
        r17 = 0;
    L_0x0049:
        r0 = r23;
        r2 = r0.eglBase;	 Catch:{ all -> 0x0078 }
        r2 = r2.surfaceWidth();	 Catch:{ all -> 0x0078 }
        r0 = r23;
        r4 = r0.surfaceWidth;	 Catch:{ all -> 0x0078 }
        if (r2 != r4) goto L_0x0065;
    L_0x0057:
        r0 = r23;
        r2 = r0.eglBase;	 Catch:{ all -> 0x0078 }
        r2 = r2.surfaceHeight();	 Catch:{ all -> 0x0078 }
        r0 = r23;
        r4 = r0.surfaceHeight;	 Catch:{ all -> 0x0078 }
        if (r2 == r4) goto L_0x00d0;
    L_0x0065:
        r17 = r17 + 1;
        r2 = 3;
        r0 = r17;
        if (r0 <= r2) goto L_0x007b;
    L_0x006c:
        r2 = "Failed to get surface of expected size - dropping frame.";
        r0 = r23;
        r0.logD(r2);	 Catch:{ all -> 0x0078 }
        org.webrtc.VideoRenderer.renderFrameDone(r14);	 Catch:{ all -> 0x0078 }
        monitor-exit(r3);	 Catch:{ all -> 0x0078 }
        goto L_0x000c;
    L_0x0078:
        r2 = move-exception;
        monitor-exit(r3);	 Catch:{ all -> 0x0078 }
        throw r2;
    L_0x007b:
        r2 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0078 }
        r2.<init>();	 Catch:{ all -> 0x0078 }
        r4 = "Surface size mismatch - clearing surface. Size: ";
        r2 = r2.append(r4);	 Catch:{ all -> 0x0078 }
        r0 = r23;
        r4 = r0.eglBase;	 Catch:{ all -> 0x0078 }
        r4 = r4.surfaceWidth();	 Catch:{ all -> 0x0078 }
        r2 = r2.append(r4);	 Catch:{ all -> 0x0078 }
        r4 = "x";
        r2 = r2.append(r4);	 Catch:{ all -> 0x0078 }
        r0 = r23;
        r4 = r0.eglBase;	 Catch:{ all -> 0x0078 }
        r4 = r4.surfaceHeight();	 Catch:{ all -> 0x0078 }
        r2 = r2.append(r4);	 Catch:{ all -> 0x0078 }
        r4 = " Expected: ";
        r2 = r2.append(r4);	 Catch:{ all -> 0x0078 }
        r0 = r23;
        r4 = r0.surfaceWidth;	 Catch:{ all -> 0x0078 }
        r2 = r2.append(r4);	 Catch:{ all -> 0x0078 }
        r4 = "x";
        r2 = r2.append(r4);	 Catch:{ all -> 0x0078 }
        r0 = r23;
        r4 = r0.surfaceHeight;	 Catch:{ all -> 0x0078 }
        r2 = r2.append(r4);	 Catch:{ all -> 0x0078 }
        r2 = r2.toString();	 Catch:{ all -> 0x0078 }
        r0 = r23;
        r0.logD(r2);	 Catch:{ all -> 0x0078 }
        r23.clearSurfaceOnRenderThread();	 Catch:{ all -> 0x0078 }
        goto L_0x0049;
    L_0x00d0:
        r0 = r23;
        r2 = r0.layoutAspectRatio;	 Catch:{ all -> 0x0078 }
        r4 = 0;
        r2 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1));
        if (r2 <= 0) goto L_0x0129;
    L_0x00d9:
        r0 = r23;
        r2 = r0.mirror;	 Catch:{ all -> 0x0078 }
        r4 = r14.rotatedWidth();	 Catch:{ all -> 0x0078 }
        r4 = (float) r4;	 Catch:{ all -> 0x0078 }
        r5 = r14.rotatedHeight();	 Catch:{ all -> 0x0078 }
        r5 = (float) r5;	 Catch:{ all -> 0x0078 }
        r4 = r4 / r5;
        r0 = r23;
        r5 = r0.layoutAspectRatio;	 Catch:{ all -> 0x0078 }
        r16 = org.webrtc.RendererCommon.getLayoutMatrix(r2, r4, r5);	 Catch:{ all -> 0x0078 }
    L_0x00f0:
        r0 = r22;
        r1 = r16;
        r11 = org.webrtc.RendererCommon.multiplyMatrices(r0, r1);	 Catch:{ all -> 0x0078 }
        monitor-exit(r3);	 Catch:{ all -> 0x0078 }
        r2 = 0;
        r3 = 0;
        r4 = 0;
        r5 = 0;
        android.opengl.GLES20.glClearColor(r2, r3, r4, r5);
        r2 = 16384; // 0x4000 float:2.2959E-41 double:8.0948E-320;
        android.opengl.GLES20.glClear(r2);
        r2 = r14.yuvFrame;
        if (r2 == 0) goto L_0x01ab;
    L_0x0109:
        r0 = r23;
        r2 = r0.yuvTextures;
        if (r2 != 0) goto L_0x0139;
    L_0x010f:
        r2 = 3;
        r2 = new int[r2];
        r0 = r23;
        r0.yuvTextures = r2;
        r15 = 0;
    L_0x0117:
        r2 = 3;
        if (r15 >= r2) goto L_0x0139;
    L_0x011a:
        r0 = r23;
        r2 = r0.yuvTextures;
        r3 = 3553; // 0xde1 float:4.979E-42 double:1.7554E-320;
        r3 = org.webrtc.GlUtil.generateTexture(r3);
        r2[r15] = r3;
        r15 = r15 + 1;
        goto L_0x0117;
    L_0x0129:
        r0 = r23;
        r2 = r0.mirror;	 Catch:{ all -> 0x0078 }
        if (r2 == 0) goto L_0x0134;
    L_0x012f:
        r16 = org.webrtc.RendererCommon.horizontalFlipMatrix();	 Catch:{ all -> 0x0078 }
    L_0x0133:
        goto L_0x00f0;
    L_0x0134:
        r16 = org.webrtc.RendererCommon.identityMatrix();	 Catch:{ all -> 0x0078 }
        goto L_0x0133;
    L_0x0139:
        r0 = r23;
        r2 = r0.yuvUploader;
        r0 = r23;
        r3 = r0.yuvTextures;
        r4 = r14.width;
        r5 = r14.height;
        r6 = r14.yuvStrides;
        r7 = r14.yuvPlanes;
        r2.uploadYuvData(r3, r4, r5, r6, r7);
        r0 = r23;
        r2 = r0.drawer;
        r0 = r23;
        r3 = r0.yuvTextures;
        r5 = r14.rotatedWidth();
        r6 = r14.rotatedHeight();
        r7 = 0;
        r8 = 0;
        r0 = r23;
        r9 = r0.surfaceWidth;
        r0 = r23;
        r10 = r0.surfaceHeight;
        r4 = r11;
        r2.drawYuv(r3, r4, r5, r6, r7, r8, r9, r10);
    L_0x016a:
        r20 = java.lang.System.nanoTime();
        r0 = r23;
        r2 = r0.eglBase;
        r2.swapBuffers();
        r12 = java.lang.System.nanoTime();
        r0 = r23;
        r3 = r0.statisticsLock;
        monitor-enter(r3);
        r0 = r23;
        r2 = r0.framesRendered;	 Catch:{ all -> 0x01c8 }
        r2 = r2 + 1;
        r0 = r23;
        r0.framesRendered = r2;	 Catch:{ all -> 0x01c8 }
        r0 = r23;
        r4 = r0.renderTimeNs;	 Catch:{ all -> 0x01c8 }
        r6 = r12 - r18;
        r4 = r4 + r6;
        r0 = r23;
        r0.renderTimeNs = r4;	 Catch:{ all -> 0x01c8 }
        r0 = r23;
        r4 = r0.renderSwapBufferTimeNs;	 Catch:{ all -> 0x01c8 }
        r6 = r12 - r20;
        r4 = r4 + r6;
        r0 = r23;
        r0.renderSwapBufferTimeNs = r4;	 Catch:{ all -> 0x01c8 }
        monitor-exit(r3);	 Catch:{ all -> 0x01c8 }
        r0 = r23;
        r1 = r22;
        r0.notifyCallbacks(r14, r1);
        org.webrtc.VideoRenderer.renderFrameDone(r14);
        goto L_0x000c;
    L_0x01ab:
        r0 = r23;
        r2 = r0.drawer;
        r3 = r14.textureId;
        r5 = r14.rotatedWidth();
        r6 = r14.rotatedHeight();
        r7 = 0;
        r8 = 0;
        r0 = r23;
        r9 = r0.surfaceWidth;
        r0 = r23;
        r10 = r0.surfaceHeight;
        r4 = r11;
        r2.drawOes(r3, r4, r5, r6, r7, r8, r9, r10);
        goto L_0x016a;
    L_0x01c8:
        r2 = move-exception;
        monitor-exit(r3);	 Catch:{ all -> 0x01c8 }
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.webrtc.EglRenderer.renderFrameOnRenderThread():void");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void notifyCallbacks(org.webrtc.VideoRenderer.I420Frame r18, float[] r19) {
        /*
        r17 = this;
        r0 = r17;
        r2 = r0.frameListenerLock;
        monitor-enter(r2);
        r0 = r17;
        r1 = r0.frameListeners;	 Catch:{ all -> 0x0067 }
        r1 = r1.isEmpty();	 Catch:{ all -> 0x0067 }
        if (r1 == 0) goto L_0x0011;
    L_0x000f:
        monitor-exit(r2);	 Catch:{ all -> 0x0067 }
    L_0x0010:
        return;
    L_0x0011:
        r15 = new java.util.ArrayList;	 Catch:{ all -> 0x0067 }
        r0 = r17;
        r1 = r0.frameListeners;	 Catch:{ all -> 0x0067 }
        r15.<init>(r1);	 Catch:{ all -> 0x0067 }
        r0 = r17;
        r1 = r0.frameListeners;	 Catch:{ all -> 0x0067 }
        r1.clear();	 Catch:{ all -> 0x0067 }
        monitor-exit(r2);	 Catch:{ all -> 0x0067 }
        r0 = r17;
        r1 = r0.mirror;
        if (r1 == 0) goto L_0x006a;
    L_0x0028:
        r1 = org.webrtc.RendererCommon.horizontalFlipMatrix();
    L_0x002c:
        r0 = r19;
        r1 = org.webrtc.RendererCommon.multiplyMatrices(r0, r1);
        r2 = org.webrtc.RendererCommon.verticalFlipMatrix();
        r3 = org.webrtc.RendererCommon.multiplyMatrices(r1, r2);
        r16 = r15.iterator();
    L_0x003e:
        r1 = r16.hasNext();
        if (r1 == 0) goto L_0x0010;
    L_0x0044:
        r14 = r16.next();
        r14 = (org.webrtc.EglRenderer.ScaleAndFrameListener) r14;
        r1 = r14.scale;
        r2 = r18.rotatedWidth();
        r2 = (float) r2;
        r1 = r1 * r2;
        r8 = (int) r1;
        r1 = r14.scale;
        r2 = r18.rotatedHeight();
        r2 = (float) r2;
        r1 = r1 * r2;
        r9 = (int) r1;
        if (r8 == 0) goto L_0x0060;
    L_0x005e:
        if (r9 != 0) goto L_0x006f;
    L_0x0060:
        r1 = r14.listener;
        r2 = 0;
        r1.onFrame(r2);
        goto L_0x003e;
    L_0x0067:
        r1 = move-exception;
        monitor-exit(r2);	 Catch:{ all -> 0x0067 }
        throw r1;
    L_0x006a:
        r1 = org.webrtc.RendererCommon.identityMatrix();
        goto L_0x002c;
    L_0x006f:
        r0 = r17;
        r1 = r0.bitmapTextureFramebuffer;
        if (r1 != 0) goto L_0x0080;
    L_0x0075:
        r1 = new org.webrtc.GlTextureFrameBuffer;
        r2 = 6408; // 0x1908 float:8.98E-42 double:3.166E-320;
        r1.<init>(r2);
        r0 = r17;
        r0.bitmapTextureFramebuffer = r1;
    L_0x0080:
        r0 = r17;
        r1 = r0.bitmapTextureFramebuffer;
        r1.setSize(r8, r9);
        r1 = 36160; // 0x8d40 float:5.0671E-41 double:1.78654E-319;
        r0 = r17;
        r2 = r0.bitmapTextureFramebuffer;
        r2 = r2.getFrameBufferId();
        android.opengl.GLES20.glBindFramebuffer(r1, r2);
        r1 = 36160; // 0x8d40 float:5.0671E-41 double:1.78654E-319;
        r2 = 36064; // 0x8ce0 float:5.0536E-41 double:1.7818E-319;
        r4 = 3553; // 0xde1 float:4.979E-42 double:1.7554E-320;
        r0 = r17;
        r5 = r0.bitmapTextureFramebuffer;
        r5 = r5.getTextureId();
        r6 = 0;
        android.opengl.GLES20.glFramebufferTexture2D(r1, r2, r4, r5, r6);
        r0 = r18;
        r1 = r0.yuvFrame;
        if (r1 == 0) goto L_0x00f6;
    L_0x00af:
        r0 = r17;
        r1 = r0.drawer;
        r0 = r17;
        r2 = r0.yuvTextures;
        r4 = r18.rotatedWidth();
        r5 = r18.rotatedHeight();
        r6 = 0;
        r7 = 0;
        r1.drawYuv(r2, r3, r4, r5, r6, r7, r8, r9);
    L_0x00c4:
        r1 = r8 * r9;
        r1 = r1 * 4;
        r12 = java.nio.ByteBuffer.allocateDirect(r1);
        r1 = 0;
        r2 = 0;
        android.opengl.GLES20.glViewport(r1, r2, r8, r9);
        r6 = 0;
        r7 = 0;
        r10 = 6408; // 0x1908 float:8.98E-42 double:3.166E-320;
        r11 = 5121; // 0x1401 float:7.176E-42 double:2.53E-320;
        android.opengl.GLES20.glReadPixels(r6, r7, r8, r9, r10, r11, r12);
        r1 = 36160; // 0x8d40 float:5.0671E-41 double:1.78654E-319;
        r2 = 0;
        android.opengl.GLES20.glBindFramebuffer(r1, r2);
        r1 = "EglRenderer.notifyCallbacks";
        org.webrtc.GlUtil.checkNoGLES2Error(r1);
        r1 = android.graphics.Bitmap.Config.ARGB_8888;
        r13 = android.graphics.Bitmap.createBitmap(r8, r9, r1);
        r13.copyPixelsFromBuffer(r12);
        r1 = r14.listener;
        r1.onFrame(r13);
        goto L_0x003e;
    L_0x00f6:
        r0 = r17;
        r1 = r0.drawer;
        r0 = r18;
        r2 = r0.textureId;
        r4 = r18.rotatedWidth();
        r5 = r18.rotatedHeight();
        r6 = 0;
        r7 = 0;
        r1.drawOes(r2, r3, r4, r5, r6, r7, r8, r9);
        goto L_0x00c4;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.webrtc.EglRenderer.notifyCallbacks(org.webrtc.VideoRenderer$I420Frame, float[]):void");
    }

    private String averageTimeAsString(long sumTimeNs, int count) {
        return count <= 0 ? "NA" : TimeUnit.NANOSECONDS.toMicros(sumTimeNs / ((long) count)) + " μs";
    }

    private void logStatistics() {
        long currentTimeNs = System.nanoTime();
        synchronized (this.statisticsLock) {
            long elapsedTimeNs = currentTimeNs - this.statisticsStartTimeNs;
            if (elapsedTimeNs <= 0) {
                return;
            }
            float renderFps = ((float) (((long) this.framesRendered) * TimeUnit.SECONDS.toNanos(1))) / ((float) elapsedTimeNs);
            logD("Duration: " + TimeUnit.NANOSECONDS.toMillis(elapsedTimeNs) + " ms." + " Frames received: " + this.framesReceived + "." + " Dropped: " + this.framesDropped + "." + " Rendered: " + this.framesRendered + "." + " Render fps: " + String.format("%.1f", new Object[]{Float.valueOf(renderFps)}) + "." + " Average render time: " + averageTimeAsString(this.renderTimeNs, this.framesRendered) + "." + " Average swapBuffer time: " + averageTimeAsString(this.renderSwapBufferTimeNs, this.framesRendered) + ".");
            resetStatistics(currentTimeNs);
        }
    }

    private void logD(String string) {
        Logging.m172d(TAG, this.name + string);
    }
}
