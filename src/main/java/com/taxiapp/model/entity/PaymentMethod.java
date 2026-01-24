package com.taxiapp.model.entity;


import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "payment_methods")
public class PaymentMethod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(unique = true, nullable = false)
    private PaymentMethodEnum name;

    @NotNull
    private String description;


    public PaymentMethod(PaymentMethodEnum name, String description) {
        this.name = name;
        this.description = description;
    }
}

