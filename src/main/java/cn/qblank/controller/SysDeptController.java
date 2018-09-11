package cn.qblank.controller;

import cn.qblank.common.JsonData;
import cn.qblank.dto.DeptLevelDto;
import cn.qblank.param.DeptParam;
import cn.qblank.service.SysDeptService;
import cn.qblank.service.SysTreeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * @author evan_qb
 * @date 2018/8/8 16:43
 */
@Controller
@RequestMapping("/sys/dept")
@Slf4j
public class SysDeptController {
    @Autowired
    private SysDeptService sysDeptService;

    @Autowired
    private SysTreeService sysTreeService;


    @PostMapping("/save.json")
    @ResponseBody
    public JsonData saveDept(DeptParam param){
        sysDeptService.save(param);
        return JsonData.success();
    }

    @GetMapping("/tree.json")
    @ResponseBody
    public JsonData deptTree(){
        List<DeptLevelDto> dtoList = sysTreeService.deptTree();
        return JsonData.success(dtoList);
    }

    @PostMapping("/update.json")
    @ResponseBody
    public JsonData updateDept(DeptParam param){
        sysDeptService.update(param);
        return JsonData.success();
    }

    @PostMapping("/delete.json")
    @ResponseBody
    public JsonData deleteDept(Integer id){
        sysDeptService.delete(id);
        return JsonData.success();
    }

    @GetMapping("/list.page")
    public ModelAndView deptList(){
        return new ModelAndView("dept/list");
    }

}
