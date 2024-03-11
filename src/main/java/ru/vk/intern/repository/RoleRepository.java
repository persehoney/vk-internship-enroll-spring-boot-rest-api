package ru.vk.intern.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.vk.intern.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    boolean existsByName(Role.Name name);
    Role findByName(Role.Name name);
}