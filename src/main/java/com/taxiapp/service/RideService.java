package com.taxiapp.service;

import com.taxiapp.model.dto.RideDto;
import com.taxiapp.model.entity.*;
import com.taxiapp.repository.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RideService {

    private final RideRepository rideRepository;
    private final CityRepository cityRepository;
    private final CarRepository carRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final UserRepository userRepository;

    public RideService(
            RideRepository rideRepository,
            CityRepository cityRepository,
            CarRepository carRepository,
            PaymentMethodRepository paymentMethodRepository,
            UserRepository userRepository) {
        this.rideRepository = rideRepository;
        this.cityRepository = cityRepository;
        this.carRepository = carRepository;
        this.paymentMethodRepository = paymentMethodRepository;
        this.userRepository = userRepository;
    }

    public void createRide(RideDto dto) {

        User user = userRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        City begin = cityRepository.findByName(CityEnum.valueOf(dto.getBeginCity()))
                .orElseThrow(() -> new IllegalArgumentException("City not found"));

        City end = cityRepository.findByName(CityEnum.valueOf(dto.getEndCity()))
                .orElseThrow(() -> new IllegalArgumentException("City not found"));

        Car car = carRepository.findByName(CarEnum.valueOf(dto.getCar()))
                .orElseThrow(() -> new IllegalArgumentException("Car not found"));

        PaymentMethod payment = paymentMethodRepository.findByName(PaymentMethodEnum.valueOf(dto.getPaymentMethod()))
                .orElseThrow(() -> new IllegalArgumentException("Payment method not found"));


        Ride ride = new Ride();
        ride.setUser(user);
        ride.setBeginPoint(begin);
        ride.setEndPoint(end);
        ride.setCar(car);
        ride.setPaymentMethod(payment);


        rideRepository.save(ride);
    }

    public List<RideDto> getAllRidesForUser(String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isAdmin = user.getRoles().stream()
                .anyMatch(role -> role.getName() == RoleEnum.ROLE_ADMIN);

        return rideRepository.findAll().stream()
                .filter(ride -> {
                    if (isAdmin) {
                        return true;
                    }
                    return ride.getUser().getUsername().equals(username);
                })
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }


    private RideDto mapToDto(Ride ride) {
        RideDto dto = new RideDto();
        dto.setId(ride.getId());
        dto.setUsername(ride.getUser().getUsername());
        dto.setBeginCity(String.valueOf(ride.getBeginPoint().getName()));
        dto.setEndCity(String.valueOf(ride.getEndPoint().getName()));
        dto.setCar(String.valueOf(ride.getCar().getName()));
        dto.setPaymentMethod(String.valueOf(ride.getPaymentMethod().getName()));
        dto.setDateTime(ride.getDateTime());
        return dto;
    }

    public List<Ride> getAllRides() {
        return rideRepository.findAll();
    }

    public boolean deleteRideById(Long id) {
        if (rideRepository.existsById(id)) {
            rideRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }


    public int getRideCountForUser(String username) {
        return rideRepository.countByUserUsername(username);
    }
}
