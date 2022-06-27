package com.savvycom.userservice.service;

import com.savvycom.userservice.domain.entity.User;
import com.savvycom.userservice.domain.model.UserInput;
import com.savvycom.userservice.domain.model.UserOutput;
import com.savvycom.userservice.domain.model.UserPasswordUpdateInput;
import com.savvycom.userservice.domain.model.UserUpdateInput;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.List;

public interface IUserService {
    List<UserOutput> findAll();

    boolean existsById(Long id);

    boolean existsByUsername(String username);

    void register(UserInput userInput);

    UserOutput findById(Long id);

    void update(Long userId, UserUpdateInput userUpdateInput);

    void updatePassword(UserPasswordUpdateInput input);

    void forgotPassword(String username) throws MessagingException, UnsupportedEncodingException;

    User updatePasswordResetToken(String username, String resetPasswordToken);

   void resetPassword(String passwordResetToken, String newPassword);

}
