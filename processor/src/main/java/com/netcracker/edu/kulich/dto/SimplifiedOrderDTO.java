package com.netcracker.edu.kulich.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class SimplifiedOrderDTO {
    private LocalDate date = LocalDate.now();
    private String email = "";
    private Set<Long> itemIds = new HashSet<>();
}
