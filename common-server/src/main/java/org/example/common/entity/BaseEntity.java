package org.example.common.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author lihui
 * @since 2022/10/25
 */
@Data
public abstract class BaseEntity implements Serializable {
    @TableId
    private String id;
    // 创建者id
    private String createId;
    // 更新者id
    private String updateId;
    // 创建时间
    private Date createTime;
    // 更新时间
    private Date updateTime;
    // 是否删除：0未删除，1已删除
    private Integer deleted = 0;
}