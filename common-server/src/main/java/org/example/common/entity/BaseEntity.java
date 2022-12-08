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
    private Date createTime;
    private Date updateTime;
    // 是否删除：0.未删除，1.已删除
    private Integer deleted = 0;
}