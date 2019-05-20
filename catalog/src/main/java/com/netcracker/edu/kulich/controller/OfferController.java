package com.netcracker.edu.kulich.controller;

import com.netcracker.edu.kulich.dto.CategoryDTO;
import com.netcracker.edu.kulich.dto.OfferDTO;
import com.netcracker.edu.kulich.dto.PairDoubleDoubleDTO;
import com.netcracker.edu.kulich.dto.TagDTO;
import com.netcracker.edu.kulich.dto.transformator.CategoryTransformator;
import com.netcracker.edu.kulich.dto.transformator.OfferTransformator;
import com.netcracker.edu.kulich.dto.transformator.TagTransformator;
import com.netcracker.edu.kulich.entity.Offer;
import com.netcracker.edu.kulich.exception.controller.ControllerException;
import com.netcracker.edu.kulich.logging.DefaultLogging;
import com.netcracker.edu.kulich.logging.Logging;
import com.netcracker.edu.kulich.service.OfferService;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/offers")
@DefaultLogging
@NoArgsConstructor
public class OfferController {
    private Logger logger = LoggerFactory.getLogger(OfferController.class);
    private OfferTransformator offerTransformator;
    private TagTransformator tagTransformator;
    private CategoryTransformator categoryTransformator;
    private OfferService offerService;

    @Autowired
    public OfferController(OfferTransformator offerTransformator, TagTransformator tagTransformator, CategoryTransformator categoryTransformator, OfferService offerService) {
        this.offerTransformator = offerTransformator;
        this.tagTransformator = tagTransformator;
        this.categoryTransformator = categoryTransformator;
        this.offerService = offerService;
    }

    @GetMapping(value = "/{id:[\\d]+}")
    @Logging(startMessage = "Request for getting offer by id is received.", endMessage = "Response on request for getting offer by id is sent.", startFromNewLine = true)
    public ResponseEntity<OfferDTO> getOffer(@PathVariable("id") Long id) {
        Offer offer = offerService.getOfferById(id);
        if (offer == null) {
            logger.error("Offer is not found.");
            throw new ControllerException("There is no offer with id: \'" + id + "\'.");
        }
        return new ResponseEntity<>(offerTransformator.convertToDto(offer), HttpStatus.OK);
    }

