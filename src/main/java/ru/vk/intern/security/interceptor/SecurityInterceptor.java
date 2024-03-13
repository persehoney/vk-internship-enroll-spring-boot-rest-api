package ru.vk.intern.security.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import ru.vk.intern.controller.Controller;
import ru.vk.intern.model.Account;
import ru.vk.intern.model.Role;
import ru.vk.intern.security.Guest;
import ru.vk.intern.security.HasRole;

import java.io.IOException;
import java.lang.reflect.Method;

@Component
public class SecurityInterceptor implements HandlerInterceptor {
    private final Controller controller;

    public SecurityInterceptor(Controller controller) {
        this.controller = controller;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        if (handler instanceof HandlerMethod handlerMethod) {
            Method method = handlerMethod.getMethod();

            Account account = controller.getAccount(request.getSession());

            if (method.getAnnotation(Guest.class) != null) {
                if (account == null) {
                    return true;
                }
                response.sendError(HttpStatus.FORBIDDEN.value(), "You are already logged in");
                return false;
            }

            HasRole hasRole = method.getAnnotation(HasRole.class);
            if (hasRole != null) {
                if (account == null) {
                    response.sendError(HttpStatus.FORBIDDEN.value(), "Log in to get access");
                    return false;
                }

                for (Role.Name name : hasRole.value()) {
                    for (Role role : account.getRoles()) {
                        if (role.getName().equals(name)) {
                            return true;
                        }
                    }
                }
                response.sendError(HttpStatus.FORBIDDEN.value(), "You have no access");
                return false;
            }
        }
        return true;
    }
}