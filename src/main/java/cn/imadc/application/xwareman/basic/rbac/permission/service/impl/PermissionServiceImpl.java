package cn.imadc.application.xwareman.basic.rbac.permission.service.impl;

import cn.imadc.application.base.common.response.ResponseW;
import cn.imadc.application.base.mybatisplus.repository.impl.BaseMPServiceImpl;
import cn.imadc.application.xwareman.basic.rbac.permission.dto.request.PermissionFindReqDTO;
import cn.imadc.application.xwareman.basic.rbac.permission.entity.Permission;
import cn.imadc.application.xwareman.basic.rbac.permission.mapper.PermissionMapper;
import cn.imadc.application.xwareman.basic.rbac.permission.service.IPermissionService;
import cn.imadc.application.xwareman.core.data.constant.Constant;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * <p>
 * 菜单表 服务实现类
 * </p>
 *
 * @author 杜劲松
 * @since 2021-12-17
 */
@AllArgsConstructor
@Service
public class PermissionServiceImpl extends BaseMPServiceImpl<PermissionMapper, Permission> implements IPermissionService {

    private final PermissionMapper permissionMapper;

    @Override
    public ResponseW find(PermissionFindReqDTO reqDTO) {
        QueryWrapper<Permission> queryWrapper = buildQueryWrapper(reqDTO);

        if (!reqDTO.pageQuery()) return ResponseW.success(list(queryWrapper));

        Page<Permission> page = new Page<>(reqDTO.getPageNo(), reqDTO.getPageSize(), true);
        IPage<Permission> pageData = page(page, queryWrapper);
        return ResponseW.success(pageData);
    }

    private QueryWrapper<Permission> buildQueryWrapper(PermissionFindReqDTO reqDTO) {
        QueryWrapper<Permission> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(Constant.DEL_FLAG, Constant.NOT_DEL_VAL);
        if (!CollectionUtils.isEmpty(reqDTO.getIncludeIdList())) {
            queryWrapper.in("id", reqDTO.getIncludeIdList());
        }

        return queryWrapper;
    }

    @Override
    public ResponseW add(Permission permission) {
        return save(permission) ? ResponseW.success() : ResponseW.error();
    }

    @Override
    public ResponseW edit(Permission permission) {
        return updateById(permission) ? ResponseW.success() : ResponseW.error();
    }

    @Override
    public ResponseW delete(Permission permission) {
        UpdateWrapper<Permission> userUpdateWrapper = new UpdateWrapper<>();
        userUpdateWrapper.eq("id", permission.getId());
        userUpdateWrapper.set(Constant.DEL_FLAG, Constant.DEL_VAL);
        return update(userUpdateWrapper) ? ResponseW.success() : ResponseW.error();
    }

    @Override
    public List<Permission> getUserPermission(Long userId) {
        return permissionMapper.getUserPermission(userId);
    }
}
