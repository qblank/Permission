package cn.qblank.service.impl;

import cn.qblank.common.RequestHolder;
import cn.qblank.dao.SysDeptMapper;
import cn.qblank.dao.SysUserMapper;
import cn.qblank.exception.ParamException;
import cn.qblank.model.SysDept;
import cn.qblank.param.DeptParam;
import cn.qblank.service.SysDeptService;
import cn.qblank.service.SysLogService;
import cn.qblank.util.BeanValidator;
import cn.qblank.util.IpUtil;
import cn.qblank.util.LevelUtil;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Preconditions;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.common.Mapper;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

/**
 * @author evan_qb
 * @date 2018/8/8 16:47
 */
@Service
public class SysDeptServiceImpl implements SysDeptService{
    @Autowired
    private SysDeptMapper mapper;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysLogService sysLogService;

    @Override
    public void save(DeptParam param){
        BeanValidator.check(param);
        if (checkExist(param.getParentId(),param.getName(),param.getId())){
            throw new ParamException("同一层级下存在下相同名称的部门");
        }
        SysDept dept = SysDept.builder().name(param.getName()).parentId(param.getParentId())
                .seq(param.getSeq()).remark(param.getRemark()).build();
        dept.setLevel(LevelUtil.calculaterLevel(getLevel(param.getParentId()),param.getParentId()));
        dept.setOperator(RequestHolder.getCurrentUser().getUsername());
        dept.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        dept.setOperateTime(new Date());
        mapper.insertSelective(dept);
        sysLogService.saveDeptLog(null,dept);
    }

    @Override
    public void update(DeptParam param) {
        BeanValidator.check(param);
        if (checkExist(param.getParentId(),param.getName(),param.getId())){
            throw new ParamException("同一层级下存在相同名称的部门");
        }
        SysDept before = mapper.selectByPrimaryKey(param.getId());
        Preconditions.checkNotNull(before,"待更新的部门不存在");
        SysDept after = SysDept.builder().id(param.getId()).name(param.getName()).parentId(param.getParentId())
                .seq(param.getSeq()).remark(param.getRemark()).build();

        after.setLevel(LevelUtil.calculaterLevel(getLevel(param.getParentId()),param.getParentId()));
        after.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        after.setOperator(RequestHolder.getCurrentUser().getUsername());
        after.setOperateTime(new Date());
        //更新
        updateWithChild(before,after);
        sysLogService.saveDeptLog(before,after);
    }

    @Override
    public void deleteById(Serializable id) {

    }

    @Override
    public void delete(int id) {
        SysDept dept = mapper.selectByPrimaryKey(id);
        Preconditions.checkNotNull(dept,"待删除的部门不存在，无法删除");
        if (mapper.countByParentId(dept.getId()) > 0){
            throw new ParamException("当前部门下有子部门，无法删除");
        }
        if (sysUserMapper.countByDeptId(dept.getId()) > 0){
            throw new ParamException("当前部门下面有用户，无法删除");
        }
        mapper.deleteByPrimaryKey(id);
    }

    @Transactional
    public void updateWithChild(SysDept before,SysDept after){
        String newLevelPrefix = after.getLevel();
        String oldLevelPrefix = before.getLevel();
        if (!after.getLevel().equals(before.getLevel())){
            List<SysDept> deptList = mapper.getChildDeptListByLevel(before.getLevel());
            if (CollectionUtils.isNotEmpty(deptList)){
                for (SysDept dept : deptList){
                    String level = dept.getLevel();
                    if (level.indexOf(oldLevelPrefix) == 0){
                        level = newLevelPrefix + level.substring(oldLevelPrefix.length());
                        dept.setLevel(level);
                    }
                }
                mapper.batchUpdateLevel(deptList);
            }
        }
        mapper.updateByPrimaryKeySelective(after);
    }

    @Override
    public DeptParam getById(Serializable id) {
        return null;
    }

    @Override
    public List<DeptParam> getList() {
        return null;
    }

    @Override
    public PageInfo<DeptParam> getListByPage(int currentNum, int pageSize) {
        return null;
    }

    private boolean checkExist(Integer parentId,String deptName,Integer deptId){
        return mapper.countByNameAndParentId(parentId,deptName,deptId) > 0;
    }

    /**
     * 获取层级
     * @param deptId
     * @return
     */
    private String getLevel(Integer deptId){
        SysDept dept = mapper.selectByPrimaryKey(deptId);
        if (dept == null){
            return null;
        }
        return dept.getLevel();
    }

}
