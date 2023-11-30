package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.UserRepository;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

//задача UserDetailsService предоставлять пользователя по его username
@Service
public class UserService implements UserDetailsService {

    private final UserRepository dao;

    @Autowired
    UserService(UserRepository dao) { this.dao = dao; }

    public List<User> getUsers(){
        return dao.findAll();
    }

    public User showUser(Long id) { return dao.findById(id).get(); }

    public void addUser(User user) { dao.save(user); }

    public void updateUser(User user) { dao.save(user); }

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
