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
        //model.addAttribute("allRoles", roleService.findAllRoles());
        model.addAttribute("currentUser", userService.findUserByUserName(principal.getName()));
        model.addAttribute("allRoles", roleService.findAllRoles());
        return "index";
    }

}
