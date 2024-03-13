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
        List<Post> holderPosts = postHolder.findAll();

        for (Post post : holderPosts) {
            if (tryAddToDB(post)) {
                posts.add(post);
            }
        }

        return posts;
    }

    public Post findById(long id) {
        Optional<Post> possiblePost = postRepository.findById(id);
        if (possiblePost.isPresent()) {
            return possiblePost.get();
        }
        try {
            Post post = postHolder.findById(id);
            tryAddToDB(post);
            return post;
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

    private boolean tryAddToDB(Post post) {
        if (!postRepository.existsByTitle(post.getTitle())) {
            postRepository.save(post);
            return true;
        }
        return false;
    }
}
