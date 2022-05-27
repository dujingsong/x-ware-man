package cn.imadc.application.xwareman.module.event.service;

import cn.imadc.application.xwareman.module.event.entity.ItemEvent;
import cn.imadc.application.base.mybatisplus.repository.IBaseMPService;
import cn.imadc.application.base.common.response.ResponseW;
import cn.imadc.application.xwareman.module.event.dto.request.ItemEventFindReqDTO;

/**
 * <p>
 * 告警记录 服务类
 * </p>
 *
 * @author 杜劲松
 * @since 2022-05-25
 */
public interface IItemEventService extends IBaseMPService<ItemEvent> {

    /**
     * 查询
     *
     * @param reqDTO 参数
     * @return 结果
     */
    ResponseW find(ItemEventFindReqDTO reqDTO);

    /**
     * 添加
     *
     * @param itemEvent 参数
     * @return 结果
     */
    ResponseW add(ItemEvent itemEvent);

    /**
     * 修改
     *
     * @param itemEvent 参数
     * @return 结果
     */
    ResponseW edit(ItemEvent itemEvent);

    /**
     * 删除
     *
     * @param itemEvent 参数
     * @return 结果
     */
    ResponseW delete(ItemEvent itemEvent);
}
