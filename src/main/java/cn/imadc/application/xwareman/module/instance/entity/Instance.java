package cn.imadc.application.xwareman.module.instance.entity;

import cn.imadc.application.base.common.persistence.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 * 实例表
 * </p>
 *
 * @author 杜劲松
 * @since 2022-03-07
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Instance extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增长主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

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

    public Instance(String ip) {
        this.ip = ip;
    }
}