package com.frank.basicblog.service;


import com.frank.basicblog.dto.PostDto;
import com.frank.basicblog.model.Post;
import com.frank.basicblog.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private AuthService authService;


    public void createPost(PostDto postDto){
        Post post = mapToPost(postDto);
        postRepository.save(post);
    }


    public List<PostDto> getAll(){
        List<Post> all = postRepository.findAll();
        return  all.stream().map(this::mapToDto).collect(toList());
    }


    public PostDto getById(Long id){
        Post post = postRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("post not found"));
       return mapToDto(post);
    }


    public Post mapToPost (PostDto postDto){
        Post post = new Post();
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        String username = authService.getCurrentUser().getUsername();
        post.setUsername(username);
        post.setCreated(Instant.now());
        return post;
    }


    public PostDto mapToDto (Post post){
        PostDto postDto = new PostDto();
        postDto.setContent(post.getContent());
        postDto.setId(post.getId());
        postDto.setUsername(post.getUsername());
        postDto.setTitle(post.getTitle());
        return postDto;

    }



}
