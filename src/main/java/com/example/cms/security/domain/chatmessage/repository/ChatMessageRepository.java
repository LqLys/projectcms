package com.example.cms.security.domain.chatmessage.repository;

import com.example.cms.security.domain.chatmessage.entity.ChatMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessageEntity, Long> {

    List<ChatMessageEntity> findAllByGroup_Id(Long groupId);
}
