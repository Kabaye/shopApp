package com.netcracker.edu.kulich.offer.entity;

import com.netcracker.edu.kulich.category.entity.Category;
import com.netcracker.edu.kulich.tag.entity.Tag;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "offers")
public class Offer {

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id = 0L;

    @Column(nullable = false)
    private String name = "";

    @OneToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "price_id")
    private Price price;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "offers_tags",
            joinColumns = @JoinColumn(name = "offer_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<Tag> tags = new HashSet<>();

    public boolean addTag(Tag tag) {
        return tags.add(tag);
    }

    public boolean removeTag(Tag tag) {
        return tags.remove(tag);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Offer offer = (Offer) o;

        if (id != offer.id) return false;
        if (!name.equals(offer.name)) return false;
        if (price != null ? !price.equals(offer.price) : offer.price != null) return false;
        if (category != null ? !category.equals(offer.category) : offer.category != null) return false;
        return tags.equals(offer.tags);

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + name.hashCode();
        result = 31 * result + (price != null ? price.hashCode() : 0);
        result = 31 * result + (category != null ? category.hashCode() : 0);
        result = 31 * result + tags.hashCode();
        return result;
    }

    public void fixNames() {
        name = name.trim().replaceAll(" +", " ");
        category.fixCategoryName();
        tags = tags.stream().filter(Objects::nonNull).collect(Collectors.toSet());
        tags.forEach(Tag::fixTagName);
    }
}
