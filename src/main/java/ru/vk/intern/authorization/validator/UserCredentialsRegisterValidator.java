package ru.vk.intern.authorization.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.vk.intern.authorization.UserCredentials;
import ru.vk.intern.service.UserService;

@Component
public class UserCredentialsRegisterValidator implements Validator {
    private final UserService accountService;

    public UserCredentialsRegisterValidator(UserService accountService) {
        this.accountService = accountService;
    }

    public boolean supports(Class<?> clazz) {
        return UserCredentials.class.equals(clazz);
    }

    public void validate(Object target, Errors errors) {
        if (!errors.hasErrors()) {
            UserCredentials userCredentials = (UserCredentials) target;
            if (!accountService.isUsernameVacant(userCredentials.getUsername())) {
                errors.reject("username.is-in-use", "Username is in use already");
            }
        }
    }
}
