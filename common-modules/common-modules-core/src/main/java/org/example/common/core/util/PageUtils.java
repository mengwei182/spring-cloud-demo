package org.example.common.core.util;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * @author lihui
 * @since 2023/4/3
 */
public class PageUtils {
    private PageUtils() {
    }

    public static <T> Page<T> wrap(Page<?> page, Class<T> clazz) {
        Page<T> resultPage = new Page<>();
        try {
            List<?> records = page.getRecords();
            BeanUtils.copyProperties(page, resultPage);
            resultPage.setRecords(CommonUtils.transformList(records, clazz));
            return resultPage;
        } catch (Exception e) {
            return resultPage;
        }
    }
}