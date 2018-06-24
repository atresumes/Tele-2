package pl.aprilapps.easyphotopicker;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.webkit.MimeTypeMap;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

class EasyImageFiles {
    public static String DEFAULT_FOLDER_NAME = "EasyImage";
    public static String TEMP_FOLDER_NAME = "Temp";

    EasyImageFiles() {
    }

    public static String getFolderName(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(BundleKeys.FOLDER_NAME, DEFAULT_FOLDER_NAME);
    }

    public static File tempImageDirectory(Context context) {
        File dir = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(BundleKeys.PUBLIC_TEMP, false) ? publicTempDir(context) : privateTempDir(context);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    public static File publicRootDir(Context context) {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
    }

    public static File publicAppExternalDir(Context context) {
        return context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
    }

    public static File publicTempDir(Context context) {
        File publicTempDir = new File(new File(getFolderLocation(context), getFolderName(context)), TEMP_FOLDER_NAME);
        if (!publicTempDir.exists()) {
            publicTempDir.mkdirs();
        }
        return publicTempDir;
    }

    private static File privateTempDir(Context context) {
        File privateTempDir = new File(context.getApplicationContext().getCacheDir(), getFolderName(context));
        if (!privateTempDir.exists()) {
            privateTempDir.mkdirs();
        }
        return privateTempDir;
    }

    public static void writeToFile(InputStream in, File file) {
        try {
            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            while (true) {
                int len = in.read(buf);
                if (len > 0) {
                    out.write(buf, 0, len);
                } else {
                    out.close();
                    in.close();
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static File pickedExistingPicture(Context context, Uri photoUri) throws IOException {
        InputStream pictureInputStream = context.getContentResolver().openInputStream(photoUri);
        File photoFile = new File(tempImageDirectory(context), UUID.randomUUID().toString() + "." + getMimeType(context, photoUri));
        photoFile.createNewFile();
        writeToFile(pictureInputStream, photoFile);
        return photoFile;
    }

    public static String getFolderLocation(Context context) {
        File publicAppExternalDir = publicAppExternalDir(context);
        String defaultFolderLocation = null;
        if (publicAppExternalDir != null) {
            defaultFolderLocation = publicAppExternalDir.getPath();
        }
        return PreferenceManager.getDefaultSharedPreferences(context).getString(BundleKeys.FOLDER_LOCATION, defaultFolderLocation);
    }

    public static File getCameraPicturesLocation(Context context) throws IOException {
        File cacheDir = context.getCacheDir();
        if (isExternalStorageWritable()) {
            cacheDir = context.getExternalCacheDir();
        }
        File dir = new File(cacheDir, DEFAULT_FOLDER_NAME);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return File.createTempFile(UUID.randomUUID().toString(), ".jpg", dir);
    }

    private static boolean isExternalStorageWritable() {
        return "mounted".equals(Environment.getExternalStorageState());
    }

    public static String getMimeType(Context context, Uri uri) {
        if (uri.getScheme().equals("content")) {
            return MimeTypeMap.getSingleton().getExtensionFromMimeType(context.getContentResolver().getType(uri));
        }
        return MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(new File(uri.getPath())).toString());
    }
}
