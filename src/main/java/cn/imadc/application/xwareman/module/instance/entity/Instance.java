package cn.imadc.application.xwareman.module.instance.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 实例表
 * </p>
 *
 * @author 杜劲松
 * @since 2022-03-07
 */
@Getter
@Setter
public class Instance implements Serializable {

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
     * ip
     */
    private String ip;

    /**
     * 区域ID
     */
    private Long zoneId;

    /**
     * cpu个数
     */
    private Integer cpu;

    /**
     * 内存b
     */
    private Long memory;

    /**
     * 磁盘b
     */
    private Long disk;

    /**
     * 名称
     */
    private String name;

    /**
     * 备注
     */
    private String notes;


}
