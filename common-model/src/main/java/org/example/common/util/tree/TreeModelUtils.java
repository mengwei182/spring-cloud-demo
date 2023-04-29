package org.example.common.util.tree;

import lombok.extern.slf4j.Slf4j;
import org.example.common.entity.base.vo.TreeModel;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author lihui
 * @since 2023/4/3
 */
@Slf4j
public class TreeModelUtils {
    /**
     * 构建树形结构
     *
     * @param objects 对象必须有id、name、parentId字段
     * @return
     */
    public static List<TreeModel> buildTreeModel(List<?> objects) {
        List<TreeModel> treeModels = new ArrayList<>();
        List<TreeModel> resultTreeModels = new ArrayList<>();
        for (Object object : objects) {
            TreeModel treeModel = new TreeModel();
            Class<?> clazz = object.getClass();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                TreeModelField annotation = field.getAnnotation(TreeModelField.class);
                if (annotation == null) {
                    continue;
                }
                try {
                    switch (annotation.field()) {
                        case ID -> treeModel.setId(String.valueOf(field.get(object)));
                        case NAME -> treeModel.setName(String.valueOf(field.get(object)));
                        case PARENT_ID -> treeModel.setParentId(String.valueOf(field.get(object)));
                    }
                } catch (Exception e) {
                    log.error("build tree model error:{}", e.getMessage());
                }
            }
            treeModels.add(treeModel);
        }
        // 获取全部id集合
        Set<String> ids = treeModels.stream().map(TreeModel::getId).collect(Collectors.toSet());
        for (TreeModel treeModel : treeModels) {
            // 所属的parentId在id集合中不存在，即为根节点
            if (!ids.contains(treeModel.getParentId())) {
                resultTreeModels.add(treeModel);
                buildChildren(treeModels, treeModel);
            }
        }
        return resultTreeModels;
    }

    private static void buildChildren(List<TreeModel> treeModels, TreeModel treeModel) {
        List<TreeModel> children = treeModels.stream().filter(o -> o.getParentId().equals(treeModel.getId())).toList();
        treeModel.setChildren(children);
        for (TreeModel tm : children) {
            buildChildren(treeModels, tm);
        }
    }
}