package com.example.cms.app.domain.travelgroup.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTravelGroupRequest {

    @Size(min = 3, max = 50, message = "Nazwa grupy może składać się z 3 do 50 znaków")
    private String name;
    private List<Long> friendIds = new ArrayList<>();

}
