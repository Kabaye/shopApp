package com.netcracker.edu.kulich.tag.controller;

import com.netcracker.edu.kulich.exception.controller.ControllerException;
import com.netcracker.edu.kulich.logging.annotation.DefaultLogging;
import com.netcracker.edu.kulich.logging.annotation.Logging;
import com.netcracker.edu.kulich.tag.dto.TagDTO;
import com.netcracker.edu.kulich.tag.dto.transformator.TagTransformator;
import com.netcracker.edu.kulich.tag.entity.Tag;
import com.netcracker.edu.kulich.tag.service.TagService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/tags")
@DefaultLogging
public class TagController {
    private Logger logger = LoggerFactory.getLogger(TagController.class);
    private TagTransformator tagTransformator;
    private TagService tagService;

    @Autowired
    public TagController(TagTransformator tagTransformator, TagService tagService) {
        this.tagTransformator = tagTransformator;
        this.tagService = tagService;
    }

    @GetMapping(value = "/{id:[\\d]+}")
    @Logging(startMessage = "Request for getting tag by id is received.", endMessage = "Response on request for getting tag by id is sent.", startFromNewLine = true)
    public ResponseEntity<TagDTO> getTag(@PathVariable("id") Long id) {
        Tag tag = tagService.getTagById(id);
        if (tag == null) {
            logger.error("Tag is not found.");
            throw new ControllerException("There is no tag with id: \'" + id + "\'.");
        }
        return new ResponseEntity<>(tagTransformator.convertToDto(tag), HttpStatus.OK);
    }

    @PostMapping
    @Logging(startMessage = "Request for saving tag is received.", endMessage = "Response on request for saving tag is sent.", startFromNewLine = true)
    public ResponseEntity<TagDTO> saveTag(@RequestBody TagDTO tagDTO) {
        Tag tag = tagService.saveTag(tagTransformator.convertToEntity(tagDTO));
        return new ResponseEntity<>(tagTransformator.convertToDto(tag), HttpStatus.CREATED);
    }

    @PostMapping(value = "/tags")
    @Logging(startMessage = "Request for saving some tags is received.", endMessage = "Response on request for saving some tags is sent.", startFromNewLine = true)
    public ResponseEntity<List<TagDTO>> saveTags(@RequestBody List<TagDTO> tags) {
        List<Tag> tagList = new ArrayList<>();
        tags.forEach(tagDTO -> tagList.add(tagService.saveTag(tagTransformator.convertToEntity(tagDTO))));
        List<TagDTO> tagDTOList = tagList.stream()
                .map(tagTransformator::convertToDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(tagDTOList, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{id:[\\d]+}")
    @Logging(startMessage = "Request for deleting tag is received.", endMessage = "Response on request for deleting tag is sent.", startFromNewLine = true)
    public ResponseEntity deleteTag(@PathVariable("id") Long id) {
        tagService.deleteTagById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping(value = "/{id:[\\d]+}")
    @Logging(startMessage = "Request for updating tag is received.", endMessage = "Response on request for updating tag is sent.", startFromNewLine = true)
    public ResponseEntity<TagDTO> updateTag(@RequestBody TagDTO tagDTO, @PathVariable("id") Long id) {
        Tag tag = tagTransformator.convertToEntity(tagDTO);
        tag.setId(id);
        tag = tagService.updateTag(tag);
        return new ResponseEntity<>(tagTransformator.convertToDto(tag), HttpStatus.OK);
    }
}
