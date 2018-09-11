package cn.qblank.dto;

import cn.qblank.model.SysAcl;
import lombok.Data;
import lombok.ToString;
import org.springframework.beans.BeanUtils;

import java.security.acl.Acl;

/**
 * @author evan_qb
 * @date 2018/8/31 16:48
 */
@Data
@ToString
public class AclDto extends SysAcl {
    //是否默认选中
    private boolean checked = false;
    //是否有权限操作
    private boolean hasAcl = false;

    public static AclDto adapt(SysAcl acl){
        AclDto dto = new AclDto();
        BeanUtils.copyProperties(acl,dto);
        return dto;
    }
}
