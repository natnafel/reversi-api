package com.cs525.reversi.models;

import com.cs525.reversi.req.CellLocation;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class Move {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NonNull
    @ManyToOne(optional = false)
    private Game game;

    @NonNull
    @ManyToOne(optional = false)
    private User player;

    @NonNull
    private int roww;

    @NonNull
    private int col;
    
    @ElementCollection
    @NonNull
    private List<CellLocation> cellsToFlip;
    
}
