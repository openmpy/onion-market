package com.openmpy.backend.controller;

import com.openmpy.backend.entity.User;
import com.openmpy.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/users")
@RestController
public class UserController {

    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<User> createUser(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String email
    ) {
        User user = userService.createUser(username, password, email);
        return ResponseEntity.ok(user);
    }
}
