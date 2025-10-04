package com.sia.pastebin.repository;

import com.sia.pastebin.entities.Comment;
import com.sia.pastebin.entities.Post;
import com.sia.pastebin.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends CrudRepository<Comment, Long> {
    List<Comment> findByPost(Post post);
}
