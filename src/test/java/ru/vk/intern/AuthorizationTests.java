package ru.vk.intern;

import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthorizationTests extends Tests {
    @Test
    @DisplayName("Successful registration")
    void register() throws Exception {
        String login = RandomString.make(15);
        String password = RandomString.make(15);
        String jsonRequest = String.format("{\"login\" :\"%s\", \"password\": \"%s\"}", login, password);
        mockMvc.perform(post(BASE_URL + "/register/")
                        .contentType(APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(content().string("Account registered successfully"));
    }

    @Test
    @DisplayName("Register existing user attempt")
    void testExistingUserRegistrationAttempt() throws Exception {
        String jsonRequest = "{\"login\":\"admin\",\"password\":\"admin\"}'";

        mockMvc.perform(post(BASE_URL + "/register/")
                        .contentType(APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Login is in use already"));
    }

    @Test
    @DisplayName("Repeated log in attempt")
    void testRepeatedLoginAttempt() throws Exception {
        MockHttpSession session = new MockHttpSession();
        String jsonRequest = "{\"login\":\"admin\",\"password\":\"admin\"}'";
        login(session, jsonRequest);

        mockMvc.perform(post(BASE_URL + "/login/")
                        .contentType(APPLICATION_JSON)
                        .session(session)
                        .content(jsonRequest))
                .andExpect(status().isForbidden())
                .andExpect(status().reason("You are already logged in"));
    }

    @Test
    @DisplayName("No access attempt")
    void testNoAccessAttempt() throws Exception {
        MockHttpSession session = new MockHttpSession();
        String jsonRequest = "{\"login\":\"roleUser\",\"password\":\"roleUser\"}'";
        login(session, jsonRequest);

        mockMvc.perform(get(BASE_URL + "/posts/")
                        .contentType(APPLICATION_JSON)
                        .session(session))
                .andExpect(status().isForbidden())
                .andExpect(status().reason("You have no access"));
    }

    @Test
    @DisplayName("Wrong password")
    void testLoginWrongPassword() throws Exception {
        String jsonRequest = "{\"login\":\"admin\",\"password\":\"qwerty\"}'";
        processInvalidCredentials(jsonRequest);
    }

    @Test
    @DisplayName("Non-existent login")
    void testLoginNonExistentUsername() throws Exception {
        String jsonRequest = "{\"login\":\"nonExistentUser2003\",\"password\":\"qwerty\"}'";
        processInvalidCredentials(jsonRequest);
    }

    private void processInvalidCredentials(String request) throws Exception {
        mockMvc.perform(post(BASE_URL + "/login/")
                        .contentType(APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid login or password"));
    }
}
