package com.example.praventa.service;

import com.google.gson.*;
import java.net.URI;
import java.net.http.*;
import java.nio.charset.StandardCharsets;

public class GeminiService {
    private static final String API_KEY = System.getenv("GEMINI_API_KEY");
    private static final String ENDPOINT = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=AIzaSyB0zdmxHxrPn75LBQVRkyjTKp78OUz4L8M";

    public static String generateRecommendation(String userData) throws Exception {
        HttpClient client = HttpClient.newHttpClient();

        // Prompt dengan instruksi eksplisit agar output pendek dan profesional
        String instruction = """
            Berdasarkan data gaya hidup berikut ini, buatlah rekomendasi gaya hidup sehat yang:
            - Singkat, maksimal 2 poin untuk masing-masing kategori (makan, tidur, aktivitas).
            - Profesional dan mudah dipahami.
            - Tidak perlu penjelasan umum atau pengantar.
            - Gunakan format berikut:

              Rekomendasi Makan:
              - ...
              - ...

              Rekomendasi Tidur:
              - ...
              - ...

              Rekomendasi Aktivitas:
              - ...
              - ...

            Berikut datanya:
            """ + userData;

        // Siapkan struktur permintaan JSON sesuai Gemini API
        JsonObject message = new JsonObject();
        JsonArray contents = new JsonArray();
        JsonObject contentObj = new JsonObject();
        contentObj.addProperty("role", "user");

        JsonArray parts = new JsonArray();
        JsonObject part = new JsonObject();
        part.addProperty("text", instruction);
        parts.add(part);

        contentObj.add("parts", parts);
        contents.add(contentObj);
        message.add("contents", contents);

        // Kirim permintaan ke Gemini API
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ENDPOINT))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(message.toString(), StandardCharsets.UTF_8))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Debug log respons mentah
        System.out.println("[DEBUG] Gemini Response Body:\n" + response.body());

        // Parsing respons JSON
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
