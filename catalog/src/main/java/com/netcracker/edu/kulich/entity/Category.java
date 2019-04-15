package com.netcracker.edu.kulich.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.StringJoiner;

@Entity
@Table(name = "categories")
@NoArgsConstructor
public class Category {

    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column (name = "category_id", nullable = false)
    private long id;

    @Getter
    @Setter
    @NonNull
    @Column(nullable = false)
    private String category;


    @Getter
    @Setter
    @OneToMany (mappedBy = "category", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private Set<Offer> offers = new HashSet<>();

    @Override
    public String toString() {
        return new StringJoiner(", ", Category.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("category='" + category + "'")
                .toString();
    }
}
