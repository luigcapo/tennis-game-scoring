package com.tenniscorp.model;

import lombok.Getter;

@Getter
public enum Player {
    PLAYER_A('A'),
    PLAYER_B('B');

    private final char symbol;

    Player(char symbol) {
        this.symbol = symbol;
    }

    // Factory Method : Convertit le char en Enum (Gestion d'erreur ici !)
    public static Player fromChar(char symbol) {
        for (Player player : values()) {
            if (player.symbol == symbol) {
                return player;
            }
        }
        throw new IllegalArgumentException("Invalid player symbol: " + symbol);
    }

    // Ça permet d'éviter les "if (isPlayerA) else ..." dans le service
    public Player opponent() {
        return (this == PLAYER_A) ? PLAYER_B : PLAYER_A;
    }
}