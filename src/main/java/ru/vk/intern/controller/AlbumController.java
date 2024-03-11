package ru.vk.intern.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.vk.intern.model.Album;
import ru.vk.intern.model.Role;
import ru.vk.intern.security.HasRole;
import ru.vk.intern.service.AlbumService;

import java.util.List;

@RestController
@RequestMapping("/api/albums")
public class AlbumController {
    private final AlbumService albumService;

    public AlbumController(AlbumService albumService) {
        this.albumService = albumService;
    }

    @GetMapping("/")
    @HasRole({Role.Name.ROLE_ALBUMS, Role.Name.ROLE_ADMIN})
    public List<Album> findAlbums() {
        return albumService.findAll();
    }

    @GetMapping("/{id}")
    @HasRole({Role.Name.ROLE_ALBUMS, Role.Name.ROLE_ADMIN})
    public Album findAlbumById(@PathVariable("id") long id) {
        return albumService.findById(id);
    }
}