package ru.vk.intern.jsonplaceholder;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.vk.intern.jsonplaceholder.config.Config;
import ru.vk.intern.model.User;

import java.util.List;

@FeignClient(name = "userHolder", configuration = Config.class)
public interface UserHolder {
    @GetMapping("/")
    List<User> findAll();

    @GetMapping("/{id}")
    User findById(@PathVariable(name = "id") long id);

    @PostMapping("/")
    User addUser(User user);
}