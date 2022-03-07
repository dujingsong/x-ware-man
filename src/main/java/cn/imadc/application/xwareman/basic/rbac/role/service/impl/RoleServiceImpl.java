package cn.imadc.application.xwareman.basic.rbac.role.service.impl;

import cn.imadc.application.base.common.response.ResponseW;
import cn.imadc.application.base.mybatisplus.repository.impl.BaseMPServiceImpl;
import cn.imadc.application.xwareman.basic.rbac.role.dto.request.RoleFindReqDTO;
import cn.imadc.application.xwareman.basic.rbac.role.entity.Role;
import cn.imadc.application.xwareman.basic.rbac.role.mapper.RoleMapper;
import cn.imadc.application.xwareman.basic.rbac.role.service.IRoleService;
import cn.imadc.application.xwareman.core.data.constant.Constant;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author 杜劲松
 * @since 2021-12-17
 */
@AllArgsConstructor
@Service
public class RoleServiceImpl extends BaseMPServiceImpl<RoleMapper, Role> implements IRoleService {

    private final RoleMapper roleMapper;

    @Override
    public ResponseW find(RoleFindReqDTO reqDTO) {
        QueryWrapper<Role> queryWrapper = buildQueryWrapper(reqDTO);

        if (!reqDTO.pageQuery()) return ResponseW.success(list(queryWrapper));

        Page<Role> page = new Page<>(reqDTO.getPageNo(), reqDTO.getPageSize(), true);
        IPage<Role> pageData = page(page, queryWrapper);
        return ResponseW.success(pageData);
    }

    private QueryWrapper<Role> buildQueryWrapper(RoleFindReqDTO reqDTO) {
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(Constant.DEL_FLAG, Constant.NOT_DEL_VAL);

        if (StringUtils.isNotBlank(reqDTO.getType())) {
            queryWrapper.eq("type", reqDTO.getType());
        }

        return queryWrapper;
    }

    @Override
    public ResponseW add(Role role) {
        return save(role) ? ResponseW.success() : ResponseW.error();
    }

    @Override
    public ResponseW edit(Role role) {
        return updateById(role) ? ResponseW.success() : ResponseW.error();
    }

    @Override
    public ResponseW delete(Role role) {
        UpdateWrapper<Role> userUpdateWrapper = new UpdateWrapper<>();
        userUpdateWrapper.eq("id", role.getId());
        userUpdateWrapper.set(Constant.DEL_FLAG, Constant.DEL_VAL);
        return update(userUpdateWrapper) ? ResponseW.success() : ResponseW.error();
    }

    @Override
    public List<Role> getUserRole(Long userId) {
        return roleMapper.getUserRole(userId);
    }
}
