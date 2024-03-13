package ru.vk.intern.controller;

import jakarta.servlet.http.HttpSession;
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
import ru.vk.intern.model.Post;
import ru.vk.intern.model.Role;
import ru.vk.intern.security.HasRole;
import ru.vk.intern.service.PostService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController extends Controller {
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
    public ResponseEntity<Post> findPostById(@PathVariable("id") long id) {
        Post post = postService.findById(id);
        if (post == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(post);
    }

    @PostMapping("/")
    @HasRole({Role.Name.ROLE_POSTS, Role.Name.ROLE_ADMIN})
    public ResponseEntity<String> addPost(@RequestBody Post post, HttpSession httpSession) {
        post.setUser(getUser(httpSession));
        Post createdPost = postService.add(post);

        URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/posts/{id}")
                .buildAndExpand(createdPost.getId()).toUri();
        return ResponseEntity.created(location).body("Post created successfully");
    }

    @DeleteMapping("/{id}")
    @HasRole({Role.Name.ROLE_POSTS, Role.Name.ROLE_ADMIN})
    public ResponseEntity<String> delete(@PathVariable("id") long id) {
        Post post = postService.delete(id);
        if (post == null) {
            return new ResponseEntity<>("No such post", HttpStatus.NO_CONTENT);
        }

        return ResponseEntity.ok("Post deleted successfully");
    }

    @PutMapping("/{id}")
    @HasRole({Role.Name.ROLE_POSTS, Role.Name.ROLE_ADMIN})
    public ResponseEntity<String> put(@PathVariable("id") long id, @RequestBody Post post, HttpSession httpSession) {
        post.setUser(getUser(httpSession));
        Post updatedPost = postService.put(id, post);

        URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/posts/{id}")
                .buildAndExpand(updatedPost.getId()).toUri();
        return ResponseEntity.created(location).body("Post updated successfully");
    }
}
