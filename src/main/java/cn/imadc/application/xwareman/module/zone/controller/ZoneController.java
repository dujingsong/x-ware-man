package cn.imadc.application.xwareman.module.zone.controller;


import cn.imadc.application.base.common.response.ResponseW;
import cn.imadc.application.xwareman.module.zone.dto.request.ZoneFindReqDTO;
import cn.imadc.application.xwareman.module.zone.entity.Zone;
import cn.imadc.application.xwareman.module.zone.service.IZoneService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 杜劲松
 * @since 2022-03-07
 */
@AllArgsConstructor
@RestController
@RequestMapping("zone")
public class ZoneController {

    private final IZoneService zoneService;

    /**
     * 查询
     *
     * @param reqDTO 参数
     * @return 结果
     */
    @RequestMapping(value = "find", method = RequestMethod.POST)
    public ResponseW find(@RequestBody ZoneFindReqDTO reqDTO) {
        return zoneService.find(reqDTO);
    }

    /**
     * 添加
     *
     * @param zone 参数
     * @return 结果
     */
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public ResponseW add(@RequestBody Zone zone) {
        return zoneService.add(zone);
    }

    /**
     * 修改
     *
     * @param zone 参数
     * @return 结果
     */
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    public ResponseW edit(@RequestBody Zone zone) {
        return zoneService.edit(zone);
    }

    /**
     * 删除
     *
     * @param zone 参数
     * @return 结果
     */
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public ResponseW delete(@RequestBody Zone zone) {
        return zoneService.delete(zone);
    }

    /**
     * 查询并转换为树节点
     *
     * @param reqDTO 参数
     * @return 结果
     */
    @RequestMapping(value = "tree", method = RequestMethod.POST)
    public ResponseW tree(@RequestBody ZoneFindReqDTO reqDTO) {
        return zoneService.tree(reqDTO);
    }
}
