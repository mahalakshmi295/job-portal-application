package com.jobportal.backend.service;

import org.springframework.stereotype.Service;
import java.util.*;
import java.util.regex.Pattern;

@Service
public class ChatbotService {

    private static final Map<String, List<String>> INTENT_PATTERNS = new HashMap<>();
    private static final Map<String, List<String>> RESPONSES = new HashMap<>();
    
    static {
        // Job Search Intent
        INTENT_PATTERNS.put("job_search", Arrays.asList(
            ".*\\bjob\\b.*", ".*\\bfind.*work\\b.*", ".*\\bposition\\b.*", 
            ".*\\bopening\\b.*", ".*\\bvacancy\\b.*", ".*\\bcareer\\b.*"
        ));
        RESPONSES.put("job_search", Arrays.asList(
            "I can help you find jobs! You can browse available positions in our job listings. What type of role are you interested in?",
            "Looking for a job? Great! I recommend checking our job board. Would you like to filter by location, skills, or experience level?",
            "Let's find you the perfect opportunity! Use our job search feature to discover roles that match your skills."
        ));
        
        // Interview Intent
        INTENT_PATTERNS.put("interview", Arrays.asList(
            ".*\\binterview\\b.*", ".*\\bprepare.*interview\\b.*", ".*\\btips\\b.*"
        ));
        RESPONSES.put("interview", Arrays.asList(
            "Interview preparation tips: 1) Research the company thoroughly 2) Practice common technical questions 3) Prepare STAR method examples 4) Dress professionally 5) Arrive 10 minutes early",
            "For interview success: Review the job description, prepare relevant examples from your experience, and practice coding problems if it's a technical role. Good luck!",
            "Interview coming up? Make sure to: research the company, review your resume, prepare questions for them, and get a good night's sleep. You've got this!"
        ));
        
        // Resume Intent
        INTENT_PATTERNS.put("resume", Arrays.asList(
            ".*\\bresume\\b.*", ".*\\bcv\\b.*", ".*\\bresume.*score\\b.*", 
            ".*\\bresume.*tips\\b.*", ".*\\bimprove.*resume\\b.*"
        ));
        RESPONSES.put("resume", Arrays.asList(
            "Want to improve your resume? Use our Resume Score feature to get AI-powered feedback and recommendations!",
            "Great resume tips: 1) Use action verbs 2) Quantify achievements 3) Keep it to 1-2 pages 4) Tailor it for each job 5) Include relevant keywords",
            "Try our Resume Scoring feature! It analyzes your resume for ATS compatibility, keyword optimization, and format quality."
        ));
        
        // Skills Intent
        INTENT_PATTERNS.put("skills", Arrays.asList(
            ".*\\bskill\\b.*", ".*\\blearn\\b.*", ".*\\bskill.*gap\\b.*", 
            ".*\\bimprove\\b.*", ".*\\btraining\\b.*"
        ));
        RESPONSES.put("skills", Arrays.asList(
            "Use our Skill Gap Analysis to identify which skills you need for your target role! It provides personalized learning recommendations.",
            "Learning new skills? Popular ones in demand: Cloud Computing (AWS/Azure), Docker, Kubernetes, React, Python, and Machine Learning.",
            "Check out our Skill Gap feature - it compares your skills with job requirements and suggests learning paths!"
        ));
        
        // Salary Intent
        INTENT_PATTERNS.put("salary", Arrays.asList(
            ".*\\bsalary\\b.*", ".*\\bpay\\b.*", ".*\\bcompensation\\b.*", 
            ".*\\bwage\\b.*", ".*\\bhow much\\b.*"
        ));
        RESPONSES.put("salary", Arrays.asList(
            "Salary ranges vary by location, experience, and skills. Research industry standards for your role and location. Sites like Glassdoor and PayScale can help!",
            "When negotiating salary, remember: 1) Know your market value 2) Consider total compensation 3) Be prepared to walk away 4) Timing matters",
            "Salary expectations depend on many factors. Research similar roles in your area and consider your experience level and skill set."
        ));
        
        // Application Intent
        INTENT_PATTERNS.put("application", Arrays.asList(
            ".*\\bapply\\b.*", ".*\\bapplication\\b.*", ".*\\bsubmit\\b.*", 
            ".*\\bhow to apply\\b.*"
        ));
        RESPONSES.put("application", Arrays.asList(
            "To apply: 1) Find a job that matches your skills 2) Tailor your resume 3) Write a compelling cover letter 4) Submit through our platform 5) Follow up after a week",
            "Application tips: Always customize your resume and cover letter for each position. Highlight relevant experience and use keywords from the job description!",
            "Ready to apply? Make sure your resume is updated, write a targeted cover letter, and double-check for typos before submitting!"
        ));
        
        // Career Advice Intent
        INTENT_PATTERNS.put("career_advice", Arrays.asList(
            ".*\\bcareer.*advice\\b.*", ".*\\bcareer.*path\\b.*", ".*\\bguidance\\b.*",
            ".*\\bsuggestion\\b.*", ".*\\bhelp.*career\\b.*"
        ));
        RESPONSES.put("career_advice", Arrays.asList(
            "Career advice: 1) Continuously learn and upskill 2) Network actively 3) Build a strong online presence 4) Set clear goals 5) Seek mentorship",
            "For career growth: Take on challenging projects, build your network, stay updated with industry trends, and don't be afraid to step out of your comfort zone!",
            "Great career moves: Learn in-demand skills, contribute to open-source, attend industry events, create a portfolio, and maintain a strong LinkedIn profile."
        ));
        
        // Greeting Intent
        INTENT_PATTERNS.put("greeting", Arrays.asList(
            "^(hi|hello|hey|greetings).*", ".*\\bhello\\b.*", ".*\\bhi\\b.*"
        ));
        RESPONSES.put("greeting", Arrays.asList(
            "Hello! I'm your Job Portal assistant. I can help you with job search, resume tips, interview preparation, and career advice. What would you like to know?",
            "Hi there! Welcome to the Job Portal. How can I assist you today? I'm here to help with jobs, resumes, interviews, and more!",
            "Hey! Great to see you. I'm here to help with your job search journey. What can I do for you today?"
        ));
        
        // Thanks Intent
        INTENT_PATTERNS.put("thanks", Arrays.asList(
            "^(thanks|thank you|thx).*", ".*\\bthank.*\\b.*", ".*\\bthanks\\b.*"
        ));
        RESPONSES.put("thanks", Arrays.asList(
            "You're welcome! Feel free to ask if you need anything else. Good luck with your job search!",
            "Happy to help! Don't hesitate to reach out if you have more questions.",
            "Glad I could assist! Wishing you success in your career journey!"
        ));
    }

