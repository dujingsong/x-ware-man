package cn.imadc.application.xwareman.basic.auth.controller;

import cn.imadc.application.base.common.context.ReqCtxConstant;
import cn.imadc.application.base.common.context.RequestContext;
import cn.imadc.application.base.common.enums.AuthType;
import cn.imadc.application.base.common.response.ResponseW;
import cn.imadc.application.xwareman.basic.auth.dto.request.UserLoginReqDTO;
import cn.imadc.application.xwareman.basic.auth.service.ICredentialService;
import cn.imadc.application.xwareman.core.data.annoations.Api;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 认证、权限
 * </p>
 *
 * @author 杜劲松
 * @since 2021-12-17
 */
@AllArgsConstructor
@RequestMapping("credential")
@RestController
public class CredentialController {

    private final ICredentialService credentialService;

    /**
     * 登出
     *
     * @return 结果
     */
    @Api(authType = AuthType.ANONYMOUS)
    @RequestMapping(value = "logout", method = RequestMethod.POST)
    public ResponseW logout() {
        return ResponseW.success();
    }

    /**
     * 登录
     *
     * @param reqDTO 参数
     * @return 结果
     */
    @Api(authType = AuthType.ANONYMOUS)
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public ResponseW login(@RequestBody UserLoginReqDTO reqDTO) {
        return credentialService.login(reqDTO);
    }

    /**
     * 当前登录的用户信息
     *
     * @return 结果
     */
    @RequestMapping(value = "info", method = RequestMethod.GET)
    public ResponseW info() {
        RequestContext requestContext = RequestContext.getCurrentContext();
        Long id = requestContext.get(ReqCtxConstant.ID, Long.class);
        return credentialService.info(id);
    }

    /**
     * 当前登录的用户菜单
     *
     * @return 结果
     */
    @RequestMapping(value = "nav", method = RequestMethod.GET)
    public ResponseW nav() {
        RequestContext requestContext = RequestContext.getCurrentContext();
        Long id = requestContext.get(ReqCtxConstant.ID, Long.class);
        return credentialService.nav(id);
    }
}
