package com.savvycom.userservice.service;

import com.savvycom.userservice.domain.entity.User;
import com.savvycom.userservice.domain.model.getUser.UserOutput;
import com.savvycom.userservice.domain.model.pagging.PageOutput;
import com.savvycom.userservice.domain.model.register.UserInput;
import com.savvycom.userservice.domain.model.updatePassword.UserPasswordUpdateInput;
import com.savvycom.userservice.domain.model.updateUser.UserUpdateInput;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.List;

public interface IUserService {
    PageOutput<UserOutput> findAll(Integer pageNo, Integer pageSize, String sortBy, String sortDir);

    boolean existsById(Long id);

    boolean existsByUsername(String username);

    User register(UserInput userInput);

    UserOutput findById(Long id);

    void update(Long userId, UserUpdateInput userUpdateInput);

    void updatePassword(UserPasswordUpdateInput input);

    void forgotPassword(String username) throws MessagingException, UnsupportedEncodingException;

    User updatePasswordResetToken(String username, String resetPasswordToken);

   void resetPassword(String passwordResetToken, String newPassword);

   List<UserOutput> findByIds(List<Long> ids);

}
