package com.savvycom.userservice.service;

import com.savvycom.userservice.domain.entity.User;
import com.savvycom.userservice.domain.model.getUser.UserOutput;
import com.savvycom.userservice.domain.model.pagging.PageOutput;
import com.savvycom.userservice.domain.model.register.UserInput;
import com.savvycom.userservice.domain.model.resetPassword.SetUserPasswordInput;
import com.savvycom.userservice.domain.model.updatePassword.UserPasswordUpdateInput;
import com.savvycom.userservice.domain.model.updateUser.UserUpdateInput;
import org.springframework.data.domain.Page;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

public interface IUserService {
    PageOutput<?> findAll(String name, Integer pageNo, Integer pageSize);

    boolean existsById(Long id);

    boolean existsByUsername(String username);

    Map<String, Object> register(UserInput userInput);

    UserOutput findById(Long id);

    UserOutput update(Long userId, UserUpdateInput userUpdateInput);

    void delete(Long userId);

    void updatePassword(UserPasswordUpdateInput input);

    void setPassword(SetUserPasswordInput input);

    void forgotPassword(String username) throws MessagingException, UnsupportedEncodingException;

    User updatePasswordResetToken(String username, String resetPasswordToken);

   void resetPassword(String passwordResetToken, String newPassword);

   List<UserOutput> findByIds(List<Long> ids);

   Long countUserByActive(Boolean active);

}
