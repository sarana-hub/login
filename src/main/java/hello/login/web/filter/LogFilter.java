package hello.login.web.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;
/**모든 요청을 로그로 남기는 필터*/

@Slf4j
public class LogFilter implements Filter {      //필터를 사용하려면 필터 인터페이스를 구현해야함
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("log filter init");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;  //HTTP를 사용하므로
        String requestURI = httpRequest.getRequestURI();

        String uuid = UUID.randomUUID().toString();     //HTTP 요청을 구분하기 위해 요청당 임의의 uuid를 생성

        try {
            log.info("REQUEST [{}][{}]", uuid, requestURI);     //uuid와 requestURI를 출력
            chain.doFilter(request, response); //다음 필터가 있으면 필터를 호출하고, 필터가 없으면 서블릿을 호출
        } catch (Exception e) {
            throw e;
        } finally {
            log.info("RESPONSE [{}][{}]", uuid, requestURI);
        }
    }

    @Override
    public void destroy() {
        log.info("log filter destroy");
    }
}
