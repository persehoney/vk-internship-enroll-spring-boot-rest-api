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
        if (post.isPresent()) {
            return post.get();
        }
        try {
            return postHolder.findById(id);
        } catch (RuntimeException e) {
            return null;
        }
    }

    public Post add(Post post) {
        postHolder.addPost(post);
        return postRepository.save(post);
    }

    public Post delete(long id) {
        Post post = findById(id);
        if (post == null) {
            return null;
        }
        postHolder.deletePost(id);
        postRepository.delete(post);
        return post;
    }

    public Post put(long id, Post newPost) {
        Post oldPost = findById(id);
        if (oldPost == null) {
            return add(newPost);
        }
        delete(id);
        return add(newPost);
    }
}
