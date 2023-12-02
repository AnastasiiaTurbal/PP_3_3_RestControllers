package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.dao.RoleRepository;
import ru.kata.spring.boot_security.demo.model.Role;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository dao;

    RoleServiceImpl(RoleRepository dao) {
        this.dao = dao;
    }

    public void addRole(Role role) {
        dao.save(role);
    }

    public void deleteRole(Long id) {
        dao.deleteById(id);
    }

    public List<Role> findAllRoles() {return dao.findAll(); }

    public Role findRoleByName(String name) {return dao.findRoleByName(name); };

}
