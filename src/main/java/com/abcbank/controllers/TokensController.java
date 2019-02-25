package com.abcbank.controllers;

import com.abcbank.token.Token;
import com.abcbank.token.TokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tokens")
@AllArgsConstructor
public class TokensController {

    @Autowired
    private TokenRepository tokenRepository;

    @GetMapping("/{id}")
    public Token getToken(@PathVariable("id") Integer tokenId) {
        return tokenRepository.getToken(tokenId);
    }

    @GetMapping
    public List<Token> getAllTokens() {
        return tokenRepository.getAllTokens();
    }
}
