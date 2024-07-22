package com.openmpy.backend.controller;

import com.openmpy.backend.dto.SignupUser;
import com.openmpy.backend.entity.User;
import com.openmpy.backend.jwt.JwtUtil;
import com.openmpy.backend.service.CustomUserDetailService;
import com.openmpy.backend.service.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.naming.AuthenticationException;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/users")
@RestController
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final CustomUserDetailService customUserDetailService;

    @PostMapping("/signup")
    public ResponseEntity<User> createUser(
            @RequestBody SignupUser signupUser
    ) {
        User user = userService.createUser(signupUser);
        return ResponseEntity.ok(user);
    }

    @GetMapping
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.ok(userService.getUsers());
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "ID of the user to be deleted", required = true) @PathVariable Long userId
    ) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/login")
    public String login(
            @RequestParam String username,
            @RequestParam String password,
            HttpServletResponse response
    ) throws AuthenticationException {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        UserDetails userDetails = customUserDetailService.loadUserByUsername(username);

        String token = jwtUtil.generateToken(userDetails.getUsername());
        Cookie cookie = new Cookie("onion_token", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60);

        response.addCookie(cookie);
        return token;
    }

    @PostMapping("/logout")
    public void logout(
            HttpServletResponse response
    ) {
        Cookie cookie = new Cookie("onion_token", null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);

        response.addCookie(cookie);
    }

    @PostMapping("/token/validation")
    @ResponseStatus(HttpStatus.OK)
    public void jwtValidate(
            @RequestParam String token
    ) {
        if (!jwtUtil.validateToken(token)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Token is not validation");
        }
    }
}
