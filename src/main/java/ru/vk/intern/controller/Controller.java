package ru.vk.intern.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;
import ru.vk.intern.model.Account;
import ru.vk.intern.service.AccountService;

// todo
@RestController
public class Controller {
    private static final String ACCOUNT_ID_SESSION_KEY = "accountId";

    @Autowired
    private AccountService accountService;

    @ModelAttribute("account")
    public Account getAccount(HttpSession httpSession) {
        Object accountId = httpSession.getAttribute(ACCOUNT_ID_SESSION_KEY);
        return accountId == null ? null : accountService.findById((Long) httpSession.getAttribute(ACCOUNT_ID_SESSION_KEY));
    }

    void setAccount(HttpSession httpSession, Account account) {
        if (account != null) {
            httpSession.setAttribute(ACCOUNT_ID_SESSION_KEY, account.getId());
        } else {
            unsetAccount(httpSession);
        }
    }

    void unsetAccount(HttpSession httpSession) {
        httpSession.removeAttribute(ACCOUNT_ID_SESSION_KEY);
    }
}
