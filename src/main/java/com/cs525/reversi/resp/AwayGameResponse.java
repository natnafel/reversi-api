package com.cs525.reversi.resp;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class AwayGameResponse {
    @NonNull
    private UUID gameId;
}
