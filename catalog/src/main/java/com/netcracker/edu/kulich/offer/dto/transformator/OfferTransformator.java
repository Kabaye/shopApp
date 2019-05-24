package com.netcracker.edu.kulich.offer.dto.transformator;

import com.netcracker.edu.kulich.category.entity.Category;
import com.netcracker.edu.kulich.dto.common.PairIdNameDTO;
import com.netcracker.edu.kulich.offer.dto.OfferDTO;
import com.netcracker.edu.kulich.offer.entity.Offer;
import com.netcracker.edu.kulich.offer.entity.Price;
import com.netcracker.edu.kulich.tag.entity.Tag;
import org.springframework.stereotype.Component;

@Component
public class OfferTransformator {
    public Offer convertToEntity(OfferDTO offerDTO) {
        Offer offer = new Offer();
        offer.setId(offerDTO.getId());
        offer.setName(offerDTO.getName());

        Price price = new Price();
        price.setPrice(offerDTO.getPrice());
        offer.setPrice(price);

        Category category = new Category();
        category.setCategory(offerDTO.getCategory().getName());
        offer.setCategory(category);

        offerDTO.getTags().forEach(pair -> {
            Tag tag = new Tag();
            tag.setTagname(pair.getName());
            offer.addTag(tag);
        });

        return offer;
    }

    public OfferDTO convertToDto(Offer offer) {
        OfferDTO offerDTO = new OfferDTO();
        offerDTO.setId(offer.getId());
        offerDTO.setName(offer.getName());

        PairIdNameDTO pairDTO = new PairIdNameDTO(offer.getCategory().getId(), offer.getCategory().getCategory());
        offerDTO.setCategory(pairDTO);

        offerDTO.setPrice(offer.getPrice().getPrice());

        offer.getTags().forEach(tag -> offerDTO.getTags().add(new PairIdNameDTO(tag.getId(), tag.getTagname())));

        return offerDTO;
    }
}
