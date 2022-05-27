package cn.imadc.application.xwareman.core.data.component.item;

import cn.hutool.core.util.StrUtil;
import cn.imadc.application.xwareman.module.item.entity.ItemRedis;
import cn.imadc.application.xwareman.module.item.service.IItemRedisService;
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

            // 类型（0：redis 1：rocketmq）
            if (trigger.getType() == 0) {
                String columName;

                Field field = ItemRedis.class.getDeclaredField(col);
                TableField tableField = field.getAnnotation(TableField.class);
                if (null != tableField && StringUtils.isEmpty(tableField.value())) {
                    columName = tableField.value();
                } else {
                    columName = StrUtil.toUnderlineCase(col);
                }

                return itemRedisService.selectColAtSpecifiedTime(columName, begin, end, trigger.getInstanceItemId());
            }
        } catch (Exception e) {
            log.error("监控项数据处理器出现异常，triggerId：{}", trigger.getId(), e);
        }

        return null;
    }

}
