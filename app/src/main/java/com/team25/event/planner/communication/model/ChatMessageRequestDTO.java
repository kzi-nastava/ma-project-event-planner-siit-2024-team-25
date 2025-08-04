package com.team25.event.planner.communication.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChatMessageRequestDTO {
    private Long senderId;
    private Long receiverId;
    private String content;
    private String chatId;
}
