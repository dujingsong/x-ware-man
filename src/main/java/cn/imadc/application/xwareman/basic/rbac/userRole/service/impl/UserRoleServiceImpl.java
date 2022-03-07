package cn.imadc.application.xwareman.basic.rbac.userRole.service.impl;

import cn.imadc.application.base.common.response.ResponseW;
import cn.imadc.application.base.mybatisplus.repository.impl.BaseMPServiceImpl;
import cn.imadc.application.xwareman.basic.rbac.role.entity.Role;
import cn.imadc.application.xwareman.basic.rbac.userRole.dto.request.UserRoleFindReqDTO;
import cn.imadc.application.xwareman.basic.rbac.userRole.entity.UserRole;
import cn.imadc.application.xwareman.basic.rbac.userRole.mapper.UserRoleMapper;
import cn.imadc.application.xwareman.basic.rbac.userRole.service.IUserRoleService;
import cn.imadc.application.xwareman.core.data.constant.Constant;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户角色表 服务实现类
 * </p>
 *
 * @author 杜劲松
 * @since 2021-12-17
 */
@AllArgsConstructor
@Service
public class UserRoleServiceImpl extends BaseMPServiceImpl<UserRoleMapper, UserRole> implements IUserRoleService {

    private final UserRoleMapper userRoleMapper;

    @Override
    public ResponseW find(UserRoleFindReqDTO reqDTO) {
        QueryWrapper<UserRole> queryWrapper = buildQueryWrapper(reqDTO);

        if (!reqDTO.pageQuery()) return ResponseW.success(list(queryWrapper));

        Page<UserRole> page = new Page<>(reqDTO.getPageNo(), reqDTO.getPageSize(), true);
        IPage<UserRole> pageData = page(page, queryWrapper);
        return ResponseW.success(pageData);
    }

    private QueryWrapper<UserRole> buildQueryWrapper(UserRoleFindReqDTO reqDTO) {
        QueryWrapper<UserRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(Constant.DEL_FLAG, Constant.NOT_DEL_VAL);

        return queryWrapper;
    }

    @Override
    public ResponseW add(UserRole userRole) {
        return save(userRole) ? ResponseW.success() : ResponseW.error();
    }

    @Override
    public ResponseW edit(UserRole userRole) {
        return updateById(userRole) ? ResponseW.success() : ResponseW.error();
    }

    @Override
    public ResponseW delete(UserRole userRole) {
        UpdateWrapper<UserRole> userUpdateWrapper = new UpdateWrapper<>();
        userUpdateWrapper.eq("id", userRole.getId());
        userUpdateWrapper.set(Constant.DEL_FLAG, Constant.DEL_VAL);
        return update(userUpdateWrapper) ? ResponseW.success() : ResponseW.error();
    }

    @Transactional
    @Override
    public ResponseW bindUser(Long userId, Set<Long> roleIds) {
        // 解绑原来的关系
        UpdateWrapper<UserRole> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("user_id", userId);
        updateWrapper.set(Constant.DEL_FLAG, Constant.DEL_VAL);
        update(updateWrapper);

        // 建立新的关系
        if (!CollectionUtils.isEmpty(roleIds)) {
            saveBatch(roleIds.stream().map(roleId -> new UserRole(userId, roleId)).collect(Collectors.toList()));
        }

        return ResponseW.success();
    }

    @Override
    public List<Map<String, Object>> getUserRoleInfo(Long userId) {
        List<Role> roleList = userRoleMapper.getUserRoleInfo(userId);
        return roleList.stream().map(item -> {
            Map<String, Object> map = new HashMap<>();
            map.put("type", item.getType());
            map.put("id", item.getId());
            return map;
        }).collect(Collectors.toList());
    }
}
