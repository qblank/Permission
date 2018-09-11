package cn.qblank.controller;

import cn.qblank.beans.PageQuery;
import cn.qblank.beans.PageResult;
import cn.qblank.common.JsonData;
import cn.qblank.model.SysUser;
import cn.qblank.param.UserParam;
import cn.qblank.service.SysRoleService;
import cn.qblank.service.SysTreeService;
import cn.qblank.service.SysUserService;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

/**
 * @author evan_qb
 * @date 2018/8/24 15:24
 */
@Controller
@RequestMapping("sys/user")
public class SysUserController {
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysTreeService sysTreeService;
    @Autowired
    private SysRoleService sysRoleService;

    @GetMapping("/noAuth.page")
    public ModelAndView noAuth(){
        return new ModelAndView("noAuth");
    }


    @PostMapping("/save.json")
    @ResponseBody
    public JsonData saveUser(UserParam param){
        sysUserService.save(param);
        return JsonData.success();
    }

    @PostMapping("/update.json")
    @ResponseBody
    public JsonData updateUser(UserParam param){
        sysUserService.update(param);
        return JsonData.success();
    }

    @PostMapping("/delete.json")
    @ResponseBody
    public JsonData deleteUser(UserParam param){
        Integer userId = param.getId();
        //查询编号
        SysUser result = sysUserService.getById(userId);
        if (result == null){
            return JsonData.fail("用户id不存在");
        }
        sysUserService.deleteById(userId);
        return JsonData.success();
    }

    @GetMapping("/page.json")
    @ResponseBody
    public JsonData page(@RequestParam("deptId") int deptId, PageQuery pageQuery){
        PageResult<SysUser> page = sysUserService.getPageByDeptId(deptId, pageQuery);
        return JsonData.success(page);
    }

    @GetMapping("/acls.json")
    @ResponseBody
    public JsonData acls(@RequestParam("userId") int userId){
        Map<String,Object> map = Maps.newHashMap();
        //存取用户的所有权限
        map.put("acls",sysTreeService.userAclTree(userId));
        map.put("roles",sysRoleService.getRoleListByUserId(userId));
        return JsonData.success(map);
    }
}
