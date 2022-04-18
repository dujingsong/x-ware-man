package cn.imadc.application.xwareman.module.trigger.service.impl;

import cn.imadc.application.base.common.response.ResponseW;
import cn.imadc.application.base.mybatisplus.repository.impl.BaseMPServiceImpl;
import cn.imadc.application.xwareman.core.data.constant.Constant;
import cn.imadc.application.xwareman.module.trigger.dto.request.TriggerFindReqDTO;
import cn.imadc.application.xwareman.module.trigger.entity.Trigger;
import cn.imadc.application.xwareman.module.trigger.mapper.TriggerMapper;
import cn.imadc.application.xwareman.module.trigger.service.ITriggerService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 触发器服务实现类
 * </p>
 *
 * @author 杜劲松
 * @since 2022-04-18
 */
@AllArgsConstructor
@Service
public class TriggerServiceImpl extends BaseMPServiceImpl<TriggerMapper, Trigger> implements ITriggerService {

    @Override
    public ResponseW find(TriggerFindReqDTO reqDTO) {
        QueryWrapper<Trigger> queryWrapper = buildQueryWrapper(reqDTO);

        if (!reqDTO.pageQuery()) return ResponseW.success(list(queryWrapper));

        Page<Trigger> page = new Page<>(reqDTO.getPageNo(), reqDTO.getPageSize(), true);
        IPage<Trigger> pageData = page(page, queryWrapper);
        return ResponseW.success(pageData);
    }

    private QueryWrapper<Trigger> buildQueryWrapper(TriggerFindReqDTO reqDTO) {
        QueryWrapper<Trigger> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(Constant.DEL_FLAG, Constant.NOT_DEL_VAL);

        return queryWrapper;
    }

    @Override
    public List<Trigger> listTrigger(TriggerFindReqDTO triggerFindReqDTO) {
        QueryWrapper<Trigger> queryWrapper = buildQueryWrapper(triggerFindReqDTO);
        return list(queryWrapper);
    }

    @Override
    public ResponseW add(Trigger trigger) {
        return save(trigger) ? ResponseW.success() : ResponseW.error();
    }

    @Override
    public ResponseW edit(Trigger trigger) {
        return updateById(trigger) ? ResponseW.success() : ResponseW.error();
    }

    @Override
    public ResponseW delete(Trigger trigger) {
        UpdateWrapper<Trigger> userUpdateWrapper = new UpdateWrapper<>();
        userUpdateWrapper.eq("id", trigger.getId());
        userUpdateWrapper.set(Constant.DEL_FLAG, Constant.DEL_VAL);
        return update(userUpdateWrapper) ? ResponseW.success() : ResponseW.error();
    }
}
