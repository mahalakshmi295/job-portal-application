package com.jobportal.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SkillGapAnalysisService {

    @Autowired
    private JobSearchService jobSearchService;

    // Skill categories with weights
    private static final Map<String, List<String>> SKILL_CATEGORIES = new HashMap<>();
    
    static {
        SKILL_CATEGORIES.put("Frontend", Arrays.asList(
            "React", "Angular", "Vue.js", "JavaScript", "TypeScript", "HTML5", "CSS3", 
            "Redux", "Webpack", "Next.js", "Tailwind CSS", "Bootstrap"
        ));
        SKILL_CATEGORIES.put("Backend", Arrays.asList(
            "Java", "Spring Boot", "Node.js", "Express", "Python", "Django", "Flask",
            "C#", ".NET", "Go", "Ruby", "PHP", "REST API", "GraphQL", "Microservices"
        ));
        SKILL_CATEGORIES.put("Database", Arrays.asList(
            "PostgreSQL", "MySQL", "MongoDB", "Redis", "Oracle", "SQL Server",
            "DynamoDB", "Cassandra", "Elasticsearch"
        ));
        SKILL_CATEGORIES.put("Cloud", Arrays.asList(
            "AWS", "Azure", "GCP", "Docker", "Kubernetes", "Terraform", "Ansible",
            "CloudFormation", "Lambda", "EC2", "S3"
        ));
        SKILL_CATEGORIES.put("DevOps", Arrays.asList(
            "Jenkins", "GitLab CI", "GitHub Actions", "CircleCI", "CI/CD", "Git",
            "Linux", "Bash", "Monitoring", "Prometheus", "Grafana"
        ));
        SKILL_CATEGORIES.put("Testing", Arrays.asList(
            "JUnit", "TestNG", "Jest", "Mocha", "Selenium", "Cypress", "Junit5",
            "Mockito", "PyTest", "Unit Testing", "Integration Testing"
        ));
    }

    /**
     * Analyze skill gap with real market demand data from API
     */
    public Map<String, Object> analyzeSkillGapWithMarketData(
            List<String> userSkills, 
            String targetRole, 
            String location) {
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            // Fetch real job requirements from market
            Map<String, Object> jobData = jobSearchService.searchJobs(
                targetRole != null ? targetRole : "software developer",
                location != null ? location : "United States",
                1,
                1
            );
            
            List<Map<String, Object>> jobs = (List<Map<String, Object>>) jobData.get("jobs");
            
            // Extract required skills from real job postings
            Map<String, Integer> marketSkillDemand = extractMarketSkillDemand(jobs);
            List<String> topMarketSkills = getTopSkills(marketSkillDemand, 15);
            
            // Analyze gap against market requirements
            Map<String, Object> gapAnalysis = analyzeSkillGap(userSkills, topMarketSkills);
            
            // Add market demand information
            gapAnalysis.put("marketSkillDemand", marketSkillDemand);
            gapAnalysis.put("topMarketSkills", topMarketSkills);
            gapAnalysis.put("jobsAnalyzed", jobs.size());
            gapAnalysis.put("targetRole", targetRole);
            gapAnalysis.put("location", location);
            gapAnalysis.put("dataSource", jobData.get("source"));
            
            // Enhance recommendations with market insights
            List<Map<String, Object>> marketRecommendations = generateMarketBasedRecommendations(
                (List<String>) gapAnalysis.get("missingSkills"),
                marketSkillDemand
            );
            gapAnalysis.put("marketRecommendations", marketRecommendations);
            
            result = gapAnalysis;
            
        } catch (Exception e) {
            System.err.println("Error analyzing skill gap with market data: " + e.getMessage());
            // Fallback to basic analysis if API fails
            result = analyzeSkillGap(userSkills, getDefaultRequiredSkills());
            result.put("dataSource", "Default Data (API unavailable)");
        }
        
        return result;
    }
    
    /**
     * Extract skill demand from real job postings
     */
    private Map<String, Integer> extractMarketSkillDemand(List<Map<String, Object>> jobs) {
        Map<String, Integer> skillDemand = new HashMap<>();
        
        // Get all skills from all categories
        List<String> allSkills = SKILL_CATEGORIES.values().stream()
            .flatMap(List::stream)
            .collect(Collectors.toList());
        
        // Count skill mentions in job descriptions
        for (Map<String, Object> job : jobs) {
            String description = (String) job.getOrDefault("job_description", "");
            String title = (String) job.getOrDefault("job_title", "");
            String combined = (description + " " + title).toLowerCase();
            
            for (String skill : allSkills) {
                if (combined.contains(skill.toLowerCase())) {
                    skillDemand.merge(skill, 1, Integer::sum);
                }
            }
        }
        
        return skillDemand;
    }
    
    /**
     * Get top N skills by demand
     */
    private List<String> getTopSkills(Map<String, Integer> skillDemand, int count) {
        return skillDemand.entrySet().stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
            .limit(count)
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());
    }
    
    /**
     * Generate recommendations based on market demand
     */
    private List<Map<String, Object>> generateMarketBasedRecommendations(
            List<String> missingSkills,
            Map<String, Integer> marketDemand) {
        
        List<Map<String, Object>> recommendations = new ArrayList<>();
        
        for (String skill : missingSkills) {
            Map<String, Object> recommendation = new HashMap<>();
            recommendation.put("skill", skill);
            
            int demand = marketDemand.getOrDefault(skill, 0);
            recommendation.put("marketDemand", demand);
            
            // Priority based on market demand
            String priority = demand > 30 ? "High" : demand > 15 ? "Medium" : "Low";
            recommendation.put("priority", priority);
            
            // Estimated learning time
            String category = findSkillCategory(skill);
            recommendation.put("category", category);
            recommendation.put("estimatedTime", getEstimatedLearningTime(category));
            
            // Learning resources
            recommendation.put("resources", buildLearningResources(skill));
            
            recommendations.add(recommendation);
        }
        
        // Sort by market demand (descending)
        recommendations.sort((r1, r2) -> 
            Integer.compare((Integer) r2.get("marketDemand"), (Integer) r1.get("marketDemand"))
        );
        
        return recommendations;
    }
    
    /**
     * Get estimated learning time for a skill category
     */
    private String getEstimatedLearningTime(String category) {
        switch (category) {
            case "Frontend":
            case "Testing":
                return "3-4 weeks";
            case "Backend":
            case "Database":
                return "4-6 weeks";
            case "Cloud":
            case "DevOps":
                return "6-8 weeks";
            default:
                return "4-6 weeks";
        }
    }
    
    /**
     * Get default required skills if API is unavailable
     */
    private List<String> getDefaultRequiredSkills() {
        return Arrays.asList(
            "Java", "Spring Boot", "React", "JavaScript", "SQL", 
            "Docker", "AWS", "Git", "REST API", "Microservices"
        );
    }

    /**
     * Analyze skill gap between user skills and job requirements
     */
    public Map<String, Object> analyzeSkillGap(List<String> userSkills, List<String> jobSkills) {
        Map<String, Object> result = new HashMap<>();
        
        // Normalize skills (case-insensitive matching)
        Set<String> normalizedUserSkills = userSkills.stream()
            .map(String::toLowerCase)
            .collect(Collectors.toSet());
        
        Set<String> normalizedJobSkills = jobSkills.stream()
            .map(String::toLowerCase)
            .collect(Collectors.toSet());
        
        // Find matching skills
        List<String> matchingSkills = jobSkills.stream()
            .filter(skill -> normalizedUserSkills.contains(skill.toLowerCase()))
            .collect(Collectors.toList());
        
        // Find missing skills
        List<String> missingSkills = jobSkills.stream()
            .filter(skill -> !normalizedUserSkills.contains(skill.toLowerCase()))
            .collect(Collectors.toList());
        
        // Calculate scores
        double coverageScore = jobSkills.isEmpty() ? 100.0 : 
            (matchingSkills.size() * 100.0) / jobSkills.size();
        
        double gapPercentage = 100.0 - coverageScore;
        
        // Categorize missing skills
        Map<String, List<String>> categorizedGaps = categorizeMissingSkills(missingSkills);
        
        // Generate priority recommendations
        List<Map<String, Object>> recommendations = generateRecommendations(
            missingSkills, categorizedGaps, coverageScore
        );
        
        // Learning resources
        List<Map<String, String>> learningResources = generateLearningResources(missingSkills);
        
        // Skill importance analysis
        Map<String, String> skillImportance = analyzeSkillImportance(missingSkills);
        
        // Build result
        result.put("matchingSkills", matchingSkills);
        result.put("missingSkills", missingSkills);
        result.put("coverageScore", Math.round(coverageScore * 10) / 10.0);
        result.put("gapPercentage", Math.round(gapPercentage * 10) / 10.0);
        result.put("totalRequired", jobSkills.size());
        result.put("totalMatched", matchingSkills.size());
        result.put("totalMissing", missingSkills.size());
        result.put("categorizedGaps", categorizedGaps);
        result.put("recommendations", recommendations);
        result.put("learningResources", learningResources);
        result.put("skillImportance", skillImportance);
        result.put("readinessLevel", getReadinessLevel(coverageScore));
        result.put("estimatedLearningTime", estimateLearningTime(missingSkills.size()));
        
        return result;
    }

    /**
     * Categorize missing skills by technology area
     */
    private Map<String, List<String>> categorizeMissingSkills(List<String> missingSkills) {
        Map<String, List<String>> categorized = new HashMap<>();
        
        for (Map.Entry<String, List<String>> category : SKILL_CATEGORIES.entrySet()) {
            List<String> categoryMissing = missingSkills.stream()
                .filter(skill -> category.getValue().stream()
                    .anyMatch(s -> s.equalsIgnoreCase(skill)))
                .collect(Collectors.toList());
            
            if (!categoryMissing.isEmpty()) {
                categorized.put(category.getKey(), categoryMissing);
            }
        }
        
        // Uncategorized skills
        List<String> uncategorized = missingSkills.stream()
            .filter(skill -> !SKILL_CATEGORIES.values().stream()
                .flatMap(List::stream)
                .anyMatch(s -> s.equalsIgnoreCase(skill)))
            .collect(Collectors.toList());
        
        if (!uncategorized.isEmpty()) {
            categorized.put("Other", uncategorized);
        }
        
        return categorized;
    }

    /**
     * Generate prioritized recommendations
     */
    private List<Map<String, Object>> generateRecommendations(
            List<String> missingSkills,
            Map<String, List<String>> categorizedGaps,
            double coverageScore) {
        
        List<Map<String, Object>> recommendations = new ArrayList<>();
        
        if (coverageScore >= 80) {
            recommendations.add(createRecommendation(
                "Excellent Match",
                "You have strong skill alignment. Focus on the few remaining skills to become a perfect fit.",
                "high",
                1
            ));
        } else if (coverageScore >= 60) {
            recommendations.add(createRecommendation(
                "Good Foundation",
                "You have a solid base. Prioritize learning the missing core skills.",
                "medium",
                2
            ));
        } else if (coverageScore >= 40) {
            recommendations.add(createRecommendation(
                "Skill Development Needed",
                "Consider taking online courses for the missing skills before applying.",
                "medium",
                3
            ));
        } else {
            recommendations.add(createRecommendation(
                "Significant Gap",
                "This role may require substantial upskilling. Consider similar roles with better match.",
                "low",
                4
            ));
        }
        
        // Category-specific recommendations
        if (categorizedGaps.containsKey("Cloud") && !categorizedGaps.get("Cloud").isEmpty()) {
            recommendations.add(createRecommendation(
                "Cloud Skills Priority",
                "Cloud computing is crucial for modern development. Start with AWS or Azure fundamentals.",
                "high",
                2
            ));
        }
        
        if (categorizedGaps.containsKey("DevOps") && categorizedGaps.get("DevOps").size() >= 3) {
            recommendations.add(createRecommendation(
                "DevOps Learning Path",
                "DevOps skills are highly valued. Learn CI/CD pipelines and containerization.",
                "medium",
                3
            ));
        }
        
        return recommendations;
    }

    private Map<String, Object> createRecommendation(String title, String description, 
                                                      String priority, int order) {
        Map<String, Object> rec = new HashMap<>();
        rec.put("title", title);
        rec.put("description", description);
        rec.put("priority", priority);
        rec.put("order", order);
        return rec;
    }

    /**
     * Generate learning resources for missing skills
     */
    private List<Map<String, String>> generateLearningResources(List<String> missingSkills) {
        List<Map<String, String>> resources = new ArrayList<>();
        
        for (String skill : missingSkills.stream().limit(5).collect(Collectors.toList())) {
            Map<String, String> resource = new HashMap<>();
            resource.put("skill", skill);
            resource.put("platform", getPlatformForSkill(skill));
            resource.put("duration", getEstimatedDuration(skill));
            resource.put("difficulty", getDifficultyLevel(skill));
            resources.add(resource);
        }
        
        return resources;
    }

    private String getPlatformForSkill(String skill) {
        String skillLower = skill.toLowerCase();
        if (skillLower.contains("aws") || skillLower.contains("cloud")) {
            return "AWS Training, Cloud Academy";
        } else if (skillLower.contains("java") || skillLower.contains("spring")) {
            return "Udemy, Coursera, Baeldung";
        } else if (skillLower.contains("react") || skillLower.contains("javascript")) {
            return "FreeCodeCamp, Udemy, Frontend Masters";
        } else if (skillLower.contains("docker") || skillLower.contains("kubernetes")) {
            return "Docker Docs, KodeKloud, A Cloud Guru";
        }
        return "Udemy, Coursera, Pluralsight";
    }

    private String getEstimatedDuration(String skill) {
        return "2-4 weeks";
    }

    private String getDifficultyLevel(String skill) {
        String skillLower = skill.toLowerCase();
        if (skillLower.contains("kubernetes") || skillLower.contains("microservices")) {
            return "Advanced";
        } else if (skillLower.contains("docker") || skillLower.contains("react")) {
            return "Intermediate";
        }
        return "Beginner to Intermediate";
    }

    /**
     * Analyze importance of missing skills
     */
    private Map<String, String> analyzeSkillImportance(List<String> missingSkills) {
        Map<String, String> importance = new HashMap<>();
        
        for (String skill : missingSkills) {
            String skillLower = skill.toLowerCase();
            if (skillLower.contains("aws") || skillLower.contains("docker") || 
                skillLower.contains("kubernetes")) {
                importance.put(skill, "Critical - High demand in industry");
            } else if (skillLower.contains("react") || skillLower.contains("spring boot") ||
                       skillLower.contains("python")) {
                importance.put(skill, "Important - Core development skill");
            } else if (skillLower.contains("testing") || skillLower.contains("git")) {
                importance.put(skill, "Essential - Required for modern development");
            } else {
                importance.put(skill, "Beneficial - Enhances profile");
            }
        }
        
        return importance;
    }

    private String getReadinessLevel(double coverageScore) {
        if (coverageScore >= 90) return "Highly Ready";
        if (coverageScore >= 75) return "Ready";
        if (coverageScore >= 60) return "Somewhat Ready";
        if (coverageScore >= 40) return "Needs Preparation";
        return "Not Ready Yet";
    }

    private String estimateLearningTime(int missingCount) {
        if (missingCount <= 2) return "1-2 months";
        if (missingCount <= 5) return "2-4 months";
        if (missingCount <= 10) return "4-6 months";
        return "6-12 months";
    }
    
    /**
     * Find which category a skill belongs to
     */
    private String findSkillCategory(String skill) {
        for (Map.Entry<String, List<String>> entry : SKILL_CATEGORIES.entrySet()) {
            if (entry.getValue().stream().anyMatch(s -> s.equalsIgnoreCase(skill))) {
                return entry.getKey();
            }
        }
        return "Other";
    }
    
    /**
     * Build learning resources for a skill
     */
    private Map<String, String> buildLearningResources(String skill) {
        Map<String, String> resources = new HashMap<>();
        resources.put("platform", "Udemy, Coursera, Pluralsight");
        resources.put("documentation", "Official " + skill + " documentation");
        resources.put("duration", getEstimatedDuration(skill));
        resources.put("difficulty", getDifficultyLevel(skill));
        return resources;
    }
}
