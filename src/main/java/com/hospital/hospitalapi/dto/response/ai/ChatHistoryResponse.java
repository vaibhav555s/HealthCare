package com.hospital.hospitalapi.dto.response.ai;

import lombok.Data;
import java.util.List;

@Data
public class ChatHistoryResponse {
    private Long prescriptionImageId;
    private List<ChatMessage> chatHistory;
    
    @Data
    public static class ChatMessage {
        private String question;
        private String answer;
        private String askedAt;
    }
}