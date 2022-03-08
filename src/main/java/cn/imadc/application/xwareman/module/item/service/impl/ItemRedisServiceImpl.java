package cn.imadc.application.xwareman.module.item.service.impl;

import cn.imadc.application.base.common.response.ResponseW;
import cn.imadc.application.base.mybatisplus.repository.impl.BaseMPServiceImpl;
import cn.imadc.application.xwareman.core.data.constant.Constant;
import cn.imadc.application.xwareman.module.item.dto.request.ItemRedisFindReqDTO;
import cn.imadc.application.xwareman.module.item.entity.ItemRedis;
import cn.imadc.application.xwareman.module.item.mapper.ItemRedisMapper;
import cn.imadc.application.xwareman.module.item.service.IItemRedisService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * <p>
 * redis监控项数据搜集表 服务实现类
 * </p>
 *
 * @author 杜劲松
 * @since 2022-03-08
 */
@AllArgsConstructor
@Service
public class ItemRedisServiceImpl extends BaseMPServiceImpl<ItemRedisMapper, ItemRedis> implements IItemRedisService {

    @Override
    public ResponseW find(ItemRedisFindReqDTO reqDTO) {
        QueryWrapper<ItemRedis> queryWrapper = buildQueryWrapper(reqDTO);

        if (!reqDTO.pageQuery()) return ResponseW.success(list(queryWrapper));

        Page<ItemRedis> page = new Page<>(reqDTO.getPageNo(), reqDTO.getPageSize(), true);
        IPage<ItemRedis> pageData = page(page, queryWrapper);
        return ResponseW.success(pageData);
    }

    private QueryWrapper<ItemRedis> buildQueryWrapper(ItemRedisFindReqDTO reqDTO) {
        QueryWrapper<ItemRedis> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(Constant.DEL_FLAG, Constant.NOT_DEL_VAL);

        return queryWrapper;
    }

    @Override
    public ResponseW add(ItemRedis itemRedis) {
        return save(itemRedis) ? ResponseW.success() : ResponseW.error();
    }

    @Override
    public ResponseW edit(ItemRedis itemRedis) {
        return updateById(itemRedis) ? ResponseW.success() : ResponseW.error();
    }

    @Override
    public ResponseW delete(ItemRedis itemRedis) {
        UpdateWrapper<ItemRedis> userUpdateWrapper = new UpdateWrapper<>();
        userUpdateWrapper.eq("id", itemRedis.getId());
        userUpdateWrapper.set(Constant.DEL_FLAG, Constant.DEL_VAL);
        return update(userUpdateWrapper) ? ResponseW.success() : ResponseW.error();
    }
}
