package com.cs525.reversi.resp;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Data
@RequiredArgsConstructor
public class AwayGameResponse {
    @NonNull
    private UUID gameId;
}
