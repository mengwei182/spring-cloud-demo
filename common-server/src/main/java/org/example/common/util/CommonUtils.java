package org.example.common.util;

import com.google.gson.Gson;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author lihui
 * @since 2023/4/3
 */
public class CommonUtils {
    private static final Gson GSON = new Gson();

    private CommonUtils() {
    }

    /**
     * 生成uuid
     *
     * @return
     */
    public static String uuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 返回全局GSON对象
     *
     * @return
     */
    public static Gson gson() {
        return GSON;
    }

    /**
     * 转换list成目标类型集合
     *
     * @param list
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> transformList(List<?> list, Class<T> clazz) {
        List<T> resultList = new ArrayList<>();
        if (CollectionUtils.isEmpty(list)) {
            return resultList;
        }
        for (Object object : list) {
            try {
                T t = clazz.getConstructor().newInstance();
                BeanUtils.copyProperties(object, t);
                resultList.add(t);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return resultList;
    }

    /**
     * 转换object成目标类型对象
     *
     * @param object
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T transformObject(Object object, Class<T> clazz) {
        try {
            T t = clazz.getConstructor().newInstance();
            BeanUtils.copyProperties(object, t);
            return t;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}