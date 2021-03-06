package cn.imadc.application.xwareman.module.instance.service.impl;

import cn.imadc.application.base.common.response.ResponseW;
import cn.imadc.application.base.mybatisplus.repository.impl.BaseMPServiceImpl;
import cn.imadc.application.xwareman.core.data.constant.Constant;
import cn.imadc.application.xwareman.module.instance.dto.request.InstanceFindReqDTO;
import cn.imadc.application.xwareman.module.instance.entity.Instance;
import cn.imadc.application.xwareman.module.instance.mapper.InstanceMapper;
import cn.imadc.application.xwareman.module.instance.service.IInstanceService;
import cn.imadc.application.xwareman.module.zone.entity.Zone;
import cn.imadc.application.xwareman.module.zone.service.IZoneService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 实例表 服务实现类
 * </p>
 *
 * @author 杜劲松
 * @since 2022-03-07
 */
@AllArgsConstructor
@Service
public class InstanceServiceImpl extends BaseMPServiceImpl<InstanceMapper, Instance> implements IInstanceService {

    private final IZoneService zoneService;

    @Override
    public ResponseW find(InstanceFindReqDTO reqDTO) {
        QueryWrapper<Instance> queryWrapper = buildQueryWrapper(reqDTO);

        if (!reqDTO.pageQuery()) {
            List<Instance> instances = list(queryWrapper);
            handleDataList(instances);
            return ResponseW.success(instances);
        }

        Page<Instance> page = new Page<>(reqDTO.getPageNo(), reqDTO.getPageSize(), true);
        IPage<Instance> pageData = page(page, queryWrapper);
        handleDataList(pageData.getRecords());
        return ResponseW.success(pageData);
    }

    private QueryWrapper<Instance> buildQueryWrapper(InstanceFindReqDTO reqDTO) {
        QueryWrapper<Instance> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(Constant.DEL_FLAG, Constant.NOT_DEL_VAL);

        // ip
        if (StringUtils.isNotBlank(reqDTO.getIp())) {
            queryWrapper.eq("ip", reqDTO.getIp());
        }

        // zoneId
        if (null != reqDTO.getZoneId()) {
            queryWrapper.eq("zone_id", reqDTO.getZoneId());
        }

        // searchStr
        if (StringUtils.isNotEmpty(reqDTO.getKeywords())) {
            queryWrapper.and(wrapper -> wrapper.like("name", reqDTO.getKeywords())
                    .or().like("ip", reqDTO.getKeywords()));
        }

        return queryWrapper;
    }

    /**
     * 处理数据
     *
     * @param dataList 数据列表
     */
    private void handleDataList(List<Instance> dataList) {
        for (Instance instance : dataList) {
            if (null != instance.getZoneId()) {
                Zone zone = zoneService.getById(instance.getZoneId());
                if (null != zone) instance.setZoneName(zone.getName());
            }
        }
    }

    @Override
    public ResponseW add(Instance instance) {
        return save(instance) ? ResponseW.success() : ResponseW.error();
    }

    @Override
    public ResponseW edit(Instance instance) {
        return updateById(instance) ? ResponseW.success() : ResponseW.error();
    }

    @Override
    public ResponseW delete(Instance instance) {
        UpdateWrapper<Instance> userUpdateWrapper = new UpdateWrapper<>();
        userUpdateWrapper.eq("id", instance.getId());
        userUpdateWrapper.set(Constant.DEL_FLAG, Constant.DEL_VAL);
        return update(userUpdateWrapper) ? ResponseW.success() : ResponseW.error();
    }

    @Override
    public Instance addIfNotExist(Instance instance) {
        InstanceFindReqDTO instanceFindReqDTO = new InstanceFindReqDTO();
        instanceFindReqDTO.setIp(instance.getIp());
        QueryWrapper<Instance> queryWrapper = buildQueryWrapper(instanceFindReqDTO);
        Instance existInstance = getOne(queryWrapper);
        if (null != existInstance) return existInstance;

        add(instance);
        return instance;
    }
}
