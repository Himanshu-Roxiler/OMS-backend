package com.roxiler.erp.service;

import java.nio.CharBuffer;
import java.util.Optional;

import com.roxiler.erp.dto.auth.CredentialsDto;
import com.roxiler.erp.dto.auth.UserDto;
import com.roxiler.erp.model.Users;
import com.roxiler.erp.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final PasswordEncoder passwordEncoder;

    @Autowired
    private UsersRepository usersRepository;

    public AuthenticationService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public UserDto authenticate(CredentialsDto credentialsDto) {
        Users user = usersRepository.readByEmail(credentialsDto.getEmail());
        //String encodedMasterPassword = passwordEncoder.encode(CharBuffer.wrap("the-password"));
        if (passwordEncoder.matches(credentialsDto.getPassword(), user.getPassword())) {
            return new UserDto(user.getId(), user.getUsername(), user.getEmail(), "login", "token");
        }
        throw new RuntimeException("Invalid password");
    }

    public UserDto findByLogin(String login) {
        System.out.printf("LOGIN : " + login );
        Integer userId = 1;
        Optional<Users> user = usersRepository.findById(userId);
        if ("login".equals(login) && user.isPresent()) {
            return new UserDto(user.get().getId(), user.get().getUsername(), user.get().getEmail(), "login", "token");
        }
        throw new RuntimeException("Invalid login");
    }
}
