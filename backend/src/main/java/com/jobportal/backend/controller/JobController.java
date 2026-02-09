package com.jobportal.backend.controller;

import com.jobportal.backend.model.Job;
import com.jobportal.backend.service.JobService;
import com.jobportal.backend.service.JobSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/jobs")
@CrossOrigin(origins = "http://localhost:3000")
public class JobController {
    @Autowired
    private JobService jobService;
    
    @Autowired
    private JobSearchService jobSearchService;

    @GetMapping
    public List<Job> getAllJobs() {
        return jobService.findAll();
    }

    @PostMapping
    public Job createJob(@RequestBody Job job) {
        return jobService.save(job);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Job> getJobById(@PathVariable Long id) {
        return jobService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJob(@PathVariable Long id) {
        jobService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    
    /**
     * Search jobs using external API
     */
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchJobs(
            @RequestParam(required = false, defaultValue = "software engineer") String query,
            @RequestParam(required = false, defaultValue = "") String location,
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "1") int numPages) {
        try {
            Map<String, Object> result = jobSearchService.searchJobs(query, location, page, numPages);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Failed to search jobs: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }
    
    /**
     * Get job details by external job ID
     */
    @GetMapping("/details/{jobId}")
    public ResponseEntity<Map<String, Object>> getJobDetails(@PathVariable String jobId) {
        try {
            Map<String, Object> result = jobSearchService.getJobDetails(jobId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Failed to get job details: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }
    
    /**
     * Get salary estimate
     */
    @GetMapping("/salary-estimate")
    public ResponseEntity<Map<String, Object>> getSalaryEstimate(
            @RequestParam String jobTitle,
            @RequestParam(required = false, defaultValue = "United States") String location) {
        try {
            Map<String, Object> result = jobSearchService.getSalaryEstimate(jobTitle, location);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Failed to get salary estimate: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }
    
    /**
     * Get trending jobs
     */
    @GetMapping("/trending")
    public ResponseEntity<Map<String, Object>> getTrendingJobs(
            @RequestParam(required = false, defaultValue = "technology") String category) {
        try {
            // Search for popular tech roles
            String[] trendingQueries = {
                "AI Engineer", "Machine Learning Engineer", "DevOps Engineer",
                "Full Stack Developer", "Cloud Architect", "Data Scientist"
            };
            
            String query = trendingQueries[new Random().nextInt(trendingQueries.length)];
            Map<String, Object> result = jobSearchService.searchJobs(query, "", 1, 1);
            result.put("category", category);
            result.put("trending", true);
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Failed to get trending jobs: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }
}
