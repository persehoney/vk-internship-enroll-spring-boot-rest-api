package ru.vk.intern;

import com.thedeanda.lorem.Lorem;
import com.thedeanda.lorem.LoremIpsum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class Tests {
    public final static Lorem lorem = LoremIpsum.getInstance();
    public final static String BASE_URL = "/api";
    public final static String USER_TEMPLATE = """
            {
                "name": "%s",
                "username": "%s",
                "email": "%s",
                "address": {
                  "street": "%s",
                  "suite": "%s",
                  "city": "%s",
                  "zipcode": "%s",
                  "geo": {
                    "lat": "%s",
                    "lng": "%s"
                  }
                },
                "phone": "%s",
                "website": "%s",
                "company": {
                  "name": "%s",
                  "catchPhrase": "%s",
                  "bs": "%s"
                }
            }
            """;
    public final static String POST_TEMPLATE = """
            {
                "title": "%s",
                "body": "%s"
              }
            """;
    public final static String ALBUM_TEMPLATE = """
            {
                "title": "%s"
              }
            """;
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

    public void getOk(String url, MockHttpSession session) throws Exception {
        mockMvc.perform(get(BASE_URL + url)
                        .contentType(APPLICATION_JSON)
                        .session(session))
                .andExpect(status().isOk());
    }
}
