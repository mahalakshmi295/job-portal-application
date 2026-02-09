package com.jobportal.backend.controller;

import com.jobportal.backend.model.*;
import com.jobportal.backend.service.ResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.util.*;

@RestController
@RequestMapping("/api/resume-score")
@CrossOrigin(origins = "http://localhost:3000")
public class ResumeScoreController {
    
    @Autowired
    private ResumeService resumeService;

    /**
     * Analyze and score a resume
     * POST /api/resume-score
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> scoreResume(@RequestBody Map<String, Object> payload) {
        try {
            // Extract user ID (default for demo)
            String userId = (String) payload.getOrDefault("userId", "demo-user");
            
            // Build ResumeData from payload
            ResumeData resumeData = buildResumeData(payload);
            
            // Build raw text from payload (or use structured data)
            String rawText = buildRawText(payload, resumeData);
            
            // Analyze resume
            Map<String, Object> result = resumeService.analyzeResume(userId, resumeData, rawText);
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Failed to analyze resume: " + e.getMessage());
            error.put("status", "error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Get simplified resume score (for compatibility with existing frontend)
     * POST /api/resume-score/simple
     */
    @PostMapping("/simple")
    public ResponseEntity<Map<String, Object>> simpleScoreResume(@RequestBody Map<String, Object> payload) {
        try {
            // Simple scoring based on skills, experience, education
            int skills = payload.containsKey("skills") ? 
                ((List<?>) payload.get("skills")).size() : 0;
            int experience = (int) payload.getOrDefault("experience", 0);
            int education = (int) payload.getOrDefault("education", 0);
            
            // Calculate weighted score
            double skillScore = Math.min(skills * 5, 50);  // Max 50 points
            double expScore = Math.min(experience * 10, 30); // Max 30 points
            double eduScore = Math.min(education * 10, 20);  // Max 20 points
            
            double totalScore = skillScore + expScore + eduScore;
            
            Map<String, Object> result = new HashMap<>();
            result.put("resumeScore", Math.round(totalScore * 100.0) / 100.0);
            result.put("breakdown", Map.of(
                "skills", Math.round(skillScore * 100.0) / 100.0,
                "experience", Math.round(expScore * 100.0) / 100.0,
                "education", Math.round(eduScore * 100.0) / 100.0
            ));
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Failed to score resume");
            error.put("resumeScore", 0);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Get all resumes for a user
     * GET /api/resume-score/user/{userId}
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Resume>> getUserResumes(@PathVariable String userId) {
        List<Resume> resumes = resumeService.getResumesByUserId(userId);
        return ResponseEntity.ok(resumes);
    }

    /**
     * Get specific resume
     * GET /api/resume-score/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Resume> getResume(@PathVariable Long id) {
        Optional<Resume> resume = resumeService.getResume(id);
        return resume.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Delete resume
     * DELETE /api/resume-score/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteResume(
            @PathVariable Long id,
            @RequestParam(required = false, defaultValue = "demo-user") String userId) {
        boolean deleted = resumeService.deleteResume(id, userId);
        
        Map<String, String> response = new HashMap<>();
        if (deleted) {
            response.put("message", "Resume deleted successfully");
            return ResponseEntity.ok(response);
        } else {
            response.put("error", "Resume not found or unauthorized");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    // Helper methods

    private ResumeData buildResumeData(Map<String, Object> payload) {
        ResumeData data = new ResumeData();
        
        // Personal info
        if (payload.containsKey("personalInfo")) {
            data.setPersonalInfo((Map<String, String>) payload.get("personalInfo"));
        }
        
        // Professional summary
        if (payload.containsKey("professionalSummary")) {
            data.setProfessionalSummary((String) payload.get("professionalSummary"));
        }
        
        // Skills
        if (payload.containsKey("skills")) {
            data.setSkills((List<String>) payload.get("skills"));
        }
        
        // Education
        if (payload.containsKey("education")) {
            List<Map<String, Object>> eduList = (List<Map<String, Object>>) payload.get("education");
            List<Education> educations = new ArrayList<>();
            for (Map<String, Object> edu : eduList) {
                Education education = new Education();
                education.setInstitution((String) edu.getOrDefault("institution", ""));
                education.setDegree((String) edu.getOrDefault("degree", ""));
                education.setField((String) edu.getOrDefault("field", ""));
                education.setStartDate((String) edu.getOrDefault("startDate", ""));
                education.setEndDate((String) edu.getOrDefault("endDate", ""));
                education.setGpa((String) edu.getOrDefault("gpa", ""));
                educations.add(education);
            }
            data.setEducation(educations);
        }
        
        // Experience
        if (payload.containsKey("experience")) {
            List<Map<String, Object>> expList = (List<Map<String, Object>>) payload.get("experience");
            List<Experience> experiences = new ArrayList<>();
            for (Map<String, Object> exp : expList) {
                Experience experience = new Experience();
                experience.setCompany((String) exp.getOrDefault("company", ""));
                experience.setPosition((String) exp.getOrDefault("position", ""));
                experience.setStartDate((String) exp.getOrDefault("startDate", ""));
                experience.setEndDate((String) exp.getOrDefault("endDate", ""));
                experience.setCurrent((Boolean) exp.getOrDefault("current", false));
                if (exp.containsKey("description")) {
                    experience.setDescription((List<String>) exp.get("description"));
                }
                experiences.add(experience);
            }
            data.setExperience(experiences);
        }
        
        // Projects
        if (payload.containsKey("projects")) {
            List<Map<String, Object>> projList = (List<Map<String, Object>>) payload.get("projects");
            List<Project> projects = new ArrayList<>();
            for (Map<String, Object> proj : projList) {
                Project project = new Project();
                project.setName((String) proj.getOrDefault("name", ""));
                project.setDescription((String) proj.getOrDefault("description", ""));
                if (proj.containsKey("technologies")) {
                    project.setTechnologies((List<String>) proj.get("technologies"));
                }
                project.setLink((String) proj.getOrDefault("link", ""));
                projects.add(project);
            }
            data.setProjects(projects);
        }
        
        // Certifications
        if (payload.containsKey("certifications")) {
            data.setCertifications((List<String>) payload.get("certifications"));
        }
        
        // Areas of interest
        if (payload.containsKey("areasOfInterest")) {
            data.setAreasOfInterest((List<String>) payload.get("areasOfInterest"));
        }
        
        return data;
    }

    private String buildRawText(Map<String, Object> payload, ResumeData resumeData) {
        // If raw text is provided, use it
        if (payload.containsKey("rawText")) {
            return (String) payload.get("rawText");
        }
        
        // Otherwise, build from structured data
        StringBuilder sb = new StringBuilder();
        
        // Personal info
        if (resumeData.getPersonalInfo() != null) {
            resumeData.getPersonalInfo().forEach((key, value) -> {
                sb.append(key).append(": ").append(value).append("\n");
            });
            sb.append("\n");
        }
        
        // Professional summary
        if (resumeData.getProfessionalSummary() != null) {
            sb.append("PROFESSIONAL SUMMARY\n");
            sb.append(resumeData.getProfessionalSummary()).append("\n\n");
        }
        
        // Skills
        if (resumeData.getSkills() != null && !resumeData.getSkills().isEmpty()) {
            sb.append("SKILLS\n");
            sb.append(String.join(", ", resumeData.getSkills())).append("\n\n");
        }
        
        // Experience
        if (resumeData.getExperience() != null && !resumeData.getExperience().isEmpty()) {
            sb.append("EXPERIENCE\n");
            for (Experience exp : resumeData.getExperience()) {
                sb.append(exp.getPosition()).append(" at ").append(exp.getCompany()).append("\n");
                sb.append(exp.getStartDate()).append(" - ").append(exp.getEndDate()).append("\n");
                if (exp.getDescription() != null) {
                    exp.getDescription().forEach(desc -> sb.append("• ").append(desc).append("\n"));
                }
                sb.append("\n");
            }
        }
        
        // Education
        if (resumeData.getEducation() != null && !resumeData.getEducation().isEmpty()) {
            sb.append("EDUCATION\n");
            for (Education edu : resumeData.getEducation()) {
                sb.append(edu.getDegree()).append(" in ").append(edu.getField()).append("\n");
                sb.append(edu.getInstitution()).append("\n");
                sb.append(edu.getStartDate()).append(" - ").append(edu.getEndDate()).append("\n\n");
            }
        }
        
        // Projects
        if (resumeData.getProjects() != null && !resumeData.getProjects().isEmpty()) {
            sb.append("PROJECTS\n");
            for (Project proj : resumeData.getProjects()) {
                sb.append(proj.getName()).append("\n");
                sb.append(proj.getDescription()).append("\n");
                if (proj.getTechnologies() != null) {
                    sb.append("Technologies: ").append(String.join(", ", proj.getTechnologies())).append("\n");
                }
                sb.append("\n");
            }
        }
        
        return sb.toString();
    }
}
