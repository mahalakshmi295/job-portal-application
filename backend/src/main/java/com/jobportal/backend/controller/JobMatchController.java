package com.jobportal.backend.controller;

import com.jobportal.backend.service.JobMatchingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.*;

@RestController
@RequestMapping("/api/job-match")
@CrossOrigin(origins = "http://localhost:3000")
public class JobMatchController {
    
    @Autowired
    private JobMatchingService jobMatchingService;
    
    @PostMapping
    public ResponseEntity<Map<String, Object>> matchJob(@RequestBody Map<String, Object> payload) {
        try {
            // Extract data from payload
            List<String> userSkills = (List<String>) payload.getOrDefault("userSkills", new ArrayList<>());
            List<String> jobSkills = (List<String>) payload.getOrDefault("jobSkills", new ArrayList<>());
            
            Integer userExperience = payload.containsKey("userExperience") ? 
                ((Number) payload.get("userExperience")).intValue() : null;
            Integer requiredExperience = payload.containsKey("requiredExperience") ? 
                ((Number) payload.get("requiredExperience")).intValue() : null;
            
            String userEducation = (String) payload.getOrDefault("userEducation", "");
            String requiredEducation = (String) payload.getOrDefault("requiredEducation", "");
            
            // Handle alternative key names
            if (userSkills.isEmpty() && payload.containsKey("skills")) {
                userSkills = (List<String>) payload.get("skills");
            }
            if (jobSkills.isEmpty() && payload.containsKey("requiredSkills")) {
                jobSkills = (List<String>) payload.get("requiredSkills");
            }
            if (userExperience == null && payload.containsKey("experience")) {
                userExperience = ((Number) payload.get("experience")).intValue();
            }
            if (userEducation.isEmpty() && payload.containsKey("education")) {
                userEducation = (String) payload.get("education");
            }
            
            // Calculate match
            Map<String, Object> result = jobMatchingService.calculateJobMatch(
                userSkills, jobSkills, userExperience, requiredExperience, 
                userEducation, requiredEducation
            );
            
            result.put("status", "success");
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("status", "error");
            error.put("message", "Failed to calculate job match: " + e.getMessage());
            error.put("matchPercent", 0);
            return ResponseEntity.internalServerError().body(error);
        }
    }
}
