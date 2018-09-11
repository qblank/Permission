package cn.qblank.service;

import cn.qblank.param.DeptParam;

/**
 * @author evan_qb
 * @date 2018/8/8 16:45
 */
public interface SysDeptService extends BaseService<DeptParam> {
    void delete(int deptId);
}
