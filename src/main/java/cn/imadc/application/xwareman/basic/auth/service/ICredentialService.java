package cn.imadc.application.xwareman.basic.auth.service;

import cn.imadc.application.base.common.response.ResponseW;
import cn.imadc.application.xwareman.basic.auth.dto.request.UserLoginReqDTO;

/**
 * <p>
 * 认证、权限
 * </p>
 *
 * @author 杜劲松
 * @since 2021-12-17
 */
public interface ICredentialService {
    /**
     * 登录
     *
     * @param reqDTO 参数
     * @return 结果
     */
    ResponseW login(UserLoginReqDTO reqDTO);

    /**
     * 根据id获取用户信息
     *
     * @param id id
     * @return 用户信息
     */
    ResponseW info(Long id);

    /**
     * 当前登录的用户菜单
     *
     * @param id id
     * @return 结果
     */
    ResponseW nav(Long id);
}
