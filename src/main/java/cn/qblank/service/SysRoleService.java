package cn.qblank.service;

import cn.qblank.model.SysRole;
import cn.qblank.model.SysUser;
import cn.qblank.param.RoleParam;

import java.util.List;

/**
 * @author evan_qb
 * @date 2018/8/31 15:54
 */
public interface SysRoleService {
    void save(RoleParam param);

    void update(RoleParam param);

    List<SysRole> getAll();

    List<SysRole> getRoleListByUserId(int userId);

    List<SysRole> getRoleListByAclId(int aclId);

    List<SysUser> getUserListByRoleList(List<SysRole> roleList);
}
