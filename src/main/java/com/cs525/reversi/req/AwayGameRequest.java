package com.cs525.reversi.req;

import com.cs525.reversi.models.AlgorithmType;
import com.cs525.reversi.models.Protocol;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AwayGameRequest {
    private String address;
    private int port;
    private Protocol protocol;
    private AlgorithmType algorithm;
    private boolean makeFirstMove;
}
