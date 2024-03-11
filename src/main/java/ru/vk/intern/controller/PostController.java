package ru.vk.intern.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.vk.intern.model.Post;
import ru.vk.intern.model.Role;
import ru.vk.intern.security.HasRole;
import ru.vk.intern.service.PostService;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/")
    @HasRole({Role.Name.ROLE_POSTS, Role.Name.ROLE_ADMIN})
    public List<Post> findPosts() {
        return postService.findAll();
    }

    @GetMapping("/{id}")
    @HasRole({Role.Name.ROLE_POSTS, Role.Name.ROLE_ADMIN})
    public Post findPostById(@PathVariable("id") long id) {
        return postService.findById(id);
    }
}
