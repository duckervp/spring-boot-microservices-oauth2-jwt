package com.savvycom.userservice.service.impl;

import com.savvycom.userservice.domain.entity.Payment;
import com.savvycom.userservice.domain.model.PaymentOutput;
import com.savvycom.userservice.common.PaymentType;
import com.savvycom.userservice.exception.UserNotFoundException;
import com.savvycom.userservice.repository.PaymentRepository;
import com.savvycom.userservice.service.IPaymentService;
import com.savvycom.userservice.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentService implements IPaymentService {
    private final IUserService userService;

    private final PaymentRepository paymentRepository;

    private final ModelMapper modelMapper;

    @Override
    public List<PaymentOutput> findAll() {
        return paymentRepository.findAll().stream()
                .map(payment -> modelMapper.map(payment, PaymentOutput.class))
                .collect(Collectors.toList());
    }

    @Override
    public PaymentOutput findById(Long id) {
        return modelMapper.map(paymentRepository.findById(id), PaymentOutput.class);
    }

    @Override
    public List<PaymentOutput> findByUserId(Long userId) {
        if (!userService.existsById(userId))
            throw new UserNotFoundException("Not found any user with userId " + userId);
        return paymentRepository.findByUserId(userId).stream()
                .map(payment -> modelMapper.map(payment, PaymentOutput.class))
                .collect(Collectors.toList());
    }

    @Override
    public void save(Payment payment) {
        if (!userService.existsById(payment.getUserId()))
            throw new UserNotFoundException("Not found any user with userId " + payment.getUserId());
        paymentRepository.save(payment);
    }

    @Override
    public void createCashInHandPayment(Long id) {
        Payment payment = new Payment();
        payment.setUserId(id);
        payment.setPaymentType(PaymentType.CASH_IN_HAND);
        payment.setCreatedAt(new Date());
        save(payment);
    }
}
