package com.example.games.mapper;

import com.example.games.dto.GameMatrixDTO;
import com.example.games.entity.GameMatrixEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class GameMatrixMapper {

    @Mapping(target = "createdAt", ignore = true)
    public abstract GameMatrixEntity toEntity(GameMatrixDTO dto);

    public abstract GameMatrixDTO toDTO(GameMatrixEntity entity);

}
