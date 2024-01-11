package org.example.common.util;

import cn.hutool.core.collection.CollectionUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.cglib.beans.BeanMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author lihui
 * @since 2023/4/3
 */
public class CommonUtils {
    private CommonUtils() {
    }

    /**
     * 生成uuid
     *
     * @return 去除“-”的uuid
     */
    public static String uuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 转换list成目标类型集合
     *
     * @param list 待转换的集合
     * @param clazz 目标类型
     * @param <T> 类型泛型
     * @return 转换后的集合
     */
    public static <T> List<T> transformList(List<?> list, Class<T> clazz) {
        List<T> resultList = new ArrayList<>();
        if (CollectionUtil.isEmpty(list)) {
            return resultList;
        }
        for (Object object : list) {
            resultList.add(transformObject(object, clazz));
        }
        return resultList;
    }

    /**
     * 转换object成目标类型对象
     *
     * @param object 待转换的对象
     * @param clazz 目标类型
     * @param <T> 类型泛型
     * @return 转换后的对象
     */
    public static <T> T transformObject(Object object, Class<T> clazz) {
        if (object == null) {
            return null;
        }
        try {
            T t = clazz.getConstructor().newInstance();
            if (object instanceof Map<?, ?>) {
                BeanMap beanMap = BeanMap.create(t);
                beanMap.putAll((Map<?, ?>) object);
            } else {
                BeanUtils.copyProperties(object, t);
            }
            return t;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}