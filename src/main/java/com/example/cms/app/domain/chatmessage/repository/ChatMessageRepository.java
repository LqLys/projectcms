package com.example.cms.app.domain.chatmessage.repository;

import com.example.cms.app.domain.chatmessage.entity.ChatMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessageEntity, Long> {

    List<ChatMessageEntity> findAllByGroup_Id(Long groupId);
}
