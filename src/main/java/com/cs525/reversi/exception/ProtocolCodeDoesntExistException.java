package com.cs525.reversi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ProtocolCodeDoesntExistException extends ReversiException {
    public ProtocolCodeDoesntExistException(String protocolCode) {
        super(String.format("Protocol code %s doesn't exist", protocolCode));
    }
}
