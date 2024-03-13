package ru.vk.intern;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;
import java.util.Objects;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AlbumHandlersTests extends Tests {
    private final MockHttpSession session = new MockHttpSession();
    private final String credentials = "{\"username\":\"roleAlbum\",\"password\":\"roleAlbum\"}";

    @Test
    @DisplayName("Get handler")
    void testGetHandler() throws Exception {
        mockMvc.perform(get(BASE_URL + "/albums/")
                        .contentType(APPLICATION_JSON)
                        .session(session))
                .andExpect(status().isForbidden())
                .andExpect(status().reason("Log in to get access"));

        login(session, credentials);

        getOk("/albums/", session);
        getOk("/albums/9", session);
    }

    @Test
    @DisplayName("Post handler")
    void testPostHandler() throws Exception {
        login(session, credentials);

        String jsonNewAlbum = String.format(ALBUM_TEMPLATE, lorem.getTitle(3, 10));

        mockMvc.perform(post(BASE_URL + "/albums/")
                        .contentType(APPLICATION_JSON)
                        .session(session)
                        .content(jsonNewAlbum))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Delete handler")
    void testDeleteHandler() throws Exception {
        login(session, credentials);

        String jsonNewAlbum = String.format(ALBUM_TEMPLATE, lorem.getTitle(3, 10));

        MvcResult result = mockMvc.perform(post(BASE_URL + "/albums/")
                        .contentType(APPLICATION_JSON)
                        .session(session)
                        .content(jsonNewAlbum))
                .andExpect(status().isCreated())
                .andReturn();

        String id = Arrays.stream(Objects.requireNonNull(result.getResponse().getHeader("Location"))
                .split("/")).toList().getLast();

        mockMvc.perform(delete(BASE_URL + "/albums/" + id)
                        .contentType(APPLICATION_JSON)
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(content().string("Album deleted successfully"));

        mockMvc.perform(delete(BASE_URL + "/albums/" + 223492389)
                        .contentType(APPLICATION_JSON)
                        .session(session))
                .andExpect(status().isNoContent())
                .andExpect(content().string("No such album"));
    }
}
