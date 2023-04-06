package org.example.common.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author lihui
 * @since 2022/10/25
 */
@Data
public abstract class BaseEntity implements Serializable {
    /**
     * 已删除
     */
    public static final Integer DELETED = 1;
    /**
     * 未删除
     */
    public static final Integer UNDELETED = 0;
    @TableField(exist = false)
    public static final String TOP_LEVEL_ID = "0";
    @TableId
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