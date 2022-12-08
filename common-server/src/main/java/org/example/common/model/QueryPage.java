package org.example.common.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class QueryPage {
    private int pageSize = 10;
    private int pageNumber = 1;
    // 排序列集合
    private List<String> columns = new ArrayList<>();
    // 排序序列集合
    private List<String> sorts = new ArrayList<>();
}