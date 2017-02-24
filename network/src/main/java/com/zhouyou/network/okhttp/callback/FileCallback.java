package com.zhouyou.network.okhttp.callback;

import android.support.annotation.NonNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Response;

/**
 * 作者：ZhouYou
 * 日期：2017/2/24.
 */
public abstract class FileCallback extends AbsCallback<File> {

    private String destPath;
    private String fileName;

    public FileCallback(@NonNull String destPath, @NonNull String fileName) {
        this.destPath = destPath;
        this.fileName = fileName;
    }

    @Override
    public File parseResponse(Response resp) {
        return downloadFile(resp);
    }

    private File downloadFile(Response resp) {
        InputStream is = null;
        File dir = new File(destPath);
        if (!dir.exists()) dir.mkdirs();
        File file = new File(dir, fileName);

        FileOutputStream fos = null;
        byte[] buf = new byte[1024];
        int len;
        try {
            is = resp.body().byteStream();
            fos = new FileOutputStream(file);
            while ((len = is.read(buf)) != -1) {

                fos.write(buf, 0, len);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (resp.body() != null) resp.body().close();
            try {
                if (fos != null) {
                    fos.flush();
                    fos.close();
                }
                if (is != null) is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }
}
