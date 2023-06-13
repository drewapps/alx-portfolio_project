package com.drewapps.ai.utils;

import android.content.Context;

import java.io.File;

public class StorageUtils {

    Context context;

    public StorageUtils(Context context) {
        this.context = context;
    }

    public File getPackageStorageDir(String folderName) {
        File file = null;
        try {

            int position = 0;
            for (position = 0; position < context.getExternalMediaDirs().length; position++) {
                if (context.getExternalMediaDirs()[position].getAbsolutePath().startsWith("/storage/emulated/0/Android/media/")) {
                    break;
                }

            }
            if (position != context.getExternalMediaDirs().length) {
                file = new File(context.getExternalMediaDirs()[position], folderName);
                if (!file.exists()) {
                    file.mkdirs();
                }
            }
            return file;
        } catch (Exception e) {
            return file;
        }

    }
}
