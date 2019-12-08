package com.example.cms.security.domain.relation.facade;

import com.example.cms.security.domain.relation.dto.BaseFriendDto;
import com.example.cms.security.domain.relation.service.RelationService;
import com.example.cms.security.domain.user.entity.UserEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RelationFacade {

    private final RelationService relationService;

    public RelationFacade(RelationService relationService) {
        this.relationService = relationService;
    }

    public List<BaseFriendDto> getUserFriends(Long userId) {
        return relationService.getFriends(userId).stream().map(this::mapToBaseFriend).collect(Collectors.toList());
    }

    private BaseFriendDto mapToBaseFriend(UserEntity userEntity) {
        return BaseFriendDto.builder()
                .id(userEntity.getId())
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName())
                .build();

    }
}
