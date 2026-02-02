package com.taxiapp;

import com.google.gson.Gson;
import com.taxiapp.model.dto.LoginResponseDto;
import com.taxiapp.model.dto.RegisterDto;
import com.taxiapp.model.entity.User;
import com.taxiapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.transaction.Transactional;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserControllerAndServiceIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Gson gson;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;


    private RegisterDto registerDto;
    private String regDto;

    @BeforeEach
    public void set_up_User() throws Exception {
        //given
        registerDto = RegisterDto.builder()
                .name("Pera")
                .surname("Peric")
                .email("pera@gmail.com")
                .username("pera")
                .password("pera1")
                .build();

        regDto = gson.toJson(registerDto);

        //when
        MvcResult result = mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(regDto))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void test_Successfully_Register_User() throws Exception {

        //then
        Optional<User> pera = userRepository.findByUsername("pera");
        assertTrue(pera.isPresent());
        User userFromDatabase = pera.get();
        assertEquals(registerDto.getName(), userFromDatabase.getName());
        assertEquals(registerDto.getSurname(), userFromDatabase.getSurname());
        assertEquals(registerDto.getEmail(), userFromDatabase.getEmail());
        assertEquals(registerDto.getUsername(), userFromDatabase.getUsername());
        assertTrue(passwordEncoder.matches(registerDto.getPassword(), userFromDatabase.getPassword()));


    }

    @Test
    public void test_Successfully_Login_User() throws Exception {
        // given
        String loginJson = gson.toJson(Map.of(
                "username", registerDto.getUsername(),
                "password", registerDto.getPassword()
        ));

        // when
        MvcResult result = mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isOk())
                .andReturn();

        // then
        String responseContent = result.getResponse().getContentAsString();
        assertTrue(responseContent.contains(registerDto.getUsername()));

        LoginResponseDto responseDto = gson.fromJson(responseContent, LoginResponseDto.class);
        assertTrue(responseDto.getRoles().contains("ROLE_USER"));


    }

    @Test
    public void test_Login_Fails_With_Wrong_Password() throws Exception {
        // given
        String loginJson = gson.toJson(Map.of(
                "username", registerDto.getUsername(),
                "password", "pogresnaLozinka"
        ));

        // when & then
        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void test_Delete_Existing_User() throws Exception {
        // given: korisnik "pera" postoji iz @BeforeEach

        // when
        mockMvc.perform(delete("/api/users/delete/{username}", registerDto.getUsername()))
                .andExpect(status().isNoContent());

        // then: proveravamo da korisnik vise ne postoji u bazi
        Optional<User> deletedUser = userRepository.findByUsername(registerDto.getUsername());
        assertTrue(deletedUser.isEmpty());
    }

    @Test
    public void test_Delete_NonExisting_User() throws Exception {
        // when: brisemo nepostojeceg korisnika
        mockMvc.perform(delete("/api/users/delete/{username}", "nepostojeci"))
                .andExpect(status().isNotFound());
    }


    @Test
    public void test_Ride_Count_For_User() throws Exception {
        // setupUser ne kreira ride pa ocekujemo 0
        MvcResult result = mockMvc.perform(get("/api/users/count/{username}", registerDto.getUsername()))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        int count = Integer.parseInt(responseContent);
        assertEquals(0, count);
    }

    @Test
    public void test_Get_All_Users() throws Exception {

        MvcResult result = mockMvc.perform(get("/api/users/allUsers"))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        String[] usernames = gson.fromJson(responseContent, String[].class);
        boolean peraFound = false;
        for (String username : usernames) {
            if (username.equals(registerDto.getUsername())) {
                peraFound = true;
                break;
            }
        }
        assertTrue(peraFound, "User 'pera' treba da bude na listi");
    }


}




