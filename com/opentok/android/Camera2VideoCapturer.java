package com.opentok.android;

import android.annotation.TargetApi;
import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCaptureSession.CaptureCallback;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraDevice.StateCallback;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureRequest.Builder;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.media.ImageReader.OnImageAvailableListener;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Range;
import android.util.Size;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;
import com.opentok.android.BaseVideoCapturer.CaptureSettings;
import com.opentok.android.BaseVideoCapturer.CaptureSwitch;
import com.opentok.android.OtLog.LogToken;
import com.opentok.android.Publisher.CameraCaptureFrameRate;
import com.opentok.android.Publisher.CameraCaptureResolution;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import org.objectweb.asm.Opcodes;

@TargetApi(21)
class Camera2VideoCapturer extends BaseVideoCapturer implements CaptureSwitch {
    private static final int PIXEL_FORMAT = 35;
    private static final int PREFERRED_FACING_CAMERA = 0;
    private static final boolean debug = false;
    private static final SparseIntArray frameRateTable = new C09593();
    private static final SparseArray<Size> resolutionTable = new C09582();
    private static final SparseIntArray rotationTable = new C09571();
    private Range<Integer> camFps;
    private Handler camHandler;
    private HandlerThread camThread;
    private CameraDevice camera;
    private ImageReader cameraFrame;
    private int cameraIndex;
    private CameraManager cameraManager;
    private StateCallback cameraObserver = new C09604();
    private CameraState cameraState;
    private CaptureCallback captureNotification = new C09637();
    private CaptureRequest captureRequest;
    private Builder captureRequestBuilder;
    private CameraCaptureSession captureSession;
    private CameraCaptureSession.StateCallback captureSessionObserver = new C09626();
    private Condition condition;
    private int desiredFps;
    private Display display;
    private Size frameDimensions;
    private OnImageAvailableListener frameObserver = new C09615();
    private boolean isPaused;
    private LogToken log = new LogToken("[camera2]", false);
    private ReentrantLock reentrantLock;
    private List<RuntimeException> runtimeExceptionList;

    static class C09571 extends SparseIntArray {
        C09571() {
            append(0, 0);
            append(1, 90);
            append(2, Opcodes.GETFIELD);
            append(3, 270);
        }
    }

    static class C09582 extends SparseArray<Size> {
        C09582() {
            append(CameraCaptureResolution.LOW.ordinal(), new Size(352, 288));
            append(CameraCaptureResolution.MEDIUM.ordinal(), new Size(640, 480));
            append(CameraCaptureResolution.HIGH.ordinal(), new Size(1280, 720));
        }
    }

    static class C09593 extends SparseIntArray {
        C09593() {
            append(CameraCaptureFrameRate.FPS_1.ordinal(), 1);
            append(CameraCaptureFrameRate.FPS_7.ordinal(), 7);
            append(CameraCaptureFrameRate.FPS_15.ordinal(), 15);
            append(CameraCaptureFrameRate.FPS_30.ordinal(), 30);
        }
    }

    class C09604 extends StateCallback {
        C09604() {
        }

        public void onOpened(CameraDevice camera) {
            Camera2VideoCapturer.this.log.m15d("onOpened", new Object[0]);
            Camera2VideoCapturer.this.cameraState = CameraState.OPEN;
            Camera2VideoCapturer.this.camera = camera;
            Camera2VideoCapturer.this.signalCamStateChange();
        }

        public void onDisconnected(CameraDevice camera) {
            try {
                Camera2VideoCapturer.this.log.m15d("onDisconnected", new Object[0]);
                Camera2VideoCapturer.this.camera.close();
                Camera2VideoCapturer.this.waitForCamStateChange(Camera2VideoCapturer.this.cameraState);
            } catch (NullPointerException e) {
            }
        }

        public void onError(CameraDevice camera, int error) {
            try {
                Camera2VideoCapturer.this.log.m15d("onError", new Object[0]);
                Camera2VideoCapturer.this.camera.close();
                Camera2VideoCapturer.this.waitForCamStateChange(Camera2VideoCapturer.this.cameraState);
            } catch (NullPointerException e) {
            }
            Camera2VideoCapturer.this.postAsyncException(new Camera2Exception("Camera Open Error: " + error));
        }

