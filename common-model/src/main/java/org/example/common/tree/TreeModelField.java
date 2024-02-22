package org.example.common.tree;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

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