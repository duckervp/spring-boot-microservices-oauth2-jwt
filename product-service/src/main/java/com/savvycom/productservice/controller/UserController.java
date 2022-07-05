package com.savvycom.productservice.controller;

import com.savvycom.productservice.common.HttpStatusCode;
import com.savvycom.productservice.domain.message.BaseMessage;
import com.savvycom.productservice.domain.message.ExtendedMessage;
import com.savvycom.userservice.domain.model.register.UserInput;
import com.savvycom.userservice.domain.model.resetPassword.UserPasswordForgotInput;
import com.savvycom.userservice.domain.model.resetPassword.UserPasswordResetInput;
import com.savvycom.userservice.domain.model.updatePassword.UserPasswordUpdateInput;
import com.savvycom.userservice.domain.model.updateUser.UserUpdateInput;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController extends BaseController {


    /**
     * Custom validate bearer token function on swagger ui
     *
     * @param appVersion spring docs version in pom.xml
     * @return OpenAPI model
     */
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
     * After register user successful then create a base payment method - cash in hand for each user
     *
     * @param userInput entity
     * @return success response
     * or failed response (handle UserAlreadyExistException when username existed)
     */
    @PostMapping()
    @Operation(summary = "Register a new account")
    @ApiResponse(responseCode = HttpStatusCode.OK, description = "Register successful",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExtendedMessage.class))})
    @ApiResponse(responseCode = HttpStatusCode.BAD_REQUEST, description = "Invalid input",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = BaseMessage.class))})
    @ApiResponse(responseCode = HttpStatusCode.INTERNAL_SERVER_ERROR, description = "Internal Server Error",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = BaseMessage.class))})
    public ResponseEntity<?> register(@RequestBody @Validated @Valid UserInput userInput) {
    }
}