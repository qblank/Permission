package cn.qblank.service;

import java.util.List;

/**
 * @author evan_qb
 * @date 2018/9/4
 */
public interface SysRoleAclService {

    void changeRoleAcls(Integer roleId, List<Integer> aclIdList);
}
