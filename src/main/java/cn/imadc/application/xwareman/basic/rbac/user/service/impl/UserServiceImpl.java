package cn.imadc.application.xwareman.basic.rbac.user.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.imadc.application.base.common.exception.BizException;
import cn.imadc.application.base.common.response.ResponseW;
import cn.imadc.application.base.mybatisplus.repository.impl.BaseMPServiceImpl;
import cn.imadc.application.base.toolkit.encryption.Md5Util;
import cn.imadc.application.xwareman.basic.rbac.user.dto.request.UserFindReqDTO;
import cn.imadc.application.xwareman.basic.rbac.user.dto.request.UserUpdatePwdReqDTO;
import cn.imadc.application.xwareman.basic.rbac.user.entity.User;
import cn.imadc.application.xwareman.basic.rbac.user.mapper.UserMapper;
import cn.imadc.application.xwareman.basic.rbac.user.service.IUserService;
import cn.imadc.application.xwareman.basic.rbac.userRole.service.IUserRoleService;
import cn.imadc.application.xwareman.core.data.constant.Constant;
import cn.imadc.application.xwareman.core.data.constant.Word;
import cn.imadc.application.xwareman.core.data.property.AppProp;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author 杜劲松
 * @since 2021-12-17
 */
@AllArgsConstructor
@Service
public class UserServiceImpl extends BaseMPServiceImpl<UserMapper, User> implements IUserService {

    private final UserMapper userMapper;
    private final IUserRoleService userRoleService;
    private final AppProp appProp;

    @Override
    public ResponseW find(UserFindReqDTO reqDTO) {
        QueryWrapper<User> queryWrapper = buildQueryWrapper(reqDTO);

        List<User> userList = list(queryWrapper);
        userList.forEach(this::handleUser);
        if (!reqDTO.pageQuery()) return ResponseW.success(userList);

        Page<User> page = new Page<>(reqDTO.getPageNo(), reqDTO.getPageSize(), true);
        IPage<User> pageData = page(page, queryWrapper);
        pageData.getRecords().forEach(this::handleUser);
        return ResponseW.success(pageData);
    }

    private void handleUser(User user) {
        List<Map<String, Object>> roleIds = userRoleService.getUserRoleInfo(user.getId());
        List<Long> permRoleIds = new ArrayList<>();
        List<Long> itemRoleIds = new ArrayList<>();
        List<Long> itemFlowRoleIds = new ArrayList<>();
        for (Map<String, Object> map : roleIds) {
            if (map.get("type").equals("PERM")) {
                permRoleIds.add(Long.parseLong(map.get("id").toString()));
            }
            if (map.get("type").equals("ITEM")) {
                itemRoleIds.add(Long.parseLong(map.get("id").toString()));
            }
            if (map.get("type").equals("ITEM_FLOW")) {
                itemFlowRoleIds.add(Long.parseLong(map.get("id").toString()));
            }
        }
        user.setPermRoleIds(permRoleIds);
        user.setItemRoleIds(itemRoleIds);
        user.setItemFlowRoleIds(itemFlowRoleIds);
    }

    private QueryWrapper<User> buildQueryWrapper(UserFindReqDTO reqDTO) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(Constant.DEL_FLAG, Constant.NOT_DEL_VAL);

        queryWrapper.ne("id", 1L);

