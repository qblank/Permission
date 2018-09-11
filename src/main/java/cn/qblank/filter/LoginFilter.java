package cn.qblank.filter;


import cn.qblank.common.RequestHolder;
import cn.qblank.model.SysUser;
import org.springframework.web.util.WebUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author evan_qb
 * @date 2018/8/29 15:29
 */
public class LoginFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        String path = req.getServletPath();

        SysUser sysUser = (SysUser) WebUtils.getSessionAttribute(req,"user");
        if (sysUser == null){
            resp.sendRedirect("/signin.jsp");
            return;
        }
        RequestHolder.add(sysUser);
        RequestHolder.add(req);
        chain.doFilter(request,response);
        return;
    }

    @Override
    public void destroy() {

    }
}
