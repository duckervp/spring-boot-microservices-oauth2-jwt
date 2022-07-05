package com.savvycom.userservice.service;

import com.savvycom.userservice.domain.entity.Payment;
import com.savvycom.userservice.domain.model.getPayment.PaymentOutput;

import java.util.List;

public interface IPaymentService {
    PaymentOutput findById(Long id);

    List<PaymentOutput> findByUserId(Long userId);

    PaymentOutput save(Long userId, Payment payment);

    void createCashInHandPayment(Long userId);

    PaymentOutput update(Long id, Payment payment);

    void delete(Long id);
}
