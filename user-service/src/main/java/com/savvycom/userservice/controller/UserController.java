package com.savvycom.userservice.controller;

import com.savvycom.userservice.domain.entity.Payment;
import com.savvycom.userservice.domain.entity.User;
import com.savvycom.userservice.exception.UserAlreadyExistException;
import com.savvycom.userservice.service.IPaymentService;
import com.savvycom.userservice.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController extends BaseController {
    private final IUserService userService;

    private final IPaymentService paymentService;

    /**
     * For user registration
     * @param user entity
     * @return success response
     * @throws UserAlreadyExistException when username existed
     */
    @PostMapping()
    public ResponseEntity<?> register(@RequestBody @Valid User user) throws UserAlreadyExistException {
        if (userService.existsByUsername(user.getUsername())) {
            throw new UserAlreadyExistException("There is an account with the email "+ user.getUsername());
        }
        user = userService.register(user);
        paymentService.createCashInHandsPayment(user.getId());
        return successResponse(
                "Register successfully!",
                null);
    }

    /**
     * Find all users
     * @return success response with List<UserOutput>
     */
    @PreAuthorize("hasAuthority('admin')")
    @GetMapping()
    public ResponseEntity<?> findAll() {
        return successResponse(userService.findAll());
    }

    /**
     * Find specific user information
     * @param userId Long type
     * @return success response with UserOutput object
     *      or failed response with IllegalArgumentException
     */
    @GetMapping("/{userId}")
    public ResponseEntity<?> findById(@PathVariable Long userId) {
        return successResponse(userService.findById(userId));
    }

    /**
     * Find all payment methods of a user
     * @param userId Long type
     * @return success response with List<PaymentOutput>
     *     or failed response with IllegalArgumentException
     */
    @GetMapping("/{userId}/payment")
    public ResponseEntity<?> findAllUserPayments(@PathVariable Long userId) {
        return successResponse(paymentService.findByUserId(userId));
    }

    /**
     * Add new payment method for a user
     * @param userId Long type
     * @param payment Payment entity
     * @return success response
     */
    @PostMapping("/{userId}/payment")
    public ResponseEntity<?> addUserPayment(@PathVariable Long userId, @RequestBody Payment payment) {
        payment.setUserId(userId);
        paymentService.save(payment);
        return successResponse();
    }

    /**
     * Find a payment by id
     * @param paymentId Long type
     * @return success response with PaymentOutput object
     *      or failed response with IllegalArgumentException
     */
    @GetMapping("/payment/{paymentId}")
    public ResponseEntity<?> findPaymentById(@PathVariable Long paymentId) {
        return successResponse(paymentService.findById(paymentId));
    }
}
