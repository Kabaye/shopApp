package com.netcracker.edu.kulich.customer.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "customers")
@NamedQueries({@NamedQuery(name = "Customer.getAll", query = "SELECT elem FROM Customer elem ORDER BY elem.id")})
public class Customer {

    @Id
    @Column(nullable = false, unique = true)
    private String email = "";

    @Column(nullable = false)
    private String fio = "";

    @Column(nullable = false)
    private int age;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Customer customer = (Customer) o;

        return email != null ? email.equals(customer.email) : customer.email == null;

    }

    @Override
    public int hashCode() {
        return email != null ? email.hashCode() : 0;
    }

    public void fioFixing() {
        fio = fio.trim().replaceAll(" +", " ");
    }
}
