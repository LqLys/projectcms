package com.example.cms.app.domain.chatmessage.service;

import com.example.cms.app.domain.chatmessage.entity.ChatMessageEntity;
import com.example.cms.app.domain.chatmessage.repository.ChatMessageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatMessageService {


    private final ChatMessageRepository chatMessageRepository;

    public ChatMessageService(ChatMessageRepository chatMessageRepository) {
        this.chatMessageRepository = chatMessageRepository;
    }

    public List<ChatMessageEntity> findAllByGroupId(Long groupId) {
        return chatMessageRepository.findAllByGroup_Id(groupId);
    }

    public void saveMessage(ChatMessageEntity message) {
        chatMessageRepository.save(message);
    }
}
