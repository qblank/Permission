package cn.qblank.controller;

import cn.qblank.common.JsonData;
import cn.qblank.exception.ParamException;
import cn.qblank.exception.PermissionException;
import cn.qblank.model.SysUser;
import cn.qblank.param.TestVo;
import cn.qblank.service.SysUserService;
import cn.qblank.util.BeanValidator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

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

    @GetMapping("/hello.json")
    @ResponseBody
    public JsonData hello(){
        log.info("hello");
        throw new RuntimeException("test Exception");
        //return JsonData.success("hello world");
    }

    @GetMapping("/validate.json")
    @ResponseBody
    public JsonData validate(TestVo vo) throws ParamException{
        log.info("validate");
        BeanValidator.check(vo);
        return JsonData.success("test validate");
    }

    @RequestMapping("/findUser")
    @ResponseBody
    public SysUser findUser(@RequestParam("username") String username){
        log.info("测试");
        return userService.findUserByUserName(username);
    }

}
