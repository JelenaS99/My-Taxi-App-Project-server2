package com.taxiapp.repository;

import com.taxiapp.model.entity.PaymentMethod;
import com.taxiapp.model.entity.PaymentMethodEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {

    Optional<PaymentMethod> findByName(PaymentMethodEnum type);
}
