package com.example.praventa.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class OpenAIService {
    //private static final String API_KEY = System.getenv("GEMINI_API_KEY");
    private static final String ENDPOINT = "https://api.openai.com/v1/chat/completions";

    public static String getHealthRecommendation(String prompt) {
        try {
            String requestBody = """
                {
                  "model": "gpt-4",
                  "messages": [
                    {"role": "system", "content": "Kamu adalah asisten kesehatan yang memberikan rekomendasi berdasarkan data gaya hidup dan data pribadi."},
                    {"role": "user", "content": "%s"}
                  ]
                }
            """.formatted(prompt);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(ENDPOINT))
                    .header("Authorization", "Bearer " + API_KEY)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());

            return response.body();
        } catch (Exception e) {
            e.printStackTrace();
            return "Gagal menghubungi AI: " + e.getMessage();
        }
    }
}
