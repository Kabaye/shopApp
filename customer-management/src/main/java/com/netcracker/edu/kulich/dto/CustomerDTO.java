package com.netcracker.edu.kulich.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CustomerDTO {
    private Long id = 0L;
    private String fio = "";
    private Integer age = 0;
}
