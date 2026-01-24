package com.taxiapp.repository;

import com.taxiapp.model.entity.City;
import com.taxiapp.model.entity.CityEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {

    Optional<City> findByName(CityEnum name);
}
