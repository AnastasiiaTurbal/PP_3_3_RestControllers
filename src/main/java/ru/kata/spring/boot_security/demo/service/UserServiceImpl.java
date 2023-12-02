package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.UserRepository;
import ru.kata.spring.boot_security.demo.model.User;
import java.util.List;

//задача UserDetailsService предоставлять пользователя по его username
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository dao;
    private final PasswordEncoder passwordEncoder;

    UserServiceImpl(UserRepository dao, PasswordEncoder passwordEncoder) {
        this.dao = dao;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getUsers(){
        return dao.findAll();
    }

    public User showUser(Long id) { return dao.findById(id).get(); }

    public void addUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        dao.save(user);
    }

    public void updateUser(User user) {
        if (user.getPassword().isEmpty()) {
            user.setPassword(showUser(user.getId()).getPassword());
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        dao.save(user); }

    public void deleteUser(Long id) {
        dao.deleteById(id);
    }

    public User findUserByUserName(String username) { return dao.findUserByUsername(username); }

    @Override
    @Transactional
    //перевод обычного User в User, которым может оперировать Spring Sequrity
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findUserByUserName(username);
        if(user == null) {
            throw new UsernameNotFoundException(String.format("User '%s' not found", username));
        }
        return new org.springframework.security.core.userdetails
                    .User(user.getUsername(), user.getPassword(), user.getAuthorities());
    }

}
