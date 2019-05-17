package com.netcracker.edu.kulich.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PairDoubleDoubleDTO {
    private Double lowerBound;
    private Double upperBound;

    public PairDoubleDoubleDTO(Double lowerBound, Double upperBound) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }
}
