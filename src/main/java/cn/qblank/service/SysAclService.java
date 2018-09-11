package cn.qblank.service;

import cn.qblank.beans.PageQuery;
import cn.qblank.beans.PageResult;
import cn.qblank.model.SysAcl;
import cn.qblank.param.AclParam;

/**
 * @author evan_qb
 * @date 2018/8/31 14:58
 */
public interface SysAclService {
    void save(AclParam param);

    void update(AclParam param);

    /**
     * 通过权限模块id获取权限点列表
     * @param aclModuleId
     * @param page
     * @return
     */
    PageResult<SysAcl> getPageByAclModuleId(int aclModuleId,PageQuery page);
}
