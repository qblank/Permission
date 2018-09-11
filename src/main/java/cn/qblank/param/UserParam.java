package cn.qblank.param;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author evan_qb
 * @date 2018/8/24 15:25
 */
@Data
public class UserParam {
    private Integer id;
    @NotBlank(message = "用户名不能为空")
    @Length(min = 0,max = 20,message = "用户名长度需要在20个字以内")
    private String username;
    @NotBlank
    @Length(min = 3,max = 13,message = "电话长度需要在13个数字以内")
    private String telephone;
    @NotBlank(message = "邮箱不能为空")
    @Length(min = 5,max = 50,message = "邮箱长度范围5~50之间")
    private String mail;
    @NotNull(message = "必须提供用户所在部门")
    private Integer deptId;
    @NotNull(message = "状态不能为空")
    @Min(value = 0,message = "状态不合法")
    @Max(value = 2,message = "状态不合法")
    private Integer status;
    @Length(min = 0,max = 200,message = "备注长度需要在200字以内")
    private String remark;
}
