package cn.imadc.application.xwareman.module.item.service.impl;

import cn.imadc.application.base.common.response.ResponseW;
import cn.imadc.application.base.mybatisplus.repository.impl.BaseMPServiceImpl;
import cn.imadc.application.xwareman.core.data.constant.Constant;
import cn.imadc.application.xwareman.module.item.dto.request.GetRocketmqTopicListReqDTO;
import cn.imadc.application.xwareman.module.item.dto.request.ItemRocketmqItemFindReqDTO;
import cn.imadc.application.xwareman.module.item.entity.ItemRocketmqItem;
import cn.imadc.application.xwareman.module.item.mapper.ItemRocketmqItemMapper;
import cn.imadc.application.xwareman.module.item.service.IItemRocketmqItemService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * <p>
 * rocketmq相关项表 服务实现类
 * </p>
 *
 * @author 杜劲松
 * @since 2022-06-13
 */
@AllArgsConstructor
@Service
public class ItemRocketmqItemServiceImpl extends BaseMPServiceImpl<ItemRocketmqItemMapper, ItemRocketmqItem> implements IItemRocketmqItemService {

    private final ItemRocketmqItemMapper itemRocketmqItemMapper;

    @Override
    public ResponseW find(ItemRocketmqItemFindReqDTO reqDTO) {
        QueryWrapper<ItemRocketmqItem> queryWrapper = buildQueryWrapper(reqDTO);

        if (!reqDTO.pageQuery()) return ResponseW.success(list(queryWrapper));

        Page<ItemRocketmqItem> page = new Page<>(reqDTO.getPageNo(), reqDTO.getPageSize(), true);
        IPage<ItemRocketmqItem> pageData = page(page, queryWrapper);
        return ResponseW.success(pageData);
    }

    private QueryWrapper<ItemRocketmqItem> buildQueryWrapper(ItemRocketmqItemFindReqDTO reqDTO) {
        QueryWrapper<ItemRocketmqItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(Constant.DEL_FLAG, Constant.NOT_DEL_VAL);

        return queryWrapper;
    }

    @Override
    public ResponseW add(ItemRocketmqItem itemRocketmqItem) {
        return save(itemRocketmqItem) ? ResponseW.success() : ResponseW.error();
    }

    @Override
    public ResponseW edit(ItemRocketmqItem itemRocketmqItem) {
        return updateById(itemRocketmqItem) ? ResponseW.success() : ResponseW.error();
    }

    @Override
    public ResponseW delete(ItemRocketmqItem itemRocketmqItem) {
        UpdateWrapper<ItemRocketmqItem> userUpdateWrapper = new UpdateWrapper<>();
        userUpdateWrapper.eq("id", itemRocketmqItem.getId());
        userUpdateWrapper.set(Constant.DEL_FLAG, Constant.DEL_VAL);
        return update(userUpdateWrapper) ? ResponseW.success() : ResponseW.error();
    }

    @Override
    public ResponseW getTopicList(GetRocketmqTopicListReqDTO reqDTO) {
        return null;
    }
}
