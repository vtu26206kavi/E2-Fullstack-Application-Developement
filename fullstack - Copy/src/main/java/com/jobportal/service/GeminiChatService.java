package com.jobportal.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GeminiChatService {

    @Value("${gemini.api.key}")
    private String apiKey;

    private static final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=";

    public String generateResponse(String userMessage) {
        if (apiKey == null || apiKey.trim().isEmpty()) {
            return "Gemini API key is not configured. Please set gemini.api.key in application.properties.";
        }

        try {
            RestTemplate restTemplate = new RestTemplate();
            String cleanApiKey = apiKey.replace("\"", "").trim();
            String url = GEMINI_API_URL + cleanApiKey;

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> textContent = new HashMap<>();
            textContent.put("type", "text");
            textContent.put("text", "You are an AI assistant for a Job Portal application. Be helpful, concise, and professional. The user says: " + userMessage);

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("content", List.of(textContent));
            requestBody.put("temperature", 0.7);
            requestBody.put("candidateCount", 1);
            requestBody.put("maxOutputTokens", 512);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
            ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Map<String, Object> body = response.getBody();
                List<Map<String, Object>> candidates = (List<Map<String, Object>>) body.get("candidates");
                if (candidates != null && !candidates.isEmpty()) {
                    Object contentResponse = candidates.get(0).get("content");
                    if (contentResponse instanceof Map<?, ?>) {
                        Map<String, Object> candidateContent = (Map<String, Object>) contentResponse;
                        if (candidateContent.containsKey("text")) {
                            return (String) candidateContent.get("text");
                        }
                        List<Map<String, Object>> partsResponse = (List<Map<String, Object>>) candidateContent.get("parts");
                        if (partsResponse != null && !partsResponse.isEmpty()) {
                            return (String) partsResponse.get(0).get("text");
                        }
                    } else if (contentResponse instanceof List<?>) {
                        List<?> contentList = (List<?>) contentResponse;
                        for (Object item : contentList) {
                            if (item instanceof Map<?, ?>) {
                                Map<String, Object> contentMap = (Map<String, Object>) item;
                                if (contentMap.containsKey("text")) {
                                    return (String) contentMap.get("text");
                                }
                            }
                        }
                    }
                }
            }

            return "I'm sorry, I couldn't generate a response at this time.";
        } catch (Exception e) {
            e.printStackTrace();
            return "An error occurred while communicating with the AI. Please check server logs.";
        }
    }
}
