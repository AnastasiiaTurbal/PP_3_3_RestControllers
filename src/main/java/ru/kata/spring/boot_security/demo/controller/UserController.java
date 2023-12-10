package ru.kata.spring.boot_security.demo.controller;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.RoleServiceImpl;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;

import java.security.Principal;


@Controller
public class UserController {

    private final UserService userService;
    private final RoleService roleService;

    UserController(UserServiceImpl userService, RoleServiceImpl roleService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/otherRole")
    public String showStartPage(Model model, Principal principal) {
        model.addAttribute("currentUser", userService.findUserByUserName(principal.getName()));
        return "/otherRole";
    }

    @GetMapping("/index")
    public String showAdminPage(Model model, Principal principal) {
        model.addAttribute("user", new User());
        model.addAttribute("allRoles", roleService.findAllRoles());
        model.addAttribute("currentUser", userService.findUserByUserName(principal.getName()));
        model.addAttribute("users", userService.getUsers());
        return "index";
    }

    @PostMapping("/index/new")
    public String addUser(@ModelAttribute("user") User user) {
        userService.addUser(user);
        return "redirect:/index";
    }

    @PatchMapping("/index/{id}/edit")
    public String updateUserByAdmin(@ModelAttribute("user") User user) {
        userService.updateUser(user);
        return "redirect:/index";
    }

    @DeleteMapping ("/index/{id}/delete")
    public String deleteUser(@ModelAttribute("user") User user) {
        userService.deleteUser(user.getId());
        return "redirect:/index";
    }

}
