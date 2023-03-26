package com.dus.back.security;

import com.dus.back.fcm.FcmService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *  로그인 성공시 처리하는 핸들러
 *  웹과 안드로이드 앱 따로 처리
 */
@Slf4j
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        String device = request.getParameter("device");
        if(device.equals("android")){
            log.info("안드로이드에서 로그인 요청");
            response.setHeader("login-userId", authentication.getName());
        }else {
            log.info("웹에서 로그인 요청");
            setDefaultTargetUrl("/home");
            super.onAuthenticationSuccess(request, response, authentication);
        }

    }
}
