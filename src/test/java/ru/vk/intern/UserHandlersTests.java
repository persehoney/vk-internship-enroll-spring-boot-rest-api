package ru.vk.intern;

import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserHandlersTests extends Tests {
    private final MockHttpSession session = new MockHttpSession();
    private final String credentials = "{\"username\":\"roleUser\",\"password\":\"roleUser\"}";

    @Test
    @DisplayName("Get handler")
    void testGetHandler() throws Exception {
        mockMvc.perform(get(BASE_URL + "/users")
                        .contentType(APPLICATION_JSON)
                        .session(session))
                .andExpect(status().isForbidden())
                .andExpect(status().reason("Log in to get access"));

        login(session, credentials);

        getOk("/users", session);
        getOk("/users/9", session);
    }

    @Test
    @DisplayName("Post handler")
    void testPostHandler() throws Exception {
        login(session, credentials);

        String jsonNewUser = String.format(USER_TEMPLATE, lorem.getName(), RandomString.make(15), lorem.getEmail(),
                "Super street", "Apt. 69", lorem.getCity(), lorem.getZipCode(), "-3.3", "66.6666", lorem.getPhone(), "meow.org", "X",
                lorem.getWords(3, 6), "social media");

        mockMvc.perform(post(BASE_URL + "/users/")
                        .contentType(APPLICATION_JSON)
                        .session(session)
                        .content(jsonNewUser))
                .andExpect(status().isCreated())
                .andExpect(content().string("User created successfully"))
                .andReturn();

        mockMvc.perform(post(BASE_URL + "/users/")
                        .contentType(APPLICATION_JSON)
                        .session(session)
                        .content(jsonNewUser))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Username is already in use"));
    }

    @Test
    @DisplayName("Delete handler")
    void testDeleteHandler() throws Exception {
        login(session, credentials);

        String jsonNewUser = String.format(USER_TEMPLATE, lorem.getName(), RandomString.make(15), lorem.getEmail(),
                "Super street", "Apt. 69", lorem.getCity(), lorem.getZipCode(), "-3.3", "66.6666", lorem.getPhone(), "meow.org", "X",
                lorem.getWords(3, 6), "social media");

        MvcResult result = mockMvc.perform(post(BASE_URL + "/users/")
                        .contentType(APPLICATION_JSON)
                        .session(session)
                        .content(jsonNewUser))
                .andExpect(status().isCreated())
                .andExpect(content().string("User created successfully"))
                .andReturn();

        String id = getId(result);

        mockMvc.perform(delete(BASE_URL + "/users/" + id)
                        .contentType(APPLICATION_JSON)
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(content().string("User deleted successfully"));

        mockMvc.perform(delete(BASE_URL + "/users/" + id)
                        .contentType(APPLICATION_JSON)
                        .session(session))
                .andExpect(status().isNoContent())
                .andExpect(content().string("No such user"));
    }

    @Test
    @DisplayName("Put handler")
    void testPutHandler() throws Exception {
        login(session, credentials);

        String jsonUser = String.format(USER_TEMPLATE, lorem.getName(), RandomString.make(15), lorem.getEmail(),
                "Super street", "Apt. 69", lorem.getCity(), lorem.getZipCode(), "-3.3", "66.6666", lorem.getPhone(), "meow.org", "X",
                lorem.getWords(3, 6), "social media");

        MvcResult result =  mockMvc.perform(post(BASE_URL + "/users/")
                        .contentType(APPLICATION_JSON)
                        .session(session)
                        .content(jsonUser))
                .andExpect(status().isCreated())
                .andExpect(content().string("User created successfully"))
                .andReturn();

        String oldId = getId(result);

        String jsonNewUser = String.format(USER_TEMPLATE, lorem.getName(), RandomString.make(15), lorem.getEmail(),
                "Super street", "Apt. 69", lorem.getCity(), lorem.getZipCode(), "-3.3", "66.6666", lorem.getPhone(), "meow.org", "X",
                lorem.getWords(3, 6), "social media");

        result = mockMvc.perform(put(BASE_URL + "/users/" + oldId)
                        .contentType(APPLICATION_JSON)
                        .session(session)
                        .content(jsonNewUser))
                .andExpect(status().isCreated())
                .andExpect(content().string("User updated successfully"))
                .andReturn();

        String newId = getId(result);

        mockMvc.perform(get(BASE_URL + "/users/" + oldId)
                        .contentType(APPLICATION_JSON)
                        .session(session))
                .andExpect(status().isNotFound());

        jsonNewUser = String.format(USER_TEMPLATE, lorem.getName(), "admin", lorem.getEmail(),
                "Super street", "Apt. 69", lorem.getCity(), lorem.getZipCode(), "-3.3", "66.6666", lorem.getPhone(), "meow.org", "X",
                lorem.getWords(3, 6), "social media");

        mockMvc.perform(put(BASE_URL + "/users/" + newId)
                        .contentType(APPLICATION_JSON)
                        .session(session)
                        .content(jsonNewUser))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Cannot update: new username is already in use"));
    }
}
