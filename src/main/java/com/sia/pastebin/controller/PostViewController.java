package com.sia.pastebin.controller;

import com.sia.pastebin.DTO.CheckRequestDto;
import com.sia.pastebin.DTO.PostDto;
import com.sia.pastebin.entities.Comment;
import com.sia.pastebin.entities.Like;
import com.sia.pastebin.entities.Post;
import com.sia.pastebin.entities.User;
import com.sia.pastebin.repository.CommentRepository;
import com.sia.pastebin.repository.LikeRepository;
import com.sia.pastebin.repository.PostRepository;
import com.sia.pastebin.service.PostService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.Optional;

@Controller
@RequestMapping("/post")
@RequiredArgsConstructor
@SessionAttributes("post")
@Slf4j
public class PostViewController {
    @Autowired
    private PostService postService;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private LikeRepository likeRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @ModelAttribute("post")
    public Post getPost(@PathVariable String hashKey) {
        return postRepository.findByHashKey(hashKey).orElseThrow();
    }

    @GetMapping("/{hashKey}")
    public String viewPost(@PathVariable String hashKey, Model model, HttpSession httpSession) throws IOException {
        Post post = postRepository.findByHashKey(hashKey).orElseThrow();
        String content = postService.getContent(post);
        if(content == null){
            model.addAttribute("post", new PostDto(""));
            model.addAttribute("message", "Message not found, link invalid or link expired");
            return "home";
        }
        long likeCount = likeRepository.countByPost(post);
        if(post.getPassword() == null){
            httpSession.setAttribute("access_" + hashKey, true);
        }
        model.addAttribute("post", post);
        model.addAttribute("likeCount", likeCount);
        model.addAttribute("comments", post.getComments());
        model.addAttribute("newComment", new Comment());
        model.addAttribute("content", content);
        return "view_post";
    }


    @PostMapping("/{hashKey}/comment")
    public String addComment(@ModelAttribute("post") Post post, @ModelAttribute Comment comment, @AuthenticationPrincipal User user) {
        comment.setPost(post);
        comment.setUser(user);
        commentRepository.save(comment);
        return "redirect:/post/" + post.getHashKey();
    }

    @GetMapping("/{hashKey}/like")
    @ResponseBody
    public ResponseEntity<?> likePost(@ModelAttribute("post") Post post, @AuthenticationPrincipal User user) {

        Optional<Like> existingLike = likeRepository.findByPostAndUser(post, user);
        if (existingLike.isPresent()) {
                    log.info("{} removed the likes under the post - {}", user.getUsername(), post.getHashKey());
            likeRepository.delete(existingLike.get());
        } else {
            log.info("{} liked the post - {}", user.getUsername(), post.getHashKey());
            Like like = new Like();
            like.setPost(post);
            like.setUser(user);
            likeRepository.save(like);
        }

        long likeCount = likeRepository.countByPost(post);
        return ResponseEntity.ok(likeCount);
    }

    @PostMapping("/check-pass")
    public String checkPassword(@ModelAttribute("check") CheckRequestDto checkRequestDto, HttpSession httpSession,
                                RedirectAttributes redirectAttributes){
        String hashKey = checkRequestDto.getHashKey();
        Post post = postRepository.findByHashKey(hashKey).orElseThrow(() -> new RuntimeException("Post not found"));

        if(passwordEncoder.matches(checkRequestDto.getPassword(), post.getPassword())){
            httpSession.setAttribute("access_" + hashKey, true);
            return "redirect:/post/" + hashKey;
        } else {
            redirectAttributes.addFlashAttribute("error", "Incorrect password");
            redirectAttributes.addFlashAttribute("check", checkRequestDto);
        }
        return "redirect:/post/" + hashKey;
    }
//    @PostMapping("/verify-password")
//    public ResponseEntity<?> checkPassword(@RequestParam String hashKey, @RequestParam String password){
//        boolean isValid = postRepository.findByHashKey(hashKey).orElseThrow().getPassword().equals(password);
//        return ResponseEntity.ok(Collections.singletonMap("success", isValid));
//    }
}


