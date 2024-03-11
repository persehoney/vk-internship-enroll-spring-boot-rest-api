package ru.vk.intern.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.vk.intern.model.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findAllByOrderById();
}