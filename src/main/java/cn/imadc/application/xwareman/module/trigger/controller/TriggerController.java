package cn.imadc.application.xwareman.module.trigger.controller;


import cn.imadc.application.base.common.enums.AuthType;
import cn.imadc.application.base.common.response.ResponseW;
import cn.imadc.application.xwareman.core.data.annoations.Api;
import cn.imadc.application.xwareman.module.trigger.dto.request.TriggerFindReqDTO;
import cn.imadc.application.xwareman.module.trigger.entity.Trigger;
import cn.imadc.application.xwareman.module.trigger.service.ITriggerService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 触发器前端控制器
 * </p>
 *
 * @author 杜劲松
 * @since 2022-04-18
 */
@AllArgsConstructor
@RestController
@RequestMapping("trigger")
public class TriggerController {

    private final ITriggerService triggerService;

    /**
     * 查询
     *
     * @param reqDTO 参数
     * @return 结果
     */
    @RequestMapping(value = "find", method = RequestMethod.POST)
    public ResponseW find(@RequestBody TriggerFindReqDTO reqDTO) {
        return triggerService.find(reqDTO);
    }

    /**
     * 添加
     *
     * @param trigger 参数
     * @return 结果
     */
    @Api(authType = AuthType.ANONYMOUS)
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public ResponseW add(@RequestBody Trigger trigger) {
        return triggerService.add(trigger);
    }

    /**
     * 修改
     *
     * @param trigger 参数
     * @return 结果
     */
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    public ResponseW edit(@RequestBody Trigger trigger) {
        return triggerService.edit(trigger);
    }

    /**
     * 删除
     *
     * @param trigger 参数
     * @return 结果
     */
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public ResponseW delete(@RequestBody Trigger trigger) {
        return triggerService.delete(trigger);
    }
}
