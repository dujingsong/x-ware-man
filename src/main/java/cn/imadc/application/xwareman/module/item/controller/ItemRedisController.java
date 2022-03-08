package cn.imadc.application.xwareman.module.item.controller;


import cn.imadc.application.base.common.response.ResponseW;
import cn.imadc.application.xwareman.module.item.dto.request.ItemRedisFindReqDTO;
import cn.imadc.application.xwareman.module.item.entity.ItemRedis;
import cn.imadc.application.xwareman.module.item.service.IItemRedisService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * redis监控项数据搜集表 前端控制器
 * </p>
 *
 * @author 杜劲松
 * @since 2022-03-08
 */
@AllArgsConstructor
@RestController
@RequestMapping("item")
public class ItemRedisController {

    private final IItemRedisService itemRedisService;

    /**
     * 查询
     *
     * @param reqDTO 参数
     * @return 结果
     */
    @RequestMapping(value = "find", method = RequestMethod.POST)
    public ResponseW find(@RequestBody ItemRedisFindReqDTO reqDTO) {
        return itemRedisService.find(reqDTO);
    }

    /**
     * 添加
     *
     * @param itemRedis 参数
     * @return 结果
     */
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public ResponseW add(@RequestBody ItemRedis itemRedis) {
        return itemRedisService.add(itemRedis);
    }

    /**
     * 修改
     *
     * @param itemRedis 参数
     * @return 结果
     */
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    public ResponseW edit(@RequestBody ItemRedis itemRedis) {
        return itemRedisService.edit(itemRedis);
    }

    /**
     * 删除
     *
     * @param itemRedis 参数
     * @return 结果
     */
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public ResponseW delete(@RequestBody ItemRedis itemRedis) {
        return itemRedisService.delete(itemRedis);
    }
}
