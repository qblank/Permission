package cn.qblank.controller;

import cn.qblank.common.JsonData;
import cn.qblank.dto.AclModuleLevelDto;
import cn.qblank.param.AclModuleParam;
import cn.qblank.service.SysAclModuleService;
import cn.qblank.service.SysTreeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * @author evan_qb
 * @date 2018/8/29 16:51
 */
@Controller
@RequestMapping("/sys/aclModule")
@Slf4j
public class SysAclModuleController {

    @Autowired
    private SysAclModuleService sysAclModuleService;
    @Autowired
    private SysTreeService sysTreeService;

    @GetMapping("/acl.page")
    @ResponseBody
    public ModelAndView page(){
        return new ModelAndView("/acl/acl");
    }

    @PostMapping("/save.json")
    @ResponseBody
    public JsonData saveAclModule(AclModuleParam param){
        sysAclModuleService.save(param);
        return JsonData.success();
    }

    @PostMapping("/update.json")
    @ResponseBody
    public JsonData updateAclModule(AclModuleParam param){
        sysAclModuleService.update(param);
        return JsonData.success();
    }

    @GetMapping("/tree.json")
    @ResponseBody
    public JsonData tree(){
        List<AclModuleLevelDto> aclModuleTree = sysTreeService.aclModuleTree();
        return JsonData.success(aclModuleTree);
    }

    @PostMapping("/delete.json")
    @ResponseBody
    public JsonData delete(@RequestParam("id") int id){
        sysAclModuleService.delete(id);
        return JsonData.success();
    }
}
