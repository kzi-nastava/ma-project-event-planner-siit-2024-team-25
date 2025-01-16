package com.team25.event.planner.communication.model;

import com.team25.event.planner.user.dto.UserResponseDTO;

import lombok.Data;

@Data
public class ChatRoom {
    private Long id;
    private String chatId;
    private UserResponseDTO sender;
    private UserResponseDTO receiver;
}
