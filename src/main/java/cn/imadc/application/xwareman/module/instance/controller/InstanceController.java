package cn.imadc.application.xwareman.module.instance.controller;


import cn.imadc.application.base.common.response.ResponseW;
import cn.imadc.application.xwareman.module.instance.dto.request.InstanceFindReqDTO;
import cn.imadc.application.xwareman.module.instance.entity.Instance;
import cn.imadc.application.xwareman.module.instance.service.IInstanceService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 实例表 前端控制器
 * </p>
 *
 * @author 杜劲松
 * @since 2022-03-07
 */
@AllArgsConstructor
@RestController
@RequestMapping("instance")
public class InstanceController {

    private final IInstanceService instanceService;

    /**
     * 查询
     *
     * @param reqDTO 参数
     * @return 结果
     */
    @RequestMapping(value = "find", method = RequestMethod.POST)
    public ResponseW find(@RequestBody InstanceFindReqDTO reqDTO) {
        return instanceService.find(reqDTO);
    }

    /**
     * 添加
     *
     * @param instance 参数
     * @return 结果
     */
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public ResponseW add(@RequestBody Instance instance) {
        return instanceService.add(instance);
    }

    /**
     * 修改
     *
     * @param instance 参数
     * @return 结果
     */
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    public ResponseW edit(@RequestBody Instance instance) {
        return instanceService.edit(instance);
    }

    /**
     * 删除
     *
     * @param instance 参数
     * @return 结果
     */
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public ResponseW delete(@RequestBody Instance instance) {
        return instanceService.delete(instance);
    }

}
