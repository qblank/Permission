package cn.qblank.service.impl;

import cn.qblank.beans.Mail;
import cn.qblank.beans.PageQuery;
import cn.qblank.beans.PageResult;
import cn.qblank.common.RequestHolder;
import cn.qblank.dao.SysUserMapper;
import cn.qblank.dao.UserMapper;
import cn.qblank.exception.ParamException;
import cn.qblank.model.SysUser;
import cn.qblank.param.UserParam;
import cn.qblank.service.SysLogService;
import cn.qblank.service.SysUserService;
import cn.qblank.util.*;
import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author evan_qb
 * @date 2018/8/7 15:31
 */
@Service
public class SysUserServiceImpl extends BaseServiceImpl<SysUser> implements SysUserService {
    @Autowired
    private UserMapper mapper;

    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysLogService sysLogService;

    /**
     * 添加用户
     * @param param
     */
    public void save(UserParam param){
        BeanValidator.check(param);
        if (checkTelephoneExist(param.getTelephone(),param.getId())){
            throw new ParamException("电话已被占用");
        }
        if (checkEmailExist(param.getMail(),param.getId())){
            throw new ParamException("邮箱已被占用");
        }
        String password = PasswordUtil.randomPassword();
        //TODO:通过发送邮件获取密码
        /*Set<String> receivers = new HashSet();  //TODO 测试
        receivers.add("798046397@qq.com");
        MailUtil.send(Mail.builder().
                subject("你的密码是: " + password).
                message("这是一个真实的信息")
                .receivers(receivers)
                .build());*/
        password = "123456";
        //MD5加密
        String encryptedPassword = MD5Util.encrypt(password);

        SysUser sysUser = SysUser.builder()
                .username(param.getUsername())
                .telephone(param.getTelephone())
                .mail(param.getMail())
                .password(encryptedPassword)
                .deptId(param.getDeptId())
                .status(param.getStatus())
                .remark(param.getRemark()).build();
        sysUser.setOperator(RequestHolder.getCurrentUser().getUsername());
        sysUser.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        sysUser.setOperateTime(new Date());
        // TODO: 发送邮件
        mapper.insertSelective(sysUser);

        sysLogService.saveUserLog(null,sysUser);
    }

    @Override
    public SysUser findByKeyword(String keyword) {
        return sysUserMapper.findByKeyword(keyword);
    }

    @Override
    public PageResult<SysUser> getPageByDeptId(int deptId, PageQuery page) {
        BeanValidator.check(page);
        int count = sysUserMapper.countByDeptId(deptId);
        if (count > 0){
            List<SysUser> list = sysUserMapper.getPageByDeptId(deptId,page);
            return PageResult.<SysUser>builder().total(count).data(list).build();
        }
        return PageResult.<SysUser>builder().build();
    }

    @Override
    public List<SysUser> getAll() {
        return sysUserMapper.getAll();
    }

    /**
     * 修改用户
     * @param param
     */
    @Override
    public void update(UserParam param){
        BeanValidator.check(param);
        if (checkTelephoneExist(param.getTelephone(),param.getId())){
            throw new ParamException("电话已被占用");
        }
        if (checkEmailExist(param.getMail(),param.getId())){
            throw new ParamException("邮箱已被占用");
        }
        SysUser before = mapper.selectByPrimaryKey(param.getId());
        Preconditions.checkNotNull(before,"待更新的用户不存在");
        SysUser sysUser = SysUser.builder()
                .id(param.getId())
                .username(param.getUsername())
                .telephone(param.getTelephone())
                .mail(param.getMail())
                .password(before.getPassword())
                .deptId(param.getDeptId())
                .status(param.getStatus())
                .remark(param.getRemark()).build();
        sysUser.setOperator(RequestHolder.getCurrentUser().getUsername());
        sysUser.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        sysUser.setOperateTime(new Date());
        //执行更新操作
        mapper.updateByPrimaryKeySelective(sysUser);

        sysLogService.saveUserLog(before,sysUser);
    }

    public boolean checkEmailExist(String mail,Integer userId){
        return sysUserMapper.countByMail(mail,userId) > 0;
    }

    public boolean checkTelephoneExist(String telephone,Integer userId){
        return sysUserMapper.countByTelephone(telephone,userId) > 0;
    }

    @Override
    public SysUser findUserByUserName(String username) {
        return this.mapper.getUserByUserName(username);
    }

    @Override
    protected Mapper<SysUser> getMapper() {
        return this.mapper;
    }
}
