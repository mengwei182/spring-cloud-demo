package org.example.common.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.example.common.tree.TreeModelField;
import org.example.common.tree.TreeModelFieldEnum;

import java.io.Serializable;
import java.util.Date;

/**
 * @author lihui
 * @since 2022/10/25
 */
@Data
public abstract class BaseEntity implements Serializable {
    /**
     * 顶级节点父级ID
     */
    public static final String TOP_PARENT_ID = "0";
    @TableId
    @TreeModelField(TreeModelFieldEnum.ID)
    private String id;
    /**
     * 创建者id
     */
    private String creator;
    /**
     * 更新者id
     */
    private String updater;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    /**
     * 删除标志：0未删除，1已删除
     */
    @TableLogic(value = "0", delval = "1")
    private Integer deleted = 0;
}