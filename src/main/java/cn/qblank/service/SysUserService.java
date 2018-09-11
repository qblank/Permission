package cn.qblank.service;

import cn.qblank.beans.PageQuery;
import cn.qblank.beans.PageResult;
import cn.qblank.model.SysUser;
import cn.qblank.param.DeptParam;
import cn.qblank.param.UserParam;

import java.util.List;

/**
 * @author evan_qb
 * @date 2018/8/7 15:30
 */
public interface SysUserService extends BaseService<SysUser>{
    SysUser findUserByUserName(String username);

    void update(UserParam param);

    /**
     * 添加用户
     * @param param
     */
    void save(UserParam param);

    SysUser findByKeyword(String keyword);

    PageResult<SysUser> getPageByDeptId(int deptId, PageQuery page);

    List<SysUser> getAll();
}
