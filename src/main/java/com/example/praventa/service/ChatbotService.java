package com.example.praventa.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class ChatbotService {
    private static final String API_KEY = System.getenv("GEMINI_API_KEY");
    private static final String ENDPOINT = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key="+API_KEY;

    public static String askVita(String userMessage) throws Exception {
        HttpClient client = HttpClient.newHttpClient();

        // Persona Prompt
        String systemPrompt = "You are Vita, a cheerful and supportive chatbot that helps people live longer and healthier lives. " +
                "Always use a positive, friendly tone. Use short helpful replies with emojis. " +
                "Slogan: 'Vita bantu kamu hidup lebih lama dan bahagia!'\nUser: " + userMessage + "\nVita:";

        JsonObject message = new JsonObject();
        JsonArray contents = new JsonArray();
        JsonObject contentObj = new JsonObject();
        contentObj.addProperty("role", "user");

        JsonArray parts = new JsonArray();
        JsonObject part = new JsonObject();
        part.addProperty("text", systemPrompt);
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
        System.out.println("[Chatbot Vita] Response:\n" + response.body());

        JsonObject jsonResponse = JsonParser.parseString(response.body()).getAsJsonObject();

        if (!jsonResponse.has("candidates")) {
            throw new RuntimeException("Tidak ada kandidat jawaban dari Gemini:\n" + response.body());
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
