package com.netcracker.edu.kulich.customer.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CustomerDTO {
    private String email = "";
    private String fio = "";
    private Integer age = 0;
}
