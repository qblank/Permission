package cn.qblank.service.impl;

import cn.qblank.common.RequestHolder;
import cn.qblank.dao.SysRoleAclMapper;
import cn.qblank.model.SysRoleAcl;
import cn.qblank.service.SysLogService;
import cn.qblank.service.SysRoleAclService;
import cn.qblank.util.IpUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author evan_qb
 * @date 2018/9/4
 */
@Service
public class SysRoleAclServiceImpl implements SysRoleAclService {
    @Autowired
    private SysRoleAclMapper sysRoleAclMapper;
    @Autowired
    private SysLogService sysLogService;

    @Override
    public void changeRoleAcls(Integer roleId, List<Integer> aclIdList) {
        List<Integer> originAclIdList = sysRoleAclMapper.getAclIdListByRoleList(Lists.newArrayList(roleId));
        if (originAclIdList.size() == aclIdList.size()){
            Set<Integer> originAclSet = Sets.newHashSet(originAclIdList);
            Set<Integer> aclIdSet = Sets.newHashSet(aclIdList);
            originAclSet.removeAll(aclIdList);
            if (CollectionUtils.isEmpty(originAclSet)){
                return;
            }
        }
        updateRoleAcls(roleId,aclIdList);

        sysLogService.saveRoleAclLog(roleId,originAclIdList,aclIdList);
    }

    @Transactional
    public void updateRoleAcls(int roleId,List<Integer> aclIdList){
        sysRoleAclMapper.deleteByRoleId(roleId);
        if (CollectionUtils.isEmpty(aclIdList)){
            return;
        }
        List<SysRoleAcl> roleAclList = Lists.newArrayList();
        for (Integer aclId: aclIdList){
            SysRoleAcl roleAcl = SysRoleAcl.builder().roleId(roleId).aclId(aclId).
                    operator(RequestHolder.getCurrentUser().getUsername())
                    .operateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()))
                    .operateTime(new Date()).build();
            roleAclList.add(roleAcl);
        }
        //批量添加
        sysRoleAclMapper.batchInsert(roleAclList);
    }
}
