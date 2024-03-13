package ru.vk.intern.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.vk.intern.model.Role;
import ru.vk.intern.model.User;
import ru.vk.intern.security.HasRole;
import ru.vk.intern.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    @HasRole({Role.Name.ROLE_USERS, Role.Name.ROLE_ADMIN})
    public List<User> findUsers() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    @HasRole({Role.Name.ROLE_USERS, Role.Name.ROLE_ADMIN})
    public User findUserById(@PathVariable("id") long id) {
        return userService.findById(id);
    }

    @PostMapping("/")
    @HasRole({Role.Name.ROLE_USERS, Role.Name.ROLE_ADMIN})
    public boolean addUser(@RequestBody User user) {
        return userService.addUser(user);
    }
}