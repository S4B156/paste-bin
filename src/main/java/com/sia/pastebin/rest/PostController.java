package com.sia.pastebin.rest;

import com.sia.pastebin.repository.PostRepository;
import com.sia.pastebin.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/post/")
@Slf4j
public class PostController {
    @Autowired
    PostService postService;
    @Autowired
    PostRepository postRepository;



    @DeleteMapping("/delete/{hashKey}")
    public ResponseEntity<Void> deletePost(@PathVariable String hashKey) throws IOException {
        postService.deletePost(postRepository.findByHashKey(hashKey).orElseThrow());
        return ResponseEntity.ok().build();
    }
}
