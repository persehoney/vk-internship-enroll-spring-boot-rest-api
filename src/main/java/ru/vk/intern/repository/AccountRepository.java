package ru.vk.intern.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.vk.intern.model.Account;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {
    @Transactional
    @Modifying
    @Query(value = "UPDATE account SET password_sha=SHA1(CONCAT('1be3db47a7684152', ?2, ?3)) WHERE id=?1", nativeQuery = true)
    void updatePasswordSha(long id, String login, String password);

    @Query(value = "SELECT * FROM account WHERE login=?1 AND password_sha=SHA1(CONCAT('1be3db47a7684152', ?1, ?2))", nativeQuery = true)
    Account findByLoginAndPassword(String login, String password);

    List<Account> findAllByOrderById();

    int countByLogin(String login);
}