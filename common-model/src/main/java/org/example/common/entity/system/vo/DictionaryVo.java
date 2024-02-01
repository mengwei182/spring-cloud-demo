package org.example.common.entity.system.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.common.entity.base.BaseEntity;
import org.example.common.tree.TreeModelField;
import org.example.common.tree.TreeModelFieldEnum;

/**
 * @author lihui
 * @since 2022/10/29
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DictionaryVo extends BaseEntity {
    /**
     * 名称
     */
    @TreeModelField(TreeModelFieldEnum.LABEL)
    private String name;
    /**
     * 编码
     */
    private String code;
    /**
     * 父级id
     */
    @TreeModelField(TreeModelFieldEnum.PARENT_ID)
    private String parentId;
    /**
     * id链
     */
    private String idChain;
}