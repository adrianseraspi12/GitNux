package com.adrianseraspi.gitnux.api.model;

import android.util.Base64;

import java.io.UnsupportedEncodingException;

public class File {

    private String name;
    private String content;

    public String getName() {
        return name;
    }

    public String getContent() {
        return decodeContent();
    }

    private String decodeContent() {
        byte[] valueDecoded = new byte[0];

        try {
            valueDecoded = Base64.decode(content.getBytes("UTF-8"), Base64.DEFAULT);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new String(valueDecoded);
    }
}