        return queryWrapper;
    }

    @Transactional
    @Override
    public ResponseW add(User user) {
        user.setPassword(Md5Util.md5(user.getPassword()));
        boolean status = save(user);
        if (status) {
            handleRole(user);
        }
        return status ? ResponseW.success() : ResponseW.error();
    }

    private void handleRole(User user) {
        Set<Long> roleIds = new HashSet<>();
        if (!CollectionUtils.isEmpty(user.getPermRoleIds())) {
            roleIds.addAll(user.getPermRoleIds());
        }
        if (!CollectionUtils.isEmpty(user.getItemRoleIds())) {
            roleIds.addAll(user.getItemRoleIds());
        }
        if (!CollectionUtils.isEmpty(user.getItemFlowRoleIds())) {
            roleIds.addAll(user.getItemFlowRoleIds());
        }
        userRoleService.bindUser(user.getId(), roleIds);
    }

    @Transactional
    @Override
    public ResponseW edit(User user) {
        boolean status = updateById(user);
        if (status) {
            handleRole(user);
        }
        return status ? ResponseW.success() : ResponseW.error();
    }

    @Transactional
    @Override
    public ResponseW delete(User user) {
        UpdateWrapper<User> userUpdateWrapper = new UpdateWrapper<>();
        userUpdateWrapper.eq("id", user.getId());
        userUpdateWrapper.set(Constant.DEL_FLAG, Constant.DEL_VAL);
        return update(userUpdateWrapper) ? ResponseW.success() : ResponseW.error();
    }

    @Transactional
    @Override
    public ResponseW updateLastLoginTime(Long id, LocalDateTime lastLoginTime) {
        User user = new User();
        if (null == lastLoginTime) lastLoginTime = LocalDateTime.now();
        user.setLastLoginTime(lastLoginTime);
        user.setCurrentLoginTime(LocalDateTime.now());

        UpdateWrapper<User> userUpdateWrapper = new UpdateWrapper<>();
        userUpdateWrapper.eq("id", id);
        return userMapper.update(user, userUpdateWrapper) == 1 ? ResponseW.success() : ResponseW.error(Word.OP_FAIL);
    }

    @Transactional
    @Override
    public ResponseW updateBasicInfo(User user) {
        User newUser = new User();
        newUser.setName(user.getName());
        newUser.setProfile(user.getProfile());
        newUser.setEmail(user.getEmail());

        UpdateWrapper<User> userUpdateWrapper = new UpdateWrapper<>();
        userUpdateWrapper.eq("id", user.getId());
        return userMapper.update(newUser, userUpdateWrapper) == 1 ? ResponseW.success() : ResponseW.error(Word.OP_FAIL);
    }

    @Transactional
    @Override
    public ResponseW updateAvatar(Long userId, MultipartFile avatar) {
        String fileStorePath = appProp.getFileStorePath(), avatarPath = appProp.getAvatarPath();
        String ymd = DateUtil.format(LocalDateTime.now(), Constant.YYYY_MM_DD) + Constant.SLASH;

        File targetFilePath = new File(fileStorePath + avatarPath + ymd);
        if (!targetFilePath.exists() && !targetFilePath.mkdirs()) {
            return ResponseW.error(Word.MK_DIRS_FAIL);
        }

        String originalFilename = avatar.getOriginalFilename();
        if (StringUtils.isEmpty(originalFilename)) {
            return ResponseW.error("图片名称为空");
        }

        String newFilename = userId + "-" + System.currentTimeMillis() + ".png";
        File targetFile = new File(fileStorePath + avatarPath + ymd + newFilename);
        try {
            avatar.transferTo(targetFile);
        } catch (IOException e) {
            return ResponseW.error("图片存储异常");
        }

        // 更新头像
        User newUser = new User();
        newUser.setAvatar(ymd + newFilename);

        UpdateWrapper<User> userUpdateWrapper = new UpdateWrapper<>();
        userUpdateWrapper.eq("id", userId);

        int updatedRows = userMapper.update(newUser, userUpdateWrapper);
        if (updatedRows != 1) {
            throw new BizException(Word.OP_FAIL);
        }

        return ResponseW.success(ymd + newFilename);
    }

    @Transactional
    @Override
    public ResponseW updatePassword(UserUpdatePwdReqDTO reqDTO) {
        User user = userMapper.selectById(reqDTO.getUserId());
        if (!StringUtils.equals(Md5Util.md5(reqDTO.getOldPassword()), user.getPassword())) {
            return ResponseW.error("原密码不正确");
        }

        User newUser = new User();
        newUser.setPassword(Md5Util.md5(reqDTO.getNewPassword()));

        UpdateWrapper<User> userUpdateWrapper = new UpdateWrapper<>();
        userUpdateWrapper.eq("id", reqDTO.getUserId());

        if (userMapper.update(newUser, userUpdateWrapper) != 1) {
            throw new BizException(Word.OP_FAIL);
        }
        return ResponseW.success();
    }
}
