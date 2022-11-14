package org.example.common.util;

import com.google.gson.Gson;

import java.util.UUID;

public class CommonUtils {
    private static final Gson GSON = new Gson();

    private CommonUtils() {
    }

    public static String uuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static Gson gson() {
        return GSON;
    }
}