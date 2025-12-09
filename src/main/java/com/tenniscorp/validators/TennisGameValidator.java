package com.tenniscorp.validators;

public class TennisGameValidator {

    private static final int MAX_INPUT_LENGTH = 500;
    private static final String VALID_GAME_PATTERN = "^[AB]+$";

    public void validate(String input) {
        if (input == null || input.isEmpty()) {
            throw new IllegalArgumentException("La séquence de jeu ne peut pas être vide.");
        }

        if (input.length() > MAX_INPUT_LENGTH) {
            throw new SecurityException("Overflow: Input dépasse la limite autorisée (" + MAX_INPUT_LENGTH + ")");
        }

        if (!input.matches(VALID_GAME_PATTERN)) {
            throw new IllegalArgumentException("Format Invalide: Caractères non autorisés détectés. Attendu: A ou B");
        }
    }
}
