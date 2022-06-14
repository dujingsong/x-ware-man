package cn.imadc.application.xwareman.module.item.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * rocketmq相关项表
 * </p>
 *
 * @author 杜劲松
 * @since 2022-06-13
 */
@Getter
@Setter
@TableName("item_rocketmq_item")
public class ItemRocketmqItem implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增长主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 0：未删除；1：已删除
     */
    private Integer delFlag;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 创建者
     */
    private String createBy;

    /**
     * 更新者
     */
    private String updateBy;

    /**
     * 创建者ID
     */
    private Long createById;

    /**
     * 更新者ID
     */
    private Long updateById;

    /**
     * 备注
     */
    private String notes;

    /**
     * topic或消费组
     */
    private String itemName;

    /**
     * 0:topic;1:消费组
     */
    private Integer type;

    /**
     * 集群信息
     */
    private String clusterInfo;

    /**
     * accessKey
     */
    private String accessKey;

    /**
     * accessSecret
     */
    private String accessSecret;


}
