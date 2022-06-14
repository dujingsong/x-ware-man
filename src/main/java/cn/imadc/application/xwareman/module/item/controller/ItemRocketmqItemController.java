package cn.imadc.application.xwareman.module.item.controller;


import cn.imadc.application.base.common.response.ResponseW;
import cn.imadc.application.xwareman.module.item.dto.request.GetRocketmqTopicListReqDTO;
import cn.imadc.application.xwareman.module.item.dto.request.ItemRocketmqItemFindReqDTO;
import cn.imadc.application.xwareman.module.item.entity.ItemRocketmqItem;
import cn.imadc.application.xwareman.module.item.service.IItemRocketmqItemService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * rocketmq相关项表 前端控制器
 * </p>
 *
 * @author 杜劲松
 * @since 2022-06-13
 */
@AllArgsConstructor
@RestController
@RequestMapping("item")
public class ItemRocketmqItemController {

    private final IItemRocketmqItemService itemRocketmqItemService;

    /**
     * 查询
     *
     * @param reqDTO 参数
     * @return 结果
     */
    @RequestMapping(value = "find", method = RequestMethod.POST)
    public ResponseW find(@RequestBody ItemRocketmqItemFindReqDTO reqDTO) {
        return itemRocketmqItemService.find(reqDTO);
    }

    /**
     * 添加
     *
     * @param itemRocketmqItem 参数
     * @return 结果
     */
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public ResponseW add(@RequestBody ItemRocketmqItem itemRocketmqItem) {
        return itemRocketmqItemService.add(itemRocketmqItem);
    }

    /**
     * 修改
     *
     * @param itemRocketmqItem 参数
     * @return 结果
     */
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    public ResponseW edit(@RequestBody ItemRocketmqItem itemRocketmqItem) {
        return itemRocketmqItemService.edit(itemRocketmqItem);
    }

    /**
     * 删除
     *
     * @param itemRocketmqItem 参数
     * @return 结果
     */
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public ResponseW delete(@RequestBody ItemRocketmqItem itemRocketmqItem) {
        return itemRocketmqItemService.delete(itemRocketmqItem);
    }

    /**
     * 获取topic列表
     *
     * @param reqDTO 参数
     * @return 结果
     */
    @RequestMapping(value = "getTopicList", method = RequestMethod.POST)
    public ResponseW getTopicList(@RequestBody GetRocketmqTopicListReqDTO reqDTO) {
        return itemRocketmqItemService.getTopicList(reqDTO);
    }
}
