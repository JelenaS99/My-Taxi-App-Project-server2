package com.taxiapp.controller;

import com.taxiapp.model.dto.RideDto;
import com.taxiapp.model.entity.Ride;
import com.taxiapp.service.RideService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/rides")
public class RideController {

    private final RideService rideService;

    public RideController(RideService rideService) {
        this.rideService = rideService;
    }

    @PostMapping("/createRide")
    public ResponseEntity<Void> createRide(@RequestBody RideDto dto) {
        rideService.createRide(dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/getRides")
    public List<RideDto> getAllRidesForUser(@RequestParam(required = false) String username) {
        return rideService.getAllRidesForUser(username);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRide(@PathVariable Long id) {
        boolean deleted = rideService.deleteRideById(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }

    }


}