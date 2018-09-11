package cn.qblank.dto;

import cn.qblank.model.SysDept;
import cn.qblank.param.DeptParam;
import com.google.common.collect.Lists;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * @author evan_qb
 * @date 2018/8/8 17:28
 */
@Data
public class DeptLevelDto extends SysDept {
    private List<DeptLevelDto> deptList = Lists.newArrayList();

    /**
     * 创建DeptLevel对象并通过Dept对象获取对应属性
     * @param dept
     * @return
     */
    public static DeptLevelDto adapt(SysDept dept){
        DeptLevelDto dto = new DeptLevelDto();
        BeanUtils.copyProperties(dept,dto);
        return dto;
    }
}
