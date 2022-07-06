package com.savvycom.userservice.controller;

import com.savvycom.userservice.common.DefaultPagination;
import com.savvycom.userservice.common.HttpStatusCode;
import com.savvycom.userservice.domain.entity.Payment;
import com.savvycom.userservice.domain.entity.SaleStaff;
import com.savvycom.userservice.domain.entity.User;
import com.savvycom.userservice.domain.feign.AuthInfo;
import com.savvycom.userservice.domain.message.BaseMessage;
import com.savvycom.userservice.domain.message.ExtendedMessage;
import com.savvycom.userservice.domain.model.getPayment.PaymentOutputResponse;
import com.savvycom.userservice.domain.model.getPayment.PaymentOutputsResponse;
import com.savvycom.userservice.domain.model.getUser.UserOutputResponse;
import com.savvycom.userservice.domain.model.getUser.UserOutputsResponse;
import com.savvycom.userservice.domain.model.pagging.PageOutputResponse;
import com.savvycom.userservice.domain.model.register.UserInput;
import com.savvycom.userservice.domain.model.resetPassword.SetUserPasswordInput;
import com.savvycom.userservice.domain.model.resetPassword.UserPasswordForgotInput;
import com.savvycom.userservice.domain.model.resetPassword.UserPasswordResetInput;
import com.savvycom.userservice.domain.model.updatePassword.UserPasswordUpdateInput;
import com.savvycom.userservice.domain.model.updateUser.UserUpdateInput;
import com.savvycom.userservice.service.IPaymentService;
import com.savvycom.userservice.service.ISaleStaffService;
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
import java.util.List;
import java.util.Map;

