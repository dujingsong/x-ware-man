package cn.imadc.application.xwareman.basic.rbac.user.service;

import cn.imadc.application.base.common.response.ResponseW;
import cn.imadc.application.base.mybatisplus.repository.IBaseMPService;
import cn.imadc.application.xwareman.basic.rbac.user.dto.request.UserFindReqDTO;
import cn.imadc.application.xwareman.basic.rbac.user.dto.request.UserUpdatePwdReqDTO;
import cn.imadc.application.xwareman.basic.rbac.user.entity.User;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author 杜劲松
 * @since 2021-12-17
 */
public interface IUserService extends IBaseMPService<User> {

    /**
     * 查询
     *
     * @param reqDTO 参数
     * @return 结果
     */
    ResponseW find(UserFindReqDTO reqDTO);

    /**
     * 添加
     *
     * @param user 参数
     * @return 结果
     */
    ResponseW add(User user);

    /**
     * 修改
     *
     * @param user 参数
     * @return 结果
     */
    ResponseW edit(User user);

    /**
     * 删除
     *
     * @param user 参数
     * @return 结果
     */
    ResponseW delete(User user);

    /**
     * 更新最后一次登录时间
     *
     * @param id 数据ID
     * @return 结果
     */
    ResponseW updateLastLoginTime(Long id);

    /**
     * 更新基本信息
     *
     * @param user 用户信息
     * @return 结果
     */
    ResponseW updateBasicInfo(User user);

    /**
     * 更新头像
     *
     * @param userId 用户ID
     * @param avatar 头像图片
     * @return 结果
     */
    ResponseW updateAvatar(Long userId, MultipartFile avatar);

    /**
     * 更新密码
     *
     * @param reqDTO 参数
     * @return 结果
     */
    ResponseW updatePassword(UserUpdatePwdReqDTO reqDTO);
}
