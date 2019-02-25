package com.abcbank.token;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TokenRepository {

    private static int tokenNo = 0;
    private static final Map<Integer, Token> tokenHashMap = new HashMap<>();

    public Token getToken(Integer tokenId) {
        return tokenHashMap.get(tokenId);
    }

    public Token getNewToken() {
        Token token = new Token();
        token.setId(++tokenNo);
        tokenHashMap.put(tokenNo, token);
        return token;
    }

    public List<Token> getAllTokens() {
        return new ArrayList<>(tokenHashMap.values());
    }
}
