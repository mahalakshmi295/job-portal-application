package com.jobportal.backend.service;

import com.jobportal.backend.model.*;
import com.jobportal.backend.repository.ResumeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class ResumeService {
    
    @Autowired
    private ResumeRepository resumeRepository;

    // List of common technical skills for extraction
    private static final List<String> COMMON_SKILLS = Arrays.asList(
        "Java", "Python", "JavaScript", "TypeScript", "React", "Angular", "Vue", "Node.js",
        "Spring Boot", "Django", "Flask", "Express", "MongoDB", "PostgreSQL", "MySQL",
        "Docker", "Kubernetes", "AWS", "Azure", "GCP", "Git", "Jenkins", "CI/CD",
        "HTML", "CSS", "REST API", "GraphQL", "Microservices", "Agile", "Scrum",
        "Machine Learning", "Deep Learning", "TensorFlow", "PyTorch", "NLP",
        "C++", "C#", ".NET", "Ruby", "Go", "Rust", "Scala", "Kotlin", "Swift",
        "Redux", "Webpack", "Babel", "Jest", "JUnit", "Selenium", "TestNG"
    );

    // Industry keywords for ATS scoring
    private static final List<String> INDUSTRY_KEYWORDS = Arrays.asList(
        "developed", "implemented", "designed", "created", "built", "managed", "led",
        "architected", "optimized", "improved", "reduced", "increased", "achieved",
        "collaborated", "coordinated", "delivered", "launched", "executed", "analyzed",
        "troubleshooted", "debugged", "tested", "deployed", "maintained", "documented"
    );

    /**
     * Analyze resume and calculate comprehensive scores
     */
    @Transactional
    public Map<String, Object> analyzeResume(String userId, ResumeData resumeData, String rawText) {
        Resume resume = new Resume(userId);
        resume.setResumeData(resumeData);
        resume.setRawText(rawText);
        
        // Perform analysis
        ResumeAnalysisResult analysisResult = performAnalysis(resumeData, rawText);
        resume.setAnalysisResult(analysisResult);
        resume.setUpdatedAt(LocalDateTime.now());
        
        // Save to database
        resume = resumeRepository.save(resume);
        
        // Build response
        Map<String, Object> response = new HashMap<>();
        response.put("resumeId", resume.getId());
        response.put("atsScore", analysisResult.getAtsScore());
        response.put("keywordMatchScore", analysisResult.getKeywordMatchScore());
        response.put("formatScore", analysisResult.getFormatScore());
        response.put("overallScore", calculateOverallScore(analysisResult));
        response.put("extractedSkills", analysisResult.getExtractedSkills());
        response.put("missingSkills", analysisResult.getMissingSkills());
        response.put("weakSections", analysisResult.getWeakSections());
        response.put("suggestions", analysisResult.getSuggestions());
        response.put("sectionCompleteness", analysisResult.getSectionCompleteness());
        
        return response;
    }

    /**
     * Perform comprehensive resume analysis
     */
    private ResumeAnalysisResult performAnalysis(ResumeData resumeData, String rawText) {
        ResumeAnalysisResult result = new ResumeAnalysisResult();
        
        // Extract skills from resume
        List<String> extractedSkills = extractSkills(resumeData, rawText);
        result.setExtractedSkills(extractedSkills);
        
        // Calculate ATS score (based on keyword density and formatting)
        double atsScore = calculateATSScore(rawText, extractedSkills);
        result.setAtsScore(atsScore);
        
        // Calculate keyword match score
        double keywordScore = calculateKeywordMatchScore(rawText);
        result.setKeywordMatchScore(keywordScore);
        
        // Calculate format score
        double formatScore = calculateFormatScore(resumeData);
        result.setFormatScore(formatScore);
        
        // Check section completeness
        Map<String, Boolean> sectionCompleteness = checkSectionCompleteness(resumeData);
        result.setSectionCompleteness(sectionCompleteness);
        
        // Identify weak sections
        List<String> weakSections = identifyWeakSections(resumeData, sectionCompleteness);
        result.setWeakSections(weakSections);
        
        // Identify missing skills
        List<String> missingSkills = identifyMissingSkills(extractedSkills);
        result.setMissingSkills(missingSkills);
        
        // Generate suggestions
        List<String> suggestions = generateSuggestions(result, resumeData);
        result.setSuggestions(suggestions);
        
        return result;
    }

    /**
     * Extract skills from resume data and raw text
     */
    private List<String> extractSkills(ResumeData resumeData, String rawText) {
        Set<String> skills = new HashSet<>();
        
        // Add skills from structured data
        if (resumeData.getSkills() != null) {
            skills.addAll(resumeData.getSkills());
        }
        
        // Extract skills from raw text using pattern matching
        String textUpper = rawText.toUpperCase();
        for (String skill : COMMON_SKILLS) {
            if (textUpper.contains(skill.toUpperCase())) {
                skills.add(skill);
            }
        }
        
        // Extract skills from project technologies
        if (resumeData.getProjects() != null) {
            for (Project project : resumeData.getProjects()) {
                if (project.getTechnologies() != null) {
                    skills.addAll(project.getTechnologies());
                }
            }
        }
        
        return new ArrayList<>(skills);
    }

    /**
     * Calculate ATS (Applicant Tracking System) score
     */
    private double calculateATSScore(String rawText, List<String> skills) {
        double score = 0.0;
        int wordCount = rawText.split("\\s+").length;
        
        // Skill density (40% weight)
        double skillDensity = (skills.size() * 100.0) / Math.max(wordCount, 1);
        score += Math.min(skillDensity * 4, 40.0);
        
        // Keyword presence (30% weight)
        int keywordCount = 0;
        String textLower = rawText.toLowerCase();
        for (String keyword : INDUSTRY_KEYWORDS) {
            if (textLower.contains(keyword.toLowerCase())) {
                keywordCount++;
            }
        }
        double keywordRatio = (keywordCount * 100.0) / INDUSTRY_KEYWORDS.size();
        score += (keywordRatio * 0.3);
        
        // Length appropriateness (15% weight) - ideal 400-800 words
        double lengthScore = 0;
        if (wordCount >= 400 && wordCount <= 800) {
            lengthScore = 15.0;
        } else if (wordCount >= 300 && wordCount <= 1000) {
            lengthScore = 10.0;
        } else if (wordCount >= 200) {
            lengthScore = 5.0;
        }
        score += lengthScore;
        
        // Formatting indicators (15% weight)
        double formatScore = 0;
        if (rawText.contains("@")) formatScore += 3; // Email present
        if (rawText.matches(".*\\d{3}[-.]?\\d{3}[-.]?\\d{4}.*")) formatScore += 3; // Phone present
        if (rawText.toLowerCase().contains("education")) formatScore += 3; // Section headers
        if (rawText.toLowerCase().contains("experience")) formatScore += 3;
        if (rawText.toLowerCase().contains("skills")) formatScore += 3;
        score += formatScore;
        
        return Math.min(score, 100.0);
    }

    /**
     * Calculate keyword match score
     */
    private double calculateKeywordMatchScore(String rawText) {
        int matchCount = 0;
        String textLower = rawText.toLowerCase();
        
        for (String keyword : INDUSTRY_KEYWORDS) {
            if (textLower.contains(keyword.toLowerCase())) {
                matchCount++;
            }
        }
        
        return (matchCount * 100.0) / INDUSTRY_KEYWORDS.size();
    }

    /**
     * Calculate format score based on resume structure
     */
    private double calculateFormatScore(ResumeData resumeData) {
        double score = 0.0;
        
        // Personal info completeness (20 points)
        if (resumeData.getPersonalInfo() != null && !resumeData.getPersonalInfo().isEmpty()) {
            score += 20.0;
        }
        
        // Professional summary (20 points)
        if (resumeData.getProfessionalSummary() != null && 
            resumeData.getProfessionalSummary().length() > 50) {
            score += 20.0;
        }
        
        // Education section (20 points)
        if (resumeData.getEducation() != null && !resumeData.getEducation().isEmpty()) {
            score += 20.0;
        }
        
        // Experience section (20 points)
        if (resumeData.getExperience() != null && !resumeData.getExperience().isEmpty()) {
            score += 20.0;
        }
        
        // Skills section (20 points)
        if (resumeData.getSkills() != null && resumeData.getSkills().size() >= 5) {
            score += 20.0;
        }
        
        return score;
    }

    /**
     * Check completeness of each section
     */
    private Map<String, Boolean> checkSectionCompleteness(ResumeData resumeData) {
        Map<String, Boolean> completeness = new HashMap<>();
        
        completeness.put("personalInfo", 
            resumeData.getPersonalInfo() != null && !resumeData.getPersonalInfo().isEmpty());
        completeness.put("professionalSummary", 
            resumeData.getProfessionalSummary() != null && resumeData.getProfessionalSummary().length() > 50);
        completeness.put("education", 
            resumeData.getEducation() != null && !resumeData.getEducation().isEmpty());
        completeness.put("experience", 
            resumeData.getExperience() != null && !resumeData.getExperience().isEmpty());
        completeness.put("skills", 
            resumeData.getSkills() != null && resumeData.getSkills().size() >= 5);
        completeness.put("projects", 
            resumeData.getProjects() != null && !resumeData.getProjects().isEmpty());
        completeness.put("certifications", 
            resumeData.getCertifications() != null && !resumeData.getCertifications().isEmpty());
        
        return completeness;
    }

    /**
     * Identify weak sections in the resume
     */
    private List<String> identifyWeakSections(ResumeData resumeData, Map<String, Boolean> completeness) {
        List<String> weakSections = new ArrayList<>();
        
        for (Map.Entry<String, Boolean> entry : completeness.entrySet()) {
            if (!entry.getValue()) {
                weakSections.add(entry.getKey());
            }
        }
        
        // Check for weak experience descriptions
        if (resumeData.getExperience() != null) {
            for (Experience exp : resumeData.getExperience()) {
                if (exp.getDescription() == null || exp.getDescription().isEmpty() || 
                    exp.getDescription().size() < 2) {
                    if (!weakSections.contains("experience")) {
                        weakSections.add("experienceDescriptions");
                    }
                    break;
                }
            }
        }
        
        return weakSections;
    }

    /**
     * Identify missing important skills
     */
    private List<String> identifyMissingSkills(List<String> extractedSkills) {
        List<String> missing = new ArrayList<>();
        
        // Define critical skill categories
        List<String> criticalSkills = Arrays.asList(
            "Git", "Docker", "CI/CD", "REST API", "Agile"
        );
        
        for (String skill : criticalSkills) {
            boolean found = false;
            for (String extracted : extractedSkills) {
                if (extracted.equalsIgnoreCase(skill)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                missing.add(skill);
            }
        }
        
        return missing;
    }

    /**
     * Generate improvement suggestions
     */
    private List<String> generateSuggestions(ResumeAnalysisResult result, ResumeData resumeData) {
        List<String> suggestions = new ArrayList<>();
        
        // ATS Score suggestions
        if (result.getAtsScore() < 70) {
            suggestions.add("Increase keyword density by incorporating more industry-relevant terms");
            suggestions.add("Add action verbs like 'developed', 'implemented', 'led' to describe achievements");
        }
        
        // Section completeness suggestions
        if (!result.getSectionCompleteness().get("professionalSummary")) {
            suggestions.add("Add a professional summary highlighting your key strengths and career objectives");
        }
        
        if (!result.getSectionCompleteness().get("projects")) {
            suggestions.add("Include personal or professional projects to showcase practical experience");
        }
        
        if (!result.getSectionCompleteness().get("certifications")) {
            suggestions.add("List relevant certifications to strengthen your technical credibility");
        }
        
        // Skills suggestions
        if (result.getExtractedSkills().size() < 10) {
            suggestions.add("Expand your skills section to include at least 10-15 relevant technical skills");
        }
        
        // Experience suggestions
        if (resumeData.getExperience() != null && !resumeData.getExperience().isEmpty()) {
            for (Experience exp : resumeData.getExperience()) {
                if (exp.getDescription() == null || exp.getDescription().size() < 3) {
                    suggestions.add("Add more detailed bullet points (3-5) for each work experience");
                    break;
                }
            }
        }
        
        // Missing skills suggestions
        if (!result.getMissingSkills().isEmpty()) {
            suggestions.add("Consider adding these in-demand skills: " + 
                String.join(", ", result.getMissingSkills().subList(0, 
                Math.min(3, result.getMissingSkills().size()))));
        }
        
        // Format suggestions
        if (result.getFormatScore() < 80) {
            suggestions.add("Ensure all contact information (email, phone, LinkedIn) is clearly visible");
            suggestions.add("Use consistent formatting with clear section headers");
        }
        
        return suggestions;
    }

    /**
     * Calculate overall score (weighted average)
     */
    private double calculateOverallScore(ResumeAnalysisResult result) {
        double atsWeight = 0.40;
        double keywordWeight = 0.30;
        double formatWeight = 0.30;
        
        return (result.getAtsScore() * atsWeight) + 
               (result.getKeywordMatchScore() * keywordWeight) + 
               (result.getFormatScore() * formatWeight);
    }

    /**
     * Get resume by user ID
     */
    public List<Resume> getResumesByUserId(String userId) {
        return resumeRepository.findByUserId(userId);
    }

    /**
     * Get specific resume
     */
    public Optional<Resume> getResume(Long id) {
        return resumeRepository.findById(id);
    }

    /**
     * Delete resume
     */
    @Transactional
    public boolean deleteResume(Long id, String userId) {
        Optional<Resume> resume = resumeRepository.findByUserIdAndId(userId, id);
        if (resume.isPresent()) {
            resumeRepository.delete(resume.get());
            return true;
        }
        return false;
    }
}
