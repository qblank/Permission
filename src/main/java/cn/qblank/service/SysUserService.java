package cn.qblank.service;

import cn.qblank.model.SysUser;

/**
 * @author evan_qb
 * @date 2018/8/7 15:30
 */
public interface SysUserService extends BaseService<SysUser>{
    SysUser findUserByUserName(String username);
}
