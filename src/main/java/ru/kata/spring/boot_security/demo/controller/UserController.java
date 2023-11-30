package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;
import java.util.List;


@Controller
public class UserController {

    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    UserController(UserService userService, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/otherRole")
    public String showStartPage() {
    return "/otherRole";
    }

    @GetMapping("/admin")
    public String getUsers(Model model) {
        model.addAttribute("users", userService.getUsers());
        return "/admin";
    }

    @GetMapping("/admin/new")
    //public String newUser(@ModelAttribute("user") User user) {
    public String newUser(Model model) {
        User user = new User();
        List<Role> roles = roleService.findAllRoles();
        model.addAttribute("user", user);
        model.addAttribute("allRoles", roles);
        return "/new";
    }

    @PostMapping("/admin/new")
    public String addUser(@ModelAttribute("user") User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.addUser(user);
        return "redirect:/admin";
    }

    @GetMapping("/admin/{id}/edit")
    public String editUser(@PathVariable("id") Long id, Model model) {
        model.addAttribute("user", userService.showUser(id));
        List<Role> roles = roleService.findAllRoles();
        model.addAttribute("allRoles", roles);

        return "/edit";
    }

    @PatchMapping("/admin/{id}/edit")
    public String updateUserByAdmin(@ModelAttribute("user") User user) {
        if (user.getPassword().isEmpty()) {
            user.setPassword(userService.showUser(user.getId()).getPassword());
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        userService.updateUser(user);
        return "redirect:/admin";
    }

    @DeleteMapping ("/admin/{id}/delete")
    public String deleteUser(@ModelAttribute("user") User user) {
        userService.deleteUser(user.getId());
        return "redirect:/admin";
    }

    @GetMapping("/user")
    public String getUser(Model model, Principal principal) {
        User user = userService.findUserByUserName(principal.getName());
        model.addAttribute("user", user);
        return "/user";
    }

    @PatchMapping("/user/{id}/edit")
    public String updateUserByUser(@ModelAttribute("user") User user) {
        user.setRoles(userService.showUser(user.getId()).getRoles());
        if (user.getPassword().isEmpty()) {
            user.setPassword(userService.showUser(user.getId()).getPassword());
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        userService.updateUser(user);
        return "redirect:/user";
    }

}
