package hello.login.web.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;
/**요청 로그 인터셉터*/

@Slf4j
public class LogInterceptor implements HandlerInterceptor {

    public static final String LOG_ID = "logId";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        String uuid = UUID.randomUUID().toString();     //요청 로그를 구분하기 위한 uuid를 생성

        request.setAttribute(LOG_ID, uuid);
        //preHandle에서 지정한 값을 postHandle,afterCompletion에서 함께 사용하기위해, request에 담아두었다


        //@Controller,@RequestMapping을 활용한 핸들러매핑을 사용하는 경우, 핸들러정보로 HandlerMethod가 넘어온다
        // /resources/static같은 정적 리소스가 호출되는 경우, 핸들러 정보로 ResourceHttpRequestHandler가 넘어온다
        if (handler instanceof HandlerMethod) {
            HandlerMethod hm = (HandlerMethod) handler; //호출할 컨트롤러 메서드의 모든 정보가 포함
        }
        log.info("REQUEST [{}][{}][{}]", uuid, requestURI, handler);

        return true;    //true이면 인터셉터나 트롤러가 호출된다 (false이면 진행X)
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("postHandle [{}]", modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String requestURI = request.getRequestURI();
        String logId = (String)request.getAttribute(LOG_ID);
        log.info("RESPONSE [{}][{}]", logId, requestURI);
        if (ex != null) {
            log.error("afterCompletion error!!", ex);
        }
    }
}
