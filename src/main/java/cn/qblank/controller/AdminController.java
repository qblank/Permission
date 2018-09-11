package cn.qblank.controller;

import cn.qblank.common.RequestHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

/**
 * @author evan_qb
 * @date 2018/8/27 14:18
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("index.page")
    public ModelAndView index(Map<String,Object> map){
        return new ModelAndView("admin/admin");
    }
}
