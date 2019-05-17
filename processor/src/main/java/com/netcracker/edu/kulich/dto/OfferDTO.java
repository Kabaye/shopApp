package com.netcracker.edu.kulich.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class OfferDTO {
    private Long id = 0L;
    private String name = "";
    private Double price = 0.0;
    private PairIdNameDTO category = new PairIdNameDTO(0L, "");
    private Set<PairIdNameDTO> tags = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OfferDTO offerDTO = (OfferDTO) o;

        if (!id.equals(offerDTO.id)) return false;
        if (!name.equals(offerDTO.name)) return false;
        if (!price.equals(offerDTO.price)) return false;
        if (!category.equals(offerDTO.category)) return false;
        return tags.equals(offerDTO.tags);

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + price.hashCode();
        result = 31 * result + category.hashCode();
        result = 31 * result + tags.hashCode();
        return result;
    }
}
