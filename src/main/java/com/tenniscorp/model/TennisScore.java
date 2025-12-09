package com.tenniscorp.model;

public enum TennisScore {
    LOVE("0"),
    FIFTEEN("15"),
    THIRTY("30"),
    FORTY("40"),
    ADVANTAGE("Advantage"),
    WIN("Win");

    private final String score;

    TennisScore(String score) {
        this.score = score;
    }

    public String getScore() {
        return score;
    }
}
