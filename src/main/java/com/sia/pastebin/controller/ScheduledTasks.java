package com.sia.pastebin.controller;

import com.sia.pastebin.entities.Post;
import com.sia.pastebin.repository.PostRepository;
import com.sia.pastebin.service.PostService;
import jakarta.transaction.Transactional;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class ScheduledTasks {
    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private PostService postService;

    @Scheduled(fixedDelay = 60000)
    @Transactional
    public void reportCurrentTime(){
        List<Post> obsoleteObjects = postRepository.findByExpirationTimeBefore(LocalDateTime.now());
        if(!obsoleteObjects.isEmpty()){
            for(Post post : obsoleteObjects){
                log.info("Subject {} overstayed his time and was removed", post.getKey());
                try {
                    postService.deletePost(post);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        } else{
            log.info("Verification: no obsolete objects");
        }
    }
}
