package com.example.mindcard.service;

import com.example.mindcard.model.Deck;
import org.springframework.stereotype.Service;

@Service
public class DeckGeneratorService {

    private final GeminiService geminiService;
    private final QwenService qwenService;

    public DeckGeneratorService(GeminiService geminiService, QwenService qwenService) {
        this.geminiService = geminiService;
        this.qwenService = qwenService;
    }

    public Deck generateDeckWithFallback(String userPrompt) {
        try {
            // Thử gọi Gemini trước
            System.out.println("Đang thử tạo deck bằng Gemini...");
            return geminiService.generateDeck(userPrompt);
        } catch (Exception e) {
            // Nếu Gemini Rate limit, timeout, server error...
            System.err.println("Gemini thất bại (" + e.getMessage() + "). Chuyển hướng sang Qwen...");
            try {
                // Gọi Qwen để chữa cháy
                return qwenService.generateDeck(userPrompt);
            } catch (Exception ex) {
                // Nếu cả 2 đều sập
                throw new RuntimeException("Rất tiếc, tất cả các hệ thống AI đều đang bận. Vui lòng thử lại sau.", ex);
            }
        }
    }
}