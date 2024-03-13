package ru.vk.intern.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.vk.intern.authorization.UserCredentials;
import ru.vk.intern.authorization.validator.UserCredentialsLoginValidator;
import ru.vk.intern.model.User;
import ru.vk.intern.security.Guest;
import ru.vk.intern.service.UserService;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/login")
public class LoginController extends Controller {
    private final UserService userService;
    private final UserCredentialsLoginValidator userCredentialsLoginValidator;

    public LoginController(UserService userService, UserCredentialsLoginValidator userCredentialsLoginValidator) {
        this.userService = userService;
        this.userCredentialsLoginValidator = userCredentialsLoginValidator;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(userCredentialsLoginValidator);
    }

    @PostMapping("/")
    @Guest
    public ResponseEntity<String> login(@RequestBody @Valid UserCredentials userCredentials,
                                        BindingResult bindingResult,
                                        HttpSession httpSession) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining(",")), HttpStatus.BAD_REQUEST);
        }

        User user = userService.findByUsernameAndPassword(userCredentials.getUsername(), userCredentials.getPassword());
        setUser(httpSession, user);

        return ResponseEntity.ok("User logged in successfully");
    }
}
