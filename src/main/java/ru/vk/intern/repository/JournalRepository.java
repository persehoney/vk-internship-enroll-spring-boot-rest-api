package ru.vk.intern.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.vk.intern.model.Journal;

public interface JournalRepository extends JpaRepository<Journal, Long> {
}
