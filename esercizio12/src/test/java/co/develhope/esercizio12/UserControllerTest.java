package co.develhope.esercizio12;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(value = "test")
public class UserControllerTest {
    @Autowired
    private UserController userController;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void contextLoad() throws Exception{
        assertThat(userController).isNotNull();
    }

    private User createUser() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setName("Erik");
        user.setSurname("Marigliano");
        user.setActive(false);
        String userJson = objectMapper.writeValueAsString(user);

        MvcResult result = this.mockMvc.perform(post("/user/post")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(status().isOk())
                .andReturn();

        return objectMapper.readValue(result.getResponse().getContentAsString(), User.class);
    }

    private User getUserById(Long id) throws  Exception{
        MvcResult result = this.mockMvc.perform(get("/user/get/" + id))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        try{
            String userJson = result.getResponse().getContentAsString();
            return objectMapper.readValue(userJson, User.class);
        } catch(Exception e){
            return null;
        }
    }

    @Test
    void createUserTest() throws Exception{
        User user = createUser();
        assertThat(user.getId()).isNotNull();
    }

    @Test
    void getUserByIdTest() throws Exception{
        User user = createUser();
        assertThat(user.getId()).isNotNull();
        User userResponse = getUserById(user.getId());
        assertThat(userResponse.getId()).isEqualTo(user.getId());
    }
    @Test
    void updateActiveTest() throws  Exception{
        User user = createUser();
        assertThat(user.getId()).isNotNull();

        MvcResult result = this.mockMvc.perform(put("/user/put/" + user.getId() + "?isActive=true"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        User userFromResponse = objectMapper.readValue(result.getResponse().getContentAsString(), User.class);
        assertThat(userFromResponse.getId()).isEqualTo(user.getId());
        assertThat(userFromResponse.isActive()).isEqualTo(true);
    }
    @Test
    void deleteUserTest() throws  Exception{
        User user = createUser();
        assertThat(user.getId()).isNotNull();

        MvcResult result = this.mockMvc.perform(delete("/user/delete/" + user.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        User userFromResponseGet = getUserById(user.getId());
        assertThat(userFromResponseGet).isNull();
    }
}
