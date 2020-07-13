package com.cs525.reversi.req;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
public class CellLocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NonNull
    @Column(name="roww")
    private int row;
    @NonNull
    private int col;
}
