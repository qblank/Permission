package cn.qblank.controller;

import cn.qblank.model.SysUser;
import cn.qblank.service.SysUserService;
import cn.qblank.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author evan_qb
 * @date 2018/8/27 10:29
 */
@Controller
public class UserController {
    @Autowired
    private SysUserService sysUserService;

    /**
     * 登录
     * @param request
     * @param response
     * @throws Exception
     */
    @PostMapping("/login.page")
    public void login(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        SysUser sysUser = sysUserService.findByKeyword(username);
        String errorMsg = "";
        //跳转到原来的链接
        String ret = request.getParameter("ret");
        if (StringUtils.isBlank(username)){
            errorMsg = "用户名不能为空";
        }else if (StringUtils.isBlank(password)){
            errorMsg = "密码不能为空";
        }else if (sysUser == null){
            errorMsg = "用户不存在";
        }else if (!sysUser.getPassword().equals(MD5Util.encrypt(password))){
            errorMsg = "用户名或密码错误";
        }else if (sysUser.getStatus() != 1){
            errorMsg = "用户已被冻结，请联系管理员";
        }else{
            //login success
            WebUtils.setSessionAttribute(request,"user",sysUser);
            if (StringUtils.isNotBlank(ret)){
                response.sendRedirect(ret);
            }else{
                response.sendRedirect("/admin/index.page");
                return;
            }
        }

        request.setAttribute("error",errorMsg);
        request.setAttribute("username",username);
        if (StringUtils.isNotBlank(ret)){
            request.setAttribute("ret",ret);
        }
        String path = "signin.jsp";
        request.getRequestDispatcher(path).forward(request,response);
    }

    @GetMapping("/logout.page")
    public void logout(HttpServletRequest request,HttpServletResponse response) throws IOException {
        request.getSession().invalidate();
        String path = "signin.jsp";
        response.sendRedirect(path);
    }
}
