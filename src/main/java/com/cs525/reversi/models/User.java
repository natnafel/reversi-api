package com.cs525.reversi.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Type(type = "uuid-char")
    @Column( nullable = false)
    @NonNull
    private UUID uuid;

    @Column( nullable = false)
    @NonNull
    private String username;
}

