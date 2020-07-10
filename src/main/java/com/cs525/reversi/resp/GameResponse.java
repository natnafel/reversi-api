package com.cs525.reversi.resp;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import com.cs525.reversi.models.GameStatus;
import com.cs525.reversi.models.MatrixRow;
import com.cs525.reversi.models.Player;
import com.cs525.reversi.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameResponse{
	private int id;
	private UUID uuid;
	private User player1;
	private User player2;
	private Date createdAt;
	private Date updatedAt;
	private GameStatus status;
    private List<MatrixRow> rows;	
	private Player winner;
	private int moveId;
}
