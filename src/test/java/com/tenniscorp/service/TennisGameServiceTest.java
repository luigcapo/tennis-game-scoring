package com.tenniscorp.service;

import com.tenniscorp.model.Player;
import com.tenniscorp.model.TennisGame;
import com.tenniscorp.model.TennisScore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("TennisGameService - Tests Unitaires")
class TennisGameServiceTest {

    private TennisGameService service;
    private TennisGame game;

    @BeforeEach
    void setUp() {
        service = new TennisGameService();
        game = new TennisGame();
    }

    @Nested
    @DisplayName("Scénarios standard (0-15-30)")
    class StandardScoreTests {

        @Test
        @DisplayName("LOVE -> FIFTEEN quand le joueur A marque")
        void shouldIncrementFromLoveToFifteen_PlayerA() {
            service.playGame(game, Player.PLAYER_A);

            assertThat(game.getScoreA()).isEqualTo(TennisScore.FIFTEEN);
            assertThat(game.getScoreB()).isEqualTo(TennisScore.LOVE);
        }

        @Test
        @DisplayName("LOVE -> FIFTEEN quand le joueur B marque")
        void shouldIncrementFromLoveToFifteen_PlayerB() {
            service.playGame(game, Player.PLAYER_B);

            assertThat(game.getScoreA()).isEqualTo(TennisScore.LOVE);
            assertThat(game.getScoreB()).isEqualTo(TennisScore.FIFTEEN);
        }

        @Test
        @DisplayName("FIFTEEN -> THIRTY quand le joueur A marque")
        void shouldIncrementFromFifteenToThirty() {
            game.setScoreA(TennisScore.FIFTEEN);

            service.playGame(game, Player.PLAYER_A);

            assertThat(game.getScoreA()).isEqualTo(TennisScore.THIRTY);
        }

        @Test
        @DisplayName("THIRTY -> FORTY quand le joueur B marque")
        void shouldIncrementFromThirtyToForty() {
            game.setScoreB(TennisScore.THIRTY);

            service.playGame(game, Player.PLAYER_B);

            assertThat(game.getScoreB()).isEqualTo(TennisScore.FORTY);
        }
    }

    @Nested
    @DisplayName("Scénarios avec FORTY")
    class FortyScenarioTests {

        @Test
        @DisplayName("FORTY vs LOVE -> WIN")
        void shouldWinFromFortyVsLove_WhenPlayerAHasFortyAndWinsThePoint() {
            game.setScoreA(TennisScore.FORTY);
            game.setScoreB(TennisScore.LOVE);

            service.playGame(game, Player.PLAYER_A);

            assertThat(game.getScoreA()).isEqualTo(TennisScore.WIN);
            assertThat(game.getScoreB()).isEqualTo(TennisScore.LOVE);
        }

        @Test
        @DisplayName("FORTY vs THIRTY -> WIN")
        void shouldWinFromFortyVsThirty_WhenPlayerAHasFortyAndWinsThePoint() {
            game.setScoreA(TennisScore.FORTY);
            game.setScoreB(TennisScore.THIRTY);

            service.playGame(game, Player.PLAYER_A);

            assertThat(game.getScoreA()).isEqualTo(TennisScore.WIN);
        }

        @Test
        @DisplayName("FORTY vs FORTY (Deuce) -> ADVANTAGE")
        void shouldGetAdvantageFromDeuce_WhenPlayerAWinthePoint() {
            game.setScoreA(TennisScore.FORTY);
            game.setScoreB(TennisScore.FORTY);

            service.playGame(game, Player.PLAYER_A);

            assertThat(game.getScoreA()).isEqualTo(TennisScore.ADVANTAGE);
            assertThat(game.getScoreB()).isEqualTo(TennisScore.FORTY);
        }

        @Test
        @DisplayName("Joueur B marque depuis Deuce -> ADVANTAGE pour B")
        void shouldGetAdvantageForPlayerB_WhenPlayerBWinthePoint() {
            game.setScoreA(TennisScore.FORTY);
            game.setScoreB(TennisScore.FORTY);

            service.playGame(game, Player.PLAYER_B);

            assertThat(game.getScoreA()).isEqualTo(TennisScore.FORTY);
            assertThat(game.getScoreB()).isEqualTo(TennisScore.ADVANTAGE);
        }

        @Test
        @DisplayName("FORTY vs ADVANTAGE -> Retour à Deuce (FORTY vs FORTY)")
        void shouldReturnToDeuceFromAdvantage_whenPlayerAHasAdvantageAndPlayerBWinThePoint() {
            game.setScoreA(TennisScore.ADVANTAGE);
            game.setScoreB(TennisScore.FORTY);

            service.playGame(game, Player.PLAYER_B);

            assertThat(game.getScoreA()).isEqualTo(TennisScore.FORTY);
            assertThat(game.getScoreB()).isEqualTo(TennisScore.FORTY);
        }
    }