        public void onClosed(CameraDevice camera) {
            Camera2VideoCapturer.this.log.m15d("onClosed", new Object[0]);
            super.onClosed(camera);
            Camera2VideoCapturer.this.cameraState = CameraState.CLOSED;
            Camera2VideoCapturer.this.camera = null;
            Camera2VideoCapturer.this.signalCamStateChange();
        }
    }

    class C09615 implements OnImageAvailableListener {
        C09615() {
        }

        public void onImageAvailable(ImageReader reader) {
            Image frame = reader.acquireNextImage();
            if (CameraState.CAPTURE == Camera2VideoCapturer.this.cameraState) {
                Camera2VideoCapturer.this.provideBufferFramePlanar(frame.getPlanes()[0].getBuffer(), frame.getPlanes()[1].getBuffer(), frame.getPlanes()[2].getBuffer(), frame.getPlanes()[0].getPixelStride(), frame.getPlanes()[0].getRowStride(), frame.getPlanes()[1].getPixelStride(), frame.getPlanes()[1].getRowStride(), frame.getPlanes()[2].getPixelStride(), frame.getPlanes()[2].getRowStride(), frame.getWidth(), frame.getHeight(), Camera2VideoCapturer.this.calculateCamRotation(), Camera2VideoCapturer.this.isFrontCamera());
            }
            frame.close();
        }
    }

    class C09626 extends CameraCaptureSession.StateCallback {
        C09626() {
        }

        public void onConfigured(CameraCaptureSession session) {
            try {
                Camera2VideoCapturer.this.cameraState = CameraState.CAPTURE;
                Camera2VideoCapturer.this.captureSession = session;
                Camera2VideoCapturer.this.captureRequest = Camera2VideoCapturer.this.captureRequestBuilder.build();
                Camera2VideoCapturer.this.captureSession.setRepeatingRequest(Camera2VideoCapturer.this.captureRequest, Camera2VideoCapturer.this.captureNotification, Camera2VideoCapturer.this.camHandler);
                Camera2VideoCapturer.this.signalCamStateChange();
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }

        public void onConfigureFailed(CameraCaptureSession session) {
            Camera2VideoCapturer.this.cameraState = CameraState.ERROR;
            Camera2VideoCapturer.this.postAsyncException(new Camera2Exception("Camera session configuration failed"));
            Camera2VideoCapturer.this.signalCamStateChange();
        }

        public void onClosed(CameraCaptureSession session) {
            Camera2VideoCapturer.this.camera.close();
        }
    }

    class C09637 extends CaptureCallback {
        C09637() {
        }

        public void onCaptureStarted(CameraCaptureSession session, CaptureRequest request, long timestamp, long frameNumber) {
            super.onCaptureStarted(session, request, timestamp, frameNumber);
        }
    }

    public static class Camera2Exception extends RuntimeException {
        public Camera2Exception(String message) {
            super(message);
        }
    }

    private enum CameraState {
        CLOSED,
        SETUP,
        OPEN,
        CAPTURE,
        ERROR
    }

    public Camera2VideoCapturer(Context ctx, CameraCaptureResolution resolution, CameraCaptureFrameRate fps) {
        this.cameraManager = (CameraManager) ctx.getSystemService("camera");
        this.camera = null;
        this.cameraState = CameraState.CLOSED;
        this.display = ((WindowManager) ctx.getSystemService("window")).getDefaultDisplay();
        this.reentrantLock = new ReentrantLock();
        this.condition = this.reentrantLock.newCondition();
        this.frameDimensions = (Size) resolutionTable.get(resolution.ordinal());
        this.desiredFps = frameRateTable.get(fps.ordinal());
        this.runtimeExceptionList = new ArrayList();
        this.isPaused = false;
        try {
            this.cameraIndex = findCameraIndex(selectCamera(0));
        } catch (CameraAccessException e) {
            throw new Camera2Exception(e.getMessage());
        }
    }

