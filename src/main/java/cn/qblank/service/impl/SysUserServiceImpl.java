package cn.qblank.service.impl;

import cn.qblank.dao.UserMapper;
import cn.qblank.model.SysUser;
import cn.qblank.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author evan_qb
 * @date 2018/8/7 15:31
 */
@Service
public class SysUserServiceImpl extends BaseServiceImpl<SysUser> implements SysUserService {
    @Autowired
    private UserMapper mapper;

    @Override
    public SysUser findUserByUserName(String username) {
        return this.mapper.getUserByUserName(username);
    }

    @Override
    protected Mapper<SysUser> getMapper() {
        return this.mapper;
    }
}
