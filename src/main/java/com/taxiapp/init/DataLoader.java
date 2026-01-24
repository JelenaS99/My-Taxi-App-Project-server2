package com.taxiapp.init;


import com.taxiapp.model.entity.*;
import com.taxiapp.repository.CarRepository;
import com.taxiapp.repository.CityRepository;
import com.taxiapp.repository.PaymentMethodRepository;
import com.taxiapp.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Map;
@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void run(String... args) {

        loadCities();
        loadPaymentMethods();
        loadCars();
        loadRoles();
    }

    private void loadCities() {
        if (cityRepository.count() > 0) return;

        for (CityEnum cityEnum : CityEnum.values()) {
            cityRepository.save(
                    new City(cityEnum, cityEnum.getDescription())
            );
        }
    }

    private void loadPaymentMethods() {
        if (paymentMethodRepository.count() > 0) return;

        for (PaymentMethodEnum pm : PaymentMethodEnum.values()) {
            paymentMethodRepository.save(
                    new PaymentMethod(pm, pm.getDescription())
            );
        }
    }

    private void loadCars() {
        if (carRepository.count() > 0) return;

        for (CarEnum carEnum : CarEnum.values()) {
            carRepository.save(
                    new Car(carEnum, carEnum.getDescription())
            );
        }
    }

    private void loadRoles() {
        for (RoleEnum roleEnum : RoleEnum.values()) {
            roleRepository.findByName(roleEnum)
                    .orElseGet(() ->
                            roleRepository.save(
                                    new Role(null, roleEnum)
                            )
                    );
        }
    }
}
