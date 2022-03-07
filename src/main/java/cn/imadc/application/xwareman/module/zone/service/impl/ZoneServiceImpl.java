package cn.imadc.application.xwareman.module.zone.service.impl;

import cn.imadc.application.base.common.response.ResponseW;
import cn.imadc.application.base.mybatisplus.repository.impl.BaseMPServiceImpl;
import cn.imadc.application.xwareman.core.data.constant.Constant;
import cn.imadc.application.xwareman.module.zone.dto.request.ZoneFindReqDTO;
import cn.imadc.application.xwareman.module.zone.entity.Zone;
import cn.imadc.application.xwareman.module.zone.mapper.ZoneMapper;
import cn.imadc.application.xwareman.module.zone.service.IZoneService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 杜劲松
 * @since 2022-03-07
 */
@AllArgsConstructor
@Service
public class ZoneServiceImpl extends BaseMPServiceImpl<ZoneMapper, Zone> implements IZoneService {

    @Override
    public ResponseW find(ZoneFindReqDTO reqDTO) {
        QueryWrapper<Zone> queryWrapper = buildQueryWrapper(reqDTO);

        if (!reqDTO.pageQuery()) return ResponseW.success(list(queryWrapper));

        Page<Zone> page = new Page<>(reqDTO.getPageNo(), reqDTO.getPageSize(), true);
        IPage<Zone> pageData = page(page, queryWrapper);
        return ResponseW.success(pageData);
    }

    private QueryWrapper<Zone> buildQueryWrapper(ZoneFindReqDTO reqDTO) {
        QueryWrapper<Zone> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(Constant.DEL_FLAG, Constant.NOT_DEL_VAL);

        return queryWrapper;
    }

    @Override
    public ResponseW add(Zone zone) {
        return save(zone) ? ResponseW.success() : ResponseW.error();
    }

    @Override
    public ResponseW edit(Zone zone) {
        return updateById(zone) ? ResponseW.success() : ResponseW.error();
    }

    @Override
    public ResponseW delete(Zone zone) {
        UpdateWrapper<Zone> userUpdateWrapper = new UpdateWrapper<>();
        userUpdateWrapper.eq("id", zone.getId());
        userUpdateWrapper.set(Constant.DEL_FLAG, Constant.DEL_VAL);
        return update(userUpdateWrapper) ? ResponseW.success() : ResponseW.error();
    }
}
