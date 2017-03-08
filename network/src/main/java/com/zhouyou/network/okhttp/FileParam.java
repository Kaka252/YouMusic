package com.zhouyou.network.okhttp;

import java.io.File;

/**
 * Created by zhouyou on 17/3/8.
 */

public class FileParam {

    public String name;
    public String fileName;
    public File file;

    public FileParam() {
    }

    public FileParam(String name, String fileName, File file) {
        this.name = name;
        this.fileName = fileName;
        this.file = file;
    }
}
