package ru.vk.intern.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.vk.intern.model.Post;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByOrderById();
    boolean existsByTitle(String title);
}
