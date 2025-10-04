package com.sia.pastebin.controller;

import com.sia.pastebin.DTO.PostDto;
import com.sia.pastebin.entities.User;
import com.sia.pastebin.service.PostService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@RequestMapping("/post")
@Slf4j
public class CreatePostController {
    @Autowired
    PostService postService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public String homePage(Model model){
        model.addAttribute("post", new PostDto("# Caption\n" +
                "- **Bold text**\n" +
                "- *Italics*\n" +
                "- [Reference](https://example.com)"));
        return "create_post";
    }

    @PostMapping("/create")
    public String createPost(@Valid @ModelAttribute("post") PostDto post,
                             BindingResult bindingResult,
                             Model model,
                             RedirectAttributes redirectAttributes,
                             @AuthenticationPrincipal User user) throws IOException {
        if(bindingResult.hasErrors()){
            model.addAttribute("post", post);
            model.addAttribute("message", bindingResult.getFieldError().getDefaultMessage());
            return "home";
        }
        if(post.getPassword() != null){post.setPassword(passwordEncoder.encode(post.getPassword()));}
        String hashKey = postService.createNewPost(post, user.getId());
        redirectAttributes.addFlashAttribute("message", "Successfully");
        return "redirect:/post/" + hashKey;
    }
}
