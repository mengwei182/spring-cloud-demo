package org.example.common.core.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.example.common.core.tree.TreeModelField;
import org.example.common.core.tree.TreeModelFieldEnum;

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
    public static final Long TOP_PARENT_ID = 0L;
    @TableId
    @TreeModelField(TreeModelFieldEnum.ID)
    private Long id;
    /**
     * 创建者id
     */
    private Long creator;
    /**
     * 更新者id
     */
    private Long updater;
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