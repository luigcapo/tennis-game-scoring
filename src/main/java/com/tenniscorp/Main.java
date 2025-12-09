package com.tenniscorp;

import com.tenniscorp.model.Player;
import com.tenniscorp.model.TennisGame;
import com.tenniscorp.model.TennisScore;
import com.tenniscorp.service.TennisGameService;
import com.tenniscorp.validators.TennisGameValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    private static final int EXIT_ARGUMENT_ERROR = 1;
    private static final int EXIT_SECURITY_ERROR = 2;


    public static void main(String[] args) {

        if (args.length != 1) {
            exitWithInvalidArguments(args.length);
        }

        String input = args[0];
        TennisGameValidator validator = new TennisGameValidator();

        try {
            validator.validate(input);
            LOGGER.info("Démarrage du traitement pour la séquence : {}", input);
            runGame(input);
        } catch (SecurityException e) {
            handleSecurityError(e);
        } catch (IllegalArgumentException e) {
            handleFunctionalError(e);
        }
    }



    private static void exitWithInvalidArguments(int argCount) {
        System.err.println(
                "Erreur : nombre d'arguments invalide. Un seul argument est accepté : la séquence de jeu (ex : ABABAB)."
        );
        LOGGER.warn("Lancement avec {} argument(s) au lieu de 1.", argCount);
        System.exit(EXIT_ARGUMENT_ERROR);
    }

    private static void runGame(String input) {
        TennisGame game = new TennisGame();
        TennisGameService service = new TennisGameService();

        for (char c : input.toCharArray()) {
            if (isGameFinished(game)) {
                LOGGER.info("Jeu terminé. Les caractères restants sont ignorés.");
                break;
            }

            Player player = Player.fromChar(c);
            service.playGame(game, player);
            System.out.println(formatScore(game));
        }
    }


    private static String formatScore(TennisGame game) {
        if (game.getScoreA() == TennisScore.WIN) return "Player A wins the game";
        if (game.getScoreB() == TennisScore.WIN) return "Player B wins the game";

        if (game.getScoreA() == TennisScore.ADVANTAGE) return "Advantage Player A";
        if (game.getScoreB() == TennisScore.ADVANTAGE) return "Advantage Player B";

        if (game.getScoreA() == TennisScore.FORTY && game.getScoreB() == TennisScore.FORTY) {
            return "Deuce";
        }

        return "Player A : " + game.getScoreA().getScore() + " / Player B : " + game.getScoreB().getScore();
    }

    private static boolean isGameFinished(TennisGame game) {
        return game.getScoreA() == TennisScore.WIN || game.getScoreB() == TennisScore.WIN;
    }

    private static void handleSecurityError(SecurityException e) {
        LOGGER.error("ALERTE SÉCURITÉ", e);
        System.err.println("ERREUR DE SÉCURITÉ : " + e.getMessage());
        System.exit(EXIT_SECURITY_ERROR);
    }

    private static void handleFunctionalError(IllegalArgumentException e) {
        LOGGER.error("Input invalide", e);
        System.err.println("Erreur : " + e.getMessage());
        System.exit(EXIT_ARGUMENT_ERROR);
    }
}