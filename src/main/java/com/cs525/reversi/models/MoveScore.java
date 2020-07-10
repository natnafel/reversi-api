package com.cs525.reversi.models;

import com.cs525.reversi.req.CellLocation;
import lombok.Data;
import lombok.NonNull;

import java.util.List;

@Data
public class MoveScore {
    @NonNull
    private CellLocation cellLocation;
    @NonNull
    private List<CellLocation> cellsToFlip;
}
