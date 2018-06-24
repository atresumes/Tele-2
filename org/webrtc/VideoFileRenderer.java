package org.webrtc;

import android.os.Handler;
import android.os.HandlerThread;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.CountDownLatch;
import org.webrtc.EglBase.Context;
import org.webrtc.VideoRenderer.Callbacks;
import org.webrtc.VideoRenderer.I420Frame;

public class VideoFileRenderer implements Callbacks {
    private static final String TAG = "VideoFileRenderer";
    private EglBase eglBase;
    private final Object handlerLock = new Object();
    private final int outputFileHeight;
    private final int outputFileWidth;
    private final ByteBuffer outputFrameBuffer;
    private final int outputFrameSize;
    private final HandlerThread renderThread;
    private final Handler renderThreadHandler;
    private final FileOutputStream videoOutFile;
    private YuvConverter yuvConverter;

    public static native void nativeI420Scale(ByteBuffer byteBuffer, int i, ByteBuffer byteBuffer2, int i2, ByteBuffer byteBuffer3, int i3, int i4, int i5, ByteBuffer byteBuffer4, int i6, int i7);

    public VideoFileRenderer(String outputFile, int outputFileWidth, int outputFileHeight, final Context sharedContext) throws IOException {
        if (outputFileWidth % 2 == 1 || outputFileHeight % 2 == 1) {
            throw new IllegalArgumentException("Does not support uneven width or height");
        }
        this.outputFileWidth = outputFileWidth;
        this.outputFileHeight = outputFileHeight;
        this.outputFrameSize = ((outputFileWidth * outputFileHeight) * 3) / 2;
        this.outputFrameBuffer = ByteBuffer.allocateDirect(this.outputFrameSize);
        this.videoOutFile = new FileOutputStream(outputFile);
        this.videoOutFile.write(("YUV4MPEG2 C420 W" + outputFileWidth + " H" + outputFileHeight + " Ip F30:1 A1:1\n").getBytes());
        this.renderThread = new HandlerThread(TAG);
        this.renderThread.start();
        this.renderThreadHandler = new Handler(this.renderThread.getLooper());
        ThreadUtils.invokeAtFrontUninterruptibly(this.renderThreadHandler, new Runnable() {
            public void run() {
                VideoFileRenderer.this.eglBase = EglBase.create(sharedContext, EglBase.CONFIG_PIXEL_BUFFER);
                VideoFileRenderer.this.eglBase.createDummyPbufferSurface();
                VideoFileRenderer.this.eglBase.makeCurrent();
                VideoFileRenderer.this.yuvConverter = new YuvConverter();
            }
        });
    }

    public void renderFrame(final I420Frame frame) {
        this.renderThreadHandler.post(new Runnable() {
            public void run() {
                VideoFileRenderer.this.renderFrameOnRenderThread(frame);
            }
        });
    }

    private void renderFrameOnRenderThread(I420Frame frame) {
        float frameAspectRatio = ((float) frame.rotatedWidth()) / ((float) frame.rotatedHeight());
        float[] texMatrix = RendererCommon.multiplyMatrices(RendererCommon.rotateTextureMatrix(frame.samplingMatrix, (float) frame.rotationDegree), RendererCommon.getLayoutMatrix(false, frameAspectRatio, ((float) this.outputFileWidth) / ((float) this.outputFileHeight)));
        try {
            this.videoOutFile.write("FRAME\n".getBytes());
            if (frame.yuvFrame) {
                nativeI420Scale(frame.yuvPlanes[0], frame.yuvStrides[0], frame.yuvPlanes[1], frame.yuvStrides[1], frame.yuvPlanes[2], frame.yuvStrides[2], frame.width, frame.height, this.outputFrameBuffer, this.outputFileWidth, this.outputFileHeight);
                this.videoOutFile.write(this.outputFrameBuffer.array(), this.outputFrameBuffer.arrayOffset(), this.outputFrameSize);
            } else {
                int r;
                this.yuvConverter.convert(this.outputFrameBuffer, this.outputFileWidth, this.outputFileHeight, this.outputFileWidth, frame.textureId, texMatrix);
                int stride = this.outputFileWidth;
                byte[] data = this.outputFrameBuffer.array();
                int offset = this.outputFrameBuffer.arrayOffset();
                this.videoOutFile.write(data, offset, this.outputFileWidth * this.outputFileHeight);
                for (r = this.outputFileHeight; r < (this.outputFileHeight * 3) / 2; r++) {
                    this.videoOutFile.write(data, (r * stride) + offset, stride / 2);
                }
                for (r = this.outputFileHeight; r < (this.outputFileHeight * 3) / 2; r++) {
                    this.videoOutFile.write(data, ((r * stride) + offset) + (stride / 2), stride / 2);
                }
            }
            VideoRenderer.renderFrameDone(frame);
        } catch (Throwable e) {
            Logging.m173e(TAG, "Failed to write to file for video out");
            throw new RuntimeException(e);
        } catch (Throwable th) {
            VideoRenderer.renderFrameDone(frame);
        }
    }

    public void release() {
        final CountDownLatch cleanupBarrier = new CountDownLatch(1);
        this.renderThreadHandler.post(new Runnable() {
            public void run() {
                try {
                    VideoFileRenderer.this.videoOutFile.close();
                } catch (IOException e) {
                    Logging.m172d(VideoFileRenderer.TAG, "Error closing output video file");
                }
                VideoFileRenderer.this.yuvConverter.release();
                VideoFileRenderer.this.eglBase.release();
                VideoFileRenderer.this.renderThread.quit();
                cleanupBarrier.countDown();
            }
        });
        ThreadUtils.awaitUninterruptibly(cleanupBarrier);
    }
}
