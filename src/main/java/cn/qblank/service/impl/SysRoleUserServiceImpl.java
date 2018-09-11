package cn.qblank.service.impl;

import cn.qblank.common.RequestHolder;
import cn.qblank.dao.SysRoleUserMapper;
import cn.qblank.dao.SysUserMapper;
import cn.qblank.model.SysRoleUser;
import cn.qblank.model.SysUser;
import cn.qblank.service.SysLogService;
import cn.qblank.service.SysRoleUserService;
import cn.qblank.util.IpUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author evan_qb
 * @date 2018/9/4
 */
@Service
public class SysRoleUserServiceImpl implements SysRoleUserService {
    @Autowired
    private SysRoleUserMapper sysRoleUserMapper;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysLogService sysLogService;

    @Override
    public List<SysUser> getListByRoleId(int roleId) {
        List<Integer> userIdList = sysRoleUserMapper.getUserIdListByRoleId(roleId);
        if (CollectionUtils.isEmpty(userIdList)){
            return Lists.newArrayList();
        }
        return sysUserMapper.getByIdList(userIdList);
    }

    @Override
    public void changeRoleUsers(int roleId, List<Integer> userIdList) {
        List<Integer> originUserIdList = sysRoleUserMapper.getUserIdListByRoleId(roleId);
        if (originUserIdList.size() == userIdList.size()) {
            Set<Integer> originUserIdSet = Sets.newHashSet(originUserIdList);
            Set<Integer> userIdSet = Sets.newHashSet(userIdList);
            originUserIdSet.removeAll(userIdSet);
            if (CollectionUtils.isEmpty(originUserIdSet)) {
                return;
            }
        }
        updateRoleUsers(roleId, userIdList);

        sysLogService.saveRoleUserLog(roleId,originUserIdList,userIdList);
    }

    /**
     * 更新role-user
     * @param roleId
     * @param userIdList
     */
    @Transactional
    public void updateRoleUsers(int roleId,List<Integer> userIdList){
        sysRoleUserMapper.deleteByRoleId(roleId);
        if (CollectionUtils.isEmpty(userIdList)){
            return;
        }
        List<SysRoleUser> roleUserList = Lists.newArrayList();
        for(Integer userId : userIdList){
            SysRoleUser roleUser = SysRoleUser.builder().roleId(roleId).userId(userId)
                    .operator(RequestHolder.getCurrentUser().getUsername())
                    .operateTime(new Date())
                    .operateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest())).build();
            roleUserList.add(roleUser);
        }
        sysRoleUserMapper.batchInsert(roleUserList);

    }

}
