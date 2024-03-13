package ru.vk.intern;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class HandlersTests extends Tests {
    @Test
    @DisplayName("User handlers")
    void testUserHandlers() throws Exception {
        MockHttpSession session = new MockHttpSession();
        String jsonRequest = "{\"login\":\"roleUser\",\"password\":\"roleUser\"}'";

        mockMvc.perform(get(BASE_URL + "/users/")
                        .contentType(APPLICATION_JSON)
                        .session(session))
                .andExpect(status().isForbidden())
                .andExpect(status().reason("Log in to get access"));

        login(session, jsonRequest);

        mockMvc.perform(get(BASE_URL + "/users/")
                        .contentType(APPLICATION_JSON)
                        .session(session))
                .andExpect(status().isOk());

        mockMvc.perform(get(BASE_URL + "/users/9")
                        .contentType(APPLICATION_JSON)
                        .session(session))
                .andExpect(status().isOk());

//        String jsonNewUser = String.format("""
//                {
//                    "name": "%s",
//                    "username": "%s",
//                    "email": "%s",
//                    "address": {
//                      "street": "%s",
//                      "suite": "%s",
//                      "city": "%s",
//                      "zipcode": "%s",
//                      "geo": {
//                        "lat": "%s",
//                        "lng": "%s"
//                      }
//                    },
//                    "phone": "%s",
//                    "website": "%s",
//                    "company": {
//                      "name": "%s",
//                      "catchPhrase": "%s",
//                      "bs": "%s"
//                    }
//                }
//                """, "Bret Cooper", "AnotherBret", "Sincere.yours@april.ibiza", "Super street", "Apt. 69", "Miami",
//                "92348-9944", "-3.3", "66.6666", "8-800-555-35-35", "meow.org", "X",
//                "No waay", "social media");
//
//        mockMvc.perform(post(BASE_URL + "/users/")
//                    .contentType(APPLICATION_JSON)
//                    .session(session)
//                    .content(jsonNewUser))
//                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Post handlers")
    void testPostHandlers() throws Exception {
        MockHttpSession session = new MockHttpSession();
        String jsonRequest = "{\"login\":\"rolePost\",\"password\":\"rolePost\"}'";

        mockMvc.perform(get(BASE_URL + "/posts/")
                        .contentType(APPLICATION_JSON)
                        .session(session))
                .andExpect(status().isForbidden())
                .andExpect(status().reason("Log in to get access"));

        login(session, jsonRequest);

        mockMvc.perform(get(BASE_URL + "/posts/")
                        .contentType(APPLICATION_JSON)
                        .session(session))
                .andExpect(status().isOk());

        mockMvc.perform(get(BASE_URL + "/posts/9")
                        .contentType(APPLICATION_JSON)
                        .session(session))
                .andExpect(status().isOk());
    }
}
