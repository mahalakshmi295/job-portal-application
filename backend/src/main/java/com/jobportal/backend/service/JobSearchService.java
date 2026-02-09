package com.jobportal.backend.service;

import com.jobportal.backend.config.ApiConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import org.springframework.cache.annotation.Cacheable;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;

/**
 * Service for integrating with external Job Search APIs
 * Uses RapidAPI JSearch for real job listings
 */
@Service
public class JobSearchService {
    
    @Autowired
    private ApiConfig apiConfig;
    
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    /**
     * Search jobs using external API (RapidAPI JSearch)
     * Cached for 5 minutes to reduce API calls
     */
    @Cacheable(value = "jobSearchCache", key = "#query + '_' + #location + '_' + #page")
    public Map<String, Object> searchJobs(String query, String location, int page, int numPages) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // Check if API is configured
            if (!apiConfig.isRapidApiConfigured()) {
                return createMockJobResults(query, location);
            }
            
            // Build API URL
            String url = String.format("%s/search?query=%s&page=%d&num_pages=%d",
                ApiConfig.RAPID_API_BASE_URL,
                encodeQueryWithLocation(query, location),
                page,
                numPages
            );
            
            // Set headers
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-RapidAPI-Key", apiConfig.getRapidApiKey());
            headers.set("X-RapidAPI-Host", apiConfig.getRapidApiHost());
            
            HttpEntity<String> entity = new HttpEntity<>(headers);
            
