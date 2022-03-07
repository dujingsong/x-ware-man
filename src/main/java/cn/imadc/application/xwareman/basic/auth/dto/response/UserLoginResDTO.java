package cn.imadc.application.xwareman.basic.auth.dto.response;

import cn.imadc.application.base.common.response.BaseResponseDTO;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 用户登录响应体
 * </p>
 *
 * @author 杜劲松
 * @since 2021-12-17
 */
@Getter
@Setter
public class UserLoginResDTO extends BaseResponseDTO {

    /**
     * 登陆后返回的会话TOKEN
     */
    private String token;
}
