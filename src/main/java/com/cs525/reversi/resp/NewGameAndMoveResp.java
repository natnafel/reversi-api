package com.cs525.reversi.resp;

import com.cs525.reversi.models.CellValue;
import com.cs525.reversi.req.CellLocation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewGameAndMoveResp {
    private Info info;
    private UUID gameId;
    private int homeTotalScore;
    private int awayTotalScore;
    private CellLocation homeNewPiece;
    private List<List<CellValue>> board;
}
