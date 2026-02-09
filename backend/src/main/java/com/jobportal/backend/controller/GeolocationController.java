package com.jobportal.backend.controller;

import com.jobportal.backend.service.GeolocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import jakarta.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@RequestMapping("/api/geo")
@CrossOrigin(origins = "http://localhost:3000")
public class GeolocationController {
    
    @Autowired
    private GeolocationService geolocationService;
    
    /**
     * Get location from IP address
     */
    @GetMapping("/location")
    public ResponseEntity<Map<String, Object>> getLocation(
            HttpServletRequest request,
            @RequestParam(required = false) String ip) {
        try {
            // Get IP from parameter or request
            String ipAddress = ip != null ? ip : getClientIP(request);
            Map<String, Object> result = geolocationService.getLocationFromIP(ipAddress);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Failed to get location: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }
    
    /**
     * Geocode address to coordinates
     */
    @PostMapping("/geocode")
    public ResponseEntity<Map<String, Object>> geocodeAddress(@RequestBody Map<String, String> payload) {
        try {
            String address = payload.get("address");
            if (address == null || address.isEmpty()) {
                Map<String, Object> error = new HashMap<>();
                error.put("error", "Address is required");
                return ResponseEntity.badRequest().body(error);
            }
            
            Map<String, Object> result = geolocationService.geocodeAddress(address);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Failed to geocode address: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }
    
    /**
     * Calculate distance between two coordinates
     */
    @PostMapping("/distance")
    public ResponseEntity<Map<String, Object>> calculateDistance(@RequestBody Map<String, Double> payload) {
        try {
            Double lat1 = payload.get("lat1");
            Double lon1 = payload.get("lon1");
            Double lat2 = payload.get("lat2");
            Double lon2 = payload.get("lon2");
            
            if (lat1 == null || lon1 == null || lat2 == null || lon2 == null) {
                Map<String, Object> error = new HashMap<>();
                error.put("error", "All coordinates are required (lat1, lon1, lat2, lon2)");
                return ResponseEntity.badRequest().body(error);
            }
            
            double distance = geolocationService.calculateDistance(lat1, lon1, lat2, lon2);
            
            Map<String, Object> result = new HashMap<>();
            result.put("distance", Math.round(distance * 100.0) / 100.0);
            result.put("unit", "miles");
            result.put("distanceKm", Math.round(distance * 1.60934 * 100.0) / 100.0);
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Failed to calculate distance: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }
    
    /**
     * Get nearby cities
     */
    @GetMapping("/nearby-cities")
    public ResponseEntity<Map<String, Object>> getNearbyCities(@RequestParam String city) {
        try {
            List<String> cities = geolocationService.getNearbyCities(city);
            
            Map<String, Object> result = new HashMap<>();
            result.put("city", city);
            result.put("nearbyCities", cities);
            result.put("count", cities.size());
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Failed to get nearby cities: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }
    
    // Helper method to get client IP
    private String getClientIP(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
}
