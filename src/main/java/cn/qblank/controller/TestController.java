package cn.qblank.controller;

import cn.qblank.model.SysUser;
import cn.qblank.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author evan_qb
 * @date 2018/8/6 19:20
 */
@Controller
@RequestMapping("/test")
@Slf4j
public class TestController {

    @Autowired
    private SysUserService userService;

    @RequestMapping("/index")
    public String hello(){
        log.info("hello");
        return "index";
    }

    @RequestMapping("/findUser")
    @ResponseBody
    public SysUser findUser(@RequestParam("username") String username){
        log.info("测试");
        return userService.findUserByUserName(username);
    }

}
