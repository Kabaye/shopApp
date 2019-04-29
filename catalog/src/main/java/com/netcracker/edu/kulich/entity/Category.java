package com.netcracker.edu.kulich.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.StringJoiner;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "category_id", nullable = false, unique = true)
    private long id;

    @Column(nullable = false, unique = true)
    private String category;


    @OneToMany (mappedBy = "category", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private Set<Offer> offers = new HashSet<>();

    @Override
    public String toString() {
        return new StringJoiner(", ", Category.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("category='" + category + "'")
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Category category1 = (Category) o;

        if (id != category1.id) return false;
        return category != null ? category.equals(category1.category) : category1.category == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (category != null ? category.hashCode() : 0);
        return result;
    }
}