    public synchronized void init() {
        this.log.m15d("init entered", new Object[0]);
        startCamThread();
        initCamera();
        this.log.m15d("init Exit", new Object[0]);
    }

    public synchronized int startCapture() {
        this.log.m15d("startCapture entered", new Object[0]);
        if (this.camera == null || CameraState.OPEN != this.cameraState) {
            throw new Camera2Exception("Start Capture called before init successfully completed.");
        }
        try {
            if (isFrontCamera()) {
                this.captureRequestBuilder = this.camera.createCaptureRequest(1);
                this.captureRequestBuilder.addTarget(this.cameraFrame.getSurface());
                this.captureRequestBuilder.set(CaptureRequest.CONTROL_AE_TARGET_FPS_RANGE, this.camFps);
                this.captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, Integer.valueOf(2));
                this.captureRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, Integer.valueOf(4));
                this.captureRequestBuilder.set(CaptureRequest.CONTROL_SCENE_MODE, Integer.valueOf(1));
                this.camera.createCaptureSession(Arrays.asList(new Surface[]{this.cameraFrame.getSurface()}), this.captureSessionObserver, this.camHandler);
            } else {
                this.captureRequestBuilder = this.camera.createCaptureRequest(3);
                this.captureRequestBuilder.addTarget(this.cameraFrame.getSurface());
                this.captureRequestBuilder.set(CaptureRequest.CONTROL_AE_TARGET_FPS_RANGE, this.camFps);
                this.camera.createCaptureSession(Arrays.asList(new Surface[]{this.cameraFrame.getSurface()}), this.captureSessionObserver, this.camHandler);
            }
            waitForCamStateChange(CameraState.OPEN);
        } catch (CameraAccessException e) {
            throw new Camera2Exception(e.getMessage());
        }
        return 0;
    }

    public synchronized int stopCapture() {
        this.log.m15d("stopCapture entered", new Object[0]);
        if (!(this.camera == null || this.captureSession == null || CameraState.CLOSED == this.cameraState)) {
            CameraState oldState = this.cameraState;
            this.captureSession.close();
            waitForCamStateChange(oldState);
            this.cameraFrame.close();
        }
        this.log.m15d("stopCapture exited", new Object[0]);
        return 0;
    }

    public synchronized void destroy() {
        stopCamThread();
    }

    public boolean isCaptureStarted() {
        return this.cameraState == CameraState.CAPTURE;
    }

    public synchronized CaptureSettings getCaptureSettings() {
        CaptureSettings retObj;
        int i = 0;
        synchronized (this) {
            int width;
            retObj = new CaptureSettings();
            retObj.fps = this.desiredFps;
            if (this.cameraFrame != null) {
                width = this.cameraFrame.getWidth();
            } else {
                width = 0;
            }
            retObj.width = width;
            if (this.cameraFrame != null) {
                i = this.cameraFrame.getHeight();
            }
            retObj.height = i;
            retObj.format = 1;
            retObj.expectedDelay = 0;
        }
        return retObj;
    }

    public synchronized void onPause() {
        this.log.m15d("OnPause", new Object[0]);
        switch (this.cameraState) {
            case CAPTURE:
                stopCapture();
                this.isPaused = true;
                break;
        }
    }

    public void onResume() {
        this.log.m15d("onResume", new Object[0]);
        if (this.isPaused) {
            initCamera();
            startCapture();
            this.isPaused = false;
        }
    }

    public synchronized void cycleCamera() {
        try {
            swapCamera((this.cameraIndex + 1) % this.cameraManager.getCameraIdList().length);
        } catch (CameraAccessException e) {
            e.printStackTrace();
            throw new Camera2Exception(e.getMessage());
        }
    }

    public int getCameraIndex() {
        return this.cameraIndex;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void swapCamera(int r4) {
        /*
        r3 = this;
        monitor-enter(r3);
        r0 = r3.cameraState;	 Catch:{ all -> 0x0021 }
        r1 = com.opentok.android.Camera2VideoCapturer.AnonymousClass10.$SwitchMap$com$opentok$android$Camera2VideoCapturer$CameraState;	 Catch:{ all -> 0x0021 }
        r2 = r0.ordinal();	 Catch:{ all -> 0x0021 }
        r1 = r1[r2];	 Catch:{ all -> 0x0021 }
        switch(r1) {
            case 1: goto L_0x001d;
            default: goto L_0x000e;
        };	 Catch:{ all -> 0x0021 }
    L_0x000e:
        r3.cameraIndex = r4;	 Catch:{ all -> 0x0021 }
        r1 = com.opentok.android.Camera2VideoCapturer.AnonymousClass10.$SwitchMap$com$opentok$android$Camera2VideoCapturer$CameraState;	 Catch:{ all -> 0x0021 }
        r2 = r0.ordinal();	 Catch:{ all -> 0x0021 }
        r1 = r1[r2];	 Catch:{ all -> 0x0021 }
        switch(r1) {
            case 1: goto L_0x0024;
            default: goto L_0x001b;
        };
    L_0x001b:
        monitor-exit(r3);
        return;
    L_0x001d:
        r3.stopCapture();	 Catch:{ all -> 0x0021 }
        goto L_0x000e;
    L_0x0021:
        r1 = move-exception;
        monitor-exit(r3);
        throw r1;
    L_0x0024:
        r3.initCamera();	 Catch:{ all -> 0x0021 }
        r3.startCapture();	 Catch:{ all -> 0x0021 }
        goto L_0x001b;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.opentok.android.Camera2VideoCapturer.swapCamera(int):void");
    }

    private boolean isFrontCamera() {
        try {
            CameraCharacteristics characteristics = this.cameraManager.getCameraCharacteristics(this.camera.getId());
            if (characteristics == null) {
                return false;
            }
            if (((Integer) characteristics.get(CameraCharacteristics.LENS_FACING)).intValue() == 0) {
                return true;
            }
            return false;
        } catch (CameraAccessException e) {
            this.log.m15d("Error accesing camera characteristics", new Object[0]);
            return false;
        }
    }

    private void waitForCamStateChange(CameraState oldState) throws RuntimeException {
        this.reentrantLock.lock();
        try {
            this.log.m15d("wait for change from " + oldState, new Object[0]);
            while (oldState == this.cameraState) {
                this.condition.await();
            }
        } catch (InterruptedException e) {
            waitForCamStateChange(oldState);
        }
        this.reentrantLock.unlock();
        Iterator it = this.runtimeExceptionList.iterator();
        if (it.hasNext()) {
            throw ((RuntimeException) it.next());
        }
        this.runtimeExceptionList.clear();
    }

    private void signalCamStateChange() {
        this.reentrantLock.lock();
        this.condition.signalAll();
        this.reentrantLock.unlock();
    }

    private void startCamThread() {
        this.camThread = new HandlerThread("Camera-Thread");
        this.camThread.start();
        this.camHandler = new Handler(this.camThread.getLooper());
    }

    private void stopCamThread() {
        try {
            this.camThread.quitSafely();
            this.camThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (NullPointerException e2) {
        } finally {
            this.camThread = null;
            this.camHandler = null;
        }
    }

    private String selectCamera(int lenseDirection) throws CameraAccessException {
        for (String id : this.cameraManager.getCameraIdList()) {
            if (lenseDirection == ((Integer) this.cameraManager.getCameraCharacteristics(id).get(CameraCharacteristics.LENS_FACING)).intValue()) {
                return id;
            }
        }
        return null;
    }

    private Range<Integer> selectCameraFpsRange(String camId, final int fps) throws CameraAccessException {
        for (String id : this.cameraManager.getCameraIdList()) {
            if (id.equals(camId)) {
                CameraCharacteristics info = this.cameraManager.getCameraCharacteristics(id);
                List<Range<Integer>> fpsLst = new ArrayList();
                Collections.addAll(fpsLst, (Object[]) info.get(CameraCharacteristics.CONTROL_AE_AVAILABLE_TARGET_FPS_RANGES));
                return (Range) Collections.min(fpsLst, new Comparator<Range<Integer>>() {
                    private static final int MAX_FPS_DIFF_THRESHOLD = 5000;
                    private static final int MAX_FPS_HIGH_DIFF_WEIGHT = 3;
                    private static final int MAX_FPS_LOW_DIFF_WEIGHT = 1;
                    private static final int MIN_FPS_HIGH_VALUE_WEIGHT = 4;
                    private static final int MIN_FPS_LOW_VALUE_WEIGHT = 1;
                    private static final int MIN_FPS_THRESHOLD = 8000;

                    private int progressivePenalty(int value, int threshold, int lowWeight, int highWeight) {
                        return value < threshold ? value * lowWeight : (threshold * lowWeight) + ((value - threshold) * highWeight);
                    }

                    private int diff(Range<Integer> val) {
                        return progressivePenalty(((Integer) val.getLower()).intValue(), 8000, 1, 4) + progressivePenalty(Math.abs((fps * 1000) - ((Integer) val.getUpper()).intValue()), MAX_FPS_DIFF_THRESHOLD, 1, 3);
                    }

                    public int compare(Range<Integer> lhs, Range<Integer> rhs) {
                        return diff(lhs) - diff(rhs);
                    }
                });
            }
        }
        return null;
    }

    private int findCameraIndex(String camId) throws CameraAccessException {
        String[] idList = this.cameraManager.getCameraIdList();
        for (int ndx = 0; ndx < idList.length; ndx++) {
            if (idList[ndx].equals(camId)) {
                return ndx;
            }
        }
        return -1;
    }

    private Size selectPreferedSize(String camId, final int width, final int height, int format) throws CameraAccessException {
        StreamConfigurationMap dimMap = (StreamConfigurationMap) this.cameraManager.getCameraCharacteristics(camId).get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
        List<Size> sizeLst = new ArrayList();
        int[] formats = dimMap.getOutputFormats();
        Collections.addAll(sizeLst, dimMap.getOutputSizes(35));
        return (Size) Collections.min(sizeLst, new Comparator<Size>() {
            public int compare(Size lhs, Size rhs) {
                return (Math.abs(lhs.getWidth() - width) + Math.abs(lhs.getHeight() - height)) - (Math.abs(rhs.getWidth() - width) + Math.abs(rhs.getHeight() - height));
            }
        });
    }

    private int calculateCamRotation() {
        try {
            CameraCharacteristics camInfo = this.cameraManager.getCameraCharacteristics(this.camera.getId());
            int cameraRotation = rotationTable.get(this.display.getRotation());
            int cameraOrientation = ((Integer) camInfo.get(CameraCharacteristics.SENSOR_ORIENTATION)).intValue();
            if (((Integer) camInfo.get(CameraCharacteristics.LENS_FACING)).intValue() != 0) {
                return Math.abs((cameraRotation - cameraOrientation) % 360);
            }
            return ((cameraRotation + cameraOrientation) + 360) % 360;
        } catch (CameraAccessException e) {
            e.printStackTrace();
            return 0;
        }
    }

    private void initCamera() {
        try {
            this.cameraState = CameraState.SETUP;
            String camId = this.cameraManager.getCameraIdList()[this.cameraIndex];
            this.camFps = selectCameraFpsRange(camId, this.desiredFps);
            Size preferredSize = selectPreferedSize(camId, this.frameDimensions.getWidth(), this.frameDimensions.getHeight(), 35);
            this.cameraFrame = ImageReader.newInstance(preferredSize.getWidth(), preferredSize.getHeight(), 35, 3);
            this.cameraFrame.setOnImageAvailableListener(this.frameObserver, this.camHandler);
            this.cameraManager.openCamera(camId, this.cameraObserver, this.camHandler);
            waitForCamStateChange(CameraState.SETUP);
        } catch (CameraAccessException exp) {
            throw new Camera2Exception(exp.getMessage());
        }
    }

    private void postAsyncException(RuntimeException exp) {
        this.runtimeExceptionList.add(exp);
    }
}
