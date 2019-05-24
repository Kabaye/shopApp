package com.netcracker.edu.kulich.dto.common;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PairIdNameDTO {
    private Long id;
    private String name;

    public PairIdNameDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
