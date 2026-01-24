package com.taxiapp.model.dto;


import com.taxiapp.model.entity.Ride;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RideDto {

    private Long id;

    @NotBlank
    private String username;

    @NotBlank
    private String beginCity;

    @NotBlank
    private String endCity;

    @NotBlank
    private String car;

    @NotBlank
    private String paymentMethod;

    private LocalDateTime dateTime;


    public RideDto(Ride ride) {
        this.id = ride.getId();
        this.username = ride.getUser().getUsername();
        this.beginCity = String.valueOf(ride.getBeginPoint().getName());
        this.endCity = String.valueOf(ride.getEndPoint().getName());
        this.car = String.valueOf(ride.getCar().getName());
        this.paymentMethod = String.valueOf(ride.getPaymentMethod().getName());
        this.dateTime = ride.getDateTime();
    }

}
