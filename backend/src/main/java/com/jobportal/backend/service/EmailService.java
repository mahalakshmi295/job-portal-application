package com.jobportal.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    
    @Autowired
    private JavaMailSender mailSender;
    
    @Value("${spring.mail.username}")
    private String fromEmail;
    
    public void sendOtpEmail(String toEmail, String otp) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("🔐 Your Job Portal Login OTP");
            message.setText(
                "Hello!\n\n" +
                "Your One-Time Password (OTP) for Job Portal login is:\n\n" +
                "⭐ " + otp + " ⭐\n\n" +
                "This OTP is valid for 5 minutes.\n\n" +
                "If you didn't request this OTP, please ignore this email.\n\n" +
                "Best regards,\n" +
                "Job Portal Team"
            );
            
            mailSender.send(message);
            System.out.println("✅ OTP email sent successfully to: " + toEmail);
        } catch (Exception e) {
            System.err.println("❌ Failed to send OTP email: " + e.getMessage());
            throw new RuntimeException("Failed to send email. Please check email configuration.", e);
        }
    }
    
    public void sendWelcomeEmail(String toEmail, String username) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("🎉 Welcome to Job Portal!");
            message.setText(
                "Hello " + username + "!\n\n" +
                "Welcome to Job Portal - Your AI-powered job search companion!\n\n" +
                "You can now access:\n" +
                "✓ AI Skill Gap Analysis\n" +
                "✓ Resume Score Calculator\n" +
                "✓ Job Match Finder\n" +
                "✓ AI Chatbot Assistant\n" +
                "✓ Real-time Job Search (via RapidAPI)\n" +
                "✓ Analytics Dashboard\n" +
                "✓ Geolocation Services\n\n" +
                "Start exploring at: http://localhost:3000\n\n" +
                "Happy job hunting!\n\n" +
                "Best regards,\n" +
                "Job Portal Team"
            );
            
            mailSender.send(message);
            System.out.println("✅ Welcome email sent successfully to: " + toEmail);
        } catch (Exception e) {
            System.err.println("⚠️ Failed to send welcome email: " + e.getMessage());
            // Don't throw exception for welcome email failure
        }
    }
}