    /**
     * Process user message and generate response
     */
    public Map<String, Object> processMessage(String userMessage) {
        Map<String, Object> result = new HashMap<>();
        
        if (userMessage == null || userMessage.trim().isEmpty()) {
            result.put("response", "I didn't catch that. Could you please ask your question again?");
            result.put("intent", "unknown");
            result.put("confidence", 0.0);
            return result;
        }
        
        String messageLower = userMessage.toLowerCase().trim();
        
        // Detect intent
        Map<String, Double> intentScores = detectIntent(messageLower);
        
        // Get best matching intent
        String bestIntent = "default";
        double bestScore = 0.0;
        
        for (Map.Entry<String, Double> entry : intentScores.entrySet()) {
            if (entry.getValue() > bestScore) {
                bestScore = entry.getValue();
                bestIntent = entry.getKey();
            }
        }
        
        // Generate response
        String response;
        List<Map<String, String>> suggestions = new ArrayList<>();
        
        if (bestScore > 0.3) {
            response = getRandomResponse(bestIntent);
            suggestions = getSuggestions(bestIntent);
        } else {
            response = getDefaultResponse(messageLower);
            suggestions = getDefaultSuggestions();
        }
        
        result.put("response", response);
        result.put("intent", bestIntent);
        result.put("confidence", Math.round(bestScore * 100) / 100.0);
        result.put("suggestions", suggestions);
        result.put("timestamp", System.currentTimeMillis());
        
        return result;
    }

