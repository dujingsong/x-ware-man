package cn.imadc.application.xwareman.module.event.entity;

import cn.imadc.application.base.common.persistence.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 * 告警记录
 * </p>
 *
 * @author 杜劲松
 * @since 2022-05-25
 */
@Getter
@Setter
@TableName("item_event")
public class ItemEvent extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增长主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 备注
     */
    private String notes;

    /**
     * 实例ID
     */
    private Long instanceId;

    /**
     * 具体的实例ID（redis、rocketmq）
     */
    private Long instanceItemId;

    /**
     * 实例类型（0：redis、1：rocketmq）
     */
    private Integer instanceType;

    /**
     * 触发器记录ID
     */
    private Long itemTriggerId;

    /**
     * 事件说明
     */
    private String eventDesc;


}
