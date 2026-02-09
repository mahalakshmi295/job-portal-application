package com.jobportal.backend.controller;

import com.jobportal.backend.service.ChatbotService;
import com.jobportal.backend.service.AIEnhancedChatbotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.*;

@RestController
@RequestMapping("/api/chatbot")
@CrossOrigin(origins = "http://localhost:3000")
public class ChatbotController {
    
    @Autowired
    private ChatbotService chatbotService;
    
    @Autowired
    private AIEnhancedChatbotService aiEnhancedChatbotService;
    
    /**
     * Process chat message and generate intelligent response
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> chat(@RequestBody Map<String, Object> payload) {
        try {
            // Extract message from payload (handle multiple key names)
            String message = (String) payload.getOrDefault("message", 
                             payload.getOrDefault("query", 
                             payload.getOrDefault("text", "")));
            
            if (message == null || message.trim().isEmpty()) {
                Map<String, Object> error = new HashMap<>();
                error.put("error", "Message cannot be empty");
                error.put("response", "Please provide a message to chat!");
                return ResponseEntity.badRequest().body(error);
            }
            
            // Process message using ChatbotService
            Map<String, Object> result = chatbotService.processMessage(message);
            
            // Add conversation ID for tracking
            result.put("conversationId", UUID.randomUUID().toString());
            result.put("userMessage", message);
            
            return ResponseEntity.ok(result);
            
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Failed to process message: " + e.getMessage());
            error.put("response", "Sorry, I encountered an error. Please try again.");
            return ResponseEntity.internalServerError().body(error);
        }
    }
    
    /**
     * Get suggested questions
     */
    @GetMapping("/suggestions")
    public ResponseEntity<Map<String, Object>> getSuggestions() {
        Map<String, Object> result = new HashMap<>();
        
        List<String> suggestions = Arrays.asList(
            "How do I find jobs?",
            "Can you help me improve my resume?",
            "What are common interview questions?",
            "How can I identify my skill gaps?",
            "What skills are in demand?",
            "How do I prepare for a technical interview?",
            "Where can I learn new skills?",
            "How do I negotiate salary?"
        );
        
        result.put("suggestions", suggestions);
        result.put("categories", Arrays.asList(
            "Job Search", "Resume Help", "Interview Prep", "Skill Development", "Career Advice"
        ));
        
        return ResponseEntity.ok(result);
    }
    
    /**
     * Get chatbot capabilities
     */
    @GetMapping("/capabilities")
    public ResponseEntity<Map<String, Object>> getCapabilities() {
        Map<String, Object> result = new HashMap<>();
        
        result.put("name", "Job Portal Assistant");
        result.put("version", "1.0");
        result.put("capabilities", Arrays.asList(
            "Job Search Assistance",
            "Resume Analysis & Tips",
            "Interview Preparation",
            "Skill Gap Analysis",
            "Career Guidance",
            "Application Tips",
            "Salary Information"
        ));
        result.put("supportedLanguages", Arrays.asList("English"));
        result.put("status", "active");
        
        return ResponseEntity.ok(result);
    }
    
    /**
     * AI-enhanced chat endpoint (uses OpenAI if configured)
     */
    @PostMapping("/ai")
    public ResponseEntity<Map<String, Object>> aiChat(@RequestBody Map<String, Object> payload) {
        try {
            String message = (String) payload.getOrDefault("message", 
                             payload.getOrDefault("query", 
                             payload.getOrDefault("text", "")));
            
            if (message == null || message.trim().isEmpty()) {
                Map<String, Object> error = new HashMap<>();
                error.put("error", "Message cannot be empty");
                return ResponseEntity.badRequest().body(error);
            }
            
            // Use AI-enhanced service
            Map<String, Object> result = aiEnhancedChatbotService.processMessage(message);
            result.put("conversationId", UUID.randomUUID().toString());
            result.put("userMessage", message);
            
            return ResponseEntity.ok(result);
            
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Failed to process AI message: " + e.getMessage());
            error.put("response", "Sorry, I encountered an error. Please try again.");
            return ResponseEntity.internalServerError().body(error);
        }
    }
}
