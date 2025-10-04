package com.sia.pastebin.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDto {
    @NotBlank(message = "The input field is blank")
    @Size(min = 30, message = "Post must be at least 30 characters long")
    private String content;
    private LocalDateTime expirationTime;
    private String password;

    public PostDto(String content) {
        this.content = content;
    }
}
