package ru.vk.intern.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vk.intern.jsonplaceholder.UserHolder;
import ru.vk.intern.model.User;
import ru.vk.intern.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserHolder userHolder;
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAll() {
        List<User> users = userRepository.findAllByOrderById();
        return !users.isEmpty() ? users : userHolder.findAll(); // todo
    }

    public User findById(long id) {
        Optional<User> user = userRepository.findById(id);
        return user.orElseGet(() -> userHolder.findById(id));
    }

    public boolean addUser(User user) {
        User newUser = userHolder.addUser(user);
        return true;
    }
}