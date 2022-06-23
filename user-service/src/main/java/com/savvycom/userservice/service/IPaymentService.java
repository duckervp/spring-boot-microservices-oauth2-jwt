package com.savvycom.userservice.service;

import com.savvycom.userservice.domain.entity.Payment;
import com.savvycom.userservice.domain.model.PaymentOutput;

import java.util.List;

public interface IPaymentService {
    List<PaymentOutput> findAll();

    PaymentOutput findById(Long id);

    List<PaymentOutput> findByUserId(Long userId);

    void save(Payment payment);

    void createCashInHandsPayment(Long userId);
}
