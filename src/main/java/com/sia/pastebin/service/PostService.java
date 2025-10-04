package com.sia.pastebin.service;

import com.sia.pastebin.DTO.PostDto;
import com.sia.pastebin.HashGenerator;
import com.sia.pastebin.amazonAws.AWSService;
import com.sia.pastebin.entities.Post;
import com.sia.pastebin.entities.User;
import com.sia.pastebin.repository.PostRepository;
import com.sia.pastebin.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
public class PostService {
    @Autowired
    AWSService awsService;
    @Autowired
    PostRepository postRepository;
    @Autowired
    HashGenerator hashGenerator;
    @Autowired
    UserRepository userRepository;

    @Transactional
    public String createNewPost(PostDto postDto, Long userId) throws IOException {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        log.info("Creating new post for user: {}", user.getUsername());
        // Тут заливает фалй на s3
        String content = postDto.getContent();
        String key = awsService.uploadFile(content);


        log.info("New post with key: {}", key);
        // гененирурем хэш ключ
        String hashKey = hashGenerator.generateHash(key);
        Post post = new Post(key, hashKey, postDto.getExpirationTime(), user, postDto.getPassword());

        user.getPosts().add(post);
        postRepository.save(post);

        log.info("Post with key: {} and hashKey: {} created successfully", key, hashKey);

        return hashKey;
    }

    public String findPost(String hashKey) throws IOException {
        Optional<Post> s3Object = postRepository.findByHashKey(hashKey);
        if(s3Object.isEmpty()) {
            return null;
        }
        Post object = s3Object.get();
        if(object.getExpirationTime().isBefore(LocalDateTime.now())){
            deletePost(object);
            return null;
        }
        return awsService.download(object.getKey());
    }

    public String getContent(Post post) throws IOException{
        if(post.getExpirationTime().isBefore(LocalDateTime.now())){
            deletePost(post);
            return null;
        }
        return awsService.download(post.getKey());
    }

    @Transactional
    public void deletePost(Post post) throws IOException{
        awsService.deleteObject(post.getKey());
        postRepository.delete(post);
        log.info("Post with key: {} deleted successfully", post.getKey());
    }
}
