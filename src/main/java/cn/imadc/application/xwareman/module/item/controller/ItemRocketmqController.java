package cn.imadc.application.xwareman.module.item.controller;


import cn.imadc.application.xwareman.module.item.dto.request.ItemRedisFindReqDTO;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import lombok.AllArgsConstructor;
import cn.imadc.application.xwareman.module.item.service.IItemRocketmqService;
import cn.imadc.application.base.common.response.ResponseW;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import cn.imadc.application.xwareman.module.item.dto.request.ItemRocketmqFindReqDTO;
import cn.imadc.application.xwareman.module.item.entity.ItemRocketmq;

/**
 * <p>
 * redis监控项数据搜集表 前端控制器
 * </p>
 *
 * @author 杜劲松
 * @since 2022-06-10
 */
@AllArgsConstructor
@RestController
@RequestMapping("item/rocketmq")
public class ItemRocketmqController {

    private final IItemRocketmqService itemRocketmqService;

    /**
     * 查询
     *
     * @param reqDTO 参数
     * @return 结果
     */
    @RequestMapping(value = "find", method = RequestMethod.POST)
    public ResponseW find(@RequestBody ItemRocketmqFindReqDTO reqDTO) {
        return itemRocketmqService.find(reqDTO);
    }

    /**
     * 添加
     *
     * @param itemRocketmq 参数
     * @return 结果
     */
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public ResponseW add(@RequestBody ItemRocketmq itemRocketmq) {
        return itemRocketmqService.add(itemRocketmq);
    }

    /**
     * 修改
     *
     * @param itemRocketmq 参数
     * @return 结果
     */
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    public ResponseW edit(@RequestBody ItemRocketmq itemRocketmq) {
        return itemRocketmqService.edit(itemRocketmq);
    }

    /**
     * 删除
     *
     * @param itemRocketmq 参数
     * @return 结果
     */
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public ResponseW delete(@RequestBody ItemRocketmq itemRocketmq) {
        return itemRocketmqService.delete(itemRocketmq);
    }

    /**
     * 查询监控收集项列定义
     *
     * @param reqDTO 参数
     * @return 结果
     */
    @RequestMapping(value = "loadColumDefinition", method = RequestMethod.POST)
    public ResponseW loadColumDefinition(@RequestBody ItemRedisFindReqDTO reqDTO) {
        return itemRocketmqService.loadColumDefinition(reqDTO);
    }
}
