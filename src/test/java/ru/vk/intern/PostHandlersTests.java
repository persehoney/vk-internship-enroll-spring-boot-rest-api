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
class PostHandlersTests extends Tests {
    private final MockHttpSession session = new MockHttpSession();
    private final String credentials = "{\"username\":\"rolePost\",\"password\":\"rolePost\"}";

    @Test
    @DisplayName("Get handler")
    void testGetHandler() throws Exception {
        mockMvc.perform(get(BASE_URL + "/posts/")
                        .contentType(APPLICATION_JSON)
                        .session(session))
                .andExpect(status().isForbidden())
                .andExpect(status().reason("Log in to get access"));

        login(session, credentials);

        getOk("/posts/", session);
        getOk("/posts/9", session);
    }

    @Test
    @DisplayName("Post handler")
    void testPostHandler() throws Exception {
        login(session, credentials);

        String jsonNewPost = String.format(POST_TEMPLATE, lorem.getTitle(3, 10), lorem.getWords(100, 150));

        mockMvc.perform(post(BASE_URL + "/posts/")
                        .contentType(APPLICATION_JSON)
                        .session(session)
                        .content(jsonNewPost))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Delete handler")
    void testDeleteHandler() throws Exception {
        login(session, credentials);

        String jsonNewPost = String.format(POST_TEMPLATE, lorem.getTitle(3, 10), lorem.getWords(100, 150));

        MvcResult result = mockMvc.perform(post(BASE_URL + "/posts/")
                        .contentType(APPLICATION_JSON)
                        .session(session)
                        .content(jsonNewPost))
                .andExpect(status().isCreated())
                .andReturn();

        String id = Arrays.stream(Objects.requireNonNull(result.getResponse().getHeader("Location"))
                .split("/")).toList().getLast();

        mockMvc.perform(delete(BASE_URL + "/posts/" + id)
                        .contentType(APPLICATION_JSON)
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(content().string("Post deleted successfully"));

        mockMvc.perform(delete(BASE_URL + "/posts/" + 223492389)
                        .contentType(APPLICATION_JSON)
                        .session(session))
                .andExpect(status().isNoContent())
                .andExpect(content().string("No such post"));
    }
}
