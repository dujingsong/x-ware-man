package cn.imadc.application.xwareman.module.event.controller;


import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import lombok.AllArgsConstructor;
import cn.imadc.application.xwareman.module.event.service.IItemEventService;
import cn.imadc.application.base.common.response.ResponseW;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import cn.imadc.application.xwareman.module.event.dto.request.ItemEventFindReqDTO;
import cn.imadc.application.xwareman.module.event.entity.ItemEvent;

/**
 * <p>
 * 告警记录 前端控制器
 * </p>
 *
 * @author 杜劲松
 * @since 2022-05-25
 */
@AllArgsConstructor
@RestController
@RequestMapping("event")
public class ItemEventController {

    private final IItemEventService itemEventService;

    /**
     * 查询
     *
     * @param reqDTO 参数
     * @return 结果
     */
    @RequestMapping(value = "find", method = RequestMethod.POST)
    public ResponseW find(@RequestBody ItemEventFindReqDTO reqDTO) {
        return itemEventService.find(reqDTO);
    }

    /**
     * 添加
     *
     * @param itemEvent 参数
     * @return 结果
     */
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public ResponseW add(@RequestBody ItemEvent itemEvent) {
        return itemEventService.add(itemEvent);
    }

    /**
     * 修改
     *
     * @param itemEvent 参数
     * @return 结果
     */
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    public ResponseW edit(@RequestBody ItemEvent itemEvent) {
        return itemEventService.edit(itemEvent);
    }

    /**
     * 删除
     *
     * @param itemEvent 参数
     * @return 结果
     */
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public ResponseW delete(@RequestBody ItemEvent itemEvent) {
        return itemEventService.delete(itemEvent);
    }
}
