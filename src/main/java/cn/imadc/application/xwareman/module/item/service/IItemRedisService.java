package cn.imadc.application.xwareman.module.item.service;

import cn.imadc.application.base.common.response.ResponseW;
import cn.imadc.application.base.data.structure.redis.RedisInfo;
import cn.imadc.application.base.mybatisplus.repository.IBaseMPService;
import cn.imadc.application.xwareman.module.instance.dto.data.InstanceRedisData;
import cn.imadc.application.xwareman.module.item.dto.request.ItemRedisFindReqDTO;
import cn.imadc.application.xwareman.module.item.entity.ItemRedis;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * redis监控项数据搜集表 服务类
 * </p>
 *
 * @author 杜劲松
 * @since 2022-03-08
 */
public interface IItemRedisService extends IBaseMPService<ItemRedis> {

    /**
     * 查询
     *
     * @param reqDTO 参数
     * @return 结果
     */
    ResponseW find(ItemRedisFindReqDTO reqDTO);

    /**
     * 添加
     *
     * @param itemRedis 参数
     * @return 结果
     */
    ResponseW add(ItemRedis itemRedis);

    /**
     * 修改
     *
     * @param itemRedis 参数
     * @return 结果
     */
    ResponseW edit(ItemRedis itemRedis);

    /**
     * 删除
     *
     * @param itemRedis 参数
     * @return 结果
     */
    ResponseW delete(ItemRedis itemRedis);

    /**
     * redis监控项收集
     *
     * @param instanceRedisData redis的节点信息
     * @param redisInfo         redis的info信息
     */
    void storeItemRedis(InstanceRedisData instanceRedisData, RedisInfo redisInfo);

    /**
     * 查询指定时间范围内指定字段的数据
     *
     * @param col   字段名称
     * @param begin 开始时间
     * @param end   结束时间
     * @return 符合调价的字段数据列表
     */
    List<Object> selectColAtSpecifiedTime(String col, LocalDateTime begin, LocalDateTime end);

    /**
     * 查询监控收集项列定义
     *
     * @param reqDTO 参数
     * @return 结果
     */
    ResponseW loadColumDefinition(ItemRedisFindReqDTO reqDTO);
}
