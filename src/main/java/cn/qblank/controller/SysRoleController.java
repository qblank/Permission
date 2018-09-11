package cn.qblank.controller;

import cn.qblank.common.JsonData;
import cn.qblank.model.SysUser;
import cn.qblank.param.RoleParam;
import cn.qblank.service.*;
import cn.qblank.util.StringUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author evan_qb
 * @date 2018/8/31 15:53
 */
@Controller
@RequestMapping("/sys/role")
public class SysRoleController {
    @Autowired
    private SysRoleService sysRoleService;
    @Autowired
    private SysTreeService sysTreeService;
    @Autowired
    private SysRoleAclService sysRoleAclService;
    @Autowired
    private SysRoleUserService sysRoleUserService;
    @Autowired
    private SysUserService sysUserService;

    @GetMapping("/role.page")
    public ModelAndView page(){
        return new ModelAndView("/role/role");
    }

    @PostMapping("/save.json")
    @ResponseBody
    public JsonData saveRole(RoleParam param) {
        sysRoleService.save(param);
        return JsonData.success();
    }

    @PostMapping("/update.json")
    @ResponseBody
    public JsonData updateRole(RoleParam param) {
        sysRoleService.update(param);
        return JsonData.success();
    }

    @GetMapping("/list.json")
    @ResponseBody
    public JsonData list(){
        return JsonData.success(sysRoleService.getAll());
    }

    @PostMapping("/roleTree.json")
    @ResponseBody
    public JsonData roleTree(@RequestParam("roleId") int roleId){
        return JsonData.success(sysTreeService.roleTree(roleId));
    }

    @PostMapping("/changeAcls.json")
    @ResponseBody
    public JsonData changeAcls(@RequestParam("roleId") int roleId,
                               @RequestParam(value = "aclIds",required = false,defaultValue = "") String aclIds){
        List<Integer> aclIdList = StringUtil.splitToListInt(aclIds);
        sysRoleAclService.changeRoleAcls(roleId,aclIdList);
        return JsonData.success();
    }

    @PostMapping("/changeUsers.json")
    @ResponseBody
    public JsonData changeUsers(@RequestParam("roleId") int roleId,
                               @RequestParam(value = "userIds",required = false,defaultValue = "") String userIds){
        List<Integer> userIdList = StringUtil.splitToListInt(userIds);
        sysRoleUserService.changeRoleUsers(roleId,userIdList);
        return JsonData.success();
    }

    @PostMapping("/users.json")
    @ResponseBody
    public JsonData users(@RequestParam("roleId") int roleId){
        List<SysUser> selectedUserList = sysRoleUserService.getListByRoleId(roleId);
        List<SysUser> allUserList = sysUserService.getAll();
        List<SysUser> unselectedUserList = Lists.newArrayList();
        //获取未选中的用户
        //Set<Integer> selectedUserIdSet = selectedUserList.stream().map(sysUser -> sysUser.getId()).collect(Collectors.toSet());
        Set<Integer> selectedUserIdSet = selectedUserList.stream().map(sysUser -> sysUser.getId()).collect(Collectors.toSet());
        /*Set<Integer> selectedUserIdSet = Sets.newHashSet();
        for (SysUser sysUser : selectedUserList){
            selectedUserIdSet.add(sysUser.getId());
        }*/

        for(SysUser sysUser :allUserList){
            if (sysUser.getStatus() == 1 && !selectedUserIdSet.contains(sysUser.getId())){
                unselectedUserList.add(sysUser);
            }
        }
        Map<String,List<SysUser>> map = Maps.newHashMap();
        map.put("selected",selectedUserList);
        map.put("unselected",unselectedUserList);
        return JsonData.success(map);
    }
}
