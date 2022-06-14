package cn.imadc.application.xwareman.module.item.service;

import cn.imadc.application.base.common.response.ResponseW;
import cn.imadc.application.base.mybatisplus.repository.IBaseMPService;
import cn.imadc.application.xwareman.module.item.dto.request.ItemRedisFindReqDTO;
import cn.imadc.application.xwareman.module.item.dto.request.ItemRocketmqFindReqDTO;
import cn.imadc.application.xwareman.module.item.entity.ItemRocketmq;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * redis监控项数据搜集表 服务类
 * </p>
 *
 * @author 杜劲松
 * @since 2022-06-10
 */
public interface IItemRocketmqService extends IBaseMPService<ItemRocketmq> {

    /**
     * 查询
     *
     * @param reqDTO 参数
     * @return 结果
     */
    ResponseW find(ItemRocketmqFindReqDTO reqDTO);

    /**
     * 添加
     *
     * @param itemRocketmq 参数
     * @return 结果
     */
    ResponseW add(ItemRocketmq itemRocketmq);

    /**
     * 修改
     *
     * @param itemRocketmq 参数
     * @return 结果
     */
    ResponseW edit(ItemRocketmq itemRocketmq);

    /**
     * 删除
     *
     * @param itemRocketmq 参数
     * @return 结果
     */
    ResponseW delete(ItemRocketmq itemRocketmq);

    /**
     * 查询监控收集项列定义
     *
     * @param reqDTO
     * @return
     */
    ResponseW loadColumDefinition(ItemRedisFindReqDTO reqDTO);

    /**
     * 查询指定时间范围内指定字段的数据
     *
     * @param col                字段名称
     * @param begin              开始时间
     * @param end                结束时间
     * @param instanceRocketmqId rocketmq实例ID
     * @return 符合条件的字段数据列表
     */
    List<Object> selectColAtSpecifiedTime(String col, LocalDateTime begin, LocalDateTime end, Long instanceRocketmqId);
}
