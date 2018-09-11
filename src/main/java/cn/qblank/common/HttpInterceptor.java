package cn.qblank.common;

import cn.qblank.util.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import sun.misc.Request;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author evan_qb
 * @date 2018/8/8 16:11
 * 用于统计时间
 */
@Slf4j
public class HttpInterceptor extends HandlerInterceptorAdapter {

    private static final String START_TIME = "requestStartTime";

    /**
     * 处理前
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String url = request.getRequestURI().toString();
        Map parameterMap = request.getParameterMap();
        log.info("request start url:{},params:{}",url,JsonMapper.object2String(parameterMap));
        long startTime = System.currentTimeMillis();
        request.setAttribute(START_TIME,startTime);
        return true;
    }

    /**
     * 处理后调用（正常）
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        /*String url = request.getRequestURI().toString();
        Map parameterMap = request.getParameterMap();
        log.info("request finish url:{},params:{}",url,JsonMapper.object2String(parameterMap));*/

    }

    /**
     * 处理后调用(任何情况)
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String url = request.getRequestURI().toString();
        long start = (long) request.getAttribute(START_TIME);
        long end = System.currentTimeMillis();
        log.info("request exception url:{},cost:{}ms",url,end - start);
        removeThreadLocalInfo();
    }

    /**
     * 移除信息
     */
    public void removeThreadLocalInfo(){
        RequestHolder.remove();
    }
}
