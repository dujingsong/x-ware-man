package cn.imadc.application.xwareman.module.item.service.impl;

import cn.imadc.application.base.common.response.ResponseW;
import cn.imadc.application.base.data.structure.redis.RedisInfo;
import cn.imadc.application.base.data.structure.redis.RedisNode;
import cn.imadc.application.base.mybatisplus.repository.impl.BaseMPServiceImpl;
import cn.imadc.application.xwareman.core.data.constant.Constant;
import cn.imadc.application.xwareman.core.data.constant.Word;
import cn.imadc.application.xwareman.module.instance.dto.data.InstanceRedisData;
import cn.imadc.application.xwareman.module.item.dto.request.ItemRedisFindReqDTO;
import cn.imadc.application.xwareman.module.item.entity.ItemRedis;
import cn.imadc.application.xwareman.module.item.mapper.ItemRedisMapper;
import cn.imadc.application.xwareman.module.item.service.IItemRedisService;
import cn.imadc.application.xwareman.module.item.util.ItemRedisUtil;
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
 * @since 2022-03-08
 */
@AllArgsConstructor
@Service
public class ItemRedisServiceImpl extends BaseMPServiceImpl<ItemRedisMapper, ItemRedis> implements IItemRedisService {

    private final ItemRedisMapper itemRedisMapper;

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

        // 实例redisID
        if (null != reqDTO.getInstanceRedisId()) {
            queryWrapper.eq("instance_redis_id", reqDTO.getInstanceRedisId());
        }

        queryWrapper.orderByDesc("create_time");
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

    @Override
    public void storeItemRedis(InstanceRedisData instanceRedisData, RedisInfo redisInfo) {
        try {

            // 节点类型
            RedisNode redisNode = RedisNode.values()[instanceRedisData.getType()];

            // 取info信息中关心的参数构建监控项
            ItemRedis itemRedis = ItemRedisUtil.createItemRedis(redisNode, redisInfo);
            itemRedis.setInstanceId(instanceRedisData.getInstanceId());
            itemRedis.setInstanceRedisId(instanceRedisData.getId());

            add(itemRedis);

        } catch (Exception exception) {
            log.error(Word.BUILD_ITEM_REDIS_EXCEPTION, exception);
        }
    }

    @Override
    public List<Object> selectColAtSpecifiedTime(String col, LocalDateTime begin, LocalDateTime end) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return itemRedisMapper.selectColAtSpecifiedTime(col, begin.format(dateTimeFormatter), end.format(dateTimeFormatter));
    }

    @Override
    public ResponseW loadColumDefinition(ItemRedisFindReqDTO reqDTO) {
        Field[] fields = ItemRedis.class.getDeclaredFields();
        List<String> fieldList = new ArrayList<>();
        fieldList.add("createTime");
        for (Field field : fields) {
            if (field.getName().equals("id")) continue;
            if (field.getName().equals("serialVersionUID")) continue;
            if (field.getName().equals("notes")) continue;
            if (field.getName().equals("instanceId")) continue;
            if (field.getName().equals("instanceRedisId")) continue;
            fieldList.add(field.getName());
        }
        return ResponseW.success(fieldList);
    }
}
