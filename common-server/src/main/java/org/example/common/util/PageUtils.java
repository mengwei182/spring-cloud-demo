package org.example.common.util;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

public class PageUtils {
    private PageUtils() {
    }

    public static <T> Page<T> wrap(Page<?> page, Class<T> clazz) {
        Page<T> resultPage = new Page<>();
        try {
            List<?> records = page.getRecords();
            BeanUtils.copyProperties(page, records);
            List<T> resultRecords = new ArrayList<>();
            for (Object record : records) {
                T t = clazz.newInstance();
                BeanUtils.copyProperties(record, t);
                resultRecords.add(t);
                resultPage.setRecords(resultRecords);
            }
            return resultPage;
        } catch (Exception e) {
            return resultPage;
        }
    }
}