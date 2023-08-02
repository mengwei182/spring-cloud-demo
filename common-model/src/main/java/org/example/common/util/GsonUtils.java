package org.example.common.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

/**
 * @author lihui
 * @since 2023/4/3
 */
public class GsonUtils {
    private static final Gson GSON = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    private GsonUtils() {
    }

    /**
     * 返回全局GSON对象
     *
     * @return
     */
    public static Gson gson() {
        return GSON;
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        return GsonUtils.gson().fromJson(json, TypeToken.get(clazz));
    }
}