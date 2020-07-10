package com.cs525.reversi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UsernameDoesNotExist extends ReversiException {
    public UsernameDoesNotExist(String username) {
        super(String.format("User with username %s does not exist", username));
    }
}
