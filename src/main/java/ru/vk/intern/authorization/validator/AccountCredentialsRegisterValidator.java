package ru.vk.intern.authorization.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.vk.intern.authorization.AccountCredentials;
import ru.vk.intern.service.AccountService;

@Component
public class AccountCredentialsRegisterValidator implements Validator {
    private final AccountService accountService;

    public AccountCredentialsRegisterValidator(AccountService accountService) {
        this.accountService = accountService;
    }

    public boolean supports(Class<?> clazz) {
        return AccountCredentials.class.equals(clazz);
    }

    public void validate(Object target, Errors errors) {
        if (!errors.hasErrors()) {
            AccountCredentials accountCredentials = (AccountCredentials) target;
            if (!accountService.isLoginVacant(accountCredentials.getLogin())) {
                errors.reject("login.is-in-use", "Login is in use already");
            }
        }
    }
}
