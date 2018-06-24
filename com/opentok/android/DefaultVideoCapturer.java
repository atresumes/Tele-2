package com.opentok.android;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PreviewCallback;
import android.os.Build;
import android.os.Handler;
import android.view.Display;
import android.view.WindowManager;
import com.opentok.android.BaseVideoCapturer.CaptureSettings;
import com.opentok.android.BaseVideoCapturer.CaptureSwitch;
import com.opentok.android.OtLog.LogToken;
import com.opentok.android.Publisher.CameraCaptureFrameRate;
import com.opentok.android.Publisher.CameraCaptureResolution;
import com.opentok.android.VideoUtils.Size;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import org.objectweb.asm.Opcodes;

class DefaultVideoCapturer extends BaseVideoCapturer implements PreviewCallback, CaptureSwitch {
    private static final int PIXEL_FORMAT = 17;
    private static final LogToken log = new LogToken("[camera]", false);
    private boolean blackFrames;
    private Camera camera;
    private int cameraIndex;
    private int[] captureFpsRange;
    private int captureHeight;
    private int captureWidth;
    private CameraInfo currentDeviceInfo;
    private Display currentDisplay;
    private int expectedFrameSize;
    int fps;
    int[] frame;
    Handler handler;
    int height;
    private boolean isCapturePaused;
    private boolean isCaptureRunning;
    private boolean isCaptureStarted;
    Runnable newFrame;
    private final int numCaptureBuffers;
    PixelFormat pixelFormat;
    private CameraCaptureFrameRate preferredFramerate;
    private CameraCaptureResolution preferredResolution;
    public ReentrantLock previewBufferLock;
    private Publisher publisher;
    private SurfaceTexture surfaceTexture;
    int width;

    class C09711 implements Runnable {
        C09711() {
        }

        public void run() {
            if (DefaultVideoCapturer.this.isCaptureRunning) {
                if (DefaultVideoCapturer.this.frame == null) {
                    Size resolution = new Size();
                    resolution = DefaultVideoCapturer.this.getPreferredResolution();
                    DefaultVideoCapturer.this.fps = DefaultVideoCapturer.this.getPreferredFramerate();
                    DefaultVideoCapturer.this.width = resolution.width;
                    DefaultVideoCapturer.this.height = resolution.height;
                    DefaultVideoCapturer.this.frame = new int[(DefaultVideoCapturer.this.width * DefaultVideoCapturer.this.height)];
                }
                DefaultVideoCapturer.this.provideIntArrayFrame(DefaultVideoCapturer.this.frame, 2, DefaultVideoCapturer.this.width, DefaultVideoCapturer.this.height, 0, false);
                DefaultVideoCapturer.this.handler.postDelayed(DefaultVideoCapturer.this.newFrame, (long) (1000 / DefaultVideoCapturer.this.fps));
            }
        }
    }

