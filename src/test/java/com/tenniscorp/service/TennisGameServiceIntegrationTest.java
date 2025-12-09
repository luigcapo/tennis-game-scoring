package com.tenniscorp.service;

import com.tenniscorp.model.Player;
import com.tenniscorp.model.TennisGame;
import com.tenniscorp.model.TennisScore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("TennisGameService - Tests d'Intégration")
class TennisGameServiceIntegrationTest {

    private TennisGameService service;

    @BeforeEach
    void setUp() {
        service = new TennisGameService();
    }

    @ParameterizedTest(name = "Séquence ''{0}'' -> Score A: {1}, Score B: {2}")
    @CsvSource({
            "AAAA,         WIN,       LOVE",
            "BBBB,         LOVE,      WIN",
            "AAA,          FORTY,     LOVE",
            "BB,           LOVE,      THIRTY",
            "AABA,         FORTY,     FIFTEEN",
            "ABAB,         THIRTY,    THIRTY",
            "ABABAB,       FORTY,     FORTY",
            "ABABABA,      ADVANTAGE, FORTY",
            "ABABABAB,     FORTY,     FORTY",
            "ABABABAA,     WIN,       FORTY",
            "ABABABABABAA, WIN,       FORTY",
            "BABABABB,     FORTY,     WIN",
            "'',           LOVE,      LOVE",
            "AAAABBBBAB,   WIN,       LOVE"
    })
    void should_reach_expected_score_for_sequence(String sequence, TennisScore expectedA, TennisScore expectedB) {
        TennisGame game = playSequence(sequence);

        assertScores(game, expectedA, expectedB);
    }


    // --- 3. HELPER METHODS (Private) ---

    private void assertScores(TennisGame game, TennisScore expectedA, TennisScore expectedB) {
        assertThat(game.getScoreA())
                .as("Vérification Score Joueur A")
                .isEqualTo(expectedA);
        assertThat(game.getScoreB())
                .as("Vérification Score Joueur B")
                .isEqualTo(expectedB);
    }

    private TennisGame playSequence(String sequence) {
        TennisGame game = new TennisGame();
        if (sequence == null || sequence.isEmpty()) {
            return game;
        }

        for (char c : sequence.toCharArray()) {
            service.playGame(game, Player.fromChar(c));
        }
        return game;
    }
}