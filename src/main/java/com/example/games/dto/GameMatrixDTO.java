package com.example.games.dto;

import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GameMatrixDTO {

    private Long id;

    private String name;

    private Integer rows;

    private Integer columns;

    private String matrixData;
}