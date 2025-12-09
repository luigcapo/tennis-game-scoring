package com.tenniscorp.service;

import com.tenniscorp.model.Player;
import com.tenniscorp.model.TennisGame;
import com.tenniscorp.model.TennisScore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TennisGameService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TennisGameService.class);

    public void playGame(TennisGame game, Player pointWinnerPlayer) {

        if (isGameFinished(game)) {
            LOGGER.warn("Point ignoré : la partie est déjà terminée (A={}, B={}).",
                    game.getScoreA(), game.getScoreB());
            return;
        }

        TennisScore currentScore = getScoreOf(game, pointWinnerPlayer);
        switch (currentScore) {
            case LOVE, FIFTEEN, THIRTY -> incrementStandardScore(game, pointWinnerPlayer, currentScore);
            case FORTY                 -> handleFortyScenario(game, pointWinnerPlayer);
            case ADVANTAGE             -> setScore(game, pointWinnerPlayer, TennisScore.WIN);
            case WIN                   ->
                    throw new IllegalStateException("Impossible state: game already finished.");

        }
    }


    private void incrementStandardScore(TennisGame game, Player player, TennisScore currentScore) {
        int nextIndex = currentScore.ordinal() + 1;
        setScore(game, player, TennisScore.values()[nextIndex]);
    }

    private void handleFortyScenario(TennisGame game, Player player) {
        TennisScore opponentScore = getScoreOf(game, player.opponent());

        switch (opponentScore) {
            case ADVANTAGE ->
                    setScore(game, player.opponent(), TennisScore.FORTY);
            case FORTY ->
                    setScore(game, player, TennisScore.ADVANTAGE);
            case LOVE, FIFTEEN, THIRTY ->
                    setScore(game, player, TennisScore.WIN);
            case WIN ->
                    throw new IllegalStateException("Impossible state: game already finished.");
        }
    }

    private TennisScore getScoreOf(TennisGame game, Player player) {
        return (player == Player.PLAYER_A) ? game.getScoreA() : game.getScoreB();
    }

    private void setScore(TennisGame game, Player player, TennisScore newScore) {
        if (player == Player.PLAYER_A) {
            game.setScoreA(newScore);
        } else {
            game.setScoreB(newScore);
        }
    }

    private boolean isGameFinished(TennisGame game) {
        return game.getScoreA() == TennisScore.WIN || game.getScoreB() == TennisScore.WIN;
    }

}