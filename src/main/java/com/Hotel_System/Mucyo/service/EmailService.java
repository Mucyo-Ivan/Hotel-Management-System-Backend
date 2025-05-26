package com.Hotel_System.Mucyo.service;

import com.Hotel_System.Mucyo.model.Booking;
import com.Hotel_System.Mucyo.model.Role;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    public void sendBookingConfirmation(String to, Booking booking) {
        log.info("Sending booking confirmation email to: {}", to);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Booking Confirmation - " + booking.getRoom().getHotel().getName());
        message.setText(String.format(
            "Dear %s,\n\n" +
            "Your booking has been confirmed.\n\n" +
            "Booking Details:\n" +
            "Hotel: %s\n" +
            "Room Type: %s\n" +
            "Check-in: %s\n" +
            "Check-out: %s\n" +
            "Booking ID: %d\n" +
            "Total Price: $%.2f\n\n" +
            "Thank you for choosing our hotel!\n\n" +
            "Best regards,\n" +
            "Hotel Management Team",
            booking.getUser().getName(),
            booking.getRoom().getHotel().getName(),
            booking.getRoom().getRoomType(),
            booking.getCheckInDate(),
            booking.getCheckOutDate(),
            booking.getId(),
            booking.getTotalPrice()
        ));
        mailSender.send(message);
        log.info("Booking confirmation email sent successfully");
    }

    public void sendBookingCancellation(String to, Booking booking) {
        log.info("Sending booking cancellation email to: {}", to);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Booking Cancellation - " + booking.getRoom().getHotel().getName());
        message.setText(String.format(
            "Dear %s,\n\n" +
            "Your booking has been cancelled.\n\n" +
            "Booking Details:\n" +
            "Hotel: %s\n" +
            "Room Type: %s\n" +
            "Check-in: %s\n" +
            "Check-out: %s\n" +
            "Booking ID: %d\n" +
            "Total Price: $%.2f\n\n" +
            "If you have any questions, please contact us.\n\n" +
            "Best regards,\n" +
            "Hotel Management Team",
            booking.getUser().getName(),
            booking.getRoom().getHotel().getName(),
            booking.getRoom().getRoomType(),
            booking.getCheckInDate(),
            booking.getCheckOutDate(),
            booking.getId(),
            booking.getTotalPrice()
        ));
        mailSender.send(message);
        log.info("Booking cancellation email sent successfully");
    }

    public void sendWelcomeEmail(String to, String name, Role role) {
        try {
            log.info("Preparing to send welcome email to: {}", to);
            Context context = new Context();
            context.setVariable("name", name);
            context.setVariable("role", role.name());
            
            log.info("Processing email template for user: {}", name);
            String emailContent = templateEngine.process("welcome-email", context);
            
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setFrom("noreply@hotelsystem.com");
            helper.setTo(to);
            helper.setSubject("Welcome to Hotel System!");
            helper.setText(emailContent, true);
            
            log.info("Attempting to send email to: {}", to);
            mailSender.send(message);
            log.info("Welcome email sent successfully to: {}", to);
        } catch (MessagingException e) {
            log.error("Failed to send welcome email to: {}. Error: {}", to, e.getMessage(), e);
            throw new RuntimeException("Failed to send welcome email: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("Unexpected error while sending welcome email to: {}. Error: {}", to, e.getMessage(), e);
            throw new RuntimeException("Unexpected error while sending welcome email: " + e.getMessage(), e);
        }
    }
} 