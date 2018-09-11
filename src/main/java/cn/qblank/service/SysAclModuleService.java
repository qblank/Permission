package cn.qblank.service;

import cn.qblank.param.AclModuleParam;

/**
 * @author evan_qb
 * @date 2018/8/29 17:04
 */
public interface SysAclModuleService {
    void save(AclModuleParam param);

    void update(AclModuleParam param);

    void delete(int aclModuleId);
}
