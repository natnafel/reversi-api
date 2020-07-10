package com.cs525.reversi.resp;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Info {
    private ResponseStatus status;
    private String message;
}
