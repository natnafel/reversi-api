package com.cs525.reversi.exception;

import com.cs525.reversi.models.Protocol;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ProtocolNotSupportedException extends ReversiException {
    public ProtocolNotSupportedException(Protocol protocol) {
        super(String.format("Protocol with code %s and name %s not supported", protocol.getCode(), protocol.getName()));
    }
}
