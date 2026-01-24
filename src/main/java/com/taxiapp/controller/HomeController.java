package com.taxiapp.controller;

import com.taxiapp.service.RideService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final RideService rideService;

    public HomeController(RideService rideService) {
        this.rideService = rideService;
    }

    @GetMapping("/")
    public String home(Model model) {

        model.addAttribute("rides", rideService.getAllRides());


        return "home";
    }
}
