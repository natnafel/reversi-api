package com.cs525.reversi.models;

import lombok.Data;
import lombok.NonNull;

@Data
public class MovePoint {
    @NonNull
    private int row;
    @NonNull
    private int col;
    @NonNull
    private int points;
}
