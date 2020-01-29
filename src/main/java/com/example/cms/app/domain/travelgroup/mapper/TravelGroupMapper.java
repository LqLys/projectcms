package com.example.cms.app.domain.travelgroup.mapper;

import com.example.cms.app.domain.travelgroup.dto.TravelGroupDto;
import com.example.cms.app.domain.travelgroup.entity.TravelGroupEntity;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.ConfigurableMapper;
import org.springframework.stereotype.Component;

@Component
public class TravelGroupMapper extends ConfigurableMapper {

    protected void configure(MapperFactory factory) {

        factory.classMap(TravelGroupEntity.class, TravelGroupDto.class)
                .byDefault()
                .register();
    }
}
