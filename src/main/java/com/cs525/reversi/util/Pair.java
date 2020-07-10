package com.cs525.reversi.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Pair<T,U> {
    private final T key;
    private final U value;
}
