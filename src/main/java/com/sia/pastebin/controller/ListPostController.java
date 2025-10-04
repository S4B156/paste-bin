package com.sia.pastebin.controller;

import com.sia.pastebin.DTO.PostDto;
import com.sia.pastebin.entities.User;
import com.sia.pastebin.repository.PostRepository;
import com.sia.pastebin.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;
@Controller
public class ListPostController {
    @Autowired
    private PostRepository postRepository;
    @GetMapping("/myPosts")
    public String viewPost(@AuthenticationPrincipal User user, Model model) throws IOException {
        model.addAttribute("posts", postRepository.findByUser(user));
        return "listPosts";
    }
}