    private boolean forceCameraRelease(int r4) {
        /* JADX: method processing error */
/*
Error: java.util.NoSuchElementException
	at java.util.HashMap$HashIterator.nextNode(HashMap.java:1431)
	at java.util.HashMap$KeyIterator.next(HashMap.java:1453)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.applyRemove(BlockFinallyExtract.java:535)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.extractFinally(BlockFinallyExtract.java:175)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.processExceptionHandler(BlockFinallyExtract.java:79)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.visit(BlockFinallyExtract.java:51)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
        /*
        r3 = this;
        r0 = 0;
        r0 = android.hardware.Camera.open(r4);	 Catch:{ RuntimeException -> 0x000c, all -> 0x0014 }
        if (r0 == 0) goto L_0x000a;
    L_0x0007:
        r0.release();
    L_0x000a:
        r2 = 0;
    L_0x000b:
        return r2;
    L_0x000c:
        r1 = move-exception;
        r2 = 1;
        if (r0 == 0) goto L_0x000b;
    L_0x0010:
        r0.release();
        goto L_0x000b;
    L_0x0014:
        r2 = move-exception;
        if (r0 == 0) goto L_0x001a;
    L_0x0017:
        r0.release();
    L_0x001a:
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.opentok.android.DefaultVideoCapturer.forceCameraRelease(int):boolean");
    }

    public DefaultVideoCapturer(Context context, CameraCaptureResolution resolution, CameraCaptureFrameRate fps) {
        this.cameraIndex = 0;
        this.currentDeviceInfo = null;
        this.previewBufferLock = new ReentrantLock();
        this.pixelFormat = new PixelFormat();
        this.isCaptureStarted = false;
        this.isCaptureRunning = false;
        this.numCaptureBuffers = 3;
        this.expectedFrameSize = 0;
        this.captureWidth = -1;
        this.captureHeight = -1;
        this.blackFrames = false;
        this.isCapturePaused = false;
        this.preferredResolution = CameraCaptureResolution.defaultResolution();
        this.preferredFramerate = CameraCaptureFrameRate.defaultFrameRate();
        this.fps = 1;
        this.width = 0;
        this.height = 0;
        this.handler = new Handler();
        this.newFrame = new C09711();
        this.cameraIndex = getCameraIndexUsingFront(true);
        this.currentDisplay = ((WindowManager) context.getSystemService("window")).getDefaultDisplay();
        this.preferredFramerate = fps;
        this.preferredResolution = resolution;
    }

    public synchronized void init() {
        log.m23w("init() enetered", new Object[0]);
        try {
            this.camera = Camera.open(this.cameraIndex);
        } catch (RuntimeException exp) {
            log.m18e(exp, "The camera is in use by another app", new Object[0]);
            this.publisher.onCameraFailed();
        }
        this.currentDeviceInfo = new CameraInfo();
        Camera.getCameraInfo(this.cameraIndex, this.currentDeviceInfo);
        log.m23w("init() exit", new Object[0]);
    }

    public synchronized int startCapture() {
        int i = -1;
        synchronized (this) {
            log.m23w("started() entered", new Object[0]);
            if (!this.isCaptureStarted) {
                if (this.camera != null) {
                    Size resolution = getPreferredResolution();
                    if (configureCaptureSize(resolution.width, resolution.height) != null) {
                        Parameters parameters = this.camera.getParameters();
                        parameters.setPreviewSize(this.captureWidth, this.captureHeight);
                        parameters.setPreviewFormat(17);
                        parameters.setPreviewFpsRange(this.captureFpsRange[0], this.captureFpsRange[1]);
                        try {
                            this.camera.setParameters(parameters);
                            PixelFormat.getPixelFormatInfo(17, this.pixelFormat);
                            int bufSize = ((this.captureWidth * this.captureHeight) * this.pixelFormat.bitsPerPixel) / 8;
                            for (int i2 = 0; i2 < 3; i2++) {
                                this.camera.addCallbackBuffer(new byte[bufSize]);
                            }
                            try {
                                this.surfaceTexture = new SurfaceTexture(42);
                                this.camera.setPreviewTexture(this.surfaceTexture);
                                this.camera.setPreviewCallbackWithBuffer(this);
                                this.camera.startPreview();
                                this.previewBufferLock.lock();
                                this.expectedFrameSize = bufSize;
                                this.previewBufferLock.unlock();
                            } catch (Exception e) {
                                this.publisher.onCameraFailed();
                                e.printStackTrace();
                            }
                        } catch (RuntimeException exp) {
                            log.m18e(exp, "Camera.setParameters(parameters) - failed", new Object[0]);
                            this.publisher.onCameraFailed();
                        }
                    }
                } else {
                    this.blackFrames = true;
                    this.handler.postDelayed(this.newFrame, (long) (1000 / this.fps));
                }
                this.isCaptureRunning = true;
                this.isCaptureStarted = true;
                log.m23w("started() exit", new Object[0]);
                i = 0;
            }
        }
        return i;
    }

    public synchronized int stopCapture() {
        int i = 0;
        synchronized (this) {
            if (this.camera != null) {
                this.previewBufferLock.lock();
                try {
                    if (this.isCaptureRunning) {
                        this.isCaptureRunning = false;
                        this.camera.stopPreview();
                        this.camera.setPreviewCallbackWithBuffer(null);
                        this.camera.release();
                        log.m15d("Camera capture is stopped", new Object[0]);
                    }
                    this.previewBufferLock.unlock();
                } catch (RuntimeException exp) {
                    log.m18e(exp, "Camera.stopPreview() - failed ", new Object[0]);
                    this.publisher.onCameraFailed();
                    i = -1;
                }
            }
            this.isCaptureStarted = false;
            if (this.blackFrames) {
                this.handler.removeCallbacks(this.newFrame);
            }
        }
        return i;
    }

    public void destroy() {
    }

    public boolean isCaptureStarted() {
        return this.isCaptureStarted;
    }

    public CaptureSettings getCaptureSettings() {
        CaptureSettings settings = new CaptureSettings();
        Size resolution = getPreferredResolution();
        int framerate = getPreferredFramerate();
        if (this.camera == null || configureCaptureSize(resolution.width, resolution.height) == null) {
            settings.fps = framerate;
            settings.width = resolution.width;
            settings.height = resolution.height;
            settings.format = 2;
        } else {
            settings.fps = framerate;
            settings.width = resolution.width;
            settings.height = resolution.height;
            settings.format = 1;
            settings.expectedDelay = 0;
        }
        return settings;
    }

    public synchronized void onPause() {
        if (this.isCaptureStarted) {
            this.isCapturePaused = true;
            stopCapture();
        }
    }

    public void onResume() {
        if (this.isCapturePaused) {
            init();
            startCapture();
            this.isCapturePaused = false;
        }
    }

    private int getNaturalCameraOrientation() {
        if (this.currentDeviceInfo != null) {
            return this.currentDeviceInfo.orientation;
        }
        return 0;
    }

    public boolean isFrontCamera() {
        if (this.currentDeviceInfo == null) {
            return false;
        }
        if (this.currentDeviceInfo.facing == 1) {
            return true;
        }
        return false;
    }

    public int getCameraIndex() {
        return this.cameraIndex;
    }

    public synchronized void cycleCamera() {
        swapCamera((getCameraIndex() + 1) % Camera.getNumberOfCameras());
    }

    @SuppressLint({"DefaultLocale"})
    public synchronized void swapCamera(int index) {
        boolean wasStarted = this.isCaptureStarted;
        if (this.camera != null) {
            stopCapture();
            this.camera.release();
            this.camera = null;
        }
        this.cameraIndex = index;
        if (wasStarted) {
            if (-1 != Build.MODEL.toLowerCase().indexOf("samsung")) {
                forceCameraRelease(index);
            }
            this.camera = Camera.open(index);
            this.currentDeviceInfo = new CameraInfo();
            Camera.getCameraInfo(index, this.currentDeviceInfo);
            startCapture();
        }
    }

    private int compensateCameraRotation(int uiRotation) {
        int currentDeviceOrientation = 0;
        switch (uiRotation) {
            case 0:
                currentDeviceOrientation = 0;
                break;
            case 1:
                currentDeviceOrientation = 270;
                break;
            case 2:
                currentDeviceOrientation = Opcodes.GETFIELD;
                break;
            case 3:
                currentDeviceOrientation = 90;
                break;
        }
        int cameraOrientation = getNaturalCameraOrientation();
        int cameraRotation = roundRotation(currentDeviceOrientation);
        if (isFrontCamera()) {
            return (((360 - cameraRotation) % 360) + cameraOrientation) % 360;
        }
        return (cameraRotation + cameraOrientation) % 360;
    }

    private static int roundRotation(int rotation) {
        return ((int) (Math.round(((double) rotation) / 90.0d) * 90)) % 360;
    }

    private static int getCameraIndexUsingFront(boolean front) {
        for (int i = 0; i < Camera.getNumberOfCameras(); i++) {
            CameraInfo info = new CameraInfo();
            Camera.getCameraInfo(i, info);
            if (front && info.facing == 1) {
                return i;
            }
            if (!front && info.facing == 0) {
                return i;
            }
        }
        return 0;
    }

    public void onPreviewFrame(byte[] data, Camera camera) {
        this.previewBufferLock.lock();
        if (this.isCaptureRunning && data.length == this.expectedFrameSize) {
            byte[] bArr = data;
            provideByteArrayFrame(bArr, 1, this.captureWidth, this.captureHeight, compensateCameraRotation(this.currentDisplay.getRotation()), isFrontCamera());
            camera.addCallbackBuffer(data);
        }
        this.previewBufferLock.unlock();
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    private Size getPreferredResolution() {
        Size resolution = new Size();
        switch (this.preferredResolution) {
            case LOW:
                resolution.width = 352;
                resolution.height = 288;
                break;
            case MEDIUM:
                resolution.width = 640;
                resolution.height = 480;
                break;
            case HIGH:
                resolution.width = 1280;
                resolution.height = 720;
                break;
        }
        return resolution;
    }

    private int getPreferredFramerate() {
        switch (this.preferredFramerate) {
            case FPS_30:
                return 30;
            case FPS_15:
                return 15;
            case FPS_7:
                return 7;
            case FPS_1:
                return 1;
            default:
                return 0;
        }
    }

    private Size configureCaptureSize(int preferredWidth, int preferredHeight) {
        int preferredFramerate = getPreferredFramerate();
        try {
            int i;
            Camera.Size size;
            Parameters parameters = this.camera.getParameters();
            List<Camera.Size> sizes = parameters.getSupportedPreviewSizes();
            this.captureFpsRange = findClosestEnclosingFpsRange(preferredFramerate * 1000, parameters.getSupportedPreviewFpsRange());
            int maxw = 0;
            int maxh = 0;
            for (i = 0; i < sizes.size(); i++) {
                size = (Camera.Size) sizes.get(i);
                if (size.width >= maxw && size.height >= maxh && size.width <= preferredWidth && size.height <= preferredHeight) {
                    maxw = size.width;
                    maxh = size.height;
                }
            }
            if (maxw == 0 || maxh == 0) {
                size = (Camera.Size) sizes.get(0);
                int minw = size.width;
                int minh = size.height;
                for (i = 1; i < sizes.size(); i++) {
                    size = (Camera.Size) sizes.get(i);
                    if (size.width <= minw && size.height <= minh) {
                        minw = size.width;
                        minh = size.height;
                    }
                }
                maxw = minw;
                maxh = minh;
            }
            this.captureWidth = maxw;
            this.captureHeight = maxh;
            Size retSize = new Size(maxw, maxh);
            return retSize;
        } catch (RuntimeException exp) {
            log.m18e(exp, "Error configuring capture size", new Object[0]);
            this.publisher.onCameraFailed();
            return null;
        } catch (Throwable th) {
            return null;
        }
    }

    private int[] findClosestEnclosingFpsRange(int preferredFps, List<int[]> supportedFpsRanges) {
        if (supportedFpsRanges == null || supportedFpsRanges.size() == 0) {
            return new int[]{0, 0};
        }
        if (isFrontCamera() && "samsung-sm-g900a".equals(Build.MODEL.toLowerCase()) && 30000 == preferredFps) {
            preferredFps = 24000;
        }
        final int fps = preferredFps;
        int[] closestRange = (int[]) Collections.min(supportedFpsRanges, new Comparator<int[]>() {
            private static final int MAX_FPS_DIFF_THRESHOLD = 5000;
            private static final int MAX_FPS_HIGH_DIFF_WEIGHT = 3;
            private static final int MAX_FPS_LOW_DIFF_WEIGHT = 1;
            private static final int MIN_FPS_HIGH_VALUE_WEIGHT = 4;
            private static final int MIN_FPS_LOW_VALUE_WEIGHT = 1;
            private static final int MIN_FPS_THRESHOLD = 8000;

            private int progressivePenalty(int value, int threshold, int lowWeight, int highWeight) {
                return value < threshold ? value * lowWeight : (threshold * lowWeight) + ((value - threshold) * highWeight);
            }

            private int diff(int[] range) {
                return progressivePenalty(range[0], 8000, 1, 4) + progressivePenalty(Math.abs((fps * 1000) - range[1]), MAX_FPS_DIFF_THRESHOLD, 1, 3);
            }

            public int compare(int[] lhs, int[] rhs) {
                return diff(lhs) - diff(rhs);
            }
        });
        checkRangeWithWarning(preferredFps, closestRange);
        return closestRange;
    }

    private void checkRangeWithWarning(int preferredFps, int[] range) {
        if (preferredFps < range[0] || preferredFps > range[1]) {
            log.m23w("Closest fps range found [%d-%d] for desired fps: %d", Integer.valueOf(range[0] / 1000), Integer.valueOf(range[1] / 1000), Integer.valueOf(preferredFps / 1000));
        }
    }
}
