package cn.qblank.model;

import lombok.Data;

import javax.persistence.Entity;

/**
 * @author evan_qb
 * @date 2018/8/7 16:23
 */
@Data
@Entity(name = "sys_user")
public class SysUser {
    private Integer id;
    private String username;
    private String telephone;
    private String mail;
    private String password;
    private Integer deptId;
    private Integer status;
    private String remark;
    private String operator;
    private String operateIp;
}
