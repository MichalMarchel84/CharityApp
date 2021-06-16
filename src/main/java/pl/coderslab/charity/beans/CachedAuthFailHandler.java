package pl.coderslab.charity.beans;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CachedAuthFailHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException e) throws IOException, ServletException {

        request.setAttribute("email", request.getParameter("username"));
        request.setAttribute("errMsg", "Email lub hasło nieprawidłowe");
        request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
    }
}
