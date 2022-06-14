package cn.imadc.application.xwareman.module.trigger.service.impl;

import cn.imadc.application.base.common.exception.BizException;
import cn.imadc.application.base.common.response.ResponseW;
import cn.imadc.application.base.mybatisplus.repository.impl.BaseMPServiceImpl;
import cn.imadc.application.xwareman.core.data.constant.Constant;
import cn.imadc.application.xwareman.core.data.container.TriggerContainerRegister;
import cn.imadc.application.xwareman.core.data.enums.TriggerStrategyEnum;
import cn.imadc.application.xwareman.module.instance.entity.InstanceRedis;
import cn.imadc.application.xwareman.module.instance.entity.InstanceRocketmq;
import cn.imadc.application.xwareman.module.instance.service.IInstanceRedisService;
import cn.imadc.application.xwareman.module.instance.service.IInstanceRocketmqService;
import cn.imadc.application.xwareman.module.instance.service.IInstanceService;
import cn.imadc.application.xwareman.module.trigger.dto.data.TriggerStrategyData;
import cn.imadc.application.xwareman.module.trigger.dto.request.ListTriggerStrategyReqDTO;
import cn.imadc.application.xwareman.module.trigger.dto.request.TriggerFindReqDTO;
import cn.imadc.application.xwareman.module.trigger.dto.response.ListTriggerStrategyResDTO;
import cn.imadc.application.xwareman.module.trigger.entity.Trigger;
import cn.imadc.application.xwareman.module.trigger.mapper.TriggerMapper;
import cn.imadc.application.xwareman.module.trigger.service.ITriggerService;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 触发器服务实现类
 * </p>
 *
 * @author 杜劲松
 * @since 2022-04-18
 */
@AllArgsConstructor
@Service
public class TriggerServiceImpl extends BaseMPServiceImpl<TriggerMapper, Trigger> implements ITriggerService {

    private final IInstanceRedisService instanceRedisService;
    private final IInstanceRocketmqService instanceRocketmqService;
    private final IInstanceService instanceService;
    private final TriggerContainerRegister triggerContainerRegister;

    @Override
    public ResponseW find(TriggerFindReqDTO reqDTO) {
        QueryWrapper<Trigger> queryWrapper = buildQueryWrapper(reqDTO);

        List<Trigger> triggerList = list(queryWrapper);
        handle(triggerList);
        if (!reqDTO.pageQuery()) return ResponseW.success(triggerList);

        Page<Trigger> page = new Page<>(reqDTO.getPageNo(), reqDTO.getPageSize(), true);
        IPage<Trigger> pageData = page(page, queryWrapper);
        handle(pageData.getRecords());
        return ResponseW.success(pageData);
    }

    private QueryWrapper<Trigger> buildQueryWrapper(TriggerFindReqDTO reqDTO) {
        QueryWrapper<Trigger> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(Constant.DEL_FLAG, Constant.NOT_DEL_VAL);

        // 具体实例ID
        if (null != reqDTO.getInstanceItemId()) {
            queryWrapper.eq("instance_item_id", reqDTO.getInstanceItemId());
        }

        // 类型
        if (null != reqDTO.getType()) {
            queryWrapper.eq("type", reqDTO.getType());
        }

        return queryWrapper;
    }

    private void handle(List<Trigger> triggerList) {
        for (Trigger trigger : triggerList) {
            Integer strategy = trigger.getStrategy();
            TriggerStrategyEnum strategyEnum = TriggerStrategyEnum.parse(strategy);
            trigger.setStrategyDesc(Objects.requireNonNull(strategyEnum).getDescription());

            Integer periodType = trigger.getPeriodType();
            String periodDesc = periodType == 0 ? "每隔" + trigger.getPeriod() + "分钟执行一次" : trigger.getPeriod();
            trigger.setPeriodDesc(periodDesc);

            if (StringUtils.isNotEmpty(trigger.getPattern())) {
                trigger.setPatternJson(JSONObject.parseObject(trigger.getPattern()));
            }
        }
    }

    @Override
    public List<Trigger> listTrigger(TriggerFindReqDTO triggerFindReqDTO) {
        QueryWrapper<Trigger> queryWrapper = buildQueryWrapper(triggerFindReqDTO);
        return list(queryWrapper);
    }

    @Override
    public ResponseW add(Trigger trigger) {
        Integer type = trigger.getType();
        Long instanceItemId = trigger.getInstanceItemId();
        if (type == 0) {
            InstanceRedis instanceRedis = instanceRedisService.getById(instanceItemId);
            trigger.setInstanceId(instanceRedis.getInstanceId());
        } else {
            InstanceRocketmq instanceRocketmq = instanceRocketmqService.getById(instanceItemId);
            trigger.setInstanceId(instanceRocketmq.getInstanceId());
        }

        if (null != trigger.getPatternJson()) {
            String pattern = trigger.getPatternJson().toJSONString();
            trigger.setPattern(pattern);
        }

        boolean status = save(trigger);
        if (!status) return ResponseW.error();

        Integer active = trigger.getActive();
        if (active == 1) {
            triggerContainerRegister.registerContainer(trigger);
        }

        return ResponseW.success();
    }

    @Override
    public ResponseW edit(Trigger trigger) {

        if (null != trigger.getPatternJson()) {
            String pattern = trigger.getPatternJson().toJSONString();
            trigger.setPattern(pattern);
        }

        boolean status = updateById(trigger);
        if (!status) return ResponseW.error();

        if (!triggerContainerRegister.stopTrigger(trigger.getId())) {
            throw new BizException("移除定时器失败");
        }

        if (!triggerContainerRegister.removeContainer(trigger.getId())) {
            throw new BizException("移除触发器容器失败");
        }

        Integer active = trigger.getActive();
        if (active == 1) {
            triggerContainerRegister.registerContainer(trigger);
        }

        return ResponseW.success();
    }

    @Override
    public ResponseW delete(Trigger trigger) {
        UpdateWrapper<Trigger> userUpdateWrapper = new UpdateWrapper<>();
        userUpdateWrapper.eq("id", trigger.getId());
        userUpdateWrapper.set(Constant.DEL_FLAG, Constant.DEL_VAL);
        return update(userUpdateWrapper) ? ResponseW.success() : ResponseW.error();
    }

    @Override
    public ResponseW listTriggerStrategy(ListTriggerStrategyReqDTO reqDTO) {
        ListTriggerStrategyResDTO listTriggerStrategyResDTO = new ListTriggerStrategyResDTO();
        TriggerStrategyEnum[] triggerStrategyEnums = TriggerStrategyEnum.values();

        List<TriggerStrategyData> triggerStrategyDataList = new ArrayList<>();

        for (TriggerStrategyEnum triggerStrategyEnum : triggerStrategyEnums) {

            int scope = triggerStrategyEnum.getScope();
            if (scope != -1 && null != reqDTO.getScope() && reqDTO.getScope() != scope) {
                continue;
            }
            TriggerStrategyData triggerStrategyData = new TriggerStrategyData();

            triggerStrategyData.setScope(scope);
            triggerStrategyData.setDesc(triggerStrategyEnum.getDescription());
            triggerStrategyData.setStrategy(triggerStrategyEnum.getValue());
            triggerStrategyData.setStrategyEnum(triggerStrategyEnum);

            triggerStrategyDataList.add(triggerStrategyData);
        }

        listTriggerStrategyResDTO.setTriggerStrategyDataList(triggerStrategyDataList);

        return ResponseW.success(listTriggerStrategyResDTO);
    }
}
