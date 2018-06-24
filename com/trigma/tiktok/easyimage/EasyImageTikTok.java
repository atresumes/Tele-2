package com.trigma.tiktok.easyimage;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.provider.MediaStore.Images.Media;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import pl.aprilapps.easyphotopicker.BundleKeys;
import pl.aprilapps.easyphotopicker.EasyImageConfig;

public class EasyImageTikTok implements EasyImageConfig {
    private static final String KEY_LAST_CAMERA_PHOTO = "pl.aprilapps.easyphotopicker.last_photo";
    private static final String KEY_PHOTO_URI = "pl.aprilapps.easyphotopicker.photo_uri";
    private static final String KEY_TYPE = "pl.aprilapps.easyphotopicker.type";
    private static final boolean SHOW_GALLERY_IN_CHOOSER = false;

    public interface Callbacks {
        void onCanceled(ImageSource imageSource, int i);

        void onImagePicked(File file, ImageSource imageSource, int i);

        void onImagePickerError(Exception exception, ImageSource imageSource, int i);
    }

    public static class Configuration {
        private Context context;

        private Configuration(Context context) {
            this.context = context;
        }

        public Configuration setImagesFolderName(String folderName) {
            PreferenceManager.getDefaultSharedPreferences(this.context).edit().putString(BundleKeys.FOLDER_NAME, folderName).commit();
            return this;
        }

        public Configuration saveInRootPicturesDirectory() {
            PreferenceManager.getDefaultSharedPreferences(this.context).edit().putString(BundleKeys.FOLDER_LOCATION, EasyImageFiles.publicRootDir(this.context).toString()).commit();
            return this;
        }

        public Configuration saveInAppExternalFilesDir() {
            PreferenceManager.getDefaultSharedPreferences(this.context).edit().putString(BundleKeys.FOLDER_LOCATION, EasyImageFiles.publicAppExternalDir(this.context).toString()).commit();
            return this;
        }

        public Configuration setCopyExistingPicturesToPublicLocation(boolean copyToPublicLocation) {
            PreferenceManager.getDefaultSharedPreferences(this.context).edit().putBoolean(BundleKeys.PUBLIC_TEMP, copyToPublicLocation).commit();
            return this;
        }
    }

    public enum ImageSource {
        GALLERY,
        DOCUMENTS,
        CAMERA
    }

    private static Uri createCameraPictureFile(Context context) throws IOException {
        File imagePath = EasyImageFiles.getCameraPicturesLocation(context);
        Uri uri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", imagePath);
        Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(KEY_PHOTO_URI, uri.toString());
        editor.putString(KEY_LAST_CAMERA_PHOTO, imagePath.toString());
        editor.apply();
        return uri;
    }

    private static Intent createDocumentsIntent(Context context, int type) {
        storeType(context, type);
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        return intent;
    }

    private static Intent createGalleryIntent(Context context, int type) {
        storeType(context, type);
        return new Intent("android.intent.action.PICK", Media.EXTERNAL_CONTENT_URI);
    }

    private static Intent createCameraIntent(Context context, int type) {
        storeType(context, type);
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        try {
            Uri capturedImageUri = createCameraPictureFile(context);
            grantWritePermission(context, intent, capturedImageUri);
            intent.putExtra("output", capturedImageUri);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return intent;
    }

    private static void revokeWritePermission(Context context, Uri uri) {
        context.revokeUriPermission(uri, 3);
    }

    private static void grantWritePermission(Context context, Intent intent, Uri uri) {
        for (ResolveInfo resolveInfo : context.getPackageManager().queryIntentActivities(intent, 65536)) {
            context.grantUriPermission(resolveInfo.activityInfo.packageName, uri, 3);
        }
    }

    private static Intent createChooserIntent(Context context, String chooserTitle, int type) throws IOException {
        return createChooserIntent(context, chooserTitle, false, type);
    }

    private static Intent createChooserIntent(Context context, String chooserTitle, boolean showGallery, int type) throws IOException {
        Intent galleryIntent;
        storeType(context, type);
        Uri outputFileUri = createCameraPictureFile(context);
        List<Intent> cameraIntents = new ArrayList();
        Intent captureIntent = new Intent("android.media.action.IMAGE_CAPTURE");
        for (ResolveInfo res : context.getPackageManager().queryIntentActivities(captureIntent, 0)) {
            String packageName = res.activityInfo.packageName;
            Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(packageName);
            intent.putExtra("output", outputFileUri);
            grantWritePermission(context, intent, outputFileUri);
            cameraIntents.add(intent);
        }
        if (showGallery) {
            galleryIntent = createGalleryIntent(context, type);
        } else {
            galleryIntent = createDocumentsIntent(context, type);
        }
        Intent chooserIntent = Intent.createChooser(galleryIntent, chooserTitle);
        chooserIntent.putExtra("android.intent.extra.INITIAL_INTENTS", (Parcelable[]) cameraIntents.toArray(new Parcelable[cameraIntents.size()]));
        return chooserIntent;
    }

    private static void storeType(Context context, int type) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putInt(KEY_TYPE, type).commit();
    }

