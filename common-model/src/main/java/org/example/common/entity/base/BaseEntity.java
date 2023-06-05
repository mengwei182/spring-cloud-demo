package org.example.common.entity.base;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import org.example.common.util.tree.TreeModelField;
import org.example.common.util.tree.TreeModelFieldEnum;

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
    @TreeModelField(field = TreeModelFieldEnum.ID)
    private String id;
    /**
     * 创建者id
     */
    private String createId;
    /**
     * 更新者id
     */
    private String updateId;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 删除标志：0未删除，1已删除
     */
    @TableLogic(value = "0", delval = "1")
    private Integer deleted = 0;
}