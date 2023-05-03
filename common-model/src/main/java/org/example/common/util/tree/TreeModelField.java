package org.example.common.util.tree;

import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TreeModelField {
    /**
     * 标记对象中的字段和TreeModel的一个字段对应
     *
     * @return
     */
    TreeModelFieldEnum field();
}