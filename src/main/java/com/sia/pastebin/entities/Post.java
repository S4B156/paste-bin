package com.sia.pastebin.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Table(name = "post")
@Entity
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Post {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    Long id;

    @Column(name = "object_key")
    String key;

    @Column(name = "hash_key")
    String hashKey;

    @Column(name = "password", nullable = true)
    String password;

//    @Column(name = "passworded")
//    boolean passworded;

    @Column(name = "expiration_time")
    LocalDateTime expirationTime;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Like> likes = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Comment> comments = new ArrayList<>();

    public Post(String key, String hashKey, LocalDateTime expirationTime, User user, String password) {
        this.key = key;
        this.hashKey = hashKey;
        this.expirationTime = expirationTime;
        this.user = user;
        this.password = password;
    }
}
