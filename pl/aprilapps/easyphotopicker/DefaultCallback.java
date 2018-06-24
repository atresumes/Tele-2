package pl.aprilapps.easyphotopicker;

import pl.aprilapps.easyphotopicker.EasyImage.Callbacks;
import pl.aprilapps.easyphotopicker.EasyImage.ImageSource;

public abstract class DefaultCallback implements Callbacks {
    public void onImagePickerError(Exception e, ImageSource source, int type) {
    }

    public void onCanceled(ImageSource source, int type) {
    }
}
