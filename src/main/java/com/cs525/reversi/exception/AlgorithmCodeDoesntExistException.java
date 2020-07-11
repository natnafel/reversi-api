package com.cs525.reversi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AlgorithmCodeDoesntExistException extends ReversiException {
    public AlgorithmCodeDoesntExistException(String algorithmCode) {
        super("Algorithm code %s does not exist");
    }
}
