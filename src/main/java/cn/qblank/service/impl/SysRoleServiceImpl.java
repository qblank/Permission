package cn.qblank.service.impl;

import cn.qblank.common.RequestHolder;
import cn.qblank.dao.SysRoleAclMapper;
import cn.qblank.dao.SysRoleMapper;
import cn.qblank.dao.SysRoleUserMapper;
import cn.qblank.dao.SysUserMapper;
import cn.qblank.exception.ParamException;
import cn.qblank.model.SysRole;
import cn.qblank.model.SysUser;
import cn.qblank.param.RoleParam;
import cn.qblank.service.SysLogService;
import cn.qblank.service.SysRoleService;
import cn.qblank.util.BeanValidator;
import cn.qblank.util.IpUtil;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author evan_qb
 * @date 2018/8/31 15:54
 */
@Service
public class SysRoleServiceImpl implements SysRoleService {
    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private SysRoleUserMapper sysRoleUserMapper;
    @Autowired
    private SysRoleAclMapper sysRoleAclMapper;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysLogService sysLogService;


    @Override
    @Transactional
    public void save(RoleParam param) {
        BeanValidator.check(param);
        if (checkExist(param.getName(),param.getId())){
            throw new ParamException("角色名称已经存在");
        }

        SysRole role = SysRole.builder().name(param.getName()).status(param.getStatus()).type(param.getType())
                .remark(param.getRemark()).build();
        role.setOperator(RequestHolder.getCurrentUser().getUsername());
        role.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        role.setOperateTime(new Date());
        //插入角色
        sysRoleMapper.insertSelective(role);

        sysLogService.saveRoleLog(null,role);
    }

    @Override
    @Transactional
    public void update(RoleParam param) {
        BeanValidator.check(param);
        if (checkExist(param.getName(), param.getId())) {
            throw new ParamException("角色名称已经存在");
        }
        SysRole before = sysRoleMapper.selectByPrimaryKey(param.getId());
        Preconditions.checkNotNull(before,"待更新的角色不存在");
        SysRole after = SysRole.builder().id(param.getId()).name(param.getName()).status(param.getStatus())
                .type(param.getType()).remark(param.getRemark()).build();
        after.setOperator(RequestHolder.getCurrentUser().getUsername());
        after.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        after.setOperateTime(new Date());
        //更新角色
        sysRoleMapper.updateByPrimaryKeySelective(after);

        sysLogService.saveRoleLog(before,after);
    }

    @Override
    public List<SysRole> getAll(){
        return sysRoleMapper.getAll();
    }

    @Override
    public List<SysRole> getRoleListByUserId(int userId) {
        List<Integer> roleIdList = sysRoleUserMapper.getRoleIdListByUserId(userId);
        if (CollectionUtils.isEmpty(roleIdList)){
            return Lists.newArrayList();
        }
        return sysRoleMapper.getByIdList(roleIdList);
    }

    @Override
    public List<SysRole> getRoleListByAclId(int aclId) {
        List<Integer> roleIdList = sysRoleAclMapper.getRoleIdListByAclId(aclId);
        if (CollectionUtils.isEmpty(roleIdList)){
            return Lists.newArrayList();
        }
        return sysRoleMapper.getByIdList(roleIdList);
    }

    @Override
    public List<SysUser> getUserListByRoleList(List<SysRole> roleList) {
        if (CollectionUtils.isEmpty(roleList)){
            return Lists.newArrayList();
        }
        List<Integer> roleIdList = roleList.stream().map(role -> role.getId()).collect(Collectors.toList());
        List<Integer> userIdList = sysRoleUserMapper.getUserIdListByRoleList(roleIdList);
        if (CollectionUtils.isEmpty(userIdList)){
            return Lists.newArrayList();
        }
        return sysUserMapper.getByIdList(userIdList);
    }


    private boolean checkExist(String name, Integer id) {
        return sysRoleMapper.countByName(name, id) > 0;
    }
}
