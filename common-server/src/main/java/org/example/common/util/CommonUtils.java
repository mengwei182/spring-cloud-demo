package org.example.common.util;

import com.google.gson.Gson;
import org.example.common.entity.vo.TreeModel;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CommonUtils {
    private static final Gson GSON = new Gson();

    private CommonUtils() {
    }

    public static String uuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static Gson gson() {
        return GSON;
    }

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
}