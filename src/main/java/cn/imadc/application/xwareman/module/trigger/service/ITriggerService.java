package cn.imadc.application.xwareman.module.trigger.service;

import cn.imadc.application.base.common.response.ResponseW;
import cn.imadc.application.base.mybatisplus.repository.IBaseMPService;
import cn.imadc.application.xwareman.module.trigger.dto.request.ListTriggerStrategyReqDTO;
import cn.imadc.application.xwareman.module.trigger.dto.request.TriggerFindReqDTO;
import cn.imadc.application.xwareman.module.trigger.entity.Trigger;

import java.util.List;

/**
 * <p>
 * 触发器服务类
 * </p>
 *
 * @author 杜劲松
 * @since 2022-04-18
 */
public interface ITriggerService extends IBaseMPService<Trigger> {

    /**
     * 查询
     *
     * @param reqDTO 参数
     * @return 结果
     */
    ResponseW find(TriggerFindReqDTO reqDTO);

    /**
     * 列表查询
     *
     * @param triggerFindReqDTO 参数
     * @return 查询结果
     */
    List<Trigger> listTrigger(TriggerFindReqDTO triggerFindReqDTO);

    /**
     * 添加
     *
     * @param trigger 参数
     * @return 结果
     */
    ResponseW add(Trigger trigger);

    /**
     * 修改
     *
     * @param trigger 参数
     * @return 结果
     */
    ResponseW edit(Trigger trigger);

    /**
     * 删除
     *
     * @param trigger 参数
     * @return 结果
     */
    ResponseW delete(Trigger trigger);

    /**
     * 查询触发器规则
     *
     * @param reqDTO 参数
     * @return 结果
     */
    ResponseW listTriggerStrategy(ListTriggerStrategyReqDTO reqDTO);
}
