package ru.vk.intern.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.vk.intern.model.Album;

import java.util.List;

public interface AlbumRepository extends JpaRepository<Album, Long> {
    List<Album> findAllByOrderById();
}