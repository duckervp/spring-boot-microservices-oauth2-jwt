package com.savvycom.userservice.service.impl;

import com.savvycom.userservice.common.RoleType;
import com.savvycom.userservice.common.StatusType;
import com.savvycom.userservice.config.ServiceConfig;
import com.savvycom.userservice.domain.entity.User;
import com.savvycom.userservice.domain.model.*;
import com.savvycom.userservice.exception.PasswordResetTokenInvalidException;
import com.savvycom.userservice.exception.UserAlreadyExistException;
import com.savvycom.userservice.exception.UserNotFoundException;
import com.savvycom.userservice.exception.UsernamePasswordIncorrectException;
import com.savvycom.userservice.repository.UserRepository;
import com.savvycom.userservice.service.IPaymentService;
import com.savvycom.userservice.service.IUserService;
import com.savvycom.userservice.util.Mail;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.persistence.EntityNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final Mail mail;

    private final ServiceConfig serviceConfig;
    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final IPaymentService paymentService;
    private final ModelMapper modelMapper;

    @Override
    public List<UserOutput> findAll() {
        return userRepository.findAll().stream()
                .map(user -> modelMapper.map(user, UserOutput.class))
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public void register(UserInput userInput) {
        User user = modelMapper.map(userInput, User.class);
        if (existsByUsername(user.getUsername()))
            throw new UserAlreadyExistException("There is an account with email " + user.getUsername());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(RoleType.USER);
        user.setActive(StatusType.ACTIVE);
        user = userRepository.save(user);
        paymentService.createCashInHandsPayment(user.getId());
    }

    @Override
    public UserOutput findById(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (Objects.isNull(user)) throw new EntityNotFoundException("Not found any user with id " + id);
        return modelMapper.map(user, UserOutput.class);
    }

    @Override
    public void update(Long userId, UserUpdateInput userUpdateInput) {
        User user = userRepository.findById(userId).orElse(null);
        if (Objects.isNull(user)) throw new EntityNotFoundException("Not found any user with id " + userId);
        if (Objects.nonNull(userUpdateInput.getName())) user.setName(user.getName());
        if (Objects.nonNull(userUpdateInput.getGender())) user.setName(user.getGender());
        if (Objects.nonNull(userUpdateInput.getAddress())) user.setName(user.getAddress());
        if (Objects.nonNull(userUpdateInput.getPhone())) user.setName(user.getPhone());
        if (Objects.nonNull(userUpdateInput.getAvatar())) user.setName(user.getAvatar());
        userRepository.save(user);
    }

    @Override
    public void updatePassword(UserPasswordUpdateInput input) {
        User user = userRepository.findByUsername(input.getUsername());
        if (Objects.isNull(user)) throw new UsernamePasswordIncorrectException("Wrong username or password!");
        if (passwordEncoder.matches(input.getPassword(), user.getPassword())) {
            user.setPassword(input.getNewPassword());
            userRepository.save(user);
        } else {
            throw new UsernamePasswordIncorrectException("Wrong username or password!");
        }
    }

    @Override
    public void forgotPassword(String username) throws MessagingException, UnsupportedEncodingException {
        String token = UUID.randomUUID().toString();
        String resetPasswordLink =
                String.format("%s/passwordResetToken=%s",
                        serviceConfig.getUiUrl(), token);
        User user = updatePasswordResetToken(username, token);
        mail.sendEmail(username, resetPasswordLink, user.getName());
    }

    @Override
    public void resetPassword(String passwordResetToken, String newPassword) {
        User user = userRepository.findByPasswordResetToken(passwordResetToken);
        if (Objects.isNull(user))
            throw new PasswordResetTokenInvalidException("Token invalid");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        if (calendar.getTime().after(user.getPasswordResetTokenExpiryDate()))
            throw new PasswordResetTokenInvalidException("Token is expired");
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setPasswordResetToken(null);
        user.setPasswordResetTokenExpiryDate(null);
        userRepository.save(user);
    }

    @Override
    public User updatePasswordResetToken(String username, String resetPasswordToken) {
        User user = userRepository.findByUsername(username);
        if (Objects.isNull(user)) throw new UserNotFoundException("Not found any user with username: " + username);
        user.setPasswordResetToken(resetPasswordToken);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.HOUR_OF_DAY, serviceConfig.getPasswordResetTokenValidityHours());
        Date expiryDate = calendar.getTime();
        user.setPasswordResetTokenExpiryDate(expiryDate);
        return userRepository.save(user);
    }
}
