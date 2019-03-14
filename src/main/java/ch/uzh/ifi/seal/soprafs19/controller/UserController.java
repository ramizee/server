//Post schicken, Put updaten, Get zurückholen
package ch.uzh.ifi.seal.soprafs19.controller;

import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@RestController
public class UserController {

    private final UserService service;

    UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("/users") //bekommt alle user zurück mit Funktion getUsers
    Iterable <User> all() {
        //System.out.println("User werden gesucht!");
        return service.getUsers();
    }

    @GetMapping("/users/{userId}")
        //System.out.println("User mit dieser ID wird mit GetMapping gesucht");
    ResponseEntity <User> one(@PathVariable("userId") long id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.getUser(id));
        }
        catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found", ex);
        }
    }

    @PostMapping("/login")
    ResponseEntity <User> login(@RequestBody User user) {
        try{
            return ResponseEntity.status(HttpStatus.OK).body(service.login(user));
        }
        catch (Exception ex) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "Wrong username or password", ex
            );
        }

    }

    @PostMapping("/logout/{userId}")
    User logout(@PathVariable ("userId") long id) {
        return this.service.logout(id);
    }


    @PostMapping("/users")
    ResponseEntity <User> createUser(@RequestBody User newUser) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(service.createUser(newUser));
        }
        catch (Exception ex) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Username already taken", ex
            );
        }
    }

    @CrossOrigin
    @PutMapping("/users/{userId}")
    ResponseEntity <User> replaceUser(@RequestBody User newUser, @PathVariable ("userId") Long userId) {
        User dbUser = this.service.getUser(userId);
        if (dbUser != null) {
            return ResponseEntity.status(HttpStatus.OK).body(service.replaceUser(userId, newUser));
        }
        else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
    }
}
