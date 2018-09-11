package cn.qblank.filter;

import cn.qblank.common.ApplicationContextHelper;
import cn.qblank.common.JsonData;
import cn.qblank.common.RequestHolder;
import cn.qblank.model.SysUser;
import cn.qblank.service.SysCoreService;
import cn.qblank.util.JsonMapper;
import com.google.common.base.Splitter;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * @author evan_qb
 * @date 2018/9/10
 * 权限拦截器
 */
@Slf4j
public class AclControlFilter implements Filter {
    private static Set<String> exclusionUrlSet = new ConcurrentSkipListSet<>();

    private final static String noAuthUrl = "/sys/user/noAuth.page";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String exclusionUrls = filterConfig.getInitParameter("exclusionUrls");
        List<String> exclusionUrlList = Splitter.on(",").trimResults().omitEmptyStrings().splitToList(exclusionUrls);
        exclusionUrlSet = Sets.newConcurrentHashSet(exclusionUrlList);
        exclusionUrlSet.add(noAuthUrl);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        String servletPath = req.getServletPath();
        Map requestMap = req.getParameterMap();
        if (exclusionUrlSet.contains(servletPath)){
            chain.doFilter(request,response);
            return;
        }
        SysUser sysUser = RequestHolder.getCurrentUser();
        if (sysUser == null){
            log.info("visit {} , but not login,parameter: {}",servletPath,JsonMapper.object2String(requestMap));
            noAuth(req,resp);
            return;
        }

        SysCoreService sysCoreService = ApplicationContextHelper.popBean(SysCoreService.class);
        if (!sysCoreService.hasUrlAcl(servletPath)){
            log.info("{} visit {} ,but not login,param: {}",JsonMapper.object2String(sysUser),servletPath,JsonMapper.object2String(requestMap));
            noAuth(req,resp);
            return;
        }

        chain.doFilter(request,response);
    }

    /**
     * 无权限访问处理
     * @param request
     * @param response
     */
    private void noAuth(HttpServletRequest request,HttpServletResponse response) throws IOException{
        String servletPath = request.getServletPath();
        if (servletPath.endsWith(".json")){
            JsonData jsonData = JsonData.fail("没有访问权限，如需访问，请联系管理员");
            response.setHeader("Context-Type","application/json");
            response.getWriter().print(JsonMapper.object2String(jsonData));
        }else{
            clientRedirect(noAuthUrl,response);
        }
        return;
    }

    /**
     * 跳转页面
     * @param url
     * @param response
     * @throws IOException
     */
    private void clientRedirect(String url,HttpServletResponse response) throws IOException{
        response.setHeader("Context-Type","application/json");
        response.getWriter().print("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n"
                + "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" + "<head>\n" + "<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\"/>\n"
                + "<title>跳转中...</title>\n" + "</head>\n" + "<body>\n" + "跳转中，请稍候...\n" + "<script type=\"text/javascript\">//<![CDATA[\n"
                + "window.location.href='" + url + "?ret='+encodeURIComponent(window.location.href);\n" + "//]]></script>\n" + "</body>\n" + "</html>\n");
    }

    @Override
    public void destroy() {

    }
}
