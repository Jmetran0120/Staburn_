package com.gabriel.serviceimpl;

import com.gabriel.entity.UserData;
import com.gabriel.model.User;
import com.gabriel.repository.UserDataRepository;
import com.gabriel.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDataRepository userDataRepository;
    
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public User findByEmail(String email) {
        Optional<UserData> optional = userDataRepository.findByEmail(email);
        if (optional.isPresent()) {
            UserData userData = optional.get();
            User user = new User();
            user.setId(userData.getId());
            user.setEmail(userData.getEmail());
            user.setName(userData.getName());
            user.setRole(userData.getRole().toString());
            return user;
        }
        return null;
    }

    @Override
    public User create(User user, String password) {
        log.info("Creating user: {}", user.getEmail());
        UserData userData = new UserData();
        userData.setEmail(user.getEmail());
        userData.setName(user.getName());
        // Set role from string to enum
        if (user.getRole() != null) {
            try {
                userData.setRole(UserData.UserRole.valueOf(user.getRole().toLowerCase()));
            } catch (IllegalArgumentException e) {
                userData.setRole(UserData.UserRole.customer);
            }
        } else {
            userData.setRole(UserData.UserRole.customer);
        }
        // Hash the password before saving
        userData.setPassword(passwordEncoder.encode(password));
        UserData savedUserData = userDataRepository.save(userData);
        log.info("Created user with id: {}", savedUserData.getId());
        
        // Convert back to User model (without password)
        User createdUser = new User();
        createdUser.setId(savedUserData.getId());
        createdUser.setEmail(savedUserData.getEmail());
        createdUser.setName(savedUserData.getName());
        createdUser.setRole(savedUserData.getRole().toString());
        return createdUser;
    }

    @Override
    public User findById(Integer id) {
        Optional<UserData> optional = userDataRepository.findById(id);
        if (optional.isPresent()) {
            UserData userData = optional.get();
            User user = new User();
            user.setId(userData.getId());
            user.setEmail(userData.getEmail());
            user.setName(userData.getName());
            user.setRole(userData.getRole().toString());
            return user;
        }
        return null;
    }
    
    public boolean verifyPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
    
    public UserData getUserDataByEmail(String email) {
        Optional<UserData> optional = userDataRepository.findByEmail(email);
        return optional.orElse(null);
    }
}

