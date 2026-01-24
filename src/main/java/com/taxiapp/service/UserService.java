package com.taxiapp.service;

import com.taxiapp.model.dto.LoginDto;
import com.taxiapp.model.dto.LoginResponseDto;
import com.taxiapp.model.dto.RegisterDto;
import com.taxiapp.model.entity.Role;
import com.taxiapp.model.entity.RoleEnum;
import com.taxiapp.model.entity.User;
import com.taxiapp.repository.RoleRepository;
import com.taxiapp.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;


    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public LoginResponseDto login(LoginDto request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        List<String> roles = user.getRoles() == null
                ? List.of()
                : user.getRoles().stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toList());

        return new LoginResponseDto(user.getUsername(), roles);
    }

    public void saveUser(RegisterDto dto) {


        User user = new User();
        user.setName(dto.getName());
        user.setSurname(dto.getSurname());
        user.setEmail(dto.getEmail());
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        Role defaultRole = roleRepository.findByName(RoleEnum.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Default role not found"));

        user.setRoles(Set.of(defaultRole));

        userRepository.save(user);
    }


    public boolean deleteUserByUsername(String username) {
        Optional<User> optionalUser = userRepository.findByUsername(username);

        if (optionalUser.isPresent()) {
            userRepository.delete(optionalUser.get());
            return true;
        }

        return false;
    }

    public List<String> getAllUsers() {
        return userRepository.findAllNonAdminUsernames();
    }

}
