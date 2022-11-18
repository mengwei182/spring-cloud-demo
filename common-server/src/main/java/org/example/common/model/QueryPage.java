package org.example.common.model;

import java.util.HashMap;
import java.util.Map;

public class QueryPage {
    private static final String ASC = "ASC";
    private static final String DESC = "DESC";
    private final Map<String, String> sortParamMap = new HashMap<>();
    private int rows = 10;
    private int offset = 0;
    private int pageSize = 10;
    private int pageNumber = 1;
    private boolean pageSizeSet = false;
    private boolean pageNumberSet = false;

    public int getRows() {
        return rows;
    }

    public int getOffset() {
        return offset;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
        this.pageSizeSet = true;
        if (pageNumberSet) {
            build();
        }
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
        this.pageNumberSet = true;
        if (pageSizeSet) {
            build();
        }
    }

    private void build() {
        this.offset = (this.pageNumber - 1) * this.pageSize;
        this.rows = this.pageSize;
    }

    public Map<String, String> getSortParamMap() {
        return sortParamMap;
    }

    public QueryPage addASCSortParam(String paramName) {
        this.sortParamMap.put(paramName, ASC);
        return this;
    }

    public QueryPage addDescSortParam(String paramName) {
        this.sortParamMap.put(paramName, DESC);
        return this;
    }
}