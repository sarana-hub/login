package hello.login;

import hello.login.web.argumentresolver.LoginMemberArgumentResolver;
import hello.login.web.filter.LogFilter;
import hello.login.web.filter.LoginCheckFilter;
import hello.login.web.interceptor.LogInterceptor;
import hello.login.web.interceptor.LoginCheckInterceptor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.Filter;
import java.util.List;

/** 필터 설정*/

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberArgumentResolver());
    }

    /*addInterceptors()를 사용해서 인터셉터를 등록*/
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LogInterceptor())   //인터셉터를 등록
                .order(1)   //인터셉터의 호출 순서를 지정
                .addPathPatterns("/**")     //인터셉터를 적용할 URL패턴을 지정
                .excludePathPatterns("/css/**", "/*.ico", "/error");    //인터셉터에서 제외할 패턴을 지정

        registry.addInterceptor(new LoginCheckInterceptor())
                .order(2)
                .addPathPatterns("/**")
                .excludePathPatterns("/", "/members/add", "/login", "/logout",
                        "/css/**", "/*.ico", "/error");
    }


    /*FilterRegistrationBean을 사용해서 필터를 등록*/
    //@Bean    //인터셉터와 필터가 중복되지 않도록 주석처리
    public FilterRegistrationBean logFilter() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new LogFilter());      //필터를 등록
        filterRegistrationBean.setOrder(1);     //필터는 체인으로 동작하므로, 순서가 필요

        //필터를 적용할 URL패턴을 지정
        filterRegistrationBean.addUrlPatterns("/*");    //모든 요청에 로그인 필터를 적용

        return filterRegistrationBean;
    }

    //@Bean
    public FilterRegistrationBean loginCheckFilter() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new LoginCheckFilter());
        filterRegistrationBean.setOrder(2);     //순서 2번
        filterRegistrationBean.addUrlPatterns("/*");

        return filterRegistrationBean;
    }
}
