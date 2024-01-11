package org.example.common.util.tree;

import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TreeModelField {
    /**
     * <p>标记对象中的字段和TreeModel的一个字段对应<p/>
     * <p>见@link TreeModelFieldEnum枚举类<p/>
     *
     * @return 树形字段枚举
     */
    TreeModelFieldEnum value();
}