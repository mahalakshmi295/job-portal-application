package com.jobportal.backend.controller;

import com.jobportal.backend.service.EmailService;
import com.jobportal.backend.service.OTPService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.util.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private OTPService otpService;

    @Autowired
    private EmailService emailService;
    
    @PostMapping("/otp-login")
    public ResponseEntity<Map<String, Object>> otpLogin(@RequestBody Map<String, Object> payload) {
        try {
            String email = (String) payload.get("email");
            
            if (email == null || email.trim().isEmpty() || !email.contains("@")) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Invalid email address"));
            }
            
            // Generate OTP
            String otp = otpService.generateOTP(email);
            
            // Send OTP via email
            emailService.sendOtpEmail(email, otp);
            
            Map<String, Object> result = new HashMap<>();
            result.put("otpSent", true);
            result.put("message", "OTP sent to your email. Valid for 5 minutes.");
            result.put("email", email);

            // OTP is only sent via email for security (check server logs for development testing)

            return ResponseEntity.ok(result);

        } catch (Exception e) {
            logger.error("Error sending OTP: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                    "error", "Failed to send OTP. Please check email configuration.",
                    "details", e.getMessage()
                ));
        }
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<Map<String, Object>> verifyOtp(@RequestBody Map<String, Object> payload) {
        try {
            String email = (String) payload.get("email");
            String otp = (String) payload.get("otp");
            
            if (email == null || otp == null) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Email and OTP are required"));
            }
            
            boolean verified = otpService.verifyOTP(email, otp);
            
            Map<String, Object> result = new HashMap<>();
            result.put("verified", verified);
            
            if (verified) {
                result.put("message", "Login successful!");
                result.put("email", email);
                // Send welcome email in background
                try {
                    emailService.sendWelcomeEmail(email, email.split("@")[0]);
                } catch (Exception e) {
                    // Don't fail if welcome email fails
                }
            } else {
                int remainingAttempts = otpService.getRemainingAttempts(email);
                result.put("message", "Invalid OTP. " + remainingAttempts + " attempts remaining.");
                result.put("remainingAttempts", remainingAttempts);
            }
            
            return ResponseEntity.ok(result);

        } catch (Exception e) {
            logger.error("Error verifying OTP: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Failed to verify OTP", "details", e.getMessage()));
        }
    }
    
    @GetMapping("/otp-status")
    public ResponseEntity<Map<String, Object>> getOTPStatus(@RequestParam String email) {
        Map<String, Object> result = new HashMap<>();
        result.put("hasValidOTP", otpService.hasValidOTP(email));
        result.put("remainingAttempts", otpService.getRemainingAttempts(email));
        return ResponseEntity.ok(result);
    }
}
