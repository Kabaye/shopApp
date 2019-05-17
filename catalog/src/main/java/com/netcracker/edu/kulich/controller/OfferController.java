package com.netcracker.edu.kulich.controller;

import com.netcracker.edu.kulich.dto.CategoryDTO;
import com.netcracker.edu.kulich.dto.OfferDTO;
import com.netcracker.edu.kulich.dto.PairDoubleDoubleDTO;
import com.netcracker.edu.kulich.dto.TagDTO;
import com.netcracker.edu.kulich.dto.transformator.CategoryTransformator;
import com.netcracker.edu.kulich.dto.transformator.OfferTransformator;
import com.netcracker.edu.kulich.dto.transformator.TagTransformator;
import com.netcracker.edu.kulich.entity.Offer;
import com.netcracker.edu.kulich.entity.Tag;
import com.netcracker.edu.kulich.exception.controller.ControllerException;
import com.netcracker.edu.kulich.service.OfferService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/offers")
public class OfferController {
    private OfferTransformator offerTransformator;
    private TagTransformator tagTransformator;
    private CategoryTransformator categoryTransformator;
    private OfferService offerService;

    public OfferController(OfferTransformator offerTransformator, TagTransformator tagTransformator, CategoryTransformator categoryTransformator, OfferService offerService) {
        this.offerTransformator = offerTransformator;
        this.tagTransformator = tagTransformator;
        this.categoryTransformator = categoryTransformator;
        this.offerService = offerService;
    }

    @GetMapping(value = "/{id:[\\d]+}")
    public ResponseEntity<OfferDTO> getOffer(@PathVariable("id") Long id) {
        Offer offer = offerService.getOfferById(id);
        if (offer == null) {
            throw new ControllerException("There is no offer with id: \'" + id + "\'.");
        }
        return new ResponseEntity<>(offerTransformator.convertToDto(offer), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<OfferDTO>> getOffers() {
        List<Offer> offers = offerService.getAllOffers();
        return new ResponseEntity<>(offers.stream()
                .map(offerTransformator::convertToDto)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<OfferDTO> saveOffer(@RequestBody OfferDTO offerDTO) {
        Offer offer = offerService.saveOffer(offerTransformator.convertToEntity(offerDTO));
        return new ResponseEntity<>(offerTransformator.convertToDto(offer), HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{id:[\\d]+}")
    public ResponseEntity<Object> deleteOffer(@PathVariable("id") Long id) {
        offerService.deleteOfferById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping(value = "/{id:[\\d]+}")
    public ResponseEntity<OfferDTO> updateOffer(@RequestBody OfferDTO offerDTO, @PathVariable("id") Long id) {
        Offer offer = offerTransformator.convertToEntity(offerDTO);
        offer.setId(id);
        offer = offerService.updateOffer(offer);
        return new ResponseEntity<>(offerTransformator.convertToDto(offer), HttpStatus.OK);
    }

    @PutMapping(value = "/{id:[\\d]+}/tags")
    public ResponseEntity<OfferDTO> addTagToOffer(@RequestBody TagDTO tagDTO, @PathVariable("id") Long id) {
        Offer offer = offerService.addTag(id, tagTransformator.convertToEntity(tagDTO));
        return new ResponseEntity<>(offerTransformator.convertToDto(offer), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id:[\\d]+}/tags")
    public ResponseEntity<OfferDTO> deleteTagFromOffer(@RequestBody TagDTO tagDTO, @PathVariable("id") Long id) {
        Offer offer = offerService.removeTag(id, tagTransformator.convertToEntity(tagDTO));
        return new ResponseEntity<>(offerTransformator.convertToDto(offer), HttpStatus.OK);
    }

    @PutMapping(value = "/{id:[\\d]+}/category")
    public ResponseEntity<OfferDTO> changeCategory(@RequestBody CategoryDTO categoryDTO, @PathVariable("id") Long id) {
        Offer offer = offerService.updateCategory(id, categoryTransformator.convertToEntity(categoryDTO));
        return new ResponseEntity<>(offerTransformator.convertToDto(offer), HttpStatus.OK);
    }

    @GetMapping(value = "/category")
    public ResponseEntity<List<OfferDTO>> getAllOffersByCategory(@RequestBody CategoryDTO categoryDTO) {
        List<OfferDTO> offerDTOs = new ArrayList<>();
        offerService.findOffersByCategory(categoryTransformator.convertToEntity(categoryDTO))
                .forEach(offer -> offerDTOs.add(offerTransformator.convertToDto(offer)));
        return new ResponseEntity<>(offerDTOs, HttpStatus.OK);
    }

    @GetMapping(value = "/tags")
    public ResponseEntity<List<OfferDTO>> getAllOffersByListOfTags(@RequestBody List<TagDTO> tagDTOs) {
        List<OfferDTO> offerDTOs = new ArrayList<>();
        Set<Tag> tags = new HashSet<>();
        tagDTOs.forEach(tagDTO -> tags.add(tagTransformator.convertToEntity(tagDTO)));
        offerService.findOffersByTags(tags)
                .forEach(offer -> offerDTOs.add(offerTransformator.convertToDto(offer)));
        return new ResponseEntity<>(offerDTOs, HttpStatus.OK);
    }

    @GetMapping(value = "/prices")
    public ResponseEntity<List<OfferDTO>> getAllOffersByRangeOfPrice(@RequestBody PairDoubleDoubleDTO bounds) {
        List<OfferDTO> offerDTOs = offerService.findOffersByRangeOfPrice(bounds.getLowerBound(), bounds.getUpperBound()).stream()
                .map(offerTransformator::convertToDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(offerDTOs, HttpStatus.OK);
    }
}
