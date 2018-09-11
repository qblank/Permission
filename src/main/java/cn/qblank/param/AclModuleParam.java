package cn.qblank.param;

import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author evan_qb
 * @date 2018/8/29 16:55
 */
@Data
@ToString
public class AclModuleParam {
    private Integer id;

    @NotBlank(message = "权限模块名称不能为空")
    @Length(min = 2,max = 20,message = "权限名称需要在2~64位之间")
    private String name;

    private Integer parentId = 0;

    @NotNull(message = "权限模块展示顺序不能为空")
    private Integer seq;

    @NotNull(message = "权限模块状态不能为空")
    @Min(value = 0,message = "权限模块状态不合法")
    @Max(value = 2,message = "权限模块状态不合法")
    private Integer status;

    @Length(max = 200,message = "权限模块备注需要在64个字符以内")
    private String remark;
}
