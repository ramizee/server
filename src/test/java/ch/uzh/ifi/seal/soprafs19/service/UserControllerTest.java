package ch.uzh.ifi.seal.soprafs19.service;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ch.uzh.ifi.seal.soprafs19.entity.User;
import net.minidev.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@EnableWebMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Test
    public void oneIn() throws Exception {
        User testUser = new User();
        testUser.setUsername("testOneInUsername");
        testUser.setPassword("testOneInPassword");
        testUser.setName("testOneInPassword");
        testUser.setPassword("testOneInPassword");
        testUser = userService.createUser(testUser);

        this.mockMvc.perform(
                get("/users/{userId}", testUser.getId()))
                .andExpect(status().isOk());
    }

    @Test
    public void oneNotIn() throws Exception {
        this.mockMvc.perform(
                get("/users/1000"))
                .andExpect(status().is4xxClientError());
    }


    @Test
    public void createUserOk() throws Exception {
        JSONObject userJson = new JSONObject();
        userJson.put("username", "testCreateUsername");
        userJson.put("password", "testCreatePassword");
        userJson.put("name", "testCreateName");
        userJson.put("birthday", "testCreateBirthday");

        this.mockMvc.perform(
                post("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(userJson.toJSONString()))
                .andExpect(status().isCreated());
    }

    @Test
    public void createUserNotOk() throws Exception {
        User testUser = new User();
        testUser.setUsername("testCreate2Username");
        testUser.setPassword("testCreate2Password");
        testUser.setName("testCreate2Name");
        testUser.setBirthday("testCreate2Birthday");
        userService.createUser(testUser);

        JSONObject userJson = new JSONObject();
        userJson.put("username", "testCreate2Username");
        userJson.put("password", "testCreate2Password2");

        this.mockMvc.perform(
                post("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(userJson.toJSONString()))
                .andExpect(status().isConflict());
    }

    @Test
    public void loginOk() throws Exception {
        User testUser = new User();
        testUser.setUsername("testLoginUsername");
        testUser.setPassword("testLoginPassword");
        testUser.setName("testLoginName");
        testUser.setBirthday("testLoginBirthday");
        userService.createUser(testUser);

        JSONObject userJson = new JSONObject();
        userJson.put("username", "testLoginUsername");
        userJson.put("password", "testLoginPassword");

        this.mockMvc.perform(
                post("/login")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(userJson.toJSONString()))
                .andExpect(status().isOk());
    }

    @Test
    public void loginNotOkForbidden() throws Exception {
        User testUser = new User();
        testUser.setUsername("testLogin2Username");
        testUser.setPassword("testLogin2Password");
        testUser.setName("testLogin2Name");
        testUser.setBirthday("testLogin2Birthday");
        userService.createUser(testUser);

        JSONObject userJson = new JSONObject();
        userJson.put("username", "testLogin2Username");
        userJson.put("password", "testLogin2Password2");

        this.mockMvc.perform(
                post("/login")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(userJson.toJSONString()))
                .andExpect(status().isForbidden());
    }

    @Test
    public void loginNotOkNotFound() throws Exception {
        User testUser = new User();
        testUser.setUsername("testLogin3Username");
        testUser.setPassword("testLogin3Password");
        testUser.setName("testLogin3Name");
        testUser.setBirthday("testLogin3Birthday");
        userService.createUser(testUser);

        JSONObject userJson = new JSONObject();
        userJson.put("username", "testLogin3Username2");
        userJson.put("password", "testLogin3Password");


        this.mockMvc.perform(
                post("/login")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(userJson.toJSONString()))
                .andExpect(status().isForbidden());
    }

}

