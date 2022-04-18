package cn.imadc.application.xwareman.core.component;

import cn.imadc.application.base.common.exception.BizException;
import cn.imadc.application.xwareman.core.data.annoations.StrategyTrigger;
import cn.imadc.application.xwareman.core.data.constant.Word;
import cn.imadc.application.xwareman.core.data.enums.TriggerStrategyEnum;
import cn.imadc.application.xwareman.core.data.strategy.ITriggerStrategy;
import cn.imadc.application.xwareman.core.util.SpringUtil;
import cn.imadc.application.xwareman.module.trigger.entity.Trigger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 触发器策略容器
 * </p>
 *
 * @author 杜劲松
 * @since 2022-04-18
 */
@Slf4j
@Component
public class TriggerStrategyContext {

    private final Map<TriggerStrategyEnum, ITriggerStrategy> strategy_bean_map = new HashMap<>();

    /**
     * 处理
     *
     * @param trigger 触发器
     */
    public void handle(Trigger trigger) {
        ITriggerStrategy triggerStrategy = getStrategyBean(trigger);
        try {
            triggerStrategy.handle(trigger);
        } catch (Exception e) {
            log.error(Word.TRIGGER_STRATEGY_EXCEPTION, e);
        }
    }

    private ITriggerStrategy getStrategyBean(Trigger trigger) {

        TriggerStrategyEnum matchedTriggerStrategyEnum = null;
        for (TriggerStrategyEnum triggerStrategyEnum : TriggerStrategyEnum.values()) {
            if (trigger.getStrategy() == triggerStrategyEnum.getValue()) {
                matchedTriggerStrategyEnum = triggerStrategyEnum;
                break;
            }
        }

        ITriggerStrategy triggerStrategy = strategy_bean_map.get(matchedTriggerStrategyEnum);
        if (null != triggerStrategy) return triggerStrategy;

        ApplicationContext applicationContext = SpringUtil.getApplicationContext();
        Map<String, Object> beanMap = applicationContext.getBeansWithAnnotation(StrategyTrigger.class);

        Set<Map.Entry<String, Object>> entrySet = beanMap.entrySet();

        for (Map.Entry<String, Object> entry : entrySet) {
            Object bean = entry.getValue();
            Object proxyBean = SpringUtil.getTarget(bean);

            StrategyTrigger strategyTrigger = proxyBean.getClass().getDeclaredAnnotation(StrategyTrigger.class);
            if (strategyTrigger.strategy().equals(matchedTriggerStrategyEnum)) return (ITriggerStrategy) bean;
        }

        throw new BizException(Word.OP_FAIL);
    }
}
