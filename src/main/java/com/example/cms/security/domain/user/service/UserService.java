package com.example.cms.security.domain.user.service;

import com.example.cms.security.domain.role.entity.RoleEntity;
import com.example.cms.security.domain.role.repository.RoleRepository;
import com.example.cms.security.domain.user.entity.UserEntity;
import com.example.cms.security.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;

@Service("userService")
public class UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public UserEntity findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void saveUser(UserEntity user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setActive(true);
        RoleEntity userRole = roleRepository.findByName("ADMIN");
        user.setRoles(new ArrayList<>(Arrays.asList(userRole)));
        userRepository.save(user);
    }

    public UserEntity findUserById(Long id) {
        return userRepository.getById(id);

    }
}
