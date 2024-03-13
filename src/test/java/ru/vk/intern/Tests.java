package ru.vk.intern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class Tests {
    public final static String BASE_URL = "/api";
    @Autowired
    public MockMvc mockMvc;

    public void login(MockHttpSession session, String request) throws Exception {
        mockMvc.perform(post(BASE_URL + "/login/")
                        .contentType(APPLICATION_JSON)
                        .session(session)
                        .content(request))
                .andExpect(status().isOk())
                .andExpect(content().string("User logged in successfully"));
    }
}
