package ru.vk.intern.jsonplaceholder;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.vk.intern.jsonplaceholder.config.Config;
import ru.vk.intern.model.Album;

import java.util.List;

@FeignClient(name = "albumHolder", configuration = Config.class)
public interface AlbumHolder {
    @GetMapping("/")
    List<Album> findAll();

    @GetMapping("/{id}")
    Album findById(@PathVariable(name = "id") long id);

    @PostMapping("/")
    Album addAlbum(Album album);

    @DeleteMapping("/{id}")
    void deleteAlbum(@PathVariable(name = "id") long id);
}