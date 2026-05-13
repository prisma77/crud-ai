package com.prisma77.crud.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import okhttp3.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class AiService {
    private String apiKey;
    private final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build();
    private final Gson gson = new Gson();

    public AiService() {
        Properties props = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config/api.properties")) {
            if (input == null) throw new RuntimeException("api.properties 파일을 찾을 수 없습니다.");
            props.load(input);
            this.apiKey = props.getProperty("gemini.api.key").trim();
        } catch (IOException e) {
            throw new RuntimeException("API 키 로드 실패", e);
        }
    }

    public String askGemini(String userPrompt, String dbContext) throws IOException {
        // Gemini 2.5 Flash 모델 사용
        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=" + apiKey;

        JsonObject jsonBody = new JsonObject();

        // [시스템 정체성]
        JsonObject sysInst = new JsonObject();
        JsonArray sysParts = new JsonArray();
        JsonObject sysPart = new JsonObject();
        sysPart.addProperty("text", "당신은 '대학교 관리 시스템'의 데이터 분석 전문가입니다. "
                + "제공된 [실시간 데이터]에 근거하여 답변하세요. "
                + "강조하고 싶은 단어나 문장은 마크다운의 ** 기호 대신 HTML의 <b> 태그를 사용하세요. "
                + "예를 들어 **의료IT학과**가 아니라 <b>의료IT학과</b>라고 답변해야 합니다.");
        sysParts.add(sysPart);
        sysInst.add("parts", sysParts);
        jsonBody.add("system_instruction", sysInst);

        // [사용자 질문 및 데이터]
        JsonArray contents = new JsonArray();
        JsonObject content = new JsonObject();
        JsonArray parts = new JsonArray();
        JsonObject part = new JsonObject();

        // 데이터 요약본과 질문을 합쳐서 전달
        part.addProperty("text", dbContext + "\n\n질문: " + userPrompt);

        parts.add(part);
        content.add("parts", parts);
        contents.add(content);
        jsonBody.add("contents", contents);

        RequestBody body = RequestBody.create(gson.toJson(jsonBody), MediaType.parse("application/json; charset=utf-8"));
        Request request = new Request.Builder().url(url).post(body).build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("API 에러 (" + response.code() + "): " + response.body().string());
            }

            JsonObject jsonResponse = gson.fromJson(response.body().string(), JsonObject.class);
            return jsonResponse.getAsJsonArray("candidates")
                    .get(0).getAsJsonObject()
                    .getAsJsonObject("content")
                    .getAsJsonArray("parts")
                    .get(0).getAsJsonObject()
                    .get("text").getAsString();
        }
    }
}