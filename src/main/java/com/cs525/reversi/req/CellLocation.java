package com.cs525.reversi.req;

import lombok.*;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class CellLocation {
    @NonNull
    private int row;
    @NonNull
    private int col;
}
