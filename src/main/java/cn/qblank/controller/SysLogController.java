package cn.qblank.controller;

import cn.qblank.beans.PageQuery;
import cn.qblank.common.JsonData;
import cn.qblank.param.SearchLogParam;
import cn.qblank.service.SysLogService;
import cn.qblank.util.JsonMapper;
import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author evan_qb
 * @date 2018/9/11
 */
@Controller
@RequestMapping("/sys/log")
public class SysLogController {
    @Autowired
    private SysLogService sysLogService;

    @GetMapping("/log.page")
    public ModelAndView page(){
        return new ModelAndView("log/log");
    }

    @GetMapping("/recover.json")
    @ResponseBody
    public JsonData recover(@RequestParam("id") int id){
        sysLogService.recover(id);
        return JsonData.success();
    }

    @RequestMapping("/page.json")
    @ResponseBody
    public JsonData searchPage(SearchLogParam param, PageQuery page){
        return JsonData.success(sysLogService.searchPageList(param,page));
    }
}
