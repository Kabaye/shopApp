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
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(nullable = false, name = "customer_id")
    private long id;

    @Column(nullable = false)
    private String fio;

    @Column(nullable = false)
    private int age;

    @OneToMany(mappedBy = "customer", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<Order> orders = new HashSet<>();

    @Override
    public String toString() {
        return new StringJoiner(", ", Customer.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("fio='" + fio + "'")
                .add("age=" + age)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Customer customer = (Customer) o;

        if (id != customer.id) return false;
        if (age != customer.age) return false;
        return fio != null ? fio.equals(customer.fio) : customer.fio == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (fio != null ? fio.hashCode() : 0);
        result = 31 * result + age;
        return result;
    }
}
