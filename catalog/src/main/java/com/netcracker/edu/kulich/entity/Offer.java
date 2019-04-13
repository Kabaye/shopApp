package com.netcracker.edu.kulich.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.StringJoiner;

@Entity
@Table(name = "offers")
@NoArgsConstructor
public class Offer {

    @Getter
    @Setter
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Getter
    @Setter
    @Column(nullable = false)
    private String name;

    @Getter
    @Setter
    @OneToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "price_id")
    private Price price;

    @Getter
    @Setter
    @ManyToOne(optional = false, cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    @JoinColumn(name = "category_id")
    private Category category;

    @Getter
    @Setter
    @ManyToMany (cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    @JoinTable(name = "offers_tags",
            joinColumns = @JoinColumn(name = "offer_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<Tag> tags = new HashSet<>();

    public void addTag(Tag tag) {
        tags.add(tag);
    }


    @Override
    public String toString() {
        return new StringJoiner(", ", Offer.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("name='" + name + "'")
                .add("price=" + price)
                .add("category=" + category)
                .add("tags=" + tags)
                .toString();
    }
}
