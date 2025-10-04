package com.sia.pastebin.controller;

import com.sia.pastebin.DTO.RegistraitionForm;
import com.sia.pastebin.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/register")
public class RegisterController {
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UserRepository userRepository;
    @GetMapping
    public String loginPage(){
        return "register";
    }
    @PostMapping
    public String registerUser(RegistraitionForm registraitionForm){
        userRepository.save(registraitionForm.toUser(passwordEncoder));
        return "redirect:/login";
    }
}
