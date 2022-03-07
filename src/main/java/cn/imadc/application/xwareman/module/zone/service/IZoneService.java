package cn.imadc.application.xwareman.module.zone.service;

import cn.imadc.application.base.common.response.ResponseW;
import cn.imadc.application.base.mybatisplus.repository.IBaseMPService;
import cn.imadc.application.xwareman.module.zone.dto.request.ZoneFindReqDTO;
import cn.imadc.application.xwareman.module.zone.entity.Zone;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 杜劲松
 * @since 2022-03-07
 */
public interface IZoneService extends IBaseMPService<Zone> {

    /**
     * 查询
     *
     * @param reqDTO 参数
     * @return 结果
     */
    ResponseW find(ZoneFindReqDTO reqDTO);

    /**
     * 添加
     *
     * @param zone 参数
     * @return 结果
     */
    ResponseW add(Zone zone);

    /**
     * 修改
     *
     * @param zone 参数
     * @return 结果
     */
    ResponseW edit(Zone zone);

    /**
     * 删除
     *
     * @param zone 参数
     * @return 结果
     */
    ResponseW delete(Zone zone);
}
