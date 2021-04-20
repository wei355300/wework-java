package com.qc.security;

import com.qc.security.login.LoginAuthenticationProvider;
import com.qc.security.login.LoginUserDetailService;
import com.qc.security.login.LoginAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.Filter;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private LoginUserDetailService accountUserDetailService;

    @Autowired
    private ApiRequestUserDetailService apiRequestUserDetailService;


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .authenticationProvider(loginAuthenticationProvider())
                .authenticationProvider(apiRequestAuthenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterAfter(loginFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(apiRequestFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class);
        http
                .formLogin().disable()
                .logout().disable()
                .rememberMe().disable()
                .anonymous().disable()
                .csrf().disable()
                .httpBasic().disable()
                .requestCache().disable()
                .sessionManagement().disable();//.sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.exceptionHandling().authenticationEntryPoint(new Http403ForbiddenEntryPoint());

        http.authorizeRequests().antMatchers("/api/qc/wework/**").access("@apiRequestAuthorizationChecker.check(authentication,request)").anyRequest().permitAll();
    }

//    @Bean
    PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
//        return new BCryptPasswordEncoder();
    }

    AuthenticationProvider apiRequestAuthenticationProvider() {
        ApiRequestAuthenticationProvider provider = new ApiRequestAuthenticationProvider();
        provider.setUserDetailsService(apiRequestUserDetailService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    AuthenticationProvider loginAuthenticationProvider() {
        LoginAuthenticationProvider provider = new LoginAuthenticationProvider();
        provider.setUserDetailsService(accountUserDetailService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    private Filter loginFilter(AuthenticationManager authenticationManager) {
        LoginAuthenticationFilter filter = new LoginAuthenticationFilter("/api/qc/login", authenticationManager, accountUserDetailService);
        return filter;
    }

    private Filter apiRequestFilter(AuthenticationManager authenticationManager) {
        ApiRequestAuthenticationFilter filter = new ApiRequestAuthenticationFilter("/api/qc/wework/**", authenticationManager);
        return filter;
    }
}