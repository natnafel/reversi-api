package com.cs525.reversi.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

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
    @Column(nullable = false)
    private Date createdAt;

    @NonNull
    @Column(nullable = false)
    private Date updatedAt;

    @Enumerated(EnumType.STRING)
    @NonNull
    @Column(nullable = false)
    private GameStatus status;

    // nullable
    //private Player winner;
}
