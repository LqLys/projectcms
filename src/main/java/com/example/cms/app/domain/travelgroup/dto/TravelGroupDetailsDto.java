package com.example.cms.app.domain.travelgroup.dto;

import com.example.cms.app.domain.travelgroup.entity.GroupStatus;
import com.example.cms.app.domain.travelgroup.entity.GroupVisibility;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TravelGroupDetailsDto {
    private Long groupId;
    private String name;
    private String destination;
    private LocalDate signOutDeadline;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    private GroupVisibility groupVisibility;
    private GroupStatus groupStatus;
    private BigDecimal debtLimit;
    private BigDecimal lat;
    private BigDecimal lng;
    private String imgUrl;
}
