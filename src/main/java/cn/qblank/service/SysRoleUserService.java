package cn.qblank.service;

import cn.qblank.model.SysUser;

import java.util.List;

/**
 * @author evan_qb
 * @date 2018/9/4
 */
public interface SysRoleUserService {
    List<SysUser> getListByRoleId(int roleId);

    void changeRoleUsers(int roleId,List<Integer> userIdList);
}
