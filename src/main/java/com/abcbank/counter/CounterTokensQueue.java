package com.abcbank.counter;

import com.abcbank.token.Token;
import com.abcbank.token.TokenRepository;
import com.abcbank.token.TokenStatus;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import static com.abcbank.token.TokenStatus.COMPLETED;

@Service
@AllArgsConstructor
public class CounterTokensQueue {

    private Map<Counter, Queue<Token>> counterTokensMap = new HashMap<>();

    @Autowired
    private TokenRepository tokenRepository;

    void addToCounterQueue(Counter counter, Token token) {
        Queue<Token> counterQueue = counterTokensMap.getOrDefault(counter, new LinkedList<>());
        counterQueue.add(token);
        counterTokensMap.put(counter, counterQueue);
    }

    public Queue<Token> getTokensAssignedForCounter(Counter counter) {
        return counterTokensMap.getOrDefault(counter, new LinkedList<>());
    }

    public Token getNextTokenToServeAssignedForCounter(Counter counter) {
        return counterTokensMap.getOrDefault(counter, new LinkedList<>()).peek();
    }

    public void setTokenStatus(Token token, TokenStatus tokenStatus) throws Exception {
        if (token.getPendingRequestSize() > 1 && tokenStatus == COMPLETED) {
            throw new Exception("Cannot mark token status to COMPLETED without serving all customer requests");
        }

        token.markLastRequestWithStatus(tokenStatus);
    }
}
