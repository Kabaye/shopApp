package com.netcracker.edu.kulich.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.StringJoiner;

@Entity
@Table(name = "tags")
@NoArgsConstructor
public class Tag {

    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "tag_id", nullable = false)
    private long id;

    @Getter
    @Setter
    @Column(nullable = false)
    private String tagname;

    @Getter
    @Setter
    @ManyToMany
    @JoinTable(name = "offers_tags",
            joinColumns = @JoinColumn(name = "tag_id"),
            inverseJoinColumns = @JoinColumn(name = "offer_id"))
    private Set<Offer> offers = new HashSet<>();

    public void addOffer(Offer offer) {
        offers.add(offer);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Tag.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("tagname='" + tagname + "'")
                .toString();
    }
}
