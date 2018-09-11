package cn.qblank.service;

import cn.qblank.model.SysAcl;

import java.util.List;

/**
 * @author evan_qb
 * @date 2018/8/31 16:56
 */
public interface SysCoreService {
    List<SysAcl> getCurrentuserAclList();

    List<SysAcl> getRoleAclList(int roleId);

    List<SysAcl> getUserAclList(int userId);

    boolean hasUrlAcl(String url);
}
