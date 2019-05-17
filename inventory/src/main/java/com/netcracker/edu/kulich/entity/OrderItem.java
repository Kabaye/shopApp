package com.netcracker.edu.kulich.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false)
    private String name = "";

    @Column(name = "price", nullable = false)
    private double price = 0.0;

    @Column(name = "category", nullable = false)
    private String category = "";

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @JoinTable(name = "offers_tags",
            joinColumns = @JoinColumn(name = "offer_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<Tag> tags = new HashSet<>();

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id")
    private Order order;

    public void addTag(Tag tag) {
        tags.add(tag);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", OrderItem.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("name='" + name + "'")
                .add("price=" + price)
                .add("category=" + category)
                .add("tags=" + tags)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderItem orderItem = (OrderItem) o;

        if (id != orderItem.id) return false;
        if (Double.compare(orderItem.price, price) != 0) return false;
        if (name != null ? !name.equals(orderItem.name) : orderItem.name != null) return false;
        if (category != null ? !category.equals(orderItem.category) : orderItem.category != null) return false;
        return tags != null ? tags.equals(orderItem.tags) : orderItem.tags == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        temp = Double.doubleToLongBits(price);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (category != null ? category.hashCode() : 0);
        result = 31 * result + (tags != null ? tags.hashCode() : 0);
        return result;
    }

    public void fixName() {
        category = category.trim().replaceAll(" +", " ");
        name = name.trim().replaceAll(" +", " ");
        tags = tags.stream().filter(Objects::nonNull).collect(Collectors.toSet());
        for (Tag tag : tags) {
            tag.fixName();
        }
    }
}
