package ch.uzh.ifi.seal.soprafs19.service;

import ch.uzh.ifi.seal.soprafs19.Application;
import ch.uzh.ifi.seal.soprafs19.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.repository.UserRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Test class for the UserResource REST resource.
 *
 * @see UserService
 */
@WebAppConfiguration
@RunWith(SpringRunner.class)
@SpringBootTest(classes= Application.class)
public class UserServiceTest {


    @Qualifier("userRepository")
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Test
    public void createUser() {
        Assert.assertNull(userRepository.findByUsername("testUsername"));

        User testUser = new User();
        testUser.setName("testName1");
        testUser.setUsername("testUsername1");
        testUser.setPassword("testPassword1");
        testUser.setBirthday("testBirthday1");

        User createdUser = userService.createUser(testUser);

        Assert.assertNotNull(createdUser.getToken());
        Assert.assertEquals(createdUser.getStatus(),UserStatus.OFFLINE);
        Assert.assertEquals(createdUser, userRepository.findByToken(createdUser.getToken()));
    }

    @Test
    public void testLogin(){
        Assert.assertNull(userRepository.findByUsername("testUsername2"));

        User testUser2 = new User();
        testUser2.setName("testName2");
        testUser2.setUsername("testUsername2");
        testUser2.setPassword("testPassword2");
        testUser2.setBirthday("testBirthday2");

        User createdUser2 = userService.createUser(testUser2);
        createdUser2.setStatus(UserStatus.ONLINE);

        userService.login(createdUser2);

        Assert.assertEquals(userRepository.findByUsername("testUsername2").getStatus(), UserStatus.ONLINE);
    }

    @Test
    public void testLogout(){
        Assert.assertNull(userRepository.findByUsername("testUsername3"));

        User testUser3 = new User();
        testUser3.setName("testName3");
        testUser3.setUsername("testUsername3");
        testUser3.setPassword("testPassword3");
        testUser3.setBirthday("testBirthday3");

        User createdUser3 = userService.createUser(testUser3);
        userService.login(createdUser3);
        long id3 = createdUser3.getId();
        userService.logout(id3);

        Assert.assertEquals(userRepository.findByUsername("testUsername3").getStatus(), UserStatus.OFFLINE);
    }

    @Test
    public void testReplaceUser(){
        Assert.assertNull(userRepository.findByUsername("testUsername4"));

        User testUser4 = new User();
        testUser4.setName("testName4");
        testUser4.setUsername("testUsername4");
        testUser4.setPassword("testPassword4");
        testUser4.setBirthday("testBirthday4");

        User createdUser4 = userService.createUser(testUser4);

        User appliedUser = new User();
        appliedUser.setUsername("NewUsername");
        appliedUser.setBirthday("NewBirthday");

        User testUser4applied = userService.replaceUser(createdUser4.getId(), appliedUser);

        Assert.assertEquals(testUser4applied.getUsername(), "NewUsername");
        Assert.assertEquals(testUser4applied.getBirthday(), "NewBirthday");
        Assert.assertEquals(testUser4applied.getPassword(), "testPassword4");
    }

    @Test
    public void testGetUser(){
        Assert.assertNull(userRepository.findByUsername("testUsername5"));

        User testUser5 = new User();
        testUser5.setUsername("testUsername5");
        testUser5.setName("testName5");
        testUser5.setPassword("testPassword5");
        testUser5.setBirthday("testBirthday5");

        User createdUser5 = userService.createUser(testUser5);

        User recievedUser5 = userService.getUser(createdUser5.getId());

        Assert.assertEquals(recievedUser5.getUsername(), "testUsername5");
        Assert.assertEquals(recievedUser5.getId(), createdUser5.getId());
    }
}
