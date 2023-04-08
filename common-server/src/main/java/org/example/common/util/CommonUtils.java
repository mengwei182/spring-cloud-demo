package org.example.common.util;

import com.google.gson.Gson;
import org.example.common.entity.base.vo.TreeModel;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author lihui
 * @since 2023/4/3
 */
public class CommonUtils {
    private static final Gson GSON = new Gson();

    private CommonUtils() {
    }

    /**
     * 生成uuid
     *
     * @return
     */
    public static String uuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 返回全局GSON对象
     *
     * @return
     */
    public static Gson gson() {
        return GSON;
    }

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
            BeanUtils.copyProperties(object, treeModel);
            treeModels.add(treeModel);
        }
        List<String> parentIds = treeModels.stream().map(TreeModel::getParentId).toList();
        for (TreeModel treeModel : treeModels) {
            if (!parentIds.contains(treeModel.getId())) {
                resultTreeModels.add(treeModel);
            }
        }
        return resultTreeModels;
    }

    private static void buildChildren(List<TreeModel> treeModels, TreeModel treeModel) {
        List<TreeModel> children = treeModels.stream().filter(o -> o.getParentId().equals(treeModel.getId())).toList();
        treeModel.setChildren(children);
        for (TreeModel tm : treeModels) {
            buildChildren(treeModels, tm);
        }
    }

    /**
     * 转换list成目标类型集合
     *
     * @param list
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> transformList(List<?> list, Class<T> clazz) {
        List<T> resultList = new ArrayList<>();
        for (Object object : list) {
            try {
                T t = clazz.getConstructor().newInstance();
                BeanUtils.copyProperties(object, t);
                resultList.add(t);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return resultList;
    }

    /**
     * 转换object成目标类型对象
     *
     * @param object
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T transformObject(Object object, Class<T> clazz) {
        try {
            T t = clazz.getConstructor().newInstance();
            BeanUtils.copyProperties(object, t);
            return t;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}