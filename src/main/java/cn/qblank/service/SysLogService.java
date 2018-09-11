package cn.qblank.service;

import cn.qblank.beans.PageQuery;
import cn.qblank.beans.PageResult;
import cn.qblank.model.*;
import cn.qblank.param.SearchLogParam;

import java.util.List;

/**
 * @author evan_qb
 * @date 2018/9/11
 */
public interface SysLogService {
    void saveDeptLog(SysDept before,SysDept after);

    void saveUserLog(SysUser before,SysUser after);

    void saveAclModuleLog(SysAclModule before,SysAclModule after);

    void saveAclLog(SysAcl before,SysAcl after);

    void saveRoleLog(SysRole before,SysRole after);

    void saveRoleAclLog(int roleId, List<Integer> before,List<Integer> after);

    void saveRoleUserLog(int roleId,List<Integer> before,List<Integer> after);

    PageResult<SysLogWithBLOBs> searchPageList(SearchLogParam param, PageQuery page);

    void recover(int id);
}
