package ru.kata.spring.boot_security.demo.controller;


import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserResource {

    private final UserService userService;

    public UserResource(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public List<User> getAllUsers() {
        return userService.getUsers();
    }

    @PostMapping()
    public void addUser(@RequestBody User user) { //@RequestBody говорит о том, что зачение параметра user подставляется из тела запроса
        userService.addUser(user);
    }

    @GetMapping("/{userId}")
    public User getUser(@PathVariable long userId) {
        return userService.showUser(userId);
    }

    @PutMapping("/{userId}")
    public void updateUser(@PathVariable long userId, @RequestBody User user) {
        user.setId(userId);
        userService.updateUser(user);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable long userId) {
        userService.deleteUser(userId);
    }

}