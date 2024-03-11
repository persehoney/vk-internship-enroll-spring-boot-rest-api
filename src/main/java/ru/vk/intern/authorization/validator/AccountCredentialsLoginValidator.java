package ru.vk.intern.authorization.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.vk.intern.authorization.AccountCredentials;
import ru.vk.intern.service.AccountService;

@Component
public class AccountCredentialsLoginValidator implements Validator {
    private final AccountService accountService;

    public AccountCredentialsLoginValidator(AccountService accountService) {
        this.accountService = accountService;
    }

    public boolean supports(Class<?> clazz) {
        return AccountCredentials.class.equals(clazz);
    }

    public void validate(Object target, Errors errors) {
        if (!errors.hasErrors()) {
            AccountCredentials accountCredentials = (AccountCredentials) target;
            if (accountService.findByLoginAndPassword(accountCredentials.getLogin(), accountCredentials.getPassword()) == null) {
                errors.reject("invalid-login-or-password", "Invalid login or password");
            }
        }
    }
}