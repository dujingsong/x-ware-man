package cn.imadc.application.xwareman.basic.auth.service.impl;

import cn.imadc.application.base.auth.jwt.JWT;
import cn.imadc.application.base.common.response.ResponseW;
import cn.imadc.application.base.toolkit.encryption.Md5Util;
import cn.imadc.application.xwareman.basic.auth.dto.request.UserLoginReqDTO;
import cn.imadc.application.xwareman.basic.auth.dto.response.UserInfoResDTO;
import cn.imadc.application.xwareman.basic.auth.dto.response.UserLoginResDTO;
import cn.imadc.application.xwareman.basic.auth.dto.response.UserNavResDTO;
import cn.imadc.application.xwareman.basic.auth.service.ICredentialService;
import cn.imadc.application.xwareman.basic.rbac.permission.entity.Permission;
import cn.imadc.application.xwareman.basic.rbac.permission.service.IPermissionService;
import cn.imadc.application.xwareman.basic.rbac.role.entity.Role;
import cn.imadc.application.xwareman.basic.rbac.role.service.IRoleService;
import cn.imadc.application.xwareman.basic.rbac.user.entity.User;
import cn.imadc.application.xwareman.basic.rbac.user.service.IUserService;
import cn.imadc.application.xwareman.core.data.constant.Word;
import cn.imadc.application.xwareman.core.data.property.AppProp;
import cn.imadc.application.xwareman.core.data.rbac.ST;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Service
public class CredentialServiceImpl implements ICredentialService {

    private final AppProp appProp;
    private final IUserService userService;
    private final IRoleService roleService;
    private final IPermissionService permissionService;

    @Override
    public ResponseW login(UserLoginReqDTO reqDTO) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", reqDTO.getUsername());
        queryWrapper.eq("password", Md5Util.md5(reqDTO.getPassword()));
        User user = userService.getOne(queryWrapper);

        if (null == user) return ResponseW.error(Word.CREDENTIALS_ERROR);

        // 最后一次登录时间
        userService.updateLastLoginTime(user.getId(), user.getCurrentLoginTime());

        String token = JWT.generate(ST.USER, user.getId(), appProp.getCtxTimeout(), appProp.getCtxTimeoutUnit());
        UserLoginResDTO userLoginResDTO = new UserLoginResDTO();
        userLoginResDTO.setToken(token);
        return ResponseW.success(userLoginResDTO);
    }

    @Override
    public ResponseW info(Long id) {
        UserInfoResDTO userInfoResDTO = new UserInfoResDTO();
        User user = userService.getById(id);
        userInfoResDTO.setUser(user);
        // 拥有的角色
        List<Role> roles = roleService.getUserRole(user.getId());
        userInfoResDTO.setRoles(roles);
        return ResponseW.success(userInfoResDTO);
    }

    @Override
    public ResponseW nav(Long id) {
        UserNavResDTO userNavResDTO = new UserNavResDTO();
        User user = userService.getById(id);
        List<Permission> permissions;
        permissions = permissionService.getUserPermission(user.getId());

        // 去掉重复的菜单
        Set<Long> pidSet = new HashSet<>();
        for (int i = 0; i < permissions.size(); i++) {
            Long pid = permissions.get(i).getId();
            if (pidSet.contains(pid)) permissions.remove(i--);
            pidSet.add(pid);
        }

        userNavResDTO.setPermissions(permissions);
        return ResponseW.success(userNavResDTO);
    }
}
