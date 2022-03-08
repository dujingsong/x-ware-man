package cn.imadc.application.xwareman.module.item.service;

import cn.imadc.application.base.common.response.ResponseW;
import cn.imadc.application.base.mybatisplus.repository.IBaseMPService;
import cn.imadc.application.xwareman.module.item.dto.request.ItemRedisFindReqDTO;
import cn.imadc.application.xwareman.module.item.entity.ItemRedis;

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
}
