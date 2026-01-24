package com.taxiapp.repository;

import com.taxiapp.model.entity.Ride;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RideRepository extends JpaRepository<Ride, Long> {


    int countByUserUsername(String username);



}
