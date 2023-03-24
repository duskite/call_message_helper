package com.dus.back.auth.security;

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
                .antMatchers("/","/member/signin-form", "/auth/login").permitAll()
                .antMatchers("/home","/member/info/**").authenticated();
        http.formLogin()
                .loginPage("/member/signin-form").failureHandler(loginFailHandler()).defaultSuccessUrl("/home", true);
        http.formLogin()
                .usernameParameter("userId")
                .passwordParameter("password")
                .loginProcessingUrl("/auth/login").defaultSuccessUrl("/home", true);
        http.exceptionHandling().accessDeniedPage("/forbidden");
        http.logout().logoutUrl("/logout").logoutSuccessUrl("/");
        http.userDetailsService(userDetailsService);

        return http.build();
    }

    @Bean
    public LoginFailHandler loginFailHandler(){
        return new LoginFailHandler();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
