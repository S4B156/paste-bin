package com.sia.pastebin.DTO;

import com.sia.pastebin.entities.User;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Data
public class RegistraitionForm {
    String username;
    String email;
    String password;

    public User toUser(PasswordEncoder encoder){
        return new User(username, email, encoder.encode(password));
    }
}
