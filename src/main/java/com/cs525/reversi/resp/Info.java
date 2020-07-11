package com.cs525.reversi.resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Info {
    private ResponseStatus status;
    private String message;

}
