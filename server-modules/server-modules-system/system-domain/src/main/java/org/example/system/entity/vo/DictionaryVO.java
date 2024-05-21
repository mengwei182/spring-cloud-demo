package org.example.system.entity.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.common.core.domain.BaseEntity;
import org.example.common.core.tree.TreeModelField;
import org.example.common.core.tree.TreeModelFieldEnum;

/**
 * @author lihui
 * @since 2022/10/29
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DictionaryVO extends BaseEntity {
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