package com.jobportal.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import org.springframework.cache.annotation.Cacheable;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobportal.backend.config.ApiConfig;

import java.util.*;

/**
 * Geolocation service for IP-based location detection
 * Uses ipapi.co for free IP geolocation
 */
@Service
public class GeolocationService {
    
    @Autowired
    private ApiConfig apiConfig;
    
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    /**
     * Get location from IP address
     * Cached for 1 hour
     */
    @Cacheable(value = "locationCache", key = "#ipAddress")
    public Map<String, Object> getLocationFromIP(String ipAddress) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            String url = String.format("%s/%s/json/", 
                apiConfig.getIpapiBaseUrl(), 
                ipAddress != null ? ipAddress : "");
            
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            
            if (response.getStatusCode() == HttpStatus.OK) {
                JsonNode jsonResponse = objectMapper.readTree(response.getBody());
                
                result.put("success", true);
                result.put("ip", jsonResponse.get("ip").asText());
                result.put("city", jsonResponse.get("city").asText());
                result.put("region", jsonResponse.get("region").asText());
                result.put("country", jsonResponse.get("country_name").asText());
                result.put("countryCode", jsonResponse.get("country_code").asText());
                result.put("latitude", jsonResponse.get("latitude").asDouble());
                result.put("longitude", jsonResponse.get("longitude").asDouble());
                result.put("timezone", jsonResponse.get("timezone").asText());
                result.put("currency", jsonResponse.get("currency").asText());
            }
            
        } catch (Exception e) {
            System.err.println("Geolocation API failed: " + e.getMessage());
            return createMockLocation();
        }
        
        return result;
    }
    
    /**
     * Geocode address to coordinates
     */
    public Map<String, Object> geocodeAddress(String address) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // This is a simplified version. For production, use OpenCage or Google Geocoding API
            result.put("success", true);
            result.put("address", address);
            result.put("latitude", 37.7749);  // San Francisco default
            result.put("longitude", -122.4194);
            result.put("message", "Using default coordinates. Configure geocoding API for accurate results.");
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("error", e.getMessage());
        }
        
        return result;
    }
    
    /**
     * Calculate distance between two coordinates (in miles)
     */
    public double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int EARTH_RADIUS = 3959; // miles
        
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        
        return EARTH_RADIUS * c;
    }
    
    /**
     * Get nearby cities
     */
    public List<String> getNearbyCities(String city) {
        // Simplified version - would use a real geocoding API in production
        Map<String, List<String>> nearbyCitiesMap = new HashMap<>();
        nearbyCitiesMap.put("San Francisco", Arrays.asList("Oakland", "San Jose", "Berkeley", "Palo Alto"));
        nearbyCitiesMap.put("New York", Arrays.asList("Newark", "Jersey City", "Yonkers", "Stamford"));
        nearbyCitiesMap.put("Los Angeles", Arrays.asList("Long Beach", "Anaheim", "Santa Monica", "Pasadena"));
        nearbyCitiesMap.put("Chicago", Arrays.asList("Evanston", "Oak Park", "Naperville", "Aurora"));
        
        return nearbyCitiesMap.getOrDefault(city, Arrays.asList("Nearby cities not found"));
    }
    
    private Map<String, Object> createMockLocation() {
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("ip", "127.0.0.1");
        result.put("city", "San Francisco");
        result.put("region", "California");
        result.put("country", "United States");
        result.put("countryCode", "US");
        result.put("latitude", 37.7749);
        result.put("longitude", -122.4194);
        result.put("timezone", "America/Los_Angeles");
        result.put("currency", "USD");
        result.put("source", "Mock Data");
        return result;
    }
}
