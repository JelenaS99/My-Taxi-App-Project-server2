package com.taxiapp.controller;

import com.taxiapp.model.dto.LoginDto;
import com.taxiapp.model.dto.LoginResponseDto;
import com.taxiapp.model.dto.RegisterDto;
import com.taxiapp.model.dto.RideDto;
import com.taxiapp.model.entity.User;
import com.taxiapp.repository.RideRepository;
import com.taxiapp.repository.UserRepository;
import com.taxiapp.service.RideService;
import com.taxiapp.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    private final RideService rideService;

    public UserController(UserService userService, RideService rideService) {

        this.userService = userService;
        this.rideService = rideService;

    }

    @PostMapping("/register")
    public ResponseEntity<Void> registerUser(@RequestBody RegisterDto dto) {
        userService.saveUser(dto);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginDto request) {
        try {
            LoginResponseDto response = userService.login(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }


    @GetMapping("/count/{username}")
    public int getRideCount(@PathVariable String username) {
        return rideService.getRideCountForUser(username);
    }

    @GetMapping("/allUsers")
    public List<String> getAllUsernames() {
        return userService.getAllUsers();
    }

    @DeleteMapping("/delete/{username}")
    public ResponseEntity<Void> deleteUser(@PathVariable String username) {
        boolean deleted = userService.deleteUserByUsername(username);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }

    }


}

