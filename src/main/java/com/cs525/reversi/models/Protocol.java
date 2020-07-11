package com.cs525.reversi.models;

public enum Protocol {
    G3REST("Group 3 Rest API", "G3R"),
    G1REST("Group 1 Rest API", "G1R"),
    G2REST("Group 2 Rest API", "G2R"),
    G4SOCKET("Group 4 Socket", "G4S"),
    G5SOCKET("Group 5 Socket", "G5S"),
    G6SOCKET("Group 6 Socket", "G6S");

    private final String name;
    private final String code;

    Protocol(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
