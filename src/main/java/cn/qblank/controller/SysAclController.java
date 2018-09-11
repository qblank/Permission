package cn.qblank.controller;

import cn.qblank.beans.PageQuery;
import cn.qblank.beans.PageResult;
import cn.qblank.common.JsonData;
import cn.qblank.model.SysAcl;
import cn.qblank.model.SysRole;
import cn.qblank.param.AclParam;
import cn.qblank.service.SysAclService;
import cn.qblank.service.SysRoleService;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author evan_qb
 * @date 2018/8/29 16:51
 */
@Controller
@RequestMapping("/sys/acl")
public class SysAclController {
    @Autowired
    private SysAclService sysAclService;
    @Autowired
    private SysRoleService sysRoleService;


    @PostMapping("/save.json")
    @ResponseBody
    public JsonData saveAclModule(AclParam param) {
        sysAclService.save(param);
        return JsonData.success();
    }

    @PostMapping("/update.json")
    @ResponseBody
    public JsonData updateAclModule(AclParam param) {
        sysAclService.update(param);
        return JsonData.success();
    }

    @GetMapping("/page.json")
    @ResponseBody
    public JsonData list(@RequestParam Integer aclModuleId, PageQuery pageQuery){
        return JsonData.success(sysAclService.getPageByAclModuleId(aclModuleId, pageQuery));
    }

    @GetMapping("/acls.json")
    @ResponseBody
    public JsonData acls(@RequestParam("aclId") int aclId){
        Map<String,Object> map = Maps.newHashMap();
        List<SysRole> roleList = sysRoleService.getRoleListByAclId(aclId);
        map.put("roles",roleList);
        map.put("users",sysRoleService.getUserListByRoleList(roleList));
        return JsonData.success(map);
    }
}
