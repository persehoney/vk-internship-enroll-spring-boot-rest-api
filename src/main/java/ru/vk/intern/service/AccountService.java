package ru.vk.intern.service;

import org.springframework.stereotype.Service;
import ru.vk.intern.authorization.AccountCredentials;
import ru.vk.intern.model.Account;
import ru.vk.intern.model.Role;
import ru.vk.intern.repository.AccountRepository;
import ru.vk.intern.repository.RoleRepository;

import java.util.List;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;

    public AccountService(AccountRepository accountRepository, RoleRepository roleRepository) {
        this.accountRepository = accountRepository;
        this.roleRepository = roleRepository;

        for (Role.Name name : Role.Name.values()) {
            if (!roleRepository.existsByName(name)) {
                roleRepository.save(new Role(name));
            }
        }
    }

    public List<Account> findAll() {
        return accountRepository.findAllByOrderById();
    }

    public Account findById(long id) {
        return accountRepository.findById(id).orElse(null);
    }

    public Account findByLoginAndPassword(String login, String password) {
        return login == null || password == null ? null : accountRepository.findByLoginAndPassword(login, password);
    }

    public boolean isLoginVacant(String login) {
        return accountRepository.countByLogin(login) == 0;
    }

    public Account register(AccountCredentials accountCredentials) {
        Account account = new Account();
        account.setLogin(accountCredentials.getLogin());
        account.addRole(roleRepository.findByName(Role.Name.ROLE_USERS));
        accountRepository.save(account);
        accountRepository.updatePasswordSha(account.getId(), accountCredentials.getLogin(), accountCredentials.getPassword());
        return account;
    }
}