    /**
     * Detect intent from user message
     */
    private Map<String, Double> detectIntent(String message) {
        Map<String, Double> scores = new HashMap<>();
        
        for (Map.Entry<String, List<String>> entry : INTENT_PATTERNS.entrySet()) {
            String intent = entry.getKey();
            List<String> patterns = entry.getValue();
            
            double maxScore = 0.0;
            for (String pattern : patterns) {
                if (Pattern.matches(pattern, message)) {
                    maxScore = 1.0;
                    break;
                } else if (message.contains(pattern.replaceAll("[.*\\\\b]", ""))) {
                    maxScore = Math.max(maxScore, 0.7);
                }
            }
            
            if (maxScore > 0) {
                scores.put(intent, maxScore);
            }
        }
        
        return scores;
    }

    /**
     * Get random response for intent
     */
    private String getRandomResponse(String intent) {
        List<String> responses = RESPONSES.get(intent);
        if (responses == null || responses.isEmpty()) {
            return getDefaultResponse("");
        }
        return responses.get(new Random().nextInt(responses.size()));
    }

    /**
     * Get default response when no intent matches
     */
    private String getDefaultResponse(String message) {
        if (message.contains("?")) {
            return "That's a great question! I can help you with job search, resume improvement, interview preparation, skill development, and career advice. What specific area would you like to explore?";
        }
        return "I'm here to assist with your job search! I can help with:\n" +
               "• Finding the right job opportunities\n" +
               "• Improving your resume\n" +
               "• Preparing for interviews\n" +
               "• Identifying skill gaps\n" +
               "• Career guidance\n\n" +
               "What would you like to know more about?";
    }

    /**
     * Get follow-up suggestions based on intent
     */
    private List<Map<String, String>> getSuggestions(String intent) {
        List<Map<String, String>> suggestions = new ArrayList<>();
        
        switch (intent) {
            case "job_search":
                suggestions.add(createSuggestion("Browse all jobs", "/jobs"));
                suggestions.add(createSuggestion("Match me with jobs", "/job-match"));
                suggestions.add(createSuggestion("Check my skills", "/skill-gap"));
                break;
            case "resume":
                suggestions.add(createSuggestion("Score my resume", "/resume-score"));
                suggestions.add(createSuggestion("Resume writing tips", null));
                suggestions.add(createSuggestion("ATS optimization", null));
                break;
            case "interview":
                suggestions.add(createSuggestion("Common interview questions", null));
                suggestions.add(createSuggestion("Technical interview prep", null));
                suggestions.add(createSuggestion("Behavioral questions", null));
                break;
            case "skills":
                suggestions.add(createSuggestion("Analyze skill gap", "/skill-gap"));
                suggestions.add(createSuggestion("Top skills to learn", null));
                suggestions.add(createSuggestion("Free learning resources", null));
                break;
            default:
                return getDefaultSuggestions();
        }
        
        return suggestions;
    }

    /**
     * Get default suggestions
     */
    private List<Map<String, String>> getDefaultSuggestions() {
        List<Map<String, String>> suggestions = new ArrayList<>();
        suggestions.add(createSuggestion("Find jobs", null));
        suggestions.add(createSuggestion("Improve my resume", null));
        suggestions.add(createSuggestion("Interview tips", null));
        suggestions.add(createSuggestion("Career advice", null));
        return suggestions;
    }

    private Map<String, String> createSuggestion(String text, String action) {
        Map<String, String> suggestion = new HashMap<>();
        suggestion.put("text", text);
        if (action != null) {
            suggestion.put("action", action);
        }
        return suggestion;
    }
}
