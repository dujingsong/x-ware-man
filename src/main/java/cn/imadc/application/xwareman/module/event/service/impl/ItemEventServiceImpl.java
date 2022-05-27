package cn.imadc.application.xwareman.module.event.service.impl;

import cn.imadc.application.base.common.response.ResponseW;
import cn.imadc.application.base.mybatisplus.repository.impl.BaseMPServiceImpl;
import cn.imadc.application.xwareman.core.data.constant.Constant;
import cn.imadc.application.xwareman.module.event.dto.request.ItemEventFindReqDTO;
import cn.imadc.application.xwareman.module.event.entity.ItemEvent;
import cn.imadc.application.xwareman.module.event.mapper.ItemEventMapper;
import cn.imadc.application.xwareman.module.event.service.IItemEventService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

/**
 * <p>
 * 告警记录 服务实现类
 * </p>
 *
 * @author 杜劲松
 * @since 2022-05-25
 */
@AllArgsConstructor
@Service
public class ItemEventServiceImpl extends BaseMPServiceImpl<ItemEventMapper, ItemEvent> implements IItemEventService {

    @Override
    public ResponseW find(ItemEventFindReqDTO reqDTO) {
        QueryWrapper<ItemEvent> queryWrapper = buildQueryWrapper(reqDTO);

        if (!reqDTO.pageQuery()) return ResponseW.success(list(queryWrapper));

        Page<ItemEvent> page = new Page<>(reqDTO.getPageNo(), reqDTO.getPageSize(), true);
        IPage<ItemEvent> pageData = page(page, queryWrapper);
        return ResponseW.success(pageData);
    }

    private QueryWrapper<ItemEvent> buildQueryWrapper(ItemEventFindReqDTO reqDTO) {
        QueryWrapper<ItemEvent> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(Constant.DEL_FLAG, Constant.NOT_DEL_VAL);

        if (null != reqDTO.getStartDateTime()) {
            queryWrapper.ge("create_time", reqDTO.getStartDateTime().format(DateTimeFormatter.ofPattern(Constant.YYYY_MM_DD_HH_MM_SS)));
        }
        if (null != reqDTO.getEndDateTime()) {
            queryWrapper.le("create_time", reqDTO.getEndDateTime().format(DateTimeFormatter.ofPattern(Constant.YYYY_MM_DD_HH_MM_SS)));
        }

        if (null != reqDTO.getInstanceItemId()) {
            queryWrapper.eq("instance_item_id", reqDTO.getInstanceItemId());
        }

        queryWrapper.orderByDesc("create_time");

        return queryWrapper;
    }

    @Override
    public ResponseW add(ItemEvent itemEvent) {
        return save(itemEvent) ? ResponseW.success() : ResponseW.error();
    }

    @Override
    public ResponseW edit(ItemEvent itemEvent) {
        return updateById(itemEvent) ? ResponseW.success() : ResponseW.error();
    }

    @Override
    public ResponseW delete(ItemEvent itemEvent) {
        UpdateWrapper<ItemEvent> userUpdateWrapper = new UpdateWrapper<>();
        userUpdateWrapper.eq("id", itemEvent.getId());
        userUpdateWrapper.set(Constant.DEL_FLAG, Constant.DEL_VAL);
        return update(userUpdateWrapper) ? ResponseW.success() : ResponseW.error();
    }
}
