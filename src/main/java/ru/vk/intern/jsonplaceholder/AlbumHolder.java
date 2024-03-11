package ru.vk.intern.jsonplaceholder;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.vk.intern.jsonplaceholder.config.Config;
import ru.vk.intern.model.Album;

import java.util.List;

@FeignClient(name = "albumHolder", configuration = Config.class)
public interface AlbumHolder {
    @GetMapping("/")
    List<Album> findAll();

    @GetMapping("/{id}")
    Album findById(@PathVariable(name = "id") long id);
}