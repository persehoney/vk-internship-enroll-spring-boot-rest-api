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
import ru.vk.intern.authorization.AccountCredentials;
import ru.vk.intern.authorization.validator.AccountCredentialsLoginValidator;
import ru.vk.intern.model.Account;
import ru.vk.intern.security.Guest;
import ru.vk.intern.service.AccountService;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/login")
public class LoginController extends Controller {
    private final AccountService accountService;
    private final AccountCredentialsLoginValidator accountCredentialsLoginValidator;

    public LoginController(AccountService accountService, AccountCredentialsLoginValidator accountCredentialsLoginValidator) {
        this.accountService = accountService;
        this.accountCredentialsLoginValidator = accountCredentialsLoginValidator;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(accountCredentialsLoginValidator);
    }

    @PostMapping("/")
    @Guest
    public ResponseEntity<String> login(@RequestBody @Valid AccountCredentials accountCredentials,
                                        BindingResult bindingResult,
                                        HttpSession httpSession) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining(",")), HttpStatus.BAD_REQUEST);
        }

        Account account = accountService.findByLoginAndPassword(accountCredentials.getLogin(), accountCredentials.getPassword());
        setAccount(httpSession, account);

        return ResponseEntity.ok("User logged in successfully");
    }
}
