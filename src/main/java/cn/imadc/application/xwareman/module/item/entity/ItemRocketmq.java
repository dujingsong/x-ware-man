package cn.imadc.application.xwareman.module.item.entity;

import cn.imadc.application.base.common.persistence.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 * redis监控项数据搜集表
 * </p>
 *
 * @author 杜劲松
 * @since 2022-06-10
 */
@Getter
@Setter
@TableName("item_rocketmq")
public class ItemRocketmq extends BaseEntity implements Serializable {

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
     * rocketmq实例ID
     */
    private Long instanceRocketmqId;

    /**
     * 实例ID
     */
    private Long instanceId;

    /**
     * <=0ms
     */
    private Long putMessageDistributeTime0;

    /**
     * 0~10ms
     */
    private Long putMessageDistributeTime10;

    /**
     * 10~50ms
     */
    private Long putMessageDistributeTime50;

    /**
     * 50~100ms
     */
    private Long putMessageDistributeTime100;

    /**
     * 100~200ms
     */
    private Long putMessageDistributeTime200;

    /**
     * 200~500ms
     */
    private Long putMessageDistributeTime500;

    /**
     * 500~1000ms
     */
    private Long putMessageDistributeTime1000;

    /**
     * 1000~2000ms
     */
    private Long putMessageDistributeTime2000;

    /**
     * 2000~3000ms
     */
    private Long putMessageDistributeTime3000;

    /**
     * 3000~4000ms
     */
    private Long putMessageDistributeTime4000;

    /**
     * 4000~5000ms
     */
    private Long putMessageDistributeTime5000;

    /**
     * 5000~10000ms
     */
    private Long putMessageDistributeTime10000;

    /**
     * 10000ms~
     */
    private Long putMessageDistributeTimeOver;

    /**
     * 剩余可用堆外内存
     */
    private Long remainTransientStoreBufferNumbs;

    /**
     * 消息写入均值
     */
    private Double putMessageAverageSize;

    /**
     * pagecache锁定时长
     */
    private Long pageCacheLockTimeMills;

    /**
     * 现在存储的消息数量
     */
    private Long msgPutTotalTodayNow;

    /**
     * 今天存储的消息数量
     */
    private Long msgPutTotalTodayMorning;

    /**
     * 昨天存储的消息数量
     */
    private Long msgPutTotalYesterdayMorning;

    /**
     * 昨天消费的消息数量
     */
    private Long msgGetTotalYesterdayMorning;

    /**
     * 今天消费的消息数量
     */
    private Long msgGetTotalTodayMorning;

    /**
     * 现在消费的消息数量
     */
    private Long msgGetTotalTodayNow;

    /**
     * tps
     */
    private Double putTps;

}