            // Make API call
            ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                String.class
            );
            
            // Parse response
            if (response.getStatusCode() == HttpStatus.OK) {
                JsonNode jsonResponse = objectMapper.readTree(response.getBody());
                result.put("success", true);
                result.put("data", jsonResponse.get("data"));
                result.put("total", jsonResponse.get("total_results"));
                result.put("source", "RapidAPI JSearch");
            }
            
        } catch (Exception e) {
            System.err.println("API call failed: " + e.getMessage());
            // Fallback to mock data
            return createMockJobResults(query, location);
        }
        
        return result;
    }
    
    /**
     * Get job details by ID
     */
    @Cacheable(value = "jobDetailsCache", key = "#jobId")
    public Map<String, Object> getJobDetails(String jobId) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            if (!apiConfig.isRapidApiConfigured()) {
                return createMockJobDetails(jobId);
            }
            
            String url = String.format("%s/job-details?job_id=%s",
                ApiConfig.RAPID_API_BASE_URL, jobId);
            
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-RapidAPI-Key", apiConfig.getRapidApiKey());
            headers.set("X-RapidAPI-Host", apiConfig.getRapidApiHost());
            
            HttpEntity<String> entity = new HttpEntity<>(headers);
            
            ResponseEntity<String> response = restTemplate.exchange(
                url, HttpMethod.GET, entity, String.class
            );
            
            if (response.getStatusCode() == HttpStatus.OK) {
                JsonNode jsonResponse = objectMapper.readTree(response.getBody());
                result.put("success", true);
                result.put("data", jsonResponse.get("data"));
            }
            
        } catch (Exception e) {
            return createMockJobDetails(jobId);
        }
        
        return result;
    }
    
    /**
     * Get salary estimate for a job
     */
    public Map<String, Object> getSalaryEstimate(String jobTitle, String location) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            if (!apiConfig.isRapidApiConfigured()) {
                return createMockSalaryData(jobTitle, location);
            }
            
            String url = String.format("%s/estimated-salary?job_title=%s&location=%s",
                ApiConfig.RAPID_API_BASE_URL,
                jobTitle.replace(" ", "%20"),
                location.replace(" ", "%20")
            );
            
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-RapidAPI-Key", apiConfig.getRapidApiKey());
            headers.set("X-RapidAPI-Host", apiConfig.getRapidApiHost());
            
            HttpEntity<String> entity = new HttpEntity<>(headers);
            
            ResponseEntity<String> response = restTemplate.exchange(
                url, HttpMethod.GET, entity, String.class
            );
            
            if (response.getStatusCode() == HttpStatus.OK) {
                JsonNode jsonResponse = objectMapper.readTree(response.getBody());
                result.put("success", true);
                result.put("data", jsonResponse.get("data"));
            }
            
        } catch (Exception e) {
            return createMockSalaryData(jobTitle, location);
        }
        
        return result;
    }
    
    // Helper methods
    
    private String encodeQueryWithLocation(String query, String location) {
        String combined = query;
        if (location != null && !location.isEmpty()) {
            combined += " in " + location;
        }
        return combined.replace(" ", "%20");
    }
    
    /**
     * Create mock job results when API is not configured
     */
    private Map<String, Object> createMockJobResults(String query, String location) {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> jobs = new ArrayList<>();
        
        // Sample job listings
        String[] companies = {"Google", "Microsoft", "Amazon", "Meta", "Apple", "Netflix", "Tesla", "Uber"};
        String[] titles = {
            "Senior Software Engineer",
            "Full Stack Developer",
            "Backend Engineer",
            "Frontend Developer",
            "DevOps Engineer",
            "Data Scientist",
            "Machine Learning Engineer",
            "Cloud Architect"
        };
        
        for (int i = 0; i < 10; i++) {
            Map<String, Object> job = new HashMap<>();
            job.put("job_id", "mock_" + UUID.randomUUID().toString());
            job.put("employer_name", companies[i % companies.length]);
            job.put("job_title", titles[i % titles.length]);
            job.put("job_city", location != null ? location : "San Francisco");
            job.put("job_state", "CA");
            job.put("job_country", "US");
            job.put("job_employment_type", "FULLTIME");
            job.put("job_description", "We are looking for a talented " + titles[i % titles.length] + 
                    " to join our team. You will work on cutting-edge technologies...");
            job.put("job_posted_at_datetime_utc", "2026-02-09T10:00:00.000Z");
            job.put("job_salary_min", 100000 + (i * 15000));
            job.put("job_salary_max", 150000 + (i * 20000));
            job.put("job_salary_currency", "USD");
            job.put("job_apply_link", "https://example.com/apply/" + i);
            job.put("employer_logo", "https://logo.clearbit.com/" + companies[i % companies.length].toLowerCase() + ".com");
            
            jobs.add(job);
        }
        
        result.put("success", true);
        result.put("data", jobs);
        result.put("total", jobs.size());
        result.put("source", "Mock Data (Configure RapidAPI for real jobs)");
        result.put("message", "Using mock data. Add your RapidAPI key for real job listings.");
        
        return result;
    }
    
    private Map<String, Object> createMockJobDetails(String jobId) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> job = new HashMap<>();
        
        job.put("job_id", jobId);
        job.put("employer_name", "TechCorp Inc.");
        job.put("job_title", "Senior Software Engineer");
        job.put("job_description", "Full job description here...");
        job.put("job_city", "San Francisco");
        job.put("job_state", "CA");
        job.put("job_employment_type", "FULLTIME");
        job.put("job_required_experience", Map.of(
            "required_experience_in_months", 60,
            "experience_level", "Senior"
        ));
        job.put("job_required_skills", Arrays.asList("Java", "Spring Boot", "React", "AWS", "Docker"));
        job.put("job_salary_min", 120000);
        job.put("job_salary_max", 180000);
        
        result.put("success", true);
        result.put("data", job);
        result.put("source", "Mock Data");
        
        return result;
    }
    
    private Map<String, Object> createMockSalaryData(String jobTitle, String location) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> salary = new HashMap<>();
        
        salary.put("job_title", jobTitle);
        salary.put("location", location);
        salary.put("min_salary", 90000);
        salary.put("max_salary", 160000);
        salary.put("median_salary", 125000);
        salary.put("currency", "USD");
        salary.put("salary_period", "YEARLY");
        
        result.put("success", true);
        result.put("data", salary);
        result.put("source", "Mock Data");
        
        return result;
    }
}
