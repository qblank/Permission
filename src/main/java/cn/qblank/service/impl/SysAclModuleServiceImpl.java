package cn.qblank.service.impl;

import cn.qblank.common.RequestHolder;
import cn.qblank.dao.SysAclMapper;
import cn.qblank.dao.SysAclModuleMapper;
import cn.qblank.param.AclModuleParam;
import cn.qblank.exception.ParamException;
import cn.qblank.model.SysAclModule;
import cn.qblank.service.SysAclModuleService;
import cn.qblank.service.SysLogService;
import cn.qblank.util.BeanValidator;
import cn.qblank.util.IpUtil;
import cn.qblank.util.LevelUtil;
import com.google.common.base.Preconditions;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author evan_qb
 * @date 2018/8/29 17:04
 */
@Service
public class SysAclModuleServiceImpl implements SysAclModuleService {
    @Autowired
    private SysAclModuleMapper sysAclModuleMapper;
    @Autowired
    private SysAclMapper sysAclMapper;
    @Autowired
    private SysLogService sysLogService;


    @Override
    public void save(AclModuleParam param){
        BeanValidator.check(param);
        if (checkExist(param.getParentId(),param.getName(),param.getId())){
            throw new ParamException("同一层级下存在相同名称的权限模块");
        }
        SysAclModule aclModule = SysAclModule.builder()
                .name(param.getName())
                .parentId(param.getParentId())
                .seq(param.getSeq())
                .status(param.getStatus())
                .remark(param.getRemark()).build();
        aclModule.setLevel(LevelUtil.calculaterLevel(getLevel(param.getParentId()),param.getParentId()));
        aclModule.setOperator(RequestHolder.getCurrentUser().getUsername());
        aclModule.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        aclModule.setOperateTime(new Date());
        sysAclModuleMapper.insertSelective(aclModule);

        sysLogService.saveAclModuleLog(null,aclModule);
    }

    @Override
    public void update(AclModuleParam param){
        BeanValidator.check(param);
        if (checkExist(param.getParentId(),param.getName(),param.getId())){
            throw new ParamException("同一层级下存在相同名称的权限模块");
        }
        SysAclModule before = sysAclModuleMapper.selectByPrimaryKey(param.getId());
        Preconditions.checkNotNull(before,"待更新的权限模块不存在");

        SysAclModule after = SysAclModule.builder().
                id(param.getId())
                .name(param.getName())
                .parentId(param.getParentId())
                .seq(param.getSeq())
                .status(param.getStatus())
                .remark(param.getRemark()).build();
        after.setLevel(LevelUtil.calculaterLevel(getLevel(param.getParentId()),param.getParentId()));
        after.setOperator(RequestHolder.getCurrentUser().getUsername());
        after.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        after.setOperateTime(new Date());

        updateWithChild(before,after);

        sysLogService.saveAclModuleLog(before,after);
    }

    @Override
    public void delete(int aclModuleId) {
        SysAclModule aclModule = sysAclModuleMapper.selectByPrimaryKey(aclModuleId);
        Preconditions.checkNotNull(aclModule,"待删除的权限模块不存在,无法删除");
        if (sysAclModuleMapper.countByParentId(aclModuleId) > 0){
            throw new ParamException("当前权限模块下有子模块，无法删除");
        }
        if (sysAclMapper.countByAclModuleId(aclModuleId) > 0){
            throw new ParamException("当前权限模块下有用户，无法删除");
        }
        sysAclModuleMapper.deleteByPrimaryKey(aclModuleId);
    }

    /**
     * 和子部门一起修改
     * @param before
     * @param after
     */
    @Transactional
    public void updateWithChild(SysAclModule before,SysAclModule after){
        String newLevelPrefix = after.getLevel();
        String oldLevelPrefix = before.getLevel();
        if (!after.getLevel().equals(before.getLevel())){
            List<SysAclModule> aclModuleList = sysAclModuleMapper.getChildAclModuleListByLevel(before.getLevel());
            if (CollectionUtils.isNotEmpty(aclModuleList)){
                for (SysAclModule aclModule : aclModuleList){
                    String level = aclModule.getLevel();
                    if (level.indexOf(oldLevelPrefix) == 0){
                        level = newLevelPrefix + level.substring(oldLevelPrefix.length());
                        aclModule.setLevel(level);
                    }
                }
                sysAclModuleMapper.batchUpdateLevel(aclModuleList);
            }
        }
    }

    private boolean checkExist(Integer parentId,String aclModuleName,Integer id){
        return sysAclModuleMapper.countByNameAndParentId(parentId,aclModuleName,id) > 0;
    }

    private String getLevel(Integer aclModuleId){
        SysAclModule sysAclModule = sysAclModuleMapper.selectByPrimaryKey(aclModuleId);
        if (sysAclModule == null){
            return null;
        }
        return sysAclModule.getLevel();
    }
}
