package com.example.cms.app.domain.chatmessage.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendChatMessageRequest {

    private Long groupId;
    private String message;
}
