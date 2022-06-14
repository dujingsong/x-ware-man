package cn.imadc.application.xwareman.module.item.service;

import cn.imadc.application.base.common.response.ResponseW;
import cn.imadc.application.base.mybatisplus.repository.IBaseMPService;
import cn.imadc.application.xwareman.module.item.dto.request.GetRocketmqTopicListReqDTO;
import cn.imadc.application.xwareman.module.item.dto.request.ItemRocketmqItemFindReqDTO;
import cn.imadc.application.xwareman.module.item.entity.ItemRocketmqItem;

/**
 * <p>
 * rocketmq相关项表 服务类
 * </p>
 *
 * @author 杜劲松
 * @since 2022-06-13
 */
public interface IItemRocketmqItemService extends IBaseMPService<ItemRocketmqItem> {

    /**
     * 查询
     *
     * @param reqDTO 参数
     * @return 结果
     */
    ResponseW find(ItemRocketmqItemFindReqDTO reqDTO);

    /**
     * 添加
     *
     * @param itemRocketmqItem 参数
     * @return 结果
     */
    ResponseW add(ItemRocketmqItem itemRocketmqItem);

    /**
     * 修改
     *
     * @param itemRocketmqItem 参数
     * @return 结果
     */
    ResponseW edit(ItemRocketmqItem itemRocketmqItem);

    /**
     * 删除
     *
     * @param itemRocketmqItem 参数
     * @return 结果
     */
    ResponseW delete(ItemRocketmqItem itemRocketmqItem);

    /**
     * @param reqDTO
     * @return
     */
    ResponseW getTopicList(GetRocketmqTopicListReqDTO reqDTO);
}
