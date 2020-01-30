package com.example.cms.app.domain.travelgroup.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTravelGroupRequest {

    private String groupName;
    private List<Long> friendIds = new ArrayList<>();

}
