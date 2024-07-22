package com.openmpy.backend.service;

import com.openmpy.backend.dto.SignupUser;
import com.openmpy.backend.entity.User;
import com.openmpy.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User createUser(SignupUser signupUser) {
        User user = new User();
        user.setUsername(signupUser.getUsername());
        user.setPassword(passwordEncoder.encode(signupUser.getPassword()));
        user.setEmail(signupUser.getEmail());
        return userRepository.save(user);
    }

    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }
}
