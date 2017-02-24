package com.zhouyou.network.okhttp.callback;

import android.support.annotation.NonNull;

import java.io.File;
import java.io.IOException;

import okhttp3.Response;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;

/**
 * 作者：ZhouYou
 * 日期：2017/2/24.
 */
public abstract class FileCallback extends AbsCallback<File> {

    private String dir;
    private String name;

    public FileCallback(@NonNull String dir, @NonNull String name) {
        this.dir = dir;
        this.name = name;
    }

    @Override
    public File parseResponse(Response resp) {
        return downloadFile(resp);
    }

    private File downloadFile(Response resp) {
        BufferedSink sink;
        BufferedSource source;
        File file = null;
        try {
            source = resp.body().source();
            file = new File(dir, name);
            if (!file.exists()) file.createNewFile();
            sink = Okio.buffer(Okio.sink(file));
            byte[] buf = new byte[1024];
            int len;
            while ((len = source.read(buf)) != -1) {
                sink.write(buf, 0, len);
            }
            sink.close();
            source.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (resp.body() != null) {
                resp.body().close();
            }
        }
        return file;
    }
}
