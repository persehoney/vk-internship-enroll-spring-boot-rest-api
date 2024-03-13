package ru.vk.intern.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.vk.intern.model.Role;
import ru.vk.intern.model.User;
import ru.vk.intern.security.HasRole;
import ru.vk.intern.service.UserService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("")
    @HasRole({Role.Name.ROLE_USERS, Role.Name.ROLE_ADMIN})
    public List<User> findUsers() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    @HasRole({Role.Name.ROLE_USERS, Role.Name.ROLE_ADMIN})
    public ResponseEntity<User> findUserById(@PathVariable("id") long id) {
        User user = userService.findById(id);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(user);
    }

    @PostMapping("/")
    @HasRole({Role.Name.ROLE_USERS, Role.Name.ROLE_ADMIN})
    public ResponseEntity<String> addUser(@RequestBody User user) {
        User createdUser = userService.addUser(user);
        if (createdUser == null) {
            return ResponseEntity.badRequest().body("Username is already in use");
        }

        URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/users/{id}")
                .buildAndExpand(createdUser.getId()).toUri();
        return ResponseEntity.created(location).body("User created successfully");
    }

    @DeleteMapping("/{id}")
    @HasRole({Role.Name.ROLE_USERS, Role.Name.ROLE_ADMIN})
    public ResponseEntity<String> delete(@PathVariable("id") long id) {
        User user = userService.delete(id);
        if (user == null) {
            return new ResponseEntity<>("No such user", HttpStatus.NO_CONTENT);
        }

        return ResponseEntity.ok("User deleted successfully");
    }

    @PutMapping("/{id}")
    @HasRole({Role.Name.ROLE_USERS, Role.Name.ROLE_ADMIN})
    public ResponseEntity<String> put(@PathVariable("id") long id, @RequestBody User user) {
        User updatedUser = userService.put(id, user);
        if (updatedUser == null) {
            return ResponseEntity.badRequest().body("Cannot update: new username is already in use");
        }

        URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/users/{id}")
                .buildAndExpand(updatedUser.getId()).toUri();
        return ResponseEntity.created(location).body("User updated successfully");
    }
}