//Post schicken, Put updaten, Get zurückholen
package ch.uzh.ifi.seal.soprafs19.controller;

import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    private final UserService service;

    UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("/users")
    Iterable<User> all() {
        //System.out.println("User werden gesucht!");
        //System.out.println(service.getUsers());
        return service.getUsers();
    }

    @GetMapping("/users/me")
        //System.out.println("User mit dieser ID wird mit GetMapping gesucht");
    User me(@RequestHeader("Access-Token") String token) {
        return service.getUserByToken(token);
    }

    @GetMapping("/users/{userId}")
        //System.out.println("User mit dieser ID wird mit GetMapping gesucht");
    User one(
            @PathVariable("userId") long id) {
        return service.getUser(id);
    }

    @PostMapping("/login")
        //mit token identifizieren
    User login(@RequestBody User user) {
        //System.out.println("Logging in!");
        return this.service.login(user);
    }

    @PostMapping("/logout/{userId}")
    User logout(@PathVariable ("userId") long id) {
        //System.out.println("Logging out!");
        return this.service.logout(id);
    }


    @PostMapping("/users")
        //System.out.println("User werden mit PostMapping gesucht");
    User createUser(@RequestBody User newUser) {
        return this.service.createUser(newUser);
    }

    @CrossOrigin
    @PutMapping("/users/{userId}")
    User replaceUser(@RequestBody User newUser, @PathVariable ("userId") Long userId) {
        User dbUser = this.service.getUser(userId);
        return this.service.replaceUser(userId, newUser);
    }
}
