package com.cs525.reversi.resp;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.cs525.reversi.models.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameResponse{
	private UUID uuid;
	private User player1;
	private User player2;
	private Date createdAt;
	private Date updatedAt;
	private GameStatus status;
    private List<List<CellValue>> board;
	private Player winner;
	private int lastMoveId;
}
