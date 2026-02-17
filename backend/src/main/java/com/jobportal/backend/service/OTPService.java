package com.jobportal.backend.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class OTPService {

    private static final Logger logger = LoggerFactory.getLogger(OTPService.class);
    
    private static class OTPData {
        String otp;
        LocalDateTime expiryTime;
        int attempts;
        
        OTPData(String otp, LocalDateTime expiryTime) {
            this.otp = otp;
            this.expiryTime = expiryTime;
            this.attempts = 0;
        }
    }
    
    // In-memory storage (for production, use Redis or database)
    private Map<String, OTPData> otpStorage = new HashMap<>();
    private static final int OTP_LENGTH = 6;
    private static final int OTP_VALIDITY_MINUTES = 5;
    private static final int MAX_ATTEMPTS = 3;
    private SecureRandom random = new SecureRandom();
    
    /**
     * Generate a 6-digit OTP
     */
    public String generateOTP(String email) {
        // Generate random 6-digit OTP
        int otpNumber = 100000 + random.nextInt(900000);
        String otp = String.valueOf(otpNumber);
        
        // Store OTP with expiry time
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(OTP_VALIDITY_MINUTES);
        otpStorage.put(email.toLowerCase(), new OTPData(otp, expiryTime));
        
        logger.info("🔑 Generated OTP for {}: {} (expires at {})", email, otp, expiryTime);
        return otp;
    }
    
    /**
     * Verify OTP
     */
    public boolean verifyOTP(String email, String otp) {
        String emailKey = email.toLowerCase();
        OTPData data = otpStorage.get(emailKey);
        
        if (data == null) {
            logger.debug("❌ No OTP found for: {}", email);
            return false;
        }

        // Check if OTP expired
        if (LocalDateTime.now().isAfter(data.expiryTime)) {
            logger.info("⏰ OTP expired for: {}", email);
            otpStorage.remove(emailKey);
            return false;
        }

        // Check attempts
        if (data.attempts >= MAX_ATTEMPTS) {
            logger.warn("🚫 Max attempts exceeded for: {}", email);
            otpStorage.remove(emailKey);
            return false;
        }

        // Increment attempts
        data.attempts++;

        // Verify OTP
        boolean isValid = data.otp.equals(otp);

        if (isValid) {
            logger.info("✅ OTP verified successfully for: {}", email);
            otpStorage.remove(emailKey); // Remove after successful verification
        } else {
            logger.info("❌ Invalid OTP for: {} (Attempt {}/{})", email, data.attempts, MAX_ATTEMPTS);
        }
        
        return isValid;
    }
    
    /**
     * Check if OTP exists and is valid for an email
     */
    public boolean hasValidOTP(String email) {
        OTPData data = otpStorage.get(email.toLowerCase());
        if (data == null) return false;
        return LocalDateTime.now().isBefore(data.expiryTime);
    }
    
    /**
     * Get remaining attempts
     */
    public int getRemainingAttempts(String email) {
        OTPData data = otpStorage.get(email.toLowerCase());
        if (data == null) return 0;
        return MAX_ATTEMPTS - data.attempts;
    }
    
    /**
     * Clean up expired OTPs (call periodically)
     */
    public void cleanupExpiredOTPs() {
        LocalDateTime now = LocalDateTime.now();
        otpStorage.entrySet().removeIf(entry -> now.isAfter(entry.getValue().expiryTime));
    }
}
