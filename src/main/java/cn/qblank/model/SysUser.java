package cn.qblank.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * @author evan_qb
 * @date 2018/8/7 16:23
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SysUser {
    @Id
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
    private Date operateTime;
}
