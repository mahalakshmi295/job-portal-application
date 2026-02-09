package com.jobportal.backend.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.*;

@RestController
@RequestMapping("/api/ranking")
public class RankingController {
    @PostMapping
    public ResponseEntity<Map<String, Object>> rankCandidates(@RequestBody Map<String, Object> payload) {
        // Placeholder: bias-free ranking by randomizing order
        List<Map<String, Object>> candidates = (List<Map<String, Object>>) payload.getOrDefault("candidates", new ArrayList<>());
        Collections.shuffle(candidates); // Simulate bias-free
        Map<String, Object> result = new HashMap<>();
        result.put("ranking", candidates);
        return ResponseEntity.ok(result);
    }
}
