package com.netcracker.edu.kulich.controller;

import com.netcracker.edu.kulich.dto.TagDTO;
import com.netcracker.edu.kulich.dto.transformator.TagTransformator;
import com.netcracker.edu.kulich.entity.Tag;
import com.netcracker.edu.kulich.exception.controller.ControllerException;
import com.netcracker.edu.kulich.service.TagService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/tags")
public class TagController {
    private TagTransformator tagTransformator;
    private TagService tagService;

    public TagController(TagTransformator tagTransformator, TagService tagService) {
        this.tagTransformator = tagTransformator;
        this.tagService = tagService;
    }

    @GetMapping(value = "/{id:[\\d]+}")
    public ResponseEntity<TagDTO> getTag(@PathVariable("id") Long id) {
        Tag tag = tagService.getTagById(id);
        if (tag == null) {
            throw new ControllerException("There is no tag with id: \'" + id + "\'.");
        }
        return new ResponseEntity<>(tagTransformator.convertToDto(tag), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<TagDTO> saveTag(@RequestBody TagDTO tagDTO) {
        Tag tag = tagService.saveTag(tagTransformator.convertToEntity(tagDTO));
        return new ResponseEntity<>(tagTransformator.convertToDto(tag), HttpStatus.CREATED);
    }

    @PostMapping(value = "/tags")
    public ResponseEntity<List<TagDTO>> saveTags(@RequestBody List<TagDTO> tags) {
        List<Tag> tagList = new ArrayList<>();
        tags.forEach(tagDTO -> tagList.add(tagService.saveTag(tagTransformator.convertToEntity(tagDTO))));
        List<TagDTO> tagDTOList = tagList.stream()
                .map(tagTransformator::convertToDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(tagDTOList, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{id:[\\d]+}")
    public ResponseEntity deleteTag(@PathVariable("id") Long id) {
        tagService.deleteTagById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping(value = "/{id:[\\d]+}")
    public ResponseEntity<TagDTO> updateTag(@RequestBody TagDTO tagDTO, @PathVariable("id") Long id) {
        Tag tag = tagTransformator.convertToEntity(tagDTO);
        tag.setId(id);
        tag = tagService.updateTag(tag);
        return new ResponseEntity<>(tagTransformator.convertToDto(tag), HttpStatus.OK);
    }
}
