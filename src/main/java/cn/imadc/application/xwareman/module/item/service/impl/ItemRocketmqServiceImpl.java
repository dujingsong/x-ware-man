package cn.imadc.application.xwareman.module.item.service.impl;

import cn.imadc.application.base.common.response.ResponseW;
import cn.imadc.application.base.mybatisplus.repository.impl.BaseMPServiceImpl;
import cn.imadc.application.xwareman.core.data.constant.Constant;
import cn.imadc.application.xwareman.module.item.dto.request.ItemRedisFindReqDTO;
import cn.imadc.application.xwareman.module.item.dto.request.ItemRocketmqFindReqDTO;
import cn.imadc.application.xwareman.module.item.entity.ItemRocketmq;
import cn.imadc.application.xwareman.module.item.mapper.ItemRocketmqMapper;
import cn.imadc.application.xwareman.module.item.service.IItemRocketmqService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * redis监控项数据搜集表 服务实现类
 * </p>
 *
 * @author 杜劲松
 * @since 2022-06-10
 */
@AllArgsConstructor
@Service
public class ItemRocketmqServiceImpl extends BaseMPServiceImpl<ItemRocketmqMapper, ItemRocketmq> implements IItemRocketmqService {

    private final ItemRocketmqMapper itemRocketmqMapper;

    @Override
    public ResponseW find(ItemRocketmqFindReqDTO reqDTO) {
        QueryWrapper<ItemRocketmq> queryWrapper = buildQueryWrapper(reqDTO);

        if (!reqDTO.pageQuery()) return ResponseW.success(list(queryWrapper));

        Page<ItemRocketmq> page = new Page<>(reqDTO.getPageNo(), reqDTO.getPageSize(), true);
        IPage<ItemRocketmq> pageData = page(page, queryWrapper);
        return ResponseW.success(pageData);
    }

    private QueryWrapper<ItemRocketmq> buildQueryWrapper(ItemRocketmqFindReqDTO reqDTO) {
        QueryWrapper<ItemRocketmq> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(Constant.DEL_FLAG, Constant.NOT_DEL_VAL);

        if (null != reqDTO.getStartDateTime()) {
            queryWrapper.ge("create_time", reqDTO.getStartDateTime().format(DateTimeFormatter.ofPattern(Constant.YYYY_MM_DD_HH_MM_SS)));
        }
        if (null != reqDTO.getEndDateTime()) {
            queryWrapper.le("create_time", reqDTO.getEndDateTime().format(DateTimeFormatter.ofPattern(Constant.YYYY_MM_DD_HH_MM_SS)));
        }

        // 实例rocketmqID
        if (null != reqDTO.getInstanceRocketmqId()) {
            queryWrapper.eq("instance_rocketmq_id", reqDTO.getInstanceRocketmqId());
        }

        queryWrapper.orderByDesc("create_time");
        return queryWrapper;
    }

    @Override
    public ResponseW add(ItemRocketmq itemRocketmq) {
        return save(itemRocketmq) ? ResponseW.success() : ResponseW.error();
    }

    @Override
    public ResponseW edit(ItemRocketmq itemRocketmq) {
        return updateById(itemRocketmq) ? ResponseW.success() : ResponseW.error();
    }

    @Override
    public ResponseW delete(ItemRocketmq itemRocketmq) {
        UpdateWrapper<ItemRocketmq> userUpdateWrapper = new UpdateWrapper<>();
        userUpdateWrapper.eq("id", itemRocketmq.getId());
        userUpdateWrapper.set(Constant.DEL_FLAG, Constant.DEL_VAL);
        return update(userUpdateWrapper) ? ResponseW.success() : ResponseW.error();
    }

    @Override
    public ResponseW loadColumDefinition(ItemRedisFindReqDTO reqDTO) {
        Field[] fields = ItemRocketmq.class.getDeclaredFields();
        List<String> fieldList = new ArrayList<>();
        fieldList.add("createTime");
        for (Field field : fields) {
            if (field.getName().equals("id")) continue;
            if (field.getName().equals("serialVersionUID")) continue;
            if (field.getName().equals("notes")) continue;
            if (field.getName().equals("instanceId")) continue;
            if (field.getName().equals("instanceRocketmqId")) continue;
            fieldList.add(field.getName());
        }
        return ResponseW.success(fieldList);
    }

    @Override
    public List<Object> selectColAtSpecifiedTime(String col, LocalDateTime begin, LocalDateTime end, Long instanceRocketmqId) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return itemRocketmqMapper.selectColAtSpecifiedTime(col, begin.format(dateTimeFormatter), end.format(dateTimeFormatter), instanceRocketmqId);
    }
}
