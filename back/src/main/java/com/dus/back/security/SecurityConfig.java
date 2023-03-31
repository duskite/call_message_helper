package com.dus.back.security;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests()
                .antMatchers("/","/member/sign-in", "/member/sign-up", "/member/login", "/fcm-token").permitAll()
                .antMatchers("/home", "/call/**", "/msg/**").authenticated();
        http.formLogin()
                .loginPage("/member/sign-in")
                .successHandler(loginSuccessHandler())
                .failureHandler(loginFailHandler())
                .usernameParameter("userId")
                .passwordParameter("password")
                .loginProcessingUrl("/member/login");
        http.exceptionHandling().accessDeniedPage("/forbidden");
        http.logout().logoutUrl("/logout").logoutSuccessUrl("/");
        http.userDetailsService(userDetailsService);

        return http.build();
    }

    @Bean
    public LoginFailureHandler loginFailHandler(){
        return new LoginFailureHandler();
    }

    @Bean
    public LoginSuccessHandler loginSuccessHandler(){
        return new LoginSuccessHandler();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
