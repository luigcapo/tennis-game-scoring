package com.tenniscorp.model;

import lombok.Data;

@Data
public class TennisGame {

    private TennisScore scoreA;
    private TennisScore scoreB;

    public TennisGame() {
        this(TennisScore.LOVE, TennisScore.LOVE);
    }


    public TennisGame(TennisScore startA, TennisScore startB) {
        this.scoreA = startA;
        this.scoreB = startB;
    }

}