@CrossOrigin(value = "*", maxAge = 3600)
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController extends BaseController {
    private final IUserService userService;

    private final IPaymentService paymentService;

    private final ISaleStaffService saleStaffService;

    /**
     * Custom validate bearer token function for swagger ui
     * @param appVersion spring docs version in pom.xml
     * @return OpenAPI model
     */
    @Bean
    public OpenAPI customOpenAPI(@Value("1.5.9") String appVersion) {
        var securitySchemeName = "authorization";
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
     * After register user successful then create a base payment method - cash in hand for each user
     * @param userInput entity
     * @return success response
     * or failed response (handle UserAlreadyExistException when username existed)
     */
    @PostMapping()
    @Operation(summary = "Register a new account")
    @ApiResponse(responseCode = HttpStatusCode.OK, description = "Register successful",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExtendedMessage.class)) })
    @ApiResponse(responseCode = HttpStatusCode.BAD_REQUEST, description = "Invalid input",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = BaseMessage.class)) })
    @ApiResponse(responseCode = HttpStatusCode.INTERNAL_SERVER_ERROR, description = "Internal Server Error",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = BaseMessage.class)) })
    public ResponseEntity<?> register(@RequestBody @Validated @Valid UserInput userInput) {
        Map<String, Object> map = userService.register(userInput);
        User user = (User) map.get("user");
        AuthInfo authInfo = (AuthInfo) map.get("authInfo");
        paymentService.createCashInHandPayment(user.getId());
        return successResponse("Register successfully!", authInfo);
    }

    /**
     * This function change user password in a normal way
     * @param userPasswordUpdateInput user id, password and newPassword
     * @return Success response or failed response (handle Exception when username is invalid)
     */
    @PostMapping("/updatePassword")
    @Operation(summary = "Update password of a user", security = {@SecurityRequirement(name = "authorization")})
    @ApiResponse(responseCode = HttpStatusCode.OK, description = "Update password successful",
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

    /**
     * This function change user password when user forgot password (follow front end)
     * @param input username, password and newPassword
     * @return Success response or failed response (handle Exception when username is invalid)
     */
    @PostMapping("/setPassword")
    @Operation(summary = "Set password of a user")
    @ApiResponse(responseCode = HttpStatusCode.OK, description = "Set new password success",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExtendedMessage.class)) })
    @ApiResponse(responseCode = HttpStatusCode.BAD_REQUEST, description = "Invalid input",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = BaseMessage.class)) })
    @ApiResponse(responseCode = HttpStatusCode.INTERNAL_SERVER_ERROR, description = "Internal Server Error",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = BaseMessage.class)) })
    public ResponseEntity<?> setPassword(@RequestBody @Valid SetUserPasswordInput input) {
        userService.setPassword(input);
        return successResponse("Update password successfully!");
    }

    /**
     * When user send a forgot password request, this function create a passwordResetToken
     * and send an email with the reset password link contains the token to the user
     * @param userPasswordForgotInput user email as username
     * @return Success response or Exception(UserNotFoundException) or the two exceptions below
     * @throws MessagingException when email can not be sent
     * @throws UnsupportedEncodingException when email can not be sent
     */
    @PostMapping("/forgotPassword")
    @Operation(summary = "Send a password reset email to user")
    @ApiResponse(responseCode = HttpStatusCode.OK, description = "Success",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExtendedMessage.class)) })
    @ApiResponse(responseCode = HttpStatusCode.BAD_REQUEST, description = "Invalid input",
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

    /**
     * This function change user password in case user forgot password
     * After user click reset password link in the email and type a new password
     * @param userPasswordResetInput contains passwordResetToken and new password
     * @return Success response or Exception when passwordResetToken is invalid
     */
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
    @Operation(summary = "Find all users", security = {@SecurityRequirement(name = "authorization")})
    @ApiResponse(responseCode = HttpStatusCode.OK, description = "Return all users",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = PageOutputResponse.class)) })
    @ApiResponse(responseCode = HttpStatusCode.BAD_REQUEST, description = "Invalid input",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = BaseMessage.class)) })
    @ApiResponse(responseCode = HttpStatusCode.INTERNAL_SERVER_ERROR, description = "Internal Server Error",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = BaseMessage.class)) })
    public ResponseEntity<?> findAll(
            @RequestParam(required = false) String name,
            @RequestParam(required = false, defaultValue = DefaultPagination.PAGE_NUMBER) Integer pageNo,
            @RequestParam(required = false, defaultValue = DefaultPagination.PAGE_SIZE) Integer pageSize) {
        return successResponse(userService.findAll(name, pageNo, pageSize));
    }

    /**
     * Find specific user information
     * @param userId Long type
     * @return success response with UserOutput object
     *      or failed response
     */
    @PreAuthorize("hasAuthority('admin') or @userSecurity.hasUserId(authentication,#userId)")
    @GetMapping("/{userId}")
    @Operation(summary = "Find specific user information", security = {@SecurityRequirement(name = "authorization")})
    @ApiResponse(responseCode = HttpStatusCode.OK, description = "Success",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserOutputResponse.class)) })
    @ApiResponse(responseCode = HttpStatusCode.BAD_REQUEST, description = "Invalid userId",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = BaseMessage.class)) })
    @ApiResponse(responseCode = HttpStatusCode.INTERNAL_SERVER_ERROR, description = "Internal Server Error",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = BaseMessage.class)) })
    public ResponseEntity<?> findById(@PathVariable Long userId) {
        return successResponse(userService.findById(userId));
    }

    /**
     * Find users information by list of user id
     * @param userIds list of user id
     * @return success response with list of user corresponding with list id provided
     */
    @GetMapping("/ids")
    @Operation(summary = "Find users information by list of user id", security = {@SecurityRequirement(name = "authorization")})
    @ApiResponse(responseCode = HttpStatusCode.OK, description = "Success",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserOutputsResponse.class)) })
    @ApiResponse(responseCode = HttpStatusCode.BAD_REQUEST, description = "Invalid userId",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = BaseMessage.class)) })
    @ApiResponse(responseCode = HttpStatusCode.INTERNAL_SERVER_ERROR, description = "Internal Server Error",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = BaseMessage.class)) })
    public ResponseEntity<?> findByIds(@RequestParam List<Long> userIds) {
        return successResponse(userService.findByIds(userIds));
    }

    /**
     * Update user information by id
     * @param userId id of user
     * @param userUpdateInput contains information that user want to update
     * @return success response or failed response when user info is invalid
     */
    @PostMapping("/{userId}")
    @Operation(summary = "Update user information", security = {@SecurityRequirement(name = "authorization")})
    @ApiResponse(responseCode = HttpStatusCode.OK, description = "Success",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExtendedMessage.class)) })
    @ApiResponse(responseCode = HttpStatusCode.BAD_REQUEST, description = "Invalid input",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = BaseMessage.class)) })
    @ApiResponse(responseCode = HttpStatusCode.INTERNAL_SERVER_ERROR, description = "Internal Server Error",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = BaseMessage.class)) })
    public ResponseEntity<?> updateUser(@PathVariable Long userId, @Valid @RequestBody UserUpdateInput userUpdateInput) {
        return successResponse("Update user successfully", userService.update(userId, userUpdateInput));
    }

    /**
     * Delete a user
     * @param userId User id
     * @return Success message
     */
    @PreAuthorize("hasAuthority('admin')")
    @DeleteMapping("/{userId}")
    @Operation(summary = "Delete user", security = {@SecurityRequirement(name = "authorization")})
    @ApiResponse(responseCode = HttpStatusCode.OK, description = "Success message",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExtendedMessage.class)) })
    @ApiResponse(responseCode = HttpStatusCode.BAD_REQUEST, description = "Invalid input",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = BaseMessage.class)) })
    @ApiResponse(responseCode = HttpStatusCode.INTERNAL_SERVER_ERROR, description = "Internal Server Error",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = BaseMessage.class)) })
    public ResponseEntity<?> updateUser(@PathVariable Long userId) {
        userService.delete(userId);
        return successResponse("Delete user successfully");
    }

    /**
     * Find total number of user
     * @return Number of users
     */
    @PreAuthorize("hasAuthority('admin')")
    @GetMapping("/qty")
    @Operation(summary = "Find total number of user", security = {@SecurityRequirement(name = "authorization")})
    @ApiResponse(responseCode = HttpStatusCode.OK, description = "Success message with number of users",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExtendedMessage.class)) })
    @ApiResponse(responseCode = HttpStatusCode.BAD_REQUEST, description = "Invalid input",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = BaseMessage.class)) })
    @ApiResponse(responseCode = HttpStatusCode.INTERNAL_SERVER_ERROR, description = "Internal Server Error",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = BaseMessage.class)) })
    public ResponseEntity<?> countUser(@RequestParam(required = false) Boolean active) {
        return successResponse(userService.countUserByActive(active));
    }


    /**
     * Find all payment methods of a user
     * @param userId Long type
     * @return success response with List<PaymentOutput>
     *     or failed response
     */
    @GetMapping("/{userId}/payment")
    @Operation(summary = "Find all payment methods of a user", security = {@SecurityRequirement(name = "authorization")})
    @ApiResponse(responseCode = HttpStatusCode.OK, description = "Success",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = PaymentOutputsResponse.class)) })
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
    @Operation(summary = "Add new payment method for a user", security = {@SecurityRequirement(name = "authorization")})
    @ApiResponse(responseCode = HttpStatusCode.OK, description = "Success message and Payment info",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExtendedMessage.class)) })
    @ApiResponse(responseCode = HttpStatusCode.BAD_REQUEST, description = "Invalid input",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = BaseMessage.class)) })
    @ApiResponse(responseCode = HttpStatusCode.INTERNAL_SERVER_ERROR, description = "Internal Server Error",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = BaseMessage.class)) })
    public ResponseEntity<?> addUserPayment(@PathVariable Long userId, @RequestBody Payment payment) {
        return successResponse("Add new payment successfully", paymentService.save(userId, payment));
    }

    /**
     * Find a payment by id
     * @param paymentId Long type
     * @return success response with PaymentOutput object
     *      or failed response
     */
    @GetMapping("/payment/{paymentId}")
    @Operation(summary = "Find a payment by id", security = {@SecurityRequirement(name = "authorization")})
    @ApiResponse(responseCode = HttpStatusCode.OK, description = "Return a payment information",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = PaymentOutputResponse.class)) })
    @ApiResponse(responseCode = HttpStatusCode.BAD_REQUEST, description = "Invalid input",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = BaseMessage.class)) })
    @ApiResponse(responseCode = HttpStatusCode.INTERNAL_SERVER_ERROR, description = "Internal Server Error",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = BaseMessage.class)) })
    public ResponseEntity<?> findPaymentById(@PathVariable Long paymentId) {
        return successResponse(paymentService.findById(paymentId));
    }

    /**
     * Update payment
     * @param paymentId Payment id
     * @param payment Update information
     * @return Payment after update
     */
    @PostMapping("/payment/{paymentId}")
    @Operation(summary = "Update a payment by id", security = {@SecurityRequirement(name = "authorization")})
    @ApiResponse(responseCode = HttpStatusCode.OK, description = "Success message and payment after update",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = PaymentOutputResponse.class)) })
    @ApiResponse(responseCode = HttpStatusCode.BAD_REQUEST, description = "Invalid input",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = BaseMessage.class)) })
    @ApiResponse(responseCode = HttpStatusCode.INTERNAL_SERVER_ERROR, description = "Internal Server Error",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = BaseMessage.class)) })
    public ResponseEntity<?> updatePayment(@PathVariable Long paymentId, @RequestBody Payment payment) {
        return successResponse(paymentService.update(paymentId, payment));
    }

    /**
     * Delete payment
     * @param paymentId Payment id
     * @return "Succeeded or failed message"
     */
    @DeleteMapping("/payment/{paymentId}")
    @Operation(summary = "Delete a payment by id", security = {@SecurityRequirement(name = "authorization")})
    @ApiResponse(responseCode = HttpStatusCode.OK, description = "Success message",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = PaymentOutputResponse.class)) })
    @ApiResponse(responseCode = HttpStatusCode.BAD_REQUEST, description = "Invalid input",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = BaseMessage.class)) })
    @ApiResponse(responseCode = HttpStatusCode.INTERNAL_SERVER_ERROR, description = "Internal Server Error",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = BaseMessage.class)) })
    public ResponseEntity<?> deletePayment(@PathVariable Long paymentId) {
        paymentService.delete(paymentId);
        return successResponse("Delete payment succeeded");
    }


    /**
     * Find all sale staffs
     * @return success response with list of SaleStaff object
     *      or failed response
     */
    @GetMapping("/staff/sale")
    @Operation(summary = "Find all sale staff name", security = {@SecurityRequirement(name = "authorization")})
    @ApiResponse(responseCode = HttpStatusCode.OK, description = "Return list of sale staff name",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = PaymentOutputResponse.class)) })
    @ApiResponse(responseCode = HttpStatusCode.BAD_REQUEST, description = "Invalid input",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = BaseMessage.class)) })
    @ApiResponse(responseCode = HttpStatusCode.INTERNAL_SERVER_ERROR, description = "Internal Server Error",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = BaseMessage.class)) })
    public ResponseEntity<?> findAllSaleStaff(@RequestParam Boolean active) {
        return successResponse(saleStaffService.findAllSaleStaff(active));
    }

    /**
     * Create new sale staff
     * @param saleStaff sale staff info
     * @return Result message
     */
    @PreAuthorize("hasAuthority('admin')")
    @PostMapping("/staff/sale")
    @Operation(summary = "Create sale staff", security = {@SecurityRequirement(name = "authorization")})
    @ApiResponse(responseCode = HttpStatusCode.OK, description = "Success message",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = PaymentOutputResponse.class)) })
    @ApiResponse(responseCode = HttpStatusCode.BAD_REQUEST, description = "Invalid input",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = BaseMessage.class)) })
    @ApiResponse(responseCode = HttpStatusCode.INTERNAL_SERVER_ERROR, description = "Internal Server Error",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = BaseMessage.class)) })
    public ResponseEntity<?> createSaleStaff(@RequestBody SaleStaff saleStaff) {
        saleStaffService.create(saleStaff);
        return successResponse("Create sale staff success");
    }

    /**
     * Get sale staff info by id
     * @param id Sale staff id
     * @return Sale staff info
     */
    @GetMapping("/staff/sale/{id}")
    @Operation(summary = "Get sale staff by id", security = {@SecurityRequirement(name = "authorization")})
    @ApiResponse(responseCode = HttpStatusCode.OK, description = "Sale staff info",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = PaymentOutputResponse.class)) })
    @ApiResponse(responseCode = HttpStatusCode.BAD_REQUEST, description = "Invalid input",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = BaseMessage.class)) })
    @ApiResponse(responseCode = HttpStatusCode.INTERNAL_SERVER_ERROR, description = "Internal Server Error",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = BaseMessage.class)) })
    public ResponseEntity<?> getSaleStaff(@PathVariable Long id) {
        return successResponse(saleStaffService.findById(id));
    }

    /**
     * Delete sale staff
     * @param id Sale staff id
     * @return Result message
     */
    @PreAuthorize("hasAuthority('admin')")
    @DeleteMapping("/staff/sale/{id}")
    @Operation(summary = "Delete sale staff", security = {@SecurityRequirement(name = "authorization")})
    @ApiResponse(responseCode = HttpStatusCode.OK, description = "Success message",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = PaymentOutputResponse.class)) })
    @ApiResponse(responseCode = HttpStatusCode.BAD_REQUEST, description = "Invalid input",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = BaseMessage.class)) })
    @ApiResponse(responseCode = HttpStatusCode.INTERNAL_SERVER_ERROR, description = "Internal Server Error",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = BaseMessage.class)) })
    public ResponseEntity<?> deleteSaleStaff(@PathVariable Long id) {
        saleStaffService.delete(id);
        return successResponse("Delete sale staff success");
    }



}
