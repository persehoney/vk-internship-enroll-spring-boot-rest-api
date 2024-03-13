package ru.vk.intern.security.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import ru.vk.intern.controller.Controller;
import ru.vk.intern.model.Journal;
import ru.vk.intern.model.Role;
import ru.vk.intern.model.User;
import ru.vk.intern.repository.JournalRepository;
import ru.vk.intern.security.Guest;
import ru.vk.intern.security.HasRole;

import java.io.IOException;
import java.lang.reflect.Method;

@Component
public class SecurityInterceptor implements HandlerInterceptor {
    private final Controller controller;
    private final JournalRepository journalRepository;

    public SecurityInterceptor(Controller controller, JournalRepository journalRepository) {
        this.controller = controller;
        this.journalRepository = journalRepository;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        Journal journal = new Journal();
        journal.setMethod(request.getMethod());
        journal.setRequest(request.getRequestURI());
        if (handler instanceof HandlerMethod handlerMethod) {
            Method method = handlerMethod.getMethod();

            User user = controller.getUser(request.getSession());

            if (method.getAnnotation(Guest.class) != null) {
                if (user == null) {
                    journal.setHasAccess(true);
                    journalRepository.save(journal);
                    return true;
                }
                response.sendError(HttpStatus.FORBIDDEN.value(), "You are already logged in");
                journal.setUser(user);
                journal.setHasAccess(false);
                journalRepository.save(journal);
                return false;
            }

            HasRole hasRole = method.getAnnotation(HasRole.class);
            if (hasRole != null) {
                if (user == null) {
                    response.sendError(HttpStatus.FORBIDDEN.value(), "Log in to get access");
                    journal.setHasAccess(false);
                    journalRepository.save(journal);
                    return false;
                }

                for (Role.Name name : hasRole.value()) {
                    for (Role role : user.getRoles()) {
                        if (role.getName().equals(name)) {
                            journal.setUser(user);
                            journal.setHasAccess(true);
                            journalRepository.save(journal);
                            return true;
                        }
                    }
                }
                response.sendError(HttpStatus.FORBIDDEN.value(), "You have no access");
                journal.setUser(user);
                journal.setHasAccess(true);
                journalRepository.save(journal);
                return false;
            }
        }
        return true;
    }
}