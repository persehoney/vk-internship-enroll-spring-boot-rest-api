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
import ru.vk.intern.authorization.AccountCredentials;
import ru.vk.intern.authorization.validator.AccountCredentialsRegisterValidator;
import ru.vk.intern.model.Account;
import ru.vk.intern.security.Guest;
import ru.vk.intern.service.AccountService;

import java.net.URI;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/register")
public class RegisterController extends Controller {
    private final AccountService accountService;
    private final AccountCredentialsRegisterValidator accountCredentialsRegisterValidator;

    public RegisterController(AccountService accountService, AccountCredentialsRegisterValidator AccountCredentialsRegisterValidator) {
        this.accountService = accountService;
        this.accountCredentialsRegisterValidator = AccountCredentialsRegisterValidator;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(accountCredentialsRegisterValidator);
    }

    @PostMapping("/")
    @Guest
    public ResponseEntity<String> register(@RequestBody @Valid AccountCredentials accountCredentials,
                                           BindingResult bindingResult,
                                           HttpSession httpSession) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining(",")), HttpStatus.BAD_REQUEST);
        }

        Account account = accountService.register(accountCredentials);
        setAccount(httpSession, account);

        URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/accounts/{id}")
                .buildAndExpand(account.getId()).toUri();
        return ResponseEntity.created(location).body("Account registered successfully");
    }
}