    private static int restoreType(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getInt(KEY_TYPE, 0);
    }

    public static void openChooserWithDocuments(Activity activity, String chooserTitle, int type) {
        try {
            activity.startActivityForResult(createChooserIntent(activity, chooserTitle, type), EasyImageConfig.REQ_SOURCE_CHOOSER);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void openChooserWithDocuments(Fragment fragment, String chooserTitle, int type) {
        try {
            fragment.startActivityForResult(createChooserIntent(fragment.getActivity(), chooserTitle, type), EasyImageConfig.REQ_SOURCE_CHOOSER);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void openChooserWithDocuments(android.app.Fragment fragment, String chooserTitle, int type) {
        try {
            fragment.startActivityForResult(createChooserIntent(fragment.getActivity(), chooserTitle, type), EasyImageConfig.REQ_SOURCE_CHOOSER);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void openChooserWithGallery(Activity activity, String chooserTitle, int type) {
        try {
            activity.startActivityForResult(createChooserIntent(activity, chooserTitle, true, type), EasyImageConfig.REQ_SOURCE_CHOOSER);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void openChooserWithGallery(Fragment fragment, String chooserTitle, int type) {
        try {
            fragment.startActivityForResult(createChooserIntent(fragment.getActivity(), chooserTitle, true, type), EasyImageConfig.REQ_SOURCE_CHOOSER);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void openChooserWithGallery(android.app.Fragment fragment, String chooserTitle, int type) {
        try {
            fragment.startActivityForResult(createChooserIntent(fragment.getActivity(), chooserTitle, true, type), EasyImageConfig.REQ_SOURCE_CHOOSER);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void openDocuments(Activity activity, int type) {
        activity.startActivityForResult(createDocumentsIntent(activity, type), EasyImageConfig.REQ_PICK_PICTURE_FROM_DOCUMENTS);
    }

    public static void openDocuments(Fragment fragment, int type) {
        fragment.startActivityForResult(createDocumentsIntent(fragment.getContext(), type), EasyImageConfig.REQ_PICK_PICTURE_FROM_DOCUMENTS);
    }

    public static void openDocuments(android.app.Fragment fragment, int type) {
        fragment.startActivityForResult(createDocumentsIntent(fragment.getActivity(), type), EasyImageConfig.REQ_PICK_PICTURE_FROM_DOCUMENTS);
    }

    public static void openGallery(Activity activity, int type) {
        activity.startActivityForResult(createGalleryIntent(activity, type), EasyImageConfig.REQ_PICK_PICTURE_FROM_GALLERY);
    }

    public static void openGallery(Fragment fragment, int type) {
        fragment.startActivityForResult(createGalleryIntent(fragment.getContext(), type), EasyImageConfig.REQ_PICK_PICTURE_FROM_GALLERY);
    }

    public static void openGallery(android.app.Fragment fragment, int type) {
        fragment.startActivityForResult(createGalleryIntent(fragment.getActivity(), type), EasyImageConfig.REQ_PICK_PICTURE_FROM_GALLERY);
    }

    public static void openCamera(Activity activity, int type) {
        activity.startActivityForResult(createCameraIntent(activity, type), EasyImageConfig.REQ_TAKE_PICTURE);
    }

    public static void openCamera(Fragment fragment, int type) {
        fragment.startActivityForResult(createCameraIntent(fragment.getActivity(), type), EasyImageConfig.REQ_TAKE_PICTURE);
    }

    public static void openCamera(android.app.Fragment fragment, int type) {
        fragment.startActivityForResult(createCameraIntent(fragment.getActivity(), type), EasyImageConfig.REQ_TAKE_PICTURE);
    }

    @Nullable
    private static File takenCameraPicture(Context context) throws IOException, URISyntaxException {
        String lastCameraPhoto = PreferenceManager.getDefaultSharedPreferences(context).getString(KEY_LAST_CAMERA_PHOTO, null);
        if (lastCameraPhoto != null) {
            return new File(lastCameraPhoto);
        }
        return null;
    }

    public static void handleActivityResult(int requestCode, int resultCode, Intent data, Activity activity, Callbacks callbacks) {
        if (requestCode != EasyImageConfig.REQ_SOURCE_CHOOSER && requestCode != EasyImageConfig.REQ_PICK_PICTURE_FROM_GALLERY && requestCode != EasyImageConfig.REQ_TAKE_PICTURE && requestCode != EasyImageConfig.REQ_PICK_PICTURE_FROM_DOCUMENTS) {
            return;
        }
        if (resultCode == -1) {
            if (requestCode == EasyImageConfig.REQ_PICK_PICTURE_FROM_DOCUMENTS) {
                onPictureReturnedFromDocuments(data, activity, callbacks);
            } else if (requestCode == EasyImageConfig.REQ_PICK_PICTURE_FROM_GALLERY) {
                onPictureReturnedFromGallery(data, activity, callbacks);
            } else if (requestCode == EasyImageConfig.REQ_TAKE_PICTURE) {
                onPictureReturnedFromCamera(activity, callbacks);
            } else if (data == null || data.getData() == null) {
                onPictureReturnedFromCamera(activity, callbacks);
            } else {
                onPictureReturnedFromDocuments(data, activity, callbacks);
            }
        } else if (requestCode == EasyImageConfig.REQ_PICK_PICTURE_FROM_DOCUMENTS) {
            callbacks.onCanceled(ImageSource.DOCUMENTS, restoreType(activity));
        } else if (requestCode == EasyImageConfig.REQ_PICK_PICTURE_FROM_GALLERY) {
            callbacks.onCanceled(ImageSource.GALLERY, restoreType(activity));
        } else if (requestCode == EasyImageConfig.REQ_TAKE_PICTURE) {
            callbacks.onCanceled(ImageSource.CAMERA, restoreType(activity));
        } else if (data == null || data.getData() == null) {
            callbacks.onCanceled(ImageSource.CAMERA, restoreType(activity));
        } else {
            callbacks.onCanceled(ImageSource.DOCUMENTS, restoreType(activity));
        }
    }

    public static boolean willHandleActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EasyImageConfig.REQ_SOURCE_CHOOSER || requestCode == EasyImageConfig.REQ_PICK_PICTURE_FROM_GALLERY || requestCode == EasyImageConfig.REQ_TAKE_PICTURE || requestCode == EasyImageConfig.REQ_PICK_PICTURE_FROM_DOCUMENTS) {
            return true;
        }
        return false;
    }

    public static File lastlyTakenButCanceledPhoto(Context context) {
        String filePath = PreferenceManager.getDefaultSharedPreferences(context).getString(KEY_LAST_CAMERA_PHOTO, null);
        if (filePath == null) {
            return null;
        }
        File file = new File(filePath);
        if (file.exists()) {
            return file;
        }
        return null;
    }

    private static void onPictureReturnedFromDocuments(Intent data, Activity activity, Callbacks callbacks) {
        try {
            callbacks.onImagePicked(EasyImageFiles.pickedExistingPicture(activity, data.getData()), ImageSource.DOCUMENTS, restoreType(activity));
        } catch (Exception e) {
            e.printStackTrace();
            callbacks.onImagePickerError(e, ImageSource.DOCUMENTS, restoreType(activity));
        }
    }

    private static void onPictureReturnedFromGallery(Intent data, Activity activity, Callbacks callbacks) {
        try {
            callbacks.onImagePicked(EasyImageFiles.pickedExistingPicture(activity, data.getData()), ImageSource.GALLERY, restoreType(activity));
        } catch (Exception e) {
            e.printStackTrace();
            callbacks.onImagePickerError(e, ImageSource.GALLERY, restoreType(activity));
        }
    }

    private static void onPictureReturnedFromCamera(Activity activity, Callbacks callbacks) {
        try {
            String lastImageUri = PreferenceManager.getDefaultSharedPreferences(activity).getString(KEY_PHOTO_URI, null);
            if (!TextUtils.isEmpty(lastImageUri)) {
                revokeWritePermission(activity, Uri.parse(lastImageUri));
            }
            File photoFile = takenCameraPicture(activity);
            if (photoFile == null) {
                callbacks.onImagePickerError(new IllegalStateException("Unable to get the picture returned from camera"), ImageSource.CAMERA, restoreType(activity));
            } else {
                callbacks.onImagePicked(photoFile, ImageSource.CAMERA, restoreType(activity));
            }
            PreferenceManager.getDefaultSharedPreferences(activity).edit().remove(KEY_LAST_CAMERA_PHOTO).remove(KEY_PHOTO_URI).apply();
        } catch (Exception e) {
            e.printStackTrace();
            callbacks.onImagePickerError(e, ImageSource.CAMERA, restoreType(activity));
        }
    }

    public static void clearPublicTemp(Context context) {
        List<File> tempFiles = new ArrayList();
        for (File file : EasyImageFiles.publicTempDir(context).listFiles()) {
            file.delete();
        }
    }

    public static void clearConfiguration(Context context) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().remove(BundleKeys.FOLDER_NAME).remove(BundleKeys.FOLDER_LOCATION).remove(BundleKeys.PUBLIC_TEMP).apply();
    }

    public static Configuration configuration(Context context) {
        return new Configuration(context);
    }
}
