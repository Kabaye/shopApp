package com.netcracker.edu.kulich.controller;

import com.netcracker.edu.kulich.dto.TagDTO;
import com.netcracker.edu.kulich.dto.transformator.TagTransformator;
import com.netcracker.edu.kulich.entity.Tag;
import com.netcracker.edu.kulich.exception.controller.TagControllerException;
import com.netcracker.edu.kulich.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/tags")
public class TagController {
    private final String GETTING_NOT_EXISTENT_TAG = "You try to get not existent tag.";
    private final String EMPTY_BODY_SAVING_REQUEST = "You try to call \"save\" method with empty body.";
    @Autowired
    private TagTransformator tagTransformator;
    @Autowired
    private TagService tagService;

    @GetMapping(value = "/{id:[\\d]+}")
    public ResponseEntity<TagDTO> getTag(@PathVariable("id") Long id) {
        Tag tag = tagService.getTagById(id);
        if (tag == null) {
            throw new TagControllerException(GETTING_NOT_EXISTENT_TAG);
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
        if ((tags == null) || (tags.size() == 0)) {
            throw new TagControllerException(EMPTY_BODY_SAVING_REQUEST);
        }
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
