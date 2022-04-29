package cn.imadc.application.xwareman.core.data.component.item;

import cn.imadc.application.xwareman.module.item.service.IItemRedisService;
import cn.imadc.application.xwareman.module.trigger.entity.Trigger;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

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
        String col = trigger.getCol();
        LocalDateTime end = LocalDateTime.now();
        LocalDateTime begin = end.minusMinutes(trigger.getSpan());

        // 类型（0：redis 1：rocketmq）
        if (trigger.getType() == 0) {
            return itemRedisService.selectColAtSpecifiedTime(col, begin, end);
        }

        return null;
    }

}
