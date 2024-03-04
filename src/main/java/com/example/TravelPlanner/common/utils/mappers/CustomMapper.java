package com.example.TravelPlanner.common.utils.mappers;

import java.util.List;

public interface CustomMapper <Entity, DTO>  {
    DTO mapToDTO(Entity entity);

    Entity mapToEntity(DTO dto);
}
