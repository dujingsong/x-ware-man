package cn.imadc.application.xwareman.core.data.component.item;

import cn.hutool.core.util.StrUtil;
import cn.imadc.application.xwareman.module.item.entity.ItemRedis;
import cn.imadc.application.xwareman.module.item.entity.ItemRocketmq;
import cn.imadc.application.xwareman.module.item.service.IItemRedisService;
import cn.imadc.application.xwareman.module.item.service.IItemRocketmqService;
import cn.imadc.application.xwareman.module.trigger.entity.Trigger;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 监控项数据处理器
 * </p>
 *
 * @author 杜劲松
 * @since 2022-04-22
 */
@Slf4j
@AllArgsConstructor
@Component
public class InstanceItemDataComponent {

    private final IItemRedisService itemRedisService;
    private final IItemRocketmqService itemRocketmqService;

    /**
     * 查询指定时间段内指定字段的数据
     *
     * @param trigger 触发器配置
     * @return 数据列表
     */
    public List<Object> selectColAtSpecifiedTime(Trigger trigger) {

        try {

            String col = trigger.getCol();
            LocalDateTime end = LocalDateTime.now();
            LocalDateTime begin = end.minusMinutes(trigger.getSpan());

            String columName = getColumName(trigger.getType(), col);
            // 类型（0：redis 1：rocketmq）
            if (trigger.getType() == 0) {
                return itemRedisService.selectColAtSpecifiedTime(columName, begin, end, trigger.getInstanceItemId());
            } else {
                return itemRocketmqService.selectColAtSpecifiedTime(columName, begin, end, trigger.getInstanceItemId());
            }
        } catch (Exception e) {
            log.error("监控项数据处理器出现异常，triggerId：{}", trigger.getId(), e);
        }

        return null;
    }

    /**
     * 获取实体对应数据库中字段名
     *
     * @param type 类型（0：redis 1：rocketmq）
     * @param col  实体字段名
     * @return 实体对应的数据库中字段名
     * @throws NoSuchFieldException 找不到对应实体字段时抛出
     */
    private String getColumName(Integer type, String col) throws NoSuchFieldException {
        String columName;
        Field field;
        if (type == 0) {
            field = ItemRedis.class.getDeclaredField(col);
        } else {
            field = ItemRocketmq.class.getDeclaredField(col);
        }
        TableField tableField = field.getAnnotation(TableField.class);
        if (null != tableField && StringUtils.isEmpty(tableField.value())) {
            columName = tableField.value();
        } else {
            columName = StrUtil.toUnderlineCase(col);
        }
        return columName;
    }

}
