package ru.vk.intern.jsonplaceholder;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.vk.intern.jsonplaceholder.config.Config;
import ru.vk.intern.model.Post;

import java.util.List;

@FeignClient(name = "postHolder", configuration = Config.class)
public interface PostHolder {
    @GetMapping("/")
    List<Post> findAll();

    @GetMapping("/{id}")
    Post findById(@PathVariable(name = "id") long id);

    @PostMapping("/")
    Post addPost(Post post);

    @DeleteMapping("/{id}")
    void deletePost(@PathVariable(name = "id") long id);
}
