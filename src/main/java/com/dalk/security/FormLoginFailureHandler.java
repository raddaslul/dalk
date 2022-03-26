//package com.dalk.security;
//
//import com.dalk.exception.ex.UserNotFoundException;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.web.authentication.AuthenticationFailureHandler;
//import org.springframework.util.StringUtils;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//
//public class FormLoginFailureHandler implements AuthenticationFailureHandler {
//    @Override
//    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
//        String errorMsg = "";
//        if (exception instanceof UsernameNotFoundException) {
//            errorMsg = "존재하지 않는 아이디입니다.";
////            response.sendRedirect("/error/username");
//        } else if (exception instanceof BadCredentialsException) {
//            errorMsg = "아이디 또는 비밀번호가 잘못 입력 되었습니다.";
////            response.sendRedirect("/error/password");
//        }
//        if (!StringUtils.isEmpty(errorMsg)) {
//            request.setAttribute("errorMsg", errorMsg);
//        }
//    }
//}
