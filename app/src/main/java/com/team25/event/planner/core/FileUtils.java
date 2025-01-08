package com.team25.event.planner.core;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileUtils {
    public static File getFileFromUri(Context context, Uri uri) {
        String filePath = null;
        if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme())) {
            try (Cursor cursor = context.getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (index != -1) {
                        String displayName = cursor.getString(index);
                        File file = new File(context.getCacheDir(), displayName);
                        try (InputStream inputStream = context.getContentResolver().openInputStream(uri);
                             FileOutputStream outputStream = new FileOutputStream(file)) {
                            if (inputStream != null) {
                                byte[] buffer = new byte[1024];
                                while ((inputStream.read(buffer)) != -1) {
                                    outputStream.write(buffer);
                                }
                            }
                            return file;
                        } catch (IOException e) {
                            Log.e("FileUtils", "Failed to convert uri to file", e);
                        }
                    }
                }
            }
        } else if (ContentResolver.SCHEME_FILE.equals(uri.getScheme())) {
            filePath = uri.getPath();
        }
        return filePath != null ? new File(filePath) : null;
    }
}
