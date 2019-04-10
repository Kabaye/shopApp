package com.netcracker.edu.kulich.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.StringJoiner;

@Entity
@Table (name = "customers")
@NoArgsConstructor
public class Customer {

    @Getter
    @Setter
    @Id
    @GeneratedValue
    @NonNull
    private long id;

    @Getter
    @Setter
    @NonNull
    private String fio;

    @Getter
    @Setter
    @NonNull
    private int age;

    @Override
    public String toString() {
        return new StringJoiner(", ", Customer.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("fio='" + fio + "'")
                .add("age=" + age)
                .toString();
    }
}
