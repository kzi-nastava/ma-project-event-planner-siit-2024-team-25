package com.team25.event.planner.communication.model;

import android.net.wifi.p2p.WifiP2pManager;

import com.team25.event.planner.user.dto.UserResponseDTO;
import com.team25.event.planner.user.model.User;

import java.util.Date;

import lombok.Data;

@Data
public class ChatMessage {
    private Long id;
    private String chatId;
    private UserResponseDTO sender;
    private UserResponseDTO receiver;
    private String content;
    private Date timestamp;
}
