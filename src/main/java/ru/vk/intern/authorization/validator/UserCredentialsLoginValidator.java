package ru.vk.intern.authorization.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.vk.intern.authorization.UserCredentials;
import ru.vk.intern.service.UserService;

@Component
public class UserCredentialsLoginValidator implements Validator {
    private final UserService userService;

    public UserCredentialsLoginValidator(UserService userService) {
        this.userService = userService;
    }

    public boolean supports(Class<?> clazz) {
        return UserCredentials.class.equals(clazz);
    }

    public void validate(Object target, Errors errors) {
        if (!errors.hasErrors()) {
            UserCredentials userCredentials = (UserCredentials) target;
            if (userService.findByUsernameAndPassword(userCredentials.getUsername(), userCredentials.getPassword()) == null) {
                errors.reject("invalid-username-or-password", "Invalid username or password");
            }
        }
    }
}