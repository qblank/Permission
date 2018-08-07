package cn.qblank.dao;

import cn.qblank.model.SysUser;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author evan_qb
 * @date 2018/8/7 15:20
 */
public interface UserMapper extends Mapper<SysUser> {
    SysUser getUserByUserName(String username);
}
