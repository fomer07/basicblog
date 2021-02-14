package com.frank.basicblog.controller;

import com.frank.basicblog.dto.PostDto;
import com.frank.basicblog.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/post")
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping("/new")
    public ResponseEntity createPost(@RequestBody PostDto postDto){
        postService.createPost(postDto);
    return new  ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<PostDto> getOne(@PathVariable Long id){
        return new ResponseEntity<>(postService.getById(id),HttpStatus.OK);
    }

    @GetMapping("/get/all")
    public ResponseEntity<List<PostDto>> getAll(){
        return new ResponseEntity<>(postService.getAll(),HttpStatus.OK);
    }



}
