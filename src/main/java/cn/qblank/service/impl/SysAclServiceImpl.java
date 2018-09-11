package cn.qblank.service.impl;

import cn.qblank.beans.PageQuery;
import cn.qblank.beans.PageResult;
import cn.qblank.common.RequestHolder;
import cn.qblank.dao.SysAclMapper;
import cn.qblank.exception.ParamException;
import cn.qblank.model.SysAcl;
import cn.qblank.param.AclParam;
import cn.qblank.service.SysAclService;
import cn.qblank.service.SysLogService;
import cn.qblank.util.BeanValidator;
import cn.qblank.util.IpUtil;
import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author evan_qb
 * @date 2018/8/31 15:00
 */
@Service
public class SysAclServiceImpl implements SysAclService {
    @Autowired
    private SysAclMapper sysAclMapper;
    @Autowired
    private SysLogService sysLogService;

    @Override
    public void save(AclParam param) {
        BeanValidator.check(param);
        if (checkExist(param.getAclModuleId(), param.getName(), param.getId())) {
            throw new ParamException("当前权限模块下面存在相同名称的权限点");
        }
        SysAcl acl = SysAcl.builder().name(param.getName()).aclModuleId(param.getAclModuleId()).url(param.getUrl())
                .type(param.getType()).status(param.getStatus()).seq(param.getSeq()).remark(param.getRemark()).build();
        acl.setCode(generateCode());
        acl.setOperator(RequestHolder.getCurrentUser().getUsername());
        acl.setOperateTime(new Date());
        acl.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        //进行插入
        sysAclMapper.insertSelective(acl);

        sysLogService.saveAclLog(null,acl);
    }

    @Override
    public void update(AclParam param) {
        BeanValidator.check(param);
        if (checkExist(param.getAclModuleId(),param.getName(),param.getId())){
            throw new ParamException("当前权限模块下面存在相同名称的权限点");
        }
        SysAcl before = sysAclMapper.selectByPrimaryKey(param.getId());
        //判断是否为空
        Preconditions.checkNotNull(before,"待更新的权限点不存在");
        SysAcl after = SysAcl.builder().id(param.getId()).name(param.getName()).aclModuleId(param.getAclModuleId()).url(param.getUrl())
                .type(param.getType()).status(param.getStatus()).seq(param.getSeq()).remark(param.getRemark()).build();
        after.setOperator(RequestHolder.getCurrentUser().getUsername());
        after.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        after.setOperateTime(new Date());
        //更新操作
        sysAclMapper.updateByPrimaryKeySelective(after);

        sysLogService.saveAclLog(before,after);
    }

    @Override
    public PageResult<SysAcl> getPageByAclModuleId(int aclModuleId, PageQuery page) {
        BeanValidator.check(page);
        int count = sysAclMapper.countByAclModuleId(aclModuleId);
        if (count > 0){
            List<SysAcl> aclList = sysAclMapper.getPageByAclModuleId(aclModuleId,page);
            return PageResult.<SysAcl>builder().data(aclList).total(count).build();
        }
        return PageResult.<SysAcl>builder().build();
    }

    public boolean checkExist(int aclModuleId, String name, Integer id) {
        return sysAclMapper.countByNameAndAclModuleId(aclModuleId, name, id) > 0;
    }

    public String generateCode() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return dateFormat.format(new Date()) + "_" + (int)(Math.random() * 100);
    }


}
