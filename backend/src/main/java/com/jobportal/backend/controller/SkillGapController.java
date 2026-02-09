package com.jobportal.backend.controller;

import com.jobportal.backend.service.SkillGapAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.*;

@RestController
@RequestMapping("/api/skill-gap")
@CrossOrigin(origins = "http://localhost:3000")
public class SkillGapController {
    
    @Autowired
    private SkillGapAnalysisService skillGapService;
    
    @PostMapping
    public ResponseEntity<Map<String, Object>> analyzeSkillGap(@RequestBody Map<String, Object> payload) {
        try {
            // Extract skills from payload
            List<String> userSkills = (List<String>) payload.getOrDefault("userSkills", new ArrayList<>());
            List<String> jobSkills = (List<String>) payload.getOrDefault("jobSkills", new ArrayList<>());
            
            // Handle alternative key names
            if (userSkills.isEmpty() && payload.containsKey("skills")) {
                userSkills = (List<String>) payload.get("skills");
            }
            if (jobSkills.isEmpty() && payload.containsKey("requiredSkills")) {
                jobSkills = (List<String>) payload.get("requiredSkills");
            }
            
            // Perform analysis
            Map<String, Object> result = skillGapService.analyzeSkillGap(userSkills, jobSkills);
            result.put("status", "success");
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("status", "error");
            error.put("message", "Failed to analyze skill gap: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }
}
