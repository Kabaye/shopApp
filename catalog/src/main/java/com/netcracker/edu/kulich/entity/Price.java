package com.netcracker.edu.kulich.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.*;
import java.util.StringJoiner;

@Entity
@Table (name = "prices")
@NoArgsConstructor
public class Price {

    @NonNull
    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "price_id")
    private long id;

    @Getter
    @Setter
    @NonNull
    private double price;

    @Override
    public String toString() {
        return new StringJoiner(", ", Price.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("price=" + price)
                .toString();
    }
}
