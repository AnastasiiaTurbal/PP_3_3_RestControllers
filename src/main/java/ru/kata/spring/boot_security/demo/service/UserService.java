package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserService extends UserDetailsService {
    public List<User> getUsers();

    public User showUser(Long id);

    public void addUser(User user);

    public void updateUser(User user);

    public void deleteUser(Long id);

    public User findUserByUserName(String username);

}
