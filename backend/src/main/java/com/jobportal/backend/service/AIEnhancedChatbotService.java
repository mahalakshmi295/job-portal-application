package com.jobportal.backend.service;

import com.jobportal.backend.config.ApiConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;

/**
 * Enhanced chatbot service with OpenAI GPT integration
 * Provides more intelligent and context-aware responses
 */
@Service
public class AIEnhancedChatbotService {
    
    @Autowired
    private ApiConfig apiConfig;
    
    @Autowired
    private ChatbotService fallbackChatbotService;
    
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    private static final String SYSTEM_PROMPT = 
        "You are a helpful AI assistant for a job portal application. " +
        "Help users with job search, resume improvement, interview preparation, " +
        "skill development, and career advice. Be professional, concise, and actionable. " +
        "Keep responses under 150 words.";
    
    /**
     * Process message with AI if configured, otherwise use fallback
     */
    public Map<String, Object> processMessage(String userMessage) {
        // Try AI-enhanced response if configured
        if (apiConfig.isOpenAiConfigured()) {
            try {
                return getAIResponse(userMessage);
            } catch (Exception e) {
                System.err.println("OpenAI API failed: " + e.getMessage());
                // Fallback to rule-based chatbot
            }
        }
        
        // Use fallback chatbot service
        Map<String, Object> fallbackResponse = fallbackChatbotService.processMessage(userMessage);
        fallbackResponse.put("usedAI", false);
        fallbackResponse.put("message", "Using rule-based responses. Configure OpenAI API for enhanced AI chatbot.");
        return fallbackResponse;
    }
    
    /**
     * Get response from OpenAI GPT
     */
    private Map<String, Object> getAIResponse(String userMessage) throws Exception {
        Map<String, Object> result = new HashMap<>();
        
        // Build request payload
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", apiConfig.getOpenAiModel());
        requestBody.put("messages", Arrays.asList(
            Map.of("role", "system", "content", SYSTEM_PROMPT),
            Map.of("role", "user", "content", userMessage)
        ));
        requestBody.put("max_tokens", 200);
        requestBody.put("temperature", 0.7);
        
        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiConfig.getOpenAiApiKey());
        
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
        
        // Make API call
        String url = ApiConfig.OPENAI_API_BASE_URL + "/chat/completions";
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
        
        // Parse response
        if (response.getStatusCode() == HttpStatus.OK) {
            JsonNode jsonResponse = objectMapper.readTree(response.getBody());
            String aiResponse = jsonResponse
                .get("choices")
                .get(0)
                .get("message")
                .get("content")
                .asText();
            
            result.put("response", aiResponse);
            result.put("usedAI", true);
            result.put("model", apiConfig.getOpenAiModel());
            result.put("intent", "ai_generated");
            result.put("confidence", 1.0);
            result.put("suggestions", getContextualSuggestions(aiResponse));
            result.put("timestamp", System.currentTimeMillis());
        }
        
        return result;
    }
    
    /**
     * Get contextual follow-up suggestions
     */
    private List<Map<String, String>> getContextualSuggestions(String response) {
        List<Map<String, String>> suggestions = new ArrayList<>();
        
        String lowerResponse = response.toLowerCase();
        
        if (lowerResponse.contains("resume")) {
            suggestions.add(createSuggestion("Score my resume", "/resume-score"));
        }
        if (lowerResponse.contains("skill")) {
            suggestions.add(createSuggestion("Analyze skill gaps", "/skill-gap"));
        }
        if (lowerResponse.contains("job") || lowerResponse.contains("position")) {
            suggestions.add(createSuggestion("Browse jobs", "/jobs"));
            suggestions.add(createSuggestion("Check job matches", "/job-match"));
        }
        if (lowerResponse.contains("interview")) {
            suggestions.add(createSuggestion("Interview preparation tips", null));
        }
        
        // Always add a general follow-up
        if (suggestions.isEmpty()) {
            suggestions.add(createSuggestion("Tell me more", null));
            suggestions.add(createSuggestion("What else can you help with?", null));
        }
        
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
