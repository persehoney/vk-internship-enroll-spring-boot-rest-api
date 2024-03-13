package ru.vk.intern.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.vk.intern.model.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);
    List<User> findAllByOrderById();

    @Transactional
    @Modifying
    @Query(value = "UPDATE user SET password_sha=SHA1(CONCAT('1be3db47a7684152', ?2, ?3)) WHERE id=?1", nativeQuery = true)
    void updatePasswordSha(long id, String username, String password);

    @Query(value = "SELECT * FROM user WHERE username=?1 AND password_sha=SHA1(CONCAT('1be3db47a7684152', ?1, ?2))", nativeQuery = true)
    User findByUsernameAndPassword(String username, String password);

    int countByUsername(String username);
}