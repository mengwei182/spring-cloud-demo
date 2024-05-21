package org.example.common.core.domain;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lihui
 * @since 2023/4/3
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class QueryPage extends Page<Object> {
    private int pageSize = 10;
    private int pageNumber = 1;
    /**
     * 排序列集合
     */
    private List<String> columns = new ArrayList<>();
    /**
     * 排序序列集合
     */
    private List<String> sorts = new ArrayList<>();
}