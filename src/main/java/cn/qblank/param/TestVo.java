package cn.qblank.param;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author evan_qb
 * @date 2018/8/8 13:18
 */
@Data
public class TestVo {
    @NotBlank(message = "msg不能为空值")
    private String msg;
    @NotNull(message = "编号不能为空")
    @Min(value = 0,message = "最小值不能小于0")
    @Max(value = 10,message = "最大值不能超过10")
    private Integer id;

    private List<String> str;
}