    @GetMapping
    @Logging(startMessage = "Request for getting all offers is received.", endMessage = "Response on request for getting all offers is sent.", startFromNewLine = true)
    public ResponseEntity<List<OfferDTO>> getOffers(@RequestParam(required = false) String category,
                                                    @RequestParam(required = false) Set<String> tags,
                                                    @RequestParam(required = false) Double minPrice,
                                                    @RequestParam(required = false) Double maxPrice) {
        if (category != null) {
            category = fixSpaces(category);
            return new ResponseEntity<>(getAllOffersByCategory(new CategoryDTO(category)), HttpStatus.OK);
        } else if (tags != null) {
            return new ResponseEntity<>(getAllOffersByListOfTags(tags.stream()
                    .map(this::fixSpaces)
                    .map(TagDTO::new)
                    .collect(Collectors.toList())), HttpStatus.OK);
        } else if (minPrice != null && maxPrice != null) {
            return new ResponseEntity<>(getAllOffersByRangeOfPrice(new PairDoubleDoubleDTO(minPrice, maxPrice)), HttpStatus.OK);
        }
        return new ResponseEntity<>(offerService.getAllOffers().stream()
                .map(offerTransformator::convertToDto)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    @PostMapping
    @Logging(startMessage = "Request for saving offer is received.", endMessage = "Response on request for saving offer is sent.", startFromNewLine = true)
    public ResponseEntity<OfferDTO> saveOffer(@RequestBody OfferDTO offerDTO) {
        Offer offer = offerService.saveOffer(offerTransformator.convertToEntity(offerDTO));
        return new ResponseEntity<>(offerTransformator.convertToDto(offer), HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{id:[\\d]+}")
    @Logging(startMessage = "Request for deleting offer is received.", endMessage = "Response on request for deleting offer is sent.", startFromNewLine = true)
    public ResponseEntity<Object> deleteOffer(@PathVariable("id") Long id) {
        offerService.deleteOfferById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping(value = "/{id:[\\d]+}")
    @Logging(startMessage = "Request for updating offer is received.", endMessage = "Response on request for updating offer is sent.", startFromNewLine = true)
    public ResponseEntity<OfferDTO> updateOffer(@RequestBody OfferDTO offerDTO, @PathVariable("id") Long id) {
        Offer offer = offerTransformator.convertToEntity(offerDTO);
        offer.setId(id);
        offer = offerService.updateOffer(offer);
        return new ResponseEntity<>(offerTransformator.convertToDto(offer), HttpStatus.OK);
    }

    @PutMapping(value = "/{id:[\\d]+}/tags")
    @Logging(startMessage = "Request for adding tag to offer is received.", endMessage = "Response on request for adding tag to offer is sent.", startFromNewLine = true)
    public ResponseEntity<OfferDTO> addTagToOffer(@RequestBody TagDTO tagDTO, @PathVariable("id") Long id) {
        Offer offer = offerService.addTag(id, tagTransformator.convertToEntity(tagDTO));
        return new ResponseEntity<>(offerTransformator.convertToDto(offer), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id:[\\d]+}/tags")
    @Logging(startMessage = "Request for deleting tag from offer is received.", endMessage = "Response on request for deleting tag from offer is sent.", startFromNewLine = true)
    public ResponseEntity<OfferDTO> deleteTagFromOffer(@RequestBody TagDTO tagDTO, @PathVariable("id") Long id) {
        Offer offer = offerService.removeTag(id, tagTransformator.convertToEntity(tagDTO));
        return new ResponseEntity<>(offerTransformator.convertToDto(offer), HttpStatus.OK);
    }

    @PutMapping(value = "/{id:[\\d]+}/category")
    @Logging(startMessage = "Request for changing category in offer is received.", endMessage = "Response on request for changing category in offer is sent.", startFromNewLine = true)
    public ResponseEntity<OfferDTO> changeCategory(@RequestBody CategoryDTO categoryDTO, @PathVariable("id") Long id) {
        Offer offer = offerService.updateCategory(id, categoryTransformator.convertToEntity(categoryDTO));
        return new ResponseEntity<>(offerTransformator.convertToDto(offer), HttpStatus.OK);
    }

    @Logging(startMessage = "Request for getting all offers by category is received.", endMessage = "Response on request for getting all offers by category is sent.")
    public List<OfferDTO> getAllOffersByCategory(CategoryDTO category) {
        return offerService.findOffersByCategory(categoryTransformator.convertToEntity(category))
                .stream()
                .map(offerTransformator::convertToDto)
                .collect(Collectors.toList());
    }

    @Logging(startMessage = "Request for getting all offers by tags is received.", endMessage = "Response on request for getting all offers by tags is sent.")
    public List<OfferDTO> getAllOffersByListOfTags(List<TagDTO> tagDTOs) {
        return offerService.findOffersByTags(tagDTOs.stream()
                .filter(Objects::nonNull)
                .map(tagTransformator::convertToEntity)
                .collect(Collectors.toSet()))
                .stream()
                .map(offerTransformator::convertToDto)
                .collect(Collectors.toList());
    }

    @Logging(startMessage = "Request for getting all offers by range of price is received.", endMessage = "Response on request for getting all offers by range of price is sent.")
    public List<OfferDTO> getAllOffersByRangeOfPrice(PairDoubleDoubleDTO bounds) {
        return offerService.findOffersByRangeOfPrice(bounds.getLowerBound(), bounds.getUpperBound())
                .stream()
                .map(offerTransformator::convertToDto)
                .collect(Collectors.toList());
    }

    private String fixSpaces(String str) {
        return str.replaceAll("%20", " ");
    }
}
