package com.savvycom.userservice.controller;

import com.savvycom.userservice.common.HttpStatusCode;
import com.savvycom.userservice.domain.entity.Payment;
import com.savvycom.userservice.domain.message.BaseMessage;
import com.savvycom.userservice.domain.message.ExtendedMessage;
import com.savvycom.userservice.domain.model.*;
import com.savvycom.userservice.exception.UserAlreadyExistException;
import com.savvycom.userservice.service.IPaymentService;
import com.savvycom.userservice.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController extends BaseController {
    private final IUserService userService;

    private final IPaymentService paymentService;

    @Bean
    public OpenAPI customOpenAPI(@Value("1.5.9") String appVersion) {
        var securitySchemeName = "authorization-header";
        return new OpenAPI()
                .components(new Components().addSecuritySchemes(securitySchemeName,
                        new SecurityScheme()
                                .name(securitySchemeName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")))
                .info(new Info().title("Your API").version(appVersion)
                        .license(new License().name("Apache 2.0").url("https://springdoc.org/")));
    }


    /**
     * For user registration
     * @param userInput entity
     * @return success response
     * @throws UserAlreadyExistException when username existed
     */
    @PostMapping()
    @Operation(summary = "Register a new account")
    @ApiResponse(responseCode = HttpStatusCode.OK, description = "Register successfully",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExtendedMessage.class)) })
    @ApiResponse(responseCode = HttpStatusCode.BAD_REQUEST, description = "Invalid input",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = BaseMessage.class)) })
    @ApiResponse(responseCode = HttpStatusCode.UNPROCESSABLE_ENTITY, description = "There is an account with the email provided",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = BaseMessage.class)) })
    @ApiResponse(responseCode = HttpStatusCode.INTERNAL_SERVER_ERROR, description = "Internal Server Error",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = BaseMessage.class)) })
    public ResponseEntity<?> register(@RequestBody @Validated @Valid UserInput userInput) throws UserAlreadyExistException {
        userService.register(userInput);
        return successResponse("Register successfully!");
    }

    @PostMapping("/updatePassword")
    @Operation(summary = "Update password of a user")
    @ApiResponse(responseCode = HttpStatusCode.OK, description = "Password update successfully",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExtendedMessage.class)) })
    @ApiResponse(responseCode = HttpStatusCode.BAD_REQUEST, description = "Invalid input",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = BaseMessage.class)) })
    @ApiResponse(responseCode = HttpStatusCode.INTERNAL_SERVER_ERROR, description = "Internal Server Error",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = BaseMessage.class)) })
    public ResponseEntity<?> updatePassword(@RequestBody @Valid UserPasswordUpdateInput userPasswordUpdateInput) {
        userService.updatePassword(userPasswordUpdateInput);
        return successResponse("Update password successfully!");
    }


    @PostMapping("/forgotPassword")
    @Operation(summary = "Send a password reset email to user when a user forgot password")
    @ApiResponse(responseCode = HttpStatusCode.OK, description = "Password reset email successfully sent to user",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExtendedMessage.class)) })
    @ApiResponse(responseCode = HttpStatusCode.BAD_REQUEST, description = "Invalid input",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = BaseMessage.class)) })
    @ApiResponse(responseCode = HttpStatusCode.NOT_FOUND, description = "Not found any user with username provided",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = BaseMessage.class)) })
    @ApiResponse(responseCode = HttpStatusCode.INTERNAL_SERVER_ERROR, description = "Internal Server Error",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = BaseMessage.class)) })
    public ResponseEntity<?> forgotPassword(@RequestBody @Valid UserPasswordForgotInput userPasswordForgotInput) throws MessagingException, UnsupportedEncodingException {
        userService.forgotPassword(userPasswordForgotInput.getUsername());
        String message = String.format("A password reset email has been sent to your email at %s. Please check!", userPasswordForgotInput.getUsername());
        return successResponse(message);
    }

    @PostMapping("/resetPassword")
    @Operation(summary = "Do reset user password")
    @ApiResponse(responseCode = HttpStatusCode.OK, description = "Password reset successfully",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExtendedMessage.class)) })
    @ApiResponse(responseCode = HttpStatusCode.BAD_REQUEST, description = "Invalid input",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = BaseMessage.class)) })
    @ApiResponse(responseCode = HttpStatusCode.INTERNAL_SERVER_ERROR, description = "Internal Server Error",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = BaseMessage.class)) })
    public ResponseEntity<?> resetPassword(@RequestBody @Valid UserPasswordResetInput userPasswordResetInput) {
        userService.resetPassword(
                userPasswordResetInput.getPasswordResetToken(),
                userPasswordResetInput.getNewPassword());
        return successResponse("Reset password successfully");
    }

    /**
     * Find all users
     * @return success response with List<UserOutput>
     */
    @PreAuthorize("hasAuthority('admin')")
    @GetMapping()
    @Operation(summary = "Find all users", security = {@SecurityRequirement(name = "authorization-header")})
    @ApiResponse(responseCode = HttpStatusCode.OK, description = "Return all users",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExtendedMessage.class)) })
    @ApiResponse(responseCode = HttpStatusCode.BAD_REQUEST, description = "Invalid input",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = BaseMessage.class)) })
    @ApiResponse(responseCode = HttpStatusCode.INTERNAL_SERVER_ERROR, description = "Internal Server Error",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = BaseMessage.class)) })
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
    @Operation(summary = "Find specific user information", security = {@SecurityRequirement(name = "authorization-header")})
    @ApiResponse(responseCode = HttpStatusCode.OK, description = "Success",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExtendedMessage.class)) })
    @ApiResponse(responseCode = HttpStatusCode.BAD_REQUEST, description = "Invalid userId",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = BaseMessage.class)) })
    @ApiResponse(responseCode = HttpStatusCode.NOT_FOUND, description = "Not found any user with userId provided",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = BaseMessage.class)) })
    @ApiResponse(responseCode = HttpStatusCode.INTERNAL_SERVER_ERROR, description = "Internal Server Error",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = BaseMessage.class)) })
    public ResponseEntity<?> findById(@PathVariable Long userId) {
        return successResponse(userService.findById(userId));
    }

    @PostMapping("/{userId}")
    @Operation(summary = "Find specific user information", security = {@SecurityRequirement(name = "authorization-header")})
    @ApiResponse(responseCode = HttpStatusCode.OK, description = "Success",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExtendedMessage.class)) })
    @ApiResponse(responseCode = HttpStatusCode.BAD_REQUEST, description = "Invalid input",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = BaseMessage.class)) })
    @ApiResponse(responseCode = HttpStatusCode.NOT_FOUND, description = "Not found any user with userId provided",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = BaseMessage.class)) })
    @ApiResponse(responseCode = HttpStatusCode.INTERNAL_SERVER_ERROR, description = "Internal Server Error",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = BaseMessage.class)) })
    public ResponseEntity<?> updateUser(@PathVariable Long userId, @Valid @RequestBody UserUpdateInput userUpdateInput) {
        userService.update(userId, userUpdateInput);
        return successResponse("Update user successfully");
    }



    /**
     * Find all payment methods of a user
     * @param userId Long type
     * @return success response with List<PaymentOutput>
     *     or failed response with IllegalArgumentException
     */
    @GetMapping("/{userId}/payment")
    @Operation(summary = "Find all payment methods of a user", security = {@SecurityRequirement(name = "authorization-header")})
    @ApiResponse(responseCode = HttpStatusCode.OK, description = "Success",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExtendedMessage.class)) })
    @ApiResponse(responseCode = HttpStatusCode.BAD_REQUEST, description = "Invalid input",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = BaseMessage.class)) })
    @ApiResponse(responseCode = HttpStatusCode.INTERNAL_SERVER_ERROR, description = "Internal Server Error",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = BaseMessage.class)) })
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
    @Operation(summary = "Add new payment method for a user", security = {@SecurityRequirement(name = "authorization-header")})
    @ApiResponse(responseCode = HttpStatusCode.OK, description = "Add new payment method successfully",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExtendedMessage.class)) })
    @ApiResponse(responseCode = HttpStatusCode.BAD_REQUEST, description = "Invalid input",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = BaseMessage.class)) })
    @ApiResponse(responseCode = HttpStatusCode.INTERNAL_SERVER_ERROR, description = "Internal Server Error",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = BaseMessage.class)) })
    public ResponseEntity<?> addUserPayment(@PathVariable Long userId, @RequestBody Payment payment) {
        payment.setUserId(userId);
        paymentService.save(payment);
        return successResponse("Add new payment successfully");
    }

    /**
     * Find a payment by id
     * @param paymentId Long type
     * @return success response with PaymentOutput object
     *      or failed response with IllegalArgumentException
     */
    @GetMapping("/payment/{paymentId}")
    @Operation(summary = "Find a payment by id", security = {@SecurityRequirement(name = "authorization-header")})
    @ApiResponse(responseCode = HttpStatusCode.OK, description = "Return a payment information",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExtendedMessage.class)) })
    @ApiResponse(responseCode = HttpStatusCode.BAD_REQUEST, description = "Invalid input",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = BaseMessage.class)) })
    @ApiResponse(responseCode = HttpStatusCode.INTERNAL_SERVER_ERROR, description = "Internal Server Error",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = BaseMessage.class)) })
    public ResponseEntity<?> findPaymentById(@PathVariable Long paymentId) {
        return successResponse(paymentService.findById(paymentId));
    }
}
