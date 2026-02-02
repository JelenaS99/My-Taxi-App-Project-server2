package com.taxiapp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.taxiapp.model.dto.RegisterDto;
import com.taxiapp.model.dto.RideDto;
import com.taxiapp.model.entity.CarEnum;
import com.taxiapp.model.entity.CityEnum;
import com.taxiapp.model.entity.PaymentMethodEnum;
import com.taxiapp.model.entity.Ride;
import com.taxiapp.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class RideControllerAndServiceIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Gson gson;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RideRepository rideRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    private RegisterDto registerDto;
    private String regDto;
    private String username;

    private Ride savedRide;  // voznja kreirana u @BeforeEach
    private RideDto savedRideDto;  // DTO verzija za prosledjivanje

    @BeforeEach
    public void set_Up_User_And_Ride() throws Exception {
        // Gson sa LocalDateTime podrskom
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, (com.google.gson.JsonSerializer<LocalDateTime>)
                        (src, typeOfSrc, context) -> new com.google.gson.JsonPrimitive(src.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                .registerTypeAdapter(LocalDateTime.class, (com.google.gson.JsonDeserializer<LocalDateTime>)
                        (json, type, context) -> LocalDateTime.parse(json.getAsString(), DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .create();

        // Kreiramo korisnika pre svakog testa
        registerDto = RegisterDto.builder()
                .name("Pera")
                .surname("Peric")
                .email("pera@gmail.com")
                .username("pera")
                .password("pera1")
                .build();

        username = registerDto.getUsername();
        regDto = gson.toJson(registerDto);

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(regDto))
                .andExpect(status().isOk());

        // Kreiramo voznju pre svakog testa i cuvamo DTO
        savedRide = new Ride();
        savedRide.setUser(userRepository.findByUsername(username).get());
        savedRide.setBeginPoint(cityRepository.findByName(CityEnum.BELGRADE).get());
        savedRide.setEndPoint(cityRepository.findByName(CityEnum.NIS).get());
        savedRide.setCar(carRepository.findByName(CarEnum.CARGO).get());
        savedRide.setPaymentMethod(paymentMethodRepository.findByName(PaymentMethodEnum.CASH).get());
        savedRide.setDateTime(LocalDateTime.now());

        savedRide = rideRepository.saveAndFlush(savedRide);

        savedRideDto = new RideDto();
        savedRideDto.setUsername(username);
        savedRideDto.setBeginCity(savedRide.getBeginPoint().getName().name());
        savedRideDto.setEndCity(savedRide.getEndPoint().getName().name());
        savedRideDto.setCar(savedRide.getCar().getName().name());
        savedRideDto.setPaymentMethod(savedRide.getPaymentMethod().getName().name());
        savedRideDto.setDateTime(savedRide.getDateTime());
    }

    @Test
    public void test_Create_Ride() throws Exception {
        // Umesto kreiranja nove voznje, koristimo savedRideDto iz BeforeEach
        mockMvc.perform(post("/api/rides/createRide")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(savedRideDto)))
                .andExpect(status().isOk());

        // Proveravamo da je voznja u bazi
        List<Ride> rides = rideRepository.findAll();
        assertTrue(rides.size() >= 1); // barem jedna voznja iz BeforeEach

        // Uzimamo poslednju voznju da proverimo vrednosti
        Ride lastRide = rides.get(rides.size() - 1);
        assertEquals(username, lastRide.getUser().getUsername());
        assertEquals(CityEnum.BELGRADE, lastRide.getBeginPoint().getName());
        assertEquals(CityEnum.NIS, lastRide.getEndPoint().getName());
    }


    @Test
    public void test_Get_All_Rides_For_User() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/rides/getRides")
                        .param("username", username))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        RideDto[] rideDtos = gson.fromJson(content, RideDto[].class);

        assertEquals(1, rideDtos.length);
        assertEquals(username, rideDtos[0].getUsername());
        assertEquals("BELGRADE", rideDtos[0].getBeginCity());
        assertEquals("NIS", rideDtos[0].getEndCity());
    }

    @Test
    public void test_Delete_Ride_Successful() throws Exception {
        Long rideId = savedRide.getId();

        mockMvc.perform(delete("/api/rides/{id}", rideId))
                .andExpect(status().isNoContent());

        // Provera da je voznja obrisana
        boolean exists = rideRepository.existsById(rideId);
        assertFalse(exists);
    }


    @Test
    public void test_Get_All_Rides() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/rides/getAllRides"))
                .andExpect(status().isOk())
                .andReturn();

        RideDto[] rideDtos = gson.fromJson(
                result.getResponse().getContentAsString(),
                RideDto[].class
        );

        assertTrue(
                Arrays.stream(rideDtos).anyMatch(r ->
                        r.getUsername().equals(username) &&
                                r.getBeginCity().equals("BELGRADE") &&
                                r.getEndCity().equals("NIS")
                )
        );
    }


}
