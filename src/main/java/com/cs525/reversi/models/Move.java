package com.cs525.reversi.models;

import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.cs525.reversi.req.CellLocation;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@Data
@NoArgsConstructor
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

    private int roww;

    private int col;
    
    @ElementCollection
    private List<CellLocation> cellsToFlip;
    
}
