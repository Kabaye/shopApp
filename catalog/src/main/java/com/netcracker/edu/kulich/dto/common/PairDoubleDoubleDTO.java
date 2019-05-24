package com.netcracker.edu.kulich.dto.common;

import lombok.Getter;

@Getter
public class PairDoubleDoubleDTO {
    private Double lowerBound;
    private Double upperBound;

    public PairDoubleDoubleDTO(Double lowerBound, Double upperBound) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }
}
