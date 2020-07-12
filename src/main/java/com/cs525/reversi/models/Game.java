package com.cs525.reversi.models;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@Data
@NoArgsConstructor
public class Game {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Type(type = "uuid-char")
	@Column( nullable = false)
	@NonNull
	private UUID uuid;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@NonNull
	private User player1;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@NonNull
	private User player2;

	@NonNull
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date createdAt;

	@NonNull
	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date updatedAt;

	@Enumerated(EnumType.STRING)
	@NonNull
	@Column(nullable = false)
	private GameStatus status;

	@OneToMany(cascade = CascadeType.ALL)
	@OrderBy("id")
    private List<MatrixRow> rows;	

	public void changeCellValue (Integer cellRow , Integer cellCol , CellValue value) {
		rows.get(cellRow).getCells().get(cellCol).setCellValue(value);
	}
	
	public void setDefaultCells() {
		 this.changeCellValue(3, 3, CellValue.BLACK);
		 this.changeCellValue(4, 4, CellValue.BLACK);
		 this.changeCellValue(3, 4, CellValue.WHITE);
		 this.changeCellValue(4, 3, CellValue.WHITE);
	}
	// nullable
	@Enumerated(EnumType.STRING)
	private Player winner;
}
