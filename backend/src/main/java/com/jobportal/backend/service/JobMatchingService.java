package com.jobportal.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class JobMatchingService {

    @Autowired
    private JobSearchService jobSearchService;

    /**
     * Find matching jobs for a user based on their profile using real API data
     */
    public List<Map<String, Object>> findMatchingJobs(
            List<String> userSkills,
            String preferredLocation,
            Integer experienceYears,
            String desiredRole) {
        
        List<Map<String, Object>> matchedJobs = new ArrayList<>();
        
        try {
            // Build query from user profile
            String query = desiredRole != null && !desiredRole.isEmpty() 
                ? desiredRole 
                : "software developer";
            
            // Add key skills to query for better matching
            if (userSkills != null && !userSkills.isEmpty()) {
                query += " " + String.join(" ", userSkills.subList(0, Math.min(3, userSkills.size())));
            }
            
            // Fetch jobs from external API
            Map<String, Object> jobData = jobSearchService.searchJobs(
                query, 
                preferredLocation != null ? preferredLocation : "United States", 
                1,
                1
            );
            
            List<Map<String, Object>> jobs = (List<Map<String, Object>>) jobData.get("jobs");
            
            // Calculate match score for each job
            for (Map<String, Object> job : jobs) {
                Map<String, Object> enrichedJob = calculateJobMatchScore(job, userSkills, experienceYears);
                matchedJobs.add(enrichedJob);
            }
            
            // Sort by match score (descending)
            matchedJobs.sort((j1, j2) -> 
                Double.compare((Double) j2.get("matchScore"), (Double) j1.get("matchScore"))
            );
            
        } catch (Exception e) {
            System.err.println("Error finding matching jobs: " + e.getMessage());
            // Return empty list if API fails
        }
        
        return matchedJobs;
    }
    
    /**
     * Calculate match score for a job based on user skills
     */
    private Map<String, Object> calculateJobMatchScore(
            Map<String, Object> job,
            List<String> userSkills,
            Integer experienceYears) {
        
        Map<String, Object> enrichedJob = new HashMap<>(job);
        
        // Extract job requirements from description
        String description = (String) job.getOrDefault("job_description", "");
        List<String> jobSkills = extractSkillsFromDescription(description);
        
        // Calculate skill match
        double skillMatch = calculateSkillMatchPercentage(userSkills, jobSkills);
        
        // Calculate experience match
        double experienceMatch = calculateExperienceMatchFromDescription(description, experienceYears);
        
        // Overall match score (weighted average)
        double matchScore = (skillMatch * 0.7) + (experienceMatch * 0.3);
        
        enrichedJob.put("matchScore", Math.round(matchScore * 10) / 10.0);
        enrichedJob.put("skillMatch", Math.round(skillMatch * 10) / 10.0);
        enrichedJob.put("matchLevel", getMatchLevel(matchScore));
        enrichedJob.put("matchedSkills", getMatchedSkills(userSkills, jobSkills));
        enrichedJob.put("missingSkills", getMissingSkills(userSkills, jobSkills));
        
        return enrichedJob;
    }
    
    /**
     * Extract skills from job description
     */
    private List<String> extractSkillsFromDescription(String description) {
        List<String> commonSkills = Arrays.asList(
            "Java", "Python", "JavaScript", "React", "Angular", "Vue", "Node.js",
            "Spring Boot", "Django", "Flask", "Express", "TypeScript", "HTML", "CSS",
            "SQL", "MongoDB", "PostgreSQL", "MySQL", "Redis", "AWS", "Azure", "GCP",
            "Docker", "Kubernetes", "Jenkins", "Git", "CI/CD", "REST API", "GraphQL",
            "Microservices", "Agile", "Scrum", "Machine Learning", "AI", "TensorFlow",
            "PyTorch", "C++", "C#", ".NET", "Go", "Rust", "Ruby", "PHP", "Swift", "Kotlin"
        );
        
        if (description == null) return new ArrayList<>();
        
        String lowerDesc = description.toLowerCase();
        return commonSkills.stream()
            .filter(skill -> lowerDesc.contains(skill.toLowerCase()))
            .collect(Collectors.toList());
    }
    
    /**
     * Calculate skill match percentage
     */
    private double calculateSkillMatchPercentage(List<String> userSkills, List<String> jobSkills) {
        if (jobSkills.isEmpty()) return 75.0; // Default score if no skills extracted
        if (userSkills == null || userSkills.isEmpty()) return 0.0;
        
        Set<String> normalizedUserSkills = userSkills.stream()
            .map(String::toLowerCase)
            .collect(Collectors.toSet());
        
        long matchCount = jobSkills.stream()
            .filter(skill -> normalizedUserSkills.contains(skill.toLowerCase()))
            .count();
        
        return (matchCount * 100.0) / jobSkills.size();
    }
    
    /**
     * Calculate experience match from description
     */
    private double calculateExperienceMatchFromDescription(String description, Integer userExperience) {
        if (userExperience == null) return 70.0; // Default score
        if (description == null) return 70.0;
        
        String lowerDesc = description.toLowerCase();
        
        // Extract years from description
        if (lowerDesc.contains("0-2 years") || lowerDesc.contains("entry level")) {
            return userExperience >= 0 && userExperience <= 3 ? 100.0 : 60.0;
        } else if (lowerDesc.contains("2-4 years") || lowerDesc.contains("3-5 years")) {
            return userExperience >= 2 && userExperience <= 5 ? 100.0 : 70.0;
        } else if (lowerDesc.contains("5+ years") || lowerDesc.contains("senior")) {
            return userExperience >= 5 ? 100.0 : 50.0;
        }
        
        return 70.0; // Default score
    }
    
    /**
     * Get matched skills between user and job
     */
    private List<String> getMatchedSkills(List<String> userSkills, List<String> jobSkills) {
        if (userSkills == null || jobSkills.isEmpty()) return new ArrayList<>();
        
        Set<String> normalizedUserSkills = userSkills.stream()
            .map(String::toLowerCase)
            .collect(Collectors.toSet());
        
        return jobSkills.stream()
            .filter(skill -> normalizedUserSkills.contains(skill.toLowerCase()))
            .collect(Collectors.toList());
    }
    
    /**
     * Get missing skills that user needs
     */
    private List<String> getMissingSkills(List<String> userSkills, List<String> jobSkills) {
        if (jobSkills.isEmpty()) return new ArrayList<>();
        
        Set<String> normalizedUserSkills = userSkills != null
            ? userSkills.stream().map(String::toLowerCase).collect(Collectors.toSet())
            : new HashSet<>();
        
        return jobSkills.stream()
            .filter(skill -> !normalizedUserSkills.contains(skill.toLowerCase()))
            .limit(5) // Limit to top 5 missing skills
            .collect(Collectors.toList());
    }

    /**
     * Calculate comprehensive job match score
     */
    public Map<String, Object> calculateJobMatch(
            List<String> userSkills,
            List<String> jobSkills,
            Integer userExperience,
            Integer requiredExperience,
            String userEducation,
            String requiredEducation) {
        
        Map<String, Object> result = new HashMap<>();
        
        // Normalize skills
        Set<String> normalizedUserSkills = normalizeSkills(userSkills);
        Set<String> normalizedJobSkills = normalizeSkills(jobSkills);
        
        // Calculate skill match
        Map<String, Object> skillMatch = calculateSkillMatch(
            normalizedUserSkills, normalizedJobSkills, userSkills, jobSkills
        );
        
        // Calculate experience match
        double experienceMatch = calculateExperienceMatch(userExperience, requiredExperience);
        
        // Calculate education match
        double educationMatch = calculateEducationMatch(userEducation, requiredEducation);
        
        // Calculate overall match (weighted average)
        double overallMatch = (
            (double) skillMatch.get("score") * 0.60 +  // Skills: 60%
            experienceMatch * 0.25 +                     // Experience: 25%
            educationMatch * 0.15                        // Education: 15%
        );
        
        // Build comprehensive result
        result.put("overallMatchPercentage", Math.round(overallMatch * 10) / 10.0);
        result.put("matchLevel", getMatchLevel(overallMatch));
        result.put("recommendation", getRecommendation(overallMatch));
        
        // Detailed breakdowns
        result.put("skillMatch", skillMatch);
        result.put("experienceMatch", Math.round(experienceMatch * 10) / 10.0);
        result.put("educationMatch", Math.round(educationMatch * 10) / 10.0);
        
        // Statistics
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalSkillsRequired", jobSkills.size());
        stats.put("skillsMatched", skillMatch.get("matchedCount"));
        stats.put("skillsMissing", skillMatch.get("missingCount"));
        stats.put("experienceGap", Math.max(0, (requiredExperience != null ? requiredExperience : 0) - 
                                              (userExperience != null ? userExperience : 0)));
        result.put("statistics", stats);
        
        // Action items
        result.put("nextSteps", generateNextSteps(overallMatch, skillMatch, experienceMatch));
        
        // Strengths and improvements
        result.put("strengths", identifyStrengths(skillMatch, experienceMatch, educationMatch));
        result.put("areasToImprove", identifyImprovements(skillMatch, experienceMatch, educationMatch));
        
        return result;
    }

    /**
     * Normalize skills for case-insensitive matching
     */
    private Set<String> normalizeSkills(List<String> skills) {
        if (skills == null) return new HashSet<>();
        return skills.stream()
            .map(String::toLowerCase)
            .map(String::trim)
            .collect(Collectors.toSet());
    }

    /**
     * Calculate skill match with detailed breakdown
     */
    private Map<String, Object> calculateSkillMatch(
            Set<String> normalizedUserSkills,
            Set<String> normalizedJobSkills,
            List<String> userSkills,
            List<String> jobSkills) {
        
        Map<String, Object> result = new HashMap<>();
        
        // Find exact matches
        List<String> matched = jobSkills.stream()
            .filter(skill -> normalizedUserSkills.contains(skill.toLowerCase()))
            .collect(Collectors.toList());
        
        // Find missing skills
        List<String> missing = jobSkills.stream()
            .filter(skill -> !normalizedUserSkills.contains(skill.toLowerCase()))
            .collect(Collectors.toList());
        
        // Find related skills (fuzzy matching)
        List<String> relatedSkills = findRelatedSkills(normalizedUserSkills, normalizedJobSkills);
        
        // Calculate score
        double exactMatchScore = jobSkills.isEmpty() ? 100.0 : 
            (matched.size() * 100.0) / jobSkills.size();
        
        double relatedBonus = relatedSkills.size() * 5.0; // 5% bonus per related skill
        double finalScore = Math.min(100.0, exactMatchScore + relatedBonus);
        
        result.put("score", Math.round(finalScore * 10) / 10.0);
        result.put("matchedSkills", matched);
        result.put("missingSkills", missing);
        result.put("relatedSkills", relatedSkills);
        result.put("matchedCount", matched.size());
        result.put("missingCount", missing.size());
        result.put("relatedCount", relatedSkills.size());
        
        return result;
    }

    /**
     * Find related skills (e.g., React and Redux, Java and Spring)
     */
    private List<String> findRelatedSkills(Set<String> userSkills, Set<String> jobSkills) {
        Map<String, List<String>> relatedSkillsMap = new HashMap<>();
        relatedSkillsMap.put("react", Arrays.asList("redux", "next.js", "jsx"));
        relatedSkillsMap.put("angular", Arrays.asList("typescript", "rxjs"));
        relatedSkillsMap.put("java", Arrays.asList("spring", "spring boot", "maven", "gradle"));
        relatedSkillsMap.put("python", Arrays.asList("django", "flask", "pandas", "numpy"));
        relatedSkillsMap.put("node.js", Arrays.asList("express", "nest.js"));
        relatedSkillsMap.put("aws", Arrays.asList("ec2", "s3", "lambda", "cloudformation"));
        relatedSkillsMap.put("docker", Arrays.asList("kubernetes", "containerization"));
        
        List<String> related = new ArrayList<>();
        
        for (String jobSkill : jobSkills) {
            if (userSkills.contains(jobSkill)) continue;
            
            for (Map.Entry<String, List<String>> entry : relatedSkillsMap.entrySet()) {
                if (userSkills.contains(entry.getKey()) && 
                    entry.getValue().contains(jobSkill)) {
                    related.add(jobSkill);
                    break;
                }
            }
        }
        
        return related;
    }

    /**
     * Calculate experience match percentage
     */
    private double calculateExperienceMatch(Integer userExp, Integer requiredExp) {
        if (requiredExp == null || requiredExp == 0) return 100.0;
        if (userExp == null) userExp = 0;
        
        if (userExp >= requiredExp) {
            return 100.0;
        } else if (userExp >= requiredExp * 0.75) {
            return 85.0;
        } else if (userExp >= requiredExp * 0.50) {
            return 70.0;
        } else if (userExp >= requiredExp * 0.25) {
            return 50.0;
        } else {
            return 30.0;
        }
    }

    /**
     * Calculate education match percentage
     */
    private double calculateEducationMatch(String userEdu, String requiredEdu) {
        if (requiredEdu == null || requiredEdu.isEmpty()) return 100.0;
        if (userEdu == null || userEdu.isEmpty()) return 50.0;
        
        Map<String, Integer> eduLevels = new HashMap<>();
        eduLevels.put("high school", 1);
        eduLevels.put("associate", 2);
        eduLevels.put("bachelor", 3);
        eduLevels.put("master", 4);
        eduLevels.put("phd", 5);
        eduLevels.put("doctorate", 5);
        
        int userLevel = getEducationLevel(userEdu, eduLevels);
        int requiredLevel = getEducationLevel(requiredEdu, eduLevels);
        
        if (userLevel >= requiredLevel) {
            return 100.0;
        } else if (userLevel == requiredLevel - 1) {
            return 75.0;
        } else {
            return 50.0;
        }
    }

    private int getEducationLevel(String education, Map<String, Integer> levels) {
        String eduLower = education.toLowerCase();
        for (Map.Entry<String, Integer> entry : levels.entrySet()) {
            if (eduLower.contains(entry.getKey())) {
                return entry.getValue();
            }
        }
        return 2; // Default to associate level
    }

    /**
     * Get match level description
     */
    private String getMatchLevel(double matchPercentage) {
        if (matchPercentage >= 90) return "Excellent Match";
        if (matchPercentage >= 75) return "Strong Match";
        if (matchPercentage >= 60) return "Good Match";
        if (matchPercentage >= 45) return "Fair Match";
        return "Weak Match";
    }

    /**
     * Get recommendation based on match percentage
     */
    private String getRecommendation(double matchPercentage) {
        if (matchPercentage >= 85) {
            return "Highly recommended to apply immediately! You're an excellent fit for this role.";
        } else if (matchPercentage >= 70) {
            return "Recommended to apply. Your profile aligns well with the job requirements.";
        } else if (matchPercentage >= 55) {
            return "Consider applying after addressing a few skill gaps. You have a decent foundation.";
        } else if (matchPercentage >= 40) {
            return "Significant upskilling needed. Consider similar roles or gain more experience first.";
        } else {
            return "This role may not be the best fit. Look for positions that better match your current skills.";
        }
    }

    /**
     * Generate actionable next steps
     */
    private List<Map<String, String>> generateNextSteps(
            double overallMatch,
            Map<String, Object> skillMatch,
            double experienceMatch) {
        
        List<Map<String, String>> steps = new ArrayList<>();
        
        if (overallMatch >= 75) {
            steps.add(createStep(
                "Apply Now",
                "Your profile is a strong match. Submit your application and highlight your matching skills.",
                "high"
            ));
            steps.add(createStep(
                "Customize Resume",
                "Tailor your resume to emphasize skills that match the job requirements.",
                "high"
            ));
            steps.add(createStep(
                "Prepare for Interview",
                "Review the job description and prepare examples showcasing your relevant experience.",
                "medium"
            ));
        } else if (overallMatch >= 55) {
            List<String> missingSkills = (List<String>) skillMatch.get("missingSkills");
            if (!missingSkills.isEmpty()) {
                String topSkills = String.join(", ", 
                    missingSkills.subList(0, Math.min(3, missingSkills.size())));
                steps.add(createStep(
                    "Learn Key Skills",
                    "Focus on learning: " + topSkills + " to improve your profile.",
                    "high"
                ));
            }
            steps.add(createStep(
                "Build Projects",
                "Create projects using the required technologies to demonstrate practical experience.",
                "medium"
            ));
            steps.add(createStep(
                "Apply Strategically",
                "Apply now and address skill gaps in your cover letter with learning plan.",
                "medium"
            ));
        } else {
            steps.add(createStep(
                "Skill Development Plan",
                "Create a 3-6 month learning plan to acquire the missing skills.",
                "high"
            ));
            steps.add(createStep(
                "Gain Experience",
                "Look for entry-level or related positions to build relevant experience.",
                "high"
            ));
            steps.add(createStep(
                "Network & Learn",
                "Connect with professionals in this field and learn about required competencies.",
                "medium"
            ));
        }
        
        return steps;
    }

    private Map<String, String> createStep(String action, String description, String priority) {
        Map<String, String> step = new HashMap<>();
        step.put("action", action);
        step.put("description", description);
        step.put("priority", priority);
        return step;
    }

    /**
     * Identify candidate strengths
     */
    private List<String> identifyStrengths(
            Map<String, Object> skillMatch,
            double experienceMatch,
            double educationMatch) {
        
        List<String> strengths = new ArrayList<>();
        
        double skillScore = (double) skillMatch.get("score");
        int matchedCount = (int) skillMatch.get("matchedCount");
        
        if (skillScore >= 80) {
            strengths.add("Strong technical skill alignment with " + matchedCount + " matching skills");
        }
        if (experienceMatch >= 85) {
            strengths.add("Experience level meets or exceeds requirements");
        }
        if (educationMatch >= 90) {
            strengths.add("Educational qualifications align well with requirements");
        }
        
        List<String> relatedSkills = (List<String>) skillMatch.get("relatedSkills");
        if (!relatedSkills.isEmpty()) {
            strengths.add("Possesses " + relatedSkills.size() + " related skills that complement requirements");
        }
        
        if (strengths.isEmpty()) {
            strengths.add("Foundation skills present - room for growth");
        }
        
        return strengths;
    }

    /**
     * Identify areas for improvement
     */
    private List<String> identifyImprovements(
            Map<String, Object> skillMatch,
            double experienceMatch,
            double educationMatch) {
        
        List<String> improvements = new ArrayList<>();
        
        List<String> missingSkills = (List<String>) skillMatch.get("missingSkills");
        if (!missingSkills.isEmpty()) {
            String topMissing = String.join(", ", 
                missingSkills.subList(0, Math.min(3, missingSkills.size())));
            improvements.add("Acquire key skills: " + topMissing);
        }
        
        if (experienceMatch < 70) {
            improvements.add("Gain more relevant work experience in this domain");
        }
        
        if (educationMatch < 75) {
            improvements.add("Consider additional certifications or degrees to meet educational requirements");
        }
        
        if (improvements.isEmpty()) {
            improvements.add("Continue building on your strong foundation");
        }
        
        return improvements;
    }
}
