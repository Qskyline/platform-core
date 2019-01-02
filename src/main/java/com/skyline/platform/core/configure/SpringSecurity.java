package com.skyline.platform.core.configure;

import com.skyline.platform.core.springsecurity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.RememberMeAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationFilter;
import org.springframework.security.web.authentication.session.*;
import org.springframework.security.web.session.ConcurrentSessionFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.session.SimpleRedirectSessionInformationExpiredStrategy;
import java.util.ArrayList;

@Configuration
@EnableWebSecurity
public class SpringSecurity extends WebSecurityConfigurerAdapter {

    @Autowired
    MyAccessDeniedHandler myAccessDeniedHandler;
    @Autowired
    MyAuthenticationFailureHandler myAuthenticationFailureHandler;
    @Autowired
    MyAuthenticationFailureListener myAuthenticationFailureListener;
    @Autowired
    MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;
    @Autowired
    MyAuthenticationSuccessListener myAuthenticationSuccessListener;
    @Autowired
    MyAuthEntryPoint myAuthEntryPoint;
    @Autowired
    MyClearLogoutHandler myClearLogoutHandler;
    @Autowired
    MyJdbcTokenRepositoryImpl myJdbcTokenRepository;
    @Autowired
    MyLogoutSuccessHandler myLogoutSuccessHandler;
    @Autowired
    MyRedirectStrategy myRedirectStrategy;
    @Autowired
    MySessionRegistryImpl  mySessionRegistry;
    @Autowired
    MyUserDetails myUserDetails;
    @Autowired
    MyAuthenticationProvider myAuthenticationProvider;

    @Autowired
    SecurityContextLogoutHandler securityContextLogoutHandler;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    RememberMeAuthenticationProvider rememberMeAuthenticationProvider;
    @Autowired
    CompositeSessionAuthenticationStrategy compositeSessionAuthenticationStrategy;
    @Autowired
    MyRememberMeService myRememberMeService;

    @Bean
    @Primary
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityContextLogoutHandler securityContextLogoutHandler() {
        return new SecurityContextLogoutHandler();
    }
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    @Bean
    public RememberMeAuthenticationProvider rememberMeAuthenticationProvider() {
        return new RememberMeAuthenticationProvider("skyline");
    }
    @Bean
    public CompositeSessionAuthenticationStrategy compositeSessionAuthenticationStrategy() {
        ConcurrentSessionControlAuthenticationStrategy concurrentSessionControlAuthenticationStrategy = new ConcurrentSessionControlAuthenticationStrategy(mySessionRegistry);
        concurrentSessionControlAuthenticationStrategy.setMaximumSessions(2);
        concurrentSessionControlAuthenticationStrategy.setExceptionIfMaximumExceeded(true);

        ArrayList<SessionAuthenticationStrategy> list = new ArrayList<>();
        list.add(concurrentSessionControlAuthenticationStrategy);
        list.add(new SessionFixationProtectionStrategy());
        list.add(new RegisterSessionAuthenticationStrategy(mySessionRegistry));

        return new CompositeSessionAuthenticationStrategy(list);
    }
    @Bean
    public MyRememberMeService myRememberMeService() {
        MyRememberMeService temp = new MyRememberMeService("skyline", myUserDetails, myJdbcTokenRepository);
        temp.setParameter("isRememberMe");
        temp.setCookieName("rememberMe");
        temp.setTokenValiditySeconds(604800);
        return temp;
    }
    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    public MyUserKickFilter myUserKickFilter() {
        return new MyUserKickFilter(new LogoutHandler[]{myClearLogoutHandler, securityContextLogoutHandler, myRememberMeService}, mySessionRegistry);
    }
    public ConcurrentSessionFilter concurrentSessionFilter() {
        ConcurrentSessionFilter concurrentSessionFilter = new ConcurrentSessionFilter(mySessionRegistry, new SimpleRedirectSessionInformationExpiredStrategy("/session-expired.html", myRedirectStrategy));
        concurrentSessionFilter.setLogoutHandlers(new LogoutHandler[]{myClearLogoutHandler, securityContextLogoutHandler});
        return concurrentSessionFilter;
    }
    public LogoutFilter logoutFilter() {
        LogoutFilter temp = new LogoutFilter(myLogoutSuccessHandler, myClearLogoutHandler, securityContextLogoutHandler, myRememberMeService);
        temp.setFilterProcessesUrl("/security/logout.do");
        return temp;
    }
    public MyAuthenticationFilter myAuthenticationFilter() {
        MyAuthenticationFilter temp = new MyAuthenticationFilter("/security/login.do", "POST");
        temp.setAuthenticationManager(authenticationManager);
        temp.setAuthenticationSuccessHandler(myAuthenticationSuccessHandler);
        temp.setAuthenticationFailureHandler(myAuthenticationFailureHandler);
        temp.setSessionAuthenticationStrategy(compositeSessionAuthenticationStrategy);
        temp.setRememberMeServices(myRememberMeService);
        return temp;
    }
    public MyRememberMeFilter myRememberMeFilter() {
        return new MyRememberMeFilter(authenticationManager, myRememberMeService, mySessionRegistry);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(myAuthenticationProvider).authenticationProvider(rememberMeAuthenticationProvider);
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/resource/**", "/");
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.exceptionHandling().accessDeniedHandler(myAccessDeniedHandler).authenticationEntryPoint(myAuthEntryPoint);
        httpSecurity
                .addFilterBefore(myUserKickFilter(), ConcurrentSessionFilter.class)
                .addFilterAt(concurrentSessionFilter(), ConcurrentSessionFilter.class)
                .addFilterAt(logoutFilter(), LogoutFilter.class)
                .addFilterAt(myAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterAt(myRememberMeFilter(), RememberMeAuthenticationFilter.class)
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/security/login.do", "/security/register.do", "/security/flushVerifyCode.do", "/").permitAll()
                .antMatchers("/**").hasRole("USER")
                .anyRequest().authenticated()
                .and()
                .formLogin();
    }
}