    @Nested
    @DisplayName("Scénarios avec ADVANTAGE")
    class AdvantageScenarioTests {

        @Test
        @DisplayName("Joueur A gagne depuis ADVANTAGE")
        void shouldWinFromAdvantage() {
            game.setScoreA(TennisScore.ADVANTAGE);
            game.setScoreB(TennisScore.FORTY);

            service.playGame(game, Player.PLAYER_A);

            assertThat(game.getScoreA()).isEqualTo(TennisScore.WIN);
            assertThat(game.getScoreB()).isEqualTo(TennisScore.FORTY);
        }

        @Test
        @DisplayName("Joueur B gagne depuis ADVANTAGE")
        void shouldWinFromAdvantage_PlayerB() {
            game.setScoreA(TennisScore.FORTY);
            game.setScoreB(TennisScore.ADVANTAGE);

            service.playGame(game, Player.PLAYER_B);

            assertThat(game.getScoreA()).isEqualTo(TennisScore.FORTY);
            assertThat(game.getScoreB()).isEqualTo(TennisScore.WIN);
        }
    }

    @Test
    @DisplayName("Jouer après WIN ne change rien")
    void shouldDoNothingWhenPlayerAlreadyWon() {
        game.setScoreA(TennisScore.WIN);
        game.setScoreB(TennisScore.FORTY);

        service.playGame(game, Player.PLAYER_A);

        assertThat(game.getScoreA()).isEqualTo(TennisScore.WIN);
        assertThat(game.getScoreB()).isEqualTo(TennisScore.FORTY);

        service.playGame(game, Player.PLAYER_B);

        assertThat(game.getScoreA()).isEqualTo(TennisScore.WIN);
        assertThat(game.getScoreB()).isEqualTo(TennisScore.FORTY);
    }

    @Nested
    @DisplayName("Tests de régression et cas limites")
    class EdgeCaseTests {

        @Test
        @DisplayName("Partie complète rapide : 4 points consécutifs pour A")
        void shouldWinQuickly_FourPointsInRow() {
            service.playGame(game, Player.PLAYER_A); // A: 15-0
            service.playGame(game, Player.PLAYER_A); // A: 30-0
            service.playGame(game, Player.PLAYER_A); // A: 40-0
            service.playGame(game, Player.PLAYER_A); // A: WIN

            assertThat(game.getScoreA()).isEqualTo(TennisScore.WIN);
            assertThat(game.getScoreB()).isEqualTo(TennisScore.LOVE);
        }

        @Test
        @DisplayName("Alternance de points menant au Deuce")
        void shouldReachDeuce() {
            service.playGame(game, Player.PLAYER_A); // 15-0
            service.playGame(game, Player.PLAYER_B); // 15-15
            service.playGame(game, Player.PLAYER_A); // 30-15
            service.playGame(game, Player.PLAYER_B); // 30-30
            service.playGame(game, Player.PLAYER_A); // 40-30
            service.playGame(game, Player.PLAYER_B); // 40-40 (Deuce)

            assertThat(game.getScoreA()).isEqualTo(TennisScore.FORTY);
            assertThat(game.getScoreB()).isEqualTo(TennisScore.FORTY);
        }

        @Test
        @DisplayName("Multiples Deuce/Advantage avant victoire")
        void shouldHandleMultipleDeuces() {
            game.setScoreA(TennisScore.FORTY);
            game.setScoreB(TennisScore.FORTY);

            service.playGame(game, Player.PLAYER_A); // A: ADV-40
            assertThat(game.getScoreA()).isEqualTo(TennisScore.ADVANTAGE);

            service.playGame(game, Player.PLAYER_B); // 40-40 (Deuce)
            assertThat(game.getScoreA()).isEqualTo(TennisScore.FORTY);
            assertThat(game.getScoreB()).isEqualTo(TennisScore.FORTY);

            service.playGame(game, Player.PLAYER_B); // 40-ADV
            assertThat(game.getScoreB()).isEqualTo(TennisScore.ADVANTAGE);

            service.playGame(game, Player.PLAYER_A); // 40-40 (Deuce)
            assertThat(game.getScoreA()).isEqualTo(TennisScore.FORTY);
            assertThat(game.getScoreB()).isEqualTo(TennisScore.FORTY);

            service.playGame(game, Player.PLAYER_A); // A: ADV-40
            service.playGame(game, Player.PLAYER_A); // A: WIN
            assertThat(game.getScoreA()).isEqualTo(TennisScore.WIN);
        }
    }
}