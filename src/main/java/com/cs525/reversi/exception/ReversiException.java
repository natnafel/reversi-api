package com.cs525.reversi.exception;

import java.util.logging.Logger;

public class ReversiException extends RuntimeException {
    private final Logger logger = Logger.getLogger(this.getClass().getSimpleName());
    public ReversiException(String message){
        super(message);
        logger.warning(message);
    }
}
