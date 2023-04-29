package org.example.common.entity.base.vo;

import lombok.Data;

import java.util.List;

/**
 * 树型数据模型
 *
 * @author lihui
 * @since 2023/4/2
 */
@Data
public class TreeModel {
    private String id;
    private String name;
    private String parentId;
    private List<TreeModel> children;
}