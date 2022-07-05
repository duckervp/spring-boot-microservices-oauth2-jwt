package com.savvycom.userservice.service.impl;

import com.savvycom.userservice.common.RoleType;
import com.savvycom.userservice.common.StatusType;
import com.savvycom.userservice.config.ServiceConfig;
import com.savvycom.userservice.domain.entity.User;
import com.savvycom.userservice.domain.model.getUser.UserOutput;
import com.savvycom.userservice.domain.model.pagging.PageOutput;
import com.savvycom.userservice.domain.model.register.UserInput;
import com.savvycom.userservice.domain.model.resetPassword.SetUserPasswordInput;
import com.savvycom.userservice.domain.model.updatePassword.UserPasswordUpdateInput;
import com.savvycom.userservice.domain.model.updateUser.UserUpdateInput;
import com.savvycom.userservice.exception.PasswordResetTokenInvalidException;
import com.savvycom.userservice.exception.UserAlreadyExistException;
import com.savvycom.userservice.exception.UserNotFoundException;
import com.savvycom.userservice.exception.UsernamePasswordIncorrectException;
import com.savvycom.userservice.repository.UserRepository;
import com.savvycom.userservice.service.IUserService;
import com.savvycom.userservice.service.client.AuthClient;
import com.savvycom.userservice.util.Mail;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final Mail mail;

    private final ServiceConfig serviceConfig;
    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    private final AuthClient authClient;

    /**
     * When admin wants to view all users information
     * @param name Filter by name
     * @param pageNo Paging page number
     * @param pageSize Number of user in one page
     * @return Paged list of user
     */
    @Override
    public PageOutput<?> findAll(String name, Integer pageNo, Integer pageSize) {
        Page<User> users;
        if (Objects.nonNull(name)) {
            users = userRepository.findByNameLike("%" + name + "%", PageRequest.of(pageNo, pageSize));
        } else {
            // get page of user follow paging option
            users = userRepository.findAll(PageRequest.of(pageNo, pageSize));
        }
        // map content user of page from user entity to user output object
        List<UserOutput> userOutputs = users.getContent().stream()
                .map(user -> modelMapper.map(user, UserOutput.class))
                .collect(Collectors.toList());
        // return new format of page
        return new PageOutput<>(userOutputs, pageNo, pageSize, users.getTotalElements());
    }

    /**
     * Check user existed with user id
     * @param id user id
     * @return true or false
     */
    @Override
    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }

    /**
     * Check if there is an account existed with username
     * @param username email
     * @return true of false
     */
    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    /**
     * For register new user account
     * @param userInput User information
     * @return User
     */
    @Override
    public Map<String, Object> register(UserInput userInput) {
        if (existsByUsername(userInput.getUsername()))
            throw new UserAlreadyExistException("There is an account with email " + userInput.getUsername());
        User user = modelMapper.map(userInput, User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(RoleType.USER);
        user.setActive(StatusType.ACTIVE);
        user.setCreatedAt(LocalDateTime.now());
        user = userRepository.save(user);

        Map<String, Object> map = new HashMap<>();
        map.put("user", user);
        map.put("authInfo", authClient.getToken("password", user.getUsername(), userInput.getPassword()));

        return map;
    }

    /**
     * Get user information
     * @param id User id
     * @return User info
     */
    @Override
    public UserOutput findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Not found any user with id " + id));
        return modelMapper.map(user, UserOutput.class);
    }

    /**
     * When user wants to update profile
     * @param userId The id of user
     * @param userUpdateInput Some information user want to update
     */
    @Override
    public UserOutput update(Long userId, UserUpdateInput userUpdateInput) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Not found any user with id " + userId));
        // Any field in userUpdateInput can be optional
        // so check what are the fields that user wants to update
        if (Objects.nonNull(userUpdateInput.getName())) user.setName(userUpdateInput.getName());
        if (Objects.nonNull(userUpdateInput.getGender())) user.setGender(userUpdateInput.getGender());
        if (Objects.nonNull(userUpdateInput.getAddress())) user.setAddress(userUpdateInput.getAddress());
        if (Objects.nonNull(userUpdateInput.getPhone())) user.setPhone(userUpdateInput.getPhone());
        if (Objects.nonNull(userUpdateInput.getAvatar())) user.setAvatar(userUpdateInput.getAvatar());
        user.setModifiedAt(LocalDateTime.now());
        user = userRepository.save(user);
        return modelMapper.map(user, UserOutput.class);
    }

    /**
     * Delete a user
     * @param userId User id
     */
    @Override
    public void delete(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Not found any user with id: " + userId));
        user.setActive(StatusType.IN_ACTIVE);
        user.setModifiedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    /**
     * When user wants to update user's password normally
     * @param input Includes username, old password, new password
     */
    @Override
    public void updatePassword(UserPasswordUpdateInput input) {
        // check for user
        User user = userRepository.findById(input.getId())
                .orElseThrow(() -> new UserNotFoundException("Not found any user with id: " + input.getId()));
        // check for old password
        if (!passwordEncoder.matches(input.getPassword(), user.getPassword()))
            throw new UsernamePasswordIncorrectException("Wrong username or password!");
        // update new password
        user.setPassword(passwordEncoder.encode(input.getNewPassword()));
        user.setModifiedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    /**
     * Set password follow front end
     * @param input username, new password
     */
    @Override
    public void setPassword(SetUserPasswordInput input) {
        // check for user
        User user = userRepository.findByUsername(input.getUsername())
                .orElseThrow(() -> new UserNotFoundException("Not found any user with email: " + input.getUsername()));
        // update new password
        user.setPassword(passwordEncoder.encode(input.getNewPassword()));
        user.setModifiedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    /**
     * When a user forgot password, create a password reset token and send to user's email
     * @param username User email
     * @throws MessagingException Send mail error
     * @throws UnsupportedEncodingException Send mail error
     */
    @Override
    public void forgotPassword(String username) throws MessagingException, UnsupportedEncodingException {
        String token = UUID.randomUUID().toString();
        String resetPasswordLink =
                String.format("%s/passwordResetToken=%s",
                        serviceConfig.getUiUrl(), token);
        User user = updatePasswordResetToken(username, token);
        mail.sendPasswordResetEmail(username, resetPasswordLink, user.getName());
    }

    /**
     * Update password by reset password link
     * @param passwordResetToken Token
     * @param newPassword New password
     */
    @Override
    public void resetPassword(String passwordResetToken, String newPassword) {
        User user = userRepository.findByPasswordResetToken(passwordResetToken)
                .orElseThrow(() -> new PasswordResetTokenInvalidException("Token invalid"));
        // check token expire time
        if (LocalDateTime.now().isAfter(user.getPasswordResetTokenExpiryDate()))
            throw new PasswordResetTokenInvalidException("Token is expired");
        // update password
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setPasswordResetToken(null);
        user.setPasswordResetTokenExpiryDate(null);
        user.setModifiedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    /**
     * Save password reset token to database
     * @param username User email who forgot the password
     * @param passwordResetToken The token
     * @return User Get user info for send mail content
     */
    @Override
    public User updatePasswordResetToken(String username, String passwordResetToken) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Not found any user with username: " + username));
        user.setPasswordResetToken(passwordResetToken);
        // determine reset password token expire time
        LocalDateTime expiryDate = LocalDateTime.now().plusHours(serviceConfig.getPasswordResetTokenValidityHours());
        user.setPasswordResetTokenExpiryDate(expiryDate);
        user.setModifiedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    /**
     * find users by provide a list of user id
     * @param ids list of user id
     * @return list of userOutput
     */
    @Override
    public List<UserOutput> findByIds(List<Long> ids) {
        return userRepository.findByIdIn(ids).stream()
                .map(user -> modelMapper.map(user, UserOutput.class))
                .collect(Collectors.toList());
    }

    /**
     * Find the number of users by user account status
     * @param active user account status
     * @return Number of users
     */
    @Override
    public Long countUserByActive(Boolean active) {
        Long numberOfUsers;
        if (Objects.isNull(active)) {
            numberOfUsers = userRepository.count();
        } else {
            numberOfUsers = userRepository.countUserByActive(active);
        }
        return numberOfUsers;
    }
}
