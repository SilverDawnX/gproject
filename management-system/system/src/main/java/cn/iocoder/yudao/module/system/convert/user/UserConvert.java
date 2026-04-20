package cn.iocoder.yudao.module.system.convert.user;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.framework.common.util.collection.MapUtils;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.system.controller.admin.dept.vo.dept.DeptSimpleRespVO;
import cn.iocoder.yudao.module.system.controller.admin.dept.vo.post.PostSimpleRespVO;
import cn.iocoder.yudao.module.system.controller.admin.permission.vo.role.RoleSimpleRespVO;
import cn.iocoder.yudao.module.system.controller.admin.user.vo.profile.UserProfileRespVO;
import cn.iocoder.yudao.module.system.controller.admin.user.vo.user.UserRespVO;
import cn.iocoder.yudao.module.system.controller.admin.user.vo.user.UserSimpleRespVO;
import cn.iocoder.yudao.module.system.dal.dataobject.dept.DeptDO;
import cn.iocoder.yudao.module.system.dal.dataobject.dept.PostDO;
import cn.iocoder.yudao.module.system.dal.dataobject.permission.RoleDO;
import cn.iocoder.yudao.module.system.dal.dataobject.user.AdminUserDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Mapper
public interface UserConvert {

    UserConvert INSTANCE = Mappers.getMapper(UserConvert.class);

    default List<UserRespVO> convertList(List<AdminUserDO> list, Map<Long, DeptDO> deptMap) {
        return CollectionUtils.convertList(list, user -> convert(user, deptMap.get(user.getDeptId())));
    }

    default List<UserRespVO> convertList(List<AdminUserDO> list, Map<Long, DeptDO> deptMap, Map<Long, PostDO> postMap) {
        return CollectionUtils.convertList(list, user -> convert(user, deptMap.get(user.getDeptId()), postMap));
    }

    default List<UserRespVO> convertList(List<AdminUserDO> list, Map<Long, DeptDO> deptMap,
                                         Map<Long, PostDO> postMap, Map<Long, RoleDO> roleMap,
                                         Map<Long, Set<Long>> userRoleIdMap) {
        return CollectionUtils.convertList(list, user -> convert(user, deptMap.get(user.getDeptId()),
                postMap, roleMap, userRoleIdMap.get(user.getId())));
    }

    default UserRespVO convert(AdminUserDO user, DeptDO dept) {
        UserRespVO userVO = BeanUtils.toBean(user, UserRespVO.class);
        if (dept != null) {
            userVO.setDeptName(dept.getName());
        }
        return userVO;
    }

    default UserRespVO convert(AdminUserDO user, DeptDO dept, Map<Long, PostDO> postMap) {
        UserRespVO userVO = convert(user, dept);
        if (CollUtil.isNotEmpty(user.getPostIds())) {
            List<String> postNames = new ArrayList<>();
            user.getPostIds().forEach(postId -> {
                PostDO post = postMap.get(postId);
                if (post != null) {
                    postNames.add(post.getName());
                }
            });
            userVO.setPostNames(StrUtil.join(",", postNames));
        }
        return userVO;
    }

    default UserRespVO convert(AdminUserDO user, DeptDO dept, Map<Long, PostDO> postMap,
                               Map<Long, RoleDO> roleMap, Set<Long> roleIds) {
        UserRespVO userVO = convert(user, dept, postMap);
        if (CollUtil.isNotEmpty(roleIds)) {
            List<String> roleNames = new ArrayList<>();
            roleIds.forEach(roleId -> {
                RoleDO role = roleMap.get(roleId);
                if (role != null) {
                    roleNames.add(role.getName());
                }
            });
            userVO.setRoleNames(StrUtil.join(",", roleNames));
        }
        return userVO;
    }

    default List<UserSimpleRespVO> convertSimpleList(List<AdminUserDO> list, Map<Long, DeptDO> deptMap) {
        return CollectionUtils.convertList(list, user -> {
            UserSimpleRespVO userVO = BeanUtils.toBean(user, UserSimpleRespVO.class);
            MapUtils.findAndThen(deptMap, user.getDeptId(), dept -> userVO.setDeptName(dept.getName()));
            return userVO;
        });
    }

    default UserProfileRespVO convert(AdminUserDO user, List<RoleDO> userRoles,
                                      DeptDO dept, List<PostDO> posts) {
        UserProfileRespVO userVO = BeanUtils.toBean(user, UserProfileRespVO.class);
        userVO.setRoles(BeanUtils.toBean(userRoles, RoleSimpleRespVO.class));
        userVO.setDept(BeanUtils.toBean(dept, DeptSimpleRespVO.class));
        userVO.setPosts(BeanUtils.toBean(posts, PostSimpleRespVO.class));
        return userVO;
    }

}
