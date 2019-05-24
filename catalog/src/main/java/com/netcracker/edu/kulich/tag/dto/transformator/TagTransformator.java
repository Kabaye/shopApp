package com.netcracker.edu.kulich.tag.dto.transformator;

import com.netcracker.edu.kulich.dto.common.PairIdNameDTO;
import com.netcracker.edu.kulich.tag.dto.TagDTO;
import com.netcracker.edu.kulich.tag.entity.Tag;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class TagTransformator {

    public Tag convertToEntity(TagDTO tagDTO) {
        Tag tag = new Tag();
        tag.setId(tagDTO.getId());
        tag.setTagname(tagDTO.getName());
        return tag;
    }

    public TagDTO convertToDto(Tag tag) {
        TagDTO tagDTO = new TagDTO();
        tagDTO.setId(tag.getId());
        tagDTO.setName(tag.getTagname());
        Set<PairIdNameDTO> offers = tag.getOffers().stream()
                .filter(Objects::nonNull)
                .map(offer -> new PairIdNameDTO(offer.getId(), offer.getName()))
                .collect(Collectors.toSet());
        tagDTO.setOffers(offers);
        return tagDTO;
    }
}
