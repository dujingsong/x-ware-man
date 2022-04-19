package cn.imadc.application.xwareman.module.trigger.entity;

import cn.imadc.application.base.common.persistence.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 * 触发器
 * </p>
 *
 * @author 杜劲松
 * @since 2022-04-18
 */
@Getter
@Setter
@TableName("item_trigger")
public class Trigger extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增长主键
     */
    private Long id;

    /**
     * 备注
     */
    private String notes;

    /**
     * 开启状态（0：关闭 1：开启）
     */
    private Integer active;

    /**
     * 类型（0：redis 1：rocketmq）
     */
    private Integer type;

    /**
     * 实例ID
     */
    private Long instanceId;

    /**
     * 具体的实例ID（redis、rocketmq）
     */
    private Long instanceItemId;

    /**
     * 策略
     */
    private Integer strategy;

    /**
     * 策略字段
     */
    private String col;

    /**
     * 阈值
     */
    private Double threshold;

    /**
     * 式样
     */
    private String pattern;


}
