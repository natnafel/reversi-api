package com.cs525.reversi.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AwayGameRequest {
    private String address;
    private int port;
    private String protocol;
    private String algorithm;
    private boolean makeFirstMove;
}
