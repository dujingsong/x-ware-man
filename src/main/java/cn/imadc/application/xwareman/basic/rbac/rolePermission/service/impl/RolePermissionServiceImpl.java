package cn.imadc.application.xwareman.basic.rbac.rolePermission.service.impl;

import cn.imadc.application.base.common.response.ResponseW;
import cn.imadc.application.base.mybatisplus.repository.impl.BaseMPServiceImpl;
import cn.imadc.application.xwareman.basic.rbac.rolePermission.dto.request.RolePermissionFindReqDTO;
import cn.imadc.application.xwareman.basic.rbac.rolePermission.entity.RolePermission;
import cn.imadc.application.xwareman.basic.rbac.rolePermission.mapper.RolePermissionMapper;
import cn.imadc.application.xwareman.basic.rbac.rolePermission.service.IRolePermissionService;
import cn.imadc.application.xwareman.core.data.constant.Constant;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 角色菜单表 服务实现类
 * </p>
 *
 * @author 杜劲松
 * @since 2021-12-17
 */
@AllArgsConstructor
@Service
public class RolePermissionServiceImpl extends BaseMPServiceImpl<RolePermissionMapper, RolePermission> implements IRolePermissionService {

    @Override
    public ResponseW find(RolePermissionFindReqDTO reqDTO) {
        QueryWrapper<RolePermission> queryWrapper = buildQueryWrapper(reqDTO);

        if (!reqDTO.pageQuery()) return ResponseW.success(list(queryWrapper));

        Page<RolePermission> page = new Page<>(reqDTO.getPageNo(), reqDTO.getPageSize(), true);
        IPage<RolePermission> pageData = page(page, queryWrapper);
        return ResponseW.success(pageData);
    }

    private QueryWrapper<RolePermission> buildQueryWrapper(RolePermissionFindReqDTO reqDTO) {
        QueryWrapper<RolePermission> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(Constant.DEL_FLAG, Constant.NOT_DEL_VAL);

        return queryWrapper;
    }

    @Override
    public ResponseW add(RolePermission rolePermission) {
        return save(rolePermission) ? ResponseW.success() : ResponseW.error();
    }

    @Override
    public ResponseW edit(RolePermission rolePermission) {
        return updateById(rolePermission) ? ResponseW.success() : ResponseW.error();
    }

    @Override
    public ResponseW delete(RolePermission rolePermission) {
        UpdateWrapper<RolePermission> userUpdateWrapper = new UpdateWrapper<>();
        userUpdateWrapper.eq("id", rolePermission.getId());
        userUpdateWrapper.set(Constant.DEL_FLAG, Constant.DEL_VAL);
        return update(userUpdateWrapper) ? ResponseW.success() : ResponseW.error();
    }
}
