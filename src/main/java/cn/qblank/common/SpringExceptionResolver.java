package cn.qblank.common;

import cn.qblank.exception.ParamException;
import cn.qblank.exception.PermissionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author evan_qb
 * @date 2018/8/8 10:25
 */
@Slf4j
public class SpringExceptionResolver implements HandlerExceptionResolver {

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object o, Exception e) {
        String url = request.getRequestURI().toString();
        ModelAndView mv = null;
        String defaultMsg = "System error";
        //json请求或者页面请求 json数据以.json结尾，请求页面以.page结尾
        if (url.endsWith(".json")){
            if (e instanceof PermissionException || e instanceof ParamException){
                JsonData result = JsonData.fail(e.getMessage());
                mv = new ModelAndView("jsonView",result.toMap());
            }else {
                log.error("unknow json exception,url:",url,e);
                JsonData result = JsonData.fail(defaultMsg);
                mv = new ModelAndView("jsonView",result.toMap());
            }
        }else if (url.endsWith(".page")){
            log.error("unknow page exception,url:",url,e);
            JsonData result = JsonData.fail(defaultMsg);
            mv = new ModelAndView("exception",result.toMap());
        }else{
            log.error("unknow page exception,url:",url,e);
            JsonData result = JsonData.fail(defaultMsg);
            mv = new ModelAndView("jsonView",result.toMap());
        }
        return mv;
    }
}
