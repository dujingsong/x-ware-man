package cn.imadc.application.xwareman.module.trigger.entity;

import cn.imadc.application.base.common.persistence.BaseEntity;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.TableField;
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
     * 策略
     */
    @TableField(exist = false)
    private String strategyDesc;

    /**
     * 策略字段
     */
    private String col;

    /**
     * 阈值
     */
    private Double threshold;

    /**
     * 规则式样（如：扫描redis关键key匹配）
     */
    private String pattern;

    /**
     * 规则式样
     */
    @TableField(exist = false)
    private JSONObject patternJson;

    /**
     * 周期类型（0：rate 1：cron）
     */
    private Integer periodType;

    /**
     * 周期 （ periodType=0 ： minutes periodType=1 ： cron expression）
     */
    private String period;

    /**
     * 周期说明
     */
    @TableField(exist = false)
    private String periodDesc;

    /**
     * 扫描跨度（minutes）
     */
    private Long span;
}
