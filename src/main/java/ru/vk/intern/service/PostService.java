package ru.vk.intern.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vk.intern.jsonplaceholder.PostHolder;
import ru.vk.intern.model.Post;
import ru.vk.intern.repository.PostRepository;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    @Autowired
    private PostHolder postHolder;
    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<Post> findAll() {
        List<Post> posts = postRepository.findAllByOrderById();
        return !posts.isEmpty() ? posts : postHolder.findAll(); // todo
    }

    public Post findById(long id) {
        Optional<Post> post = postRepository.findById(id);
        return post.orElseGet(() -> postHolder.findById(id));
    }
}
