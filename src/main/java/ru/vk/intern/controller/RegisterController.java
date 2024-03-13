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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.vk.intern.authorization.UserCredentials;
import ru.vk.intern.authorization.validator.UserCredentialsRegisterValidator;
import ru.vk.intern.model.User;
import ru.vk.intern.security.Guest;
import ru.vk.intern.service.UserService;

import java.net.URI;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/register")
public class RegisterController extends Controller {
    private final UserService userService;
    private final UserCredentialsRegisterValidator userCredentialsRegisterValidator;

    public RegisterController(UserService userService, UserCredentialsRegisterValidator UserCredentialsRegisterValidator) {
        this.userService = userService;
        this.userCredentialsRegisterValidator = UserCredentialsRegisterValidator;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(userCredentialsRegisterValidator);
    }

    @PostMapping("/")
    @Guest
    public ResponseEntity<String> register(@RequestBody @Valid UserCredentials userCredentials,
                                           BindingResult bindingResult,
                                           HttpSession httpSession) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining(",")), HttpStatus.BAD_REQUEST);
        }

        User user = userService.register(userCredentials);
        setUser(httpSession, user);

        URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/users/{id}")
                .buildAndExpand(user.getId()).toUri();
        return ResponseEntity.created(location).body("User registered successfully");
    }
}
