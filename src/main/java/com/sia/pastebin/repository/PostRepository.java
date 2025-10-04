package com.sia.pastebin.repository;

import com.sia.pastebin.entities.Post;
import com.sia.pastebin.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends CrudRepository<Post, Long> {
    public Optional<Post> findByHashKey(String hashKey);
    public List<Post> findByExpirationTimeBefore(LocalDateTime localDateTime);
    public List<Post> findByUser(User user);
}
