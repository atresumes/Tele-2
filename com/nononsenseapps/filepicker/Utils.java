package com.nononsenseapps.filepicker;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Utils {
    private static final String SEP = "/";

    public static boolean isValidFileName(@Nullable String name) {
        return (TextUtils.isEmpty(name) || name.contains(SEP) || name.equals(".") || name.equals("..")) ? false : true;
    }

    @NonNull
    public static String appendPath(@NonNull String first, @NonNull String second) {
        String result = first + SEP + second;
        while (result.contains("//")) {
            result = result.replaceAll("//", SEP);
        }
        if (result.length() <= 1 || !result.endsWith(SEP)) {
            return result;
        }
        return result.substring(0, result.length() - 1);
    }

    @NonNull
    public static File getFileForUri(@NonNull Uri uri) {
        String path = uri.getEncodedPath();
        int splitIndex = path.indexOf(47, 1);
        String tag = Uri.decode(path.substring(1, splitIndex));
        path = Uri.decode(path.substring(splitIndex + 1));
        if ("root".equalsIgnoreCase(tag)) {
            File root = new File(SEP);
            File file = new File(root, path);
            try {
                file = file.getCanonicalFile();
                if (file.getPath().startsWith(root.getPath())) {
                    return file;
                }
                throw new SecurityException("Resolved path jumped beyond configured root");
            } catch (IOException e) {
                throw new IllegalArgumentException("Failed to resolve canonical path for " + file);
            }
        }
        throw new IllegalArgumentException(String.format("Can't decode paths to '%s', only for 'root' paths.", new Object[]{tag}));
    }

    @NonNull
    public static List<Uri> getSelectedFilesFromResult(@NonNull Intent data) {
        List<Uri> result = new ArrayList();
        if (data.getBooleanExtra(AbstractFilePickerActivity.EXTRA_ALLOW_MULTIPLE, false)) {
            List<String> paths = data.getStringArrayListExtra(AbstractFilePickerActivity.EXTRA_PATHS);
            if (paths != null) {
                for (String path : paths) {
                    result.add(Uri.parse(path));
                }
            }
        } else {
            result.add(data.getData());
        }
        return result;
    }
}
