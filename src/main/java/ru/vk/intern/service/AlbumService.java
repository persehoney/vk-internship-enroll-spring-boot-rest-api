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
        List<Album> holderAlbums = albumHolder.findAll();

        for (Album album : holderAlbums) {
            if (tryAddToDB(album)) {
                albums.add(album);
            }
        }

        return albums;
    }

    public Album findById(long id) {
        Optional<Album> possibleAlbum = albumRepository.findById(id);
        if (possibleAlbum.isPresent()) {
            return possibleAlbum.get();
        }
        try {
            Album album = albumHolder.findById(id);
            tryAddToDB(album);
            return album;
        } catch (RuntimeException e) {
            return null;
        }
    }

    public Album add(Album album) {
        albumHolder.addAlbum(album);
        return albumRepository.save(album);
    }

    public Album delete(long id) {
        Album album = findById(id);
        if (album == null) {
            return null;
        }
        albumHolder.deleteAlbum(id);
        albumRepository.delete(album);
        return album;
    }

    public Album put(long id, Album newAlbum) {
        Album oldAlbum = findById(id);
        if (oldAlbum == null) {
            return add(newAlbum);
        }
        delete(id);
        return add(newAlbum);
    }

    private boolean tryAddToDB(Album album) {
        if (!albumRepository.existsByTitle(album.getTitle())) {
            albumRepository.save(album);
            return true;
        }
        return false;
    }
}