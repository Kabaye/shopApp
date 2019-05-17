package com.netcracker.edu.kulich.dto;

import lombok.Getter;

@Getter
public class PairIdNameDTO {
    private Long id;
    private String name;

    public PairIdNameDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}