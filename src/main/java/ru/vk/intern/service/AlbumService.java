package ru.vk.intern.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vk.intern.jsonplaceholder.AlbumHolder;
import ru.vk.intern.model.Album;
import ru.vk.intern.repository.AlbumRepository;

import java.util.List;
import java.util.Optional;

@Service
public class AlbumService {
    @Autowired
    private AlbumHolder albumHolder;
    private final AlbumRepository albumRepository;

    public AlbumService(AlbumRepository albumRepository) {
        this.albumRepository = albumRepository;
    }

    public List<Album> findAll() {
        List<Album> albums = albumRepository.findAllByOrderById();
        return !albums.isEmpty() ? albums : albumHolder.findAll(); // todo
    }

    public Album findById(long id) {
        Optional<Album> album = albumRepository.findById(id);
        return album.orElseGet(() -> albumHolder.findById(id));
    }
}