package com.cs525.reversi.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;

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
    
    public void ab() {
    	
    }
}
