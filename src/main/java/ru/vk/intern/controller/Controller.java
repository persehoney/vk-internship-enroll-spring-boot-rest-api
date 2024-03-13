package ru.vk.intern.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;
import ru.vk.intern.model.User;
import ru.vk.intern.service.UserService;

// todo
@RestController
public class Controller {
    private static final String ACCOUNT_ID_SESSION_KEY = "accountId";

    @Autowired
    private UserService userService;

    @ModelAttribute("user")
    public User getUser(HttpSession httpSession) {
        Object userId = httpSession.getAttribute(ACCOUNT_ID_SESSION_KEY);
        return userId == null ? null : userService.findById((Long) httpSession.getAttribute(ACCOUNT_ID_SESSION_KEY));
    }

    void setUser(HttpSession httpSession, User user) {
        if (user != null) {
            httpSession.setAttribute(ACCOUNT_ID_SESSION_KEY, user.getId());
        } else {
            unsetUser(httpSession);
        }
    }

    void unsetUser(HttpSession httpSession) {
        httpSession.removeAttribute(ACCOUNT_ID_SESSION_KEY);
    }
}
