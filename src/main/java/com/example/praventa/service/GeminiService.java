package com.example.praventa.service;

import com.example.praventa.model.questionnaire.RiskData;
import com.google.gson.*;
import java.net.URI;
import java.net.http.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class GeminiService {
    private static final String API_KEY = System.getenv("GEMINI_API_KEY");
    private static final String ENDPOINT = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=AIzaSyB0zdmxHxrPn75LBQVRkyjTKp78OUz4L8M";

    /**
     * Rekomendasi gaya hidup sehat (makan, tidur, aktivitas).
     */
    public static String generateRecommendation(String userData) throws Exception {
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

        return sendToGemini(instruction);
    }

    public static String calculateRiskStatus(List<RiskData> risks) {
        if (risks == null || risks.isEmpty()) return "Rendah";

        double avg = risks.stream().mapToDouble(RiskData::getPercentage).average().orElse(0.0);

        if (avg >= 0.5) return "Tinggi";
        else if (avg >= 0.25) return "Sedang";
        else return "Rendah";
    }


    /**
     * Rekomendasi pemeriksaan medis dan status risiko keseluruhan.
     */
    public static String generateMedicalCheckupRecommendation(String riskData) throws Exception {
        String instruction = """
            Berdasarkan daftar risiko penyakit berikut, berikan:

            1. Tiga rekomendasi pemeriksaan medis yang sesuai, dalam bentuk poin (tanpa penjelasan umum).
            2. Status risiko keseluruhan (Tinggi, Sedang, atau Rendah) dalam satu kata, di baris terpisah setelah daftar.

            Gunakan format seperti ini:

            - Pemeriksaan A
            - Pemeriksaan B
            - Pemeriksaan C

            Tinggi

            Berikut data risikonya:
            """ + riskData;

        return sendToGemini(instruction);
    }

    /**
     * Fungsi umum untuk mengirim prompt ke Gemini API.
     */
    private static String sendToGemini(String instruction) throws Exception {
        HttpClient client = HttpClient.newHttpClient();

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

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ENDPOINT))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(message.toString(), StandardCharsets.UTF_8))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

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