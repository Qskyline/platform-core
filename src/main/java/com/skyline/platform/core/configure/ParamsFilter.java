package com.skyline.platform.core.configure;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @ClassName qqq
 * @Description TODO
 * @Author skyline
 * @Date 2018/12/18 19:21
 * Version 1.0
 **/
@WebFilter(urlPatterns = "/*", filterName = "ParamsFilter",dispatcherTypes= DispatcherType.REQUEST)
public class ParamsFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        ParameterRequestWrapper parmsRequest = new ParameterRequestWrapper((HttpServletRequest)servletRequest);
        filterChain.doFilter(parmsRequest, servletResponse);
    }

    @Override
    public void destroy() {
    }
}
