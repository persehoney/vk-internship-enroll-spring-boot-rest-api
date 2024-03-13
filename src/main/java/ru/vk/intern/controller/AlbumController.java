package ru.vk.intern.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.vk.intern.model.Album;
import ru.vk.intern.model.Role;
import ru.vk.intern.security.HasRole;
import ru.vk.intern.service.AlbumService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/albums")
public class AlbumController extends Controller {
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

    @PostMapping("/")
    @HasRole({Role.Name.ROLE_ALBUMS, Role.Name.ROLE_ADMIN})
    public ResponseEntity<String> addAlbum(@RequestBody Album album, HttpSession httpSession) {
        album.setUser(getUser(httpSession));
        Album createdAlbum = albumService.addAlbum(album);

        URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/albums/{id}")
                .buildAndExpand(createdAlbum.getId()).toUri();
        return ResponseEntity.created(location).body("Album created successfully");
    }

    @DeleteMapping("/{id}")
    @HasRole({Role.Name.ROLE_ALBUMS, Role.Name.ROLE_ADMIN})
    public ResponseEntity<String> delete(@PathVariable("id") long id) {
        Album album = albumService.delete(id);
        if (album == null) {
            return new ResponseEntity<>("No such album", HttpStatus.NO_CONTENT);
        }

        return ResponseEntity.ok("Album deleted successfully");
    }
}