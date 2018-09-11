package cn.qblank.dto;

import cn.qblank.model.SysAclModule;
import com.google.common.collect.Lists;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * @author evan_qb
 * @date 2018/8/30 14:24
 */
@Data
public class AclModuleLevelDto extends SysAclModule {
    private List<AclModuleLevelDto> aclModuleList = Lists.newArrayList();

    private List<AclDto> aclList = Lists.newArrayList();

    public static AclModuleLevelDto adapt(SysAclModule aclModule){
        AclModuleLevelDto dto = new AclModuleLevelDto();
        BeanUtils.copyProperties(aclModule,dto);
        return dto;
    }
}
