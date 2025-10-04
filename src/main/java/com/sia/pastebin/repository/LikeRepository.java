package com.sia.pastebin.repository;

import com.sia.pastebin.entities.Like;
import com.sia.pastebin.entities.Post;
import com.sia.pastebin.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends CrudRepository<Like, Long> {
    Optional<Like> findByPostAndUser(Post post, User user);
    long countByPost(Post post);
}
