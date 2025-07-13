package com.example.praventa.service;

import com.google.gson.*;
import java.net.URI;
import java.net.http.*;
import java.nio.charset.StandardCharsets;

public class GeminiService {
    private static final String API_KEY = System.getenv("GEMINI_API_KEY");
    private static final String ENDPOINT = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key="+API_KEY;

    public static String generateRecommendation(String prompt) throws Exception {
        HttpClient client = HttpClient.newHttpClient();

        JsonObject message = new JsonObject();
        JsonArray contents = new JsonArray();
        JsonObject contentObj = new JsonObject();
        contentObj.addProperty("role", "user");

        JsonArray parts = new JsonArray();
        JsonObject part = new JsonObject();
        part.addProperty("text", prompt);
        parts.add(part);

        contentObj.add("parts", parts);
        contents.add(contentObj);
        message.add("contents", contents);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ENDPOINT))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(message.toString(), StandardCharsets.UTF_8))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Debug log
        System.out.println("[DEBUG] Gemini Response Body:\n" + response.body());

        JsonObject jsonResponse = JsonParser.parseString(response.body()).getAsJsonObject();

        if (!jsonResponse.has("candidates")) {
            throw new RuntimeException("Respons dari Gemini tidak memiliki kandidat jawaban.\n" + response.body());
        }

        return jsonResponse
                .getAsJsonArray("candidates")
                .get(0).getAsJsonObject()
                .getAsJsonObject("content")
                .getAsJsonArray("parts")
                .get(0).getAsJsonObject()
                .get("text").getAsString();
    }
}
