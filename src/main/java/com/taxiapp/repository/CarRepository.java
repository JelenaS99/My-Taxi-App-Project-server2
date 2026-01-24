package com.taxiapp.repository;

import com.taxiapp.model.entity.Car;
import com.taxiapp.model.entity.CarEnum;
import com.taxiapp.model.entity.PaymentMethodEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

    Optional<Car> findByName(CarEnum type);
}
