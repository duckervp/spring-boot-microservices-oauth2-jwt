package com.savvycom.userservice.util;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class Mail {
    private final JavaMailSender mailSender;

    /**
     * Send password reset email to user when user forgot password
     * @param recipientEmail User email
     * @param link Reset password link
     * @param name User name
     * @throws MessagingException Error
     * @throws UnsupportedEncodingException Error
     */
    public void sendPasswordResetEmail(String recipientEmail, String link, String name) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("contact@shop.com", "Shop Support");
        helper.setTo(recipientEmail);

        String subject = "Shop Password Reset";

        String content = "<p>Hello, " + (Objects.nonNull(name) ? name + "." : "") + "</p>"
                + "<p>We received a request to reset your password.</p>"
                + "<p>Use this link below to set up a new password for your account.</p>"
                + "<p>If you did not request to reset your password, ignore this email and the link will expire on its own.</p>"
                + "<p><a href=\"" + link + "\">SET NEW PASSWORD</a></p>";

        helper.setSubject(subject);

        helper.setText(content, true);

        mailSender.send(message);
    }
}
