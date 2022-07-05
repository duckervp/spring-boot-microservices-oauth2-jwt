package com.savvycom.userservice.service.impl;

import com.savvycom.userservice.common.PaymentType;
import com.savvycom.userservice.domain.entity.Payment;
import com.savvycom.userservice.domain.model.getPayment.PaymentOutput;
import com.savvycom.userservice.exception.UserNotFoundException;
import com.savvycom.userservice.repository.PaymentRepository;
import com.savvycom.userservice.service.IPaymentService;
import com.savvycom.userservice.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentService implements IPaymentService {
    private final IUserService userService;

    private final PaymentRepository paymentRepository;

    private final ModelMapper modelMapper;

    /**
     * Find all payment methods by id
     * @return payment info
     */
    @Override
    public PaymentOutput findById(Long id) {
        return modelMapper.map(paymentRepository.findById(id), PaymentOutput.class);
    }

    /**
     * Find all payment methods of a user by user id
     * @return payment info
     */
    @Override
    public List<PaymentOutput> findByUserId(Long userId) {
        if (!userService.existsById(userId))
            throw new UserNotFoundException("Not found any user with userId " + userId);
        return paymentRepository.findByUserId(userId).stream()
                .filter(Payment::isActive)
                .map(payment -> modelMapper.map(payment, PaymentOutput.class))
                .collect(Collectors.toList());
    }

    /**
     * When user wants to add new payment method
     */
    @Override
    public PaymentOutput save(Long userId, Payment payment) {
        if (!userService.existsById(payment.getUserId())) {
            throw new UserNotFoundException("Not found any user with userId " + payment.getUserId());
        }
        payment.setUserId(userId);
        Payment pmt = paymentRepository.save(payment);
        return modelMapper.map(pmt, PaymentOutput.class);
    }

    /**
     * Create default payment for each user when a user account is registered
     */
    @Override
    public void createCashInHandPayment(Long id) {
        Payment payment = new Payment();
        payment.setUserId(id);
        payment.setPaymentType(PaymentType.CASH_IN_HAND);
        payment.setCreatedAt(LocalDateTime.now());
        paymentRepository.save(payment);
    }

    /**
     * Update user payment
     * @param id payment id
     * @param payment update information
     * @return Payment after update
     */
    @Override
    public PaymentOutput update(Long id, Payment payment) {
        Payment pmt = paymentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found"));
        if (Objects.nonNull(payment.getPaymentType())) pmt.setPaymentType(payment.getPaymentType());
        if (Objects.nonNull(payment.getProvider())) pmt.setProvider(payment.getProvider());
        if (Objects.nonNull(payment.getNumber())) pmt.setNumber(payment.getNumber());
        pmt.setModifiedAt(LocalDateTime.now());
        pmt = paymentRepository.save(pmt);
        return modelMapper.map(pmt, PaymentOutput.class);
    }

    /**
     * Deactivate user payment
     * @param id payment id
     */
    @Override
    public void delete(Long id) {
        Payment pmt = paymentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found"));
        pmt.setActive(false);
        pmt.setModifiedAt(LocalDateTime.now());
        paymentRepository.save(pmt);
    }
}
