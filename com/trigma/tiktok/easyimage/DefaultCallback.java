package com.trigma.tiktok.easyimage;

import com.trigma.tiktok.easyimage.EasyImageTikTok.Callbacks;
import com.trigma.tiktok.easyimage.EasyImageTikTok.ImageSource;

public abstract class DefaultCallback implements Callbacks {
    public void onImagePickerError(Exception e, ImageSource source, int type) {
    }

    public void onCanceled(ImageSource source, int type) {
    }
}
