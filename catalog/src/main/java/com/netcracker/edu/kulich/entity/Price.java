package com.netcracker.edu.kulich.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.StringJoiner;

@Entity
@Table (name = "prices")
@NoArgsConstructor
public class Price {


    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "price_id", nullable = false)
    private long id;

    @Getter
    @Setter
    @Column(nullable = false)
    private double price;

    @Override
    public String toString() {
        return new StringJoiner(", ", Price.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("price=" + price)
                .toString();
    }
}
