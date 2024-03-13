package ru.vk.intern.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vk.intern.authorization.UserCredentials;
import ru.vk.intern.jsonplaceholder.UserHolder;
import ru.vk.intern.model.Role;
import ru.vk.intern.model.User;
import ru.vk.intern.repository.RoleRepository;
import ru.vk.intern.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserHolder userHolder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;

        for (Role.Name name : Role.Name.values()) {
            if (!roleRepository.existsByName(name)) {
                roleRepository.save(new Role(name));
            }
        }
    }

    public List<User> findAll() {
        List<User> users = userRepository.findAllByOrderById();
        return !users.isEmpty() ? users : userHolder.findAll(); // todo
    }

    public User findById(long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return user.get();
        }
        try {
            return userHolder.findById(id);
        } catch (RuntimeException e) {
            return null;
        }
    }

    public User addUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            return null;
        }
        userHolder.addUser(user);
        return userRepository.save(user);
    }

    public User delete(long id) {
        User user = findById(id);
        if (user == null) {
            return null;
        }
        userHolder.deleteUser(id);
        userRepository.delete(user);
        return user;
    }

    public User findByUsernameAndPassword(String username, String password) {
        return username == null || password == null ? null : userRepository.findByUsernameAndPassword(username, password);
    }

    public boolean isUsernameVacant(String username) {
        return userRepository.countByUsername(username) == 0;
    }

    public User register(UserCredentials userCredentials) {
        User user = new User();
        user.setUsername(userCredentials.getUsername());
        user.addRole(roleRepository.findByName(Role.Name.ROLE_USERS));
        userRepository.save(user);
        userRepository.updatePasswordSha(user.getId(), userCredentials.getUsername(), userCredentials.getPassword());
        return user;
    }
}