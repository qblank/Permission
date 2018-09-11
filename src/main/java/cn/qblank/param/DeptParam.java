package cn.qblank.param;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * @author evan_qb
 * @date 2018/8/8 16:39
 */
@Data
public class DeptParam {
    private Integer id;
    @NotBlank(message = "部门名称不能为空")
    @Length(max = 15,min = 2,message = "部门名称需要在2~15之间")
    private String name;

    private Integer parentId = 0;
    @NotNull(message = "展示顺序不能为空")
    private Integer seq;
    @Length(max = 150,message = "备注长度不能超过150")
    private String remark;
}
