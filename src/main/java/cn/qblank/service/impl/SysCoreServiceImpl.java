package cn.qblank.service.impl;

import cn.qblank.beans.CacheKeyConstants;
import cn.qblank.common.RequestHolder;
import cn.qblank.dao.SysAclMapper;
import cn.qblank.dao.SysRoleAclMapper;
import cn.qblank.dao.SysRoleUserMapper;
import cn.qblank.model.SysAcl;
import cn.qblank.model.SysUser;
import cn.qblank.service.SysCacheService;
import cn.qblank.service.SysCoreService;
import cn.qblank.util.JsonMapper;
import cn.qblank.util.StringUtil;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author evan_qb
 * @date 2018/8/31 17:31
 */
@Service
public class SysCoreServiceImpl implements SysCoreService {

    @Autowired
    private SysAclMapper sysAclMapper;
    @Autowired
    private SysRoleUserMapper sysRoleUserMapper;
    @Autowired
    private SysRoleAclMapper sysRoleAclMapper;
    @Autowired
    private SysCacheService sysCacheService;


    @Override
    public List<SysAcl> getCurrentuserAclList() {
        int userid = RequestHolder.getCurrentUser().getId();
        return getUserAclList(userid);
    }

    @Override
    public List<SysAcl> getRoleAclList(int roleId) {
        List<Integer> aclIdList = sysRoleAclMapper.getAclIdListByRoleList(Lists.newArrayList(roleId));
        if (CollectionUtils.isEmpty(aclIdList)){
            return Lists.newArrayList();
        }
        return sysAclMapper.getByIdList(aclIdList);
    }


    @Override
    public List<SysAcl> getUserAclList(int userId) {
        //TODO:可引入缓存
        if (isSupperAdmin()){
            return sysAclMapper.getAll();
        }
        List<Integer> userRoleIdList = sysRoleUserMapper.getRoleIdListByUserId(userId);
        if (CollectionUtils.isEmpty(userRoleIdList)){
            return Lists.newArrayList();
        }
        List<Integer> userAclIdList = sysRoleAclMapper.getAclIdListByRoleList(userRoleIdList);
        if (CollectionUtils.isEmpty(userAclIdList)){
            return Lists.newArrayList();
        }
        return sysAclMapper.getByIdList(userAclIdList);
    }

    /**
     * 判断是否是超级管理员
     * @return
     */
    private boolean isSupperAdmin(){
        SysUser sysUser = RequestHolder.getCurrentUser();
        if (sysUser.getMail().contains("evan")){
            return true;
        }
        return false;
    }

    /**
     * 是否有权限访问
     * @param url
     * @return
     */
    @Override
    public boolean hasUrlAcl(String url){
        if (isSupperAdmin()){
            return true;
        }
        List<SysAcl> aclList = sysAclMapper.getByUrl(url);
        if (CollectionUtils.isEmpty(aclList)){
            return true;
        }
        //此处可引入缓存
        List<SysAcl> userAclList = getCurrentUserAclListFromCache();
        Set<Integer> userAclSet = userAclList.stream().map(acl -> acl.getId()).collect(Collectors.toSet());
        boolean hasValidAcl = false;
        // 规则：只要有一个权限点有权限，那么就可以认为他有权限
        for (SysAcl acl : aclList){
            //判断一个用户是否具有某个权限的访问权限
            if (acl == null || acl.getStatus() != 1){   //权限点无效
                continue;
            }
            hasValidAcl = true;
            if (userAclSet.contains(acl.getId())){
                return true;
            }
        }
        if (!hasValidAcl){
            return true;
        }
        return false;
    }

    /**
     * 对当前用户对应的权限列表进行缓存
     * @return
     */
    public List<SysAcl> getCurrentUserAclListFromCache(){
        int userId = RequestHolder.getCurrentUser().getId();
        String cacheValue = sysCacheService.getFromCache(CacheKeyConstants.USER_ACLS,String.valueOf(userId));
        if (StringUtils.isBlank(cacheValue)){
            List<SysAcl> aclList = getCurrentuserAclList();
            if (CollectionUtils.isNotEmpty(aclList)){
                sysCacheService.saveCache(JsonMapper.object2String(aclList),600,
                        CacheKeyConstants.USER_ACLS,String.valueOf(userId));
            }
            return aclList;
        }
        return JsonMapper.string2Object(cacheValue, new TypeReference<List<SysAcl>>() {});
    }
}
