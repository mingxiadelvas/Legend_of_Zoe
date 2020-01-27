import java.util.ArrayList;

/**
 * Classe principale du programme.
 *
 * NOTEZ : VOUS NE DEVEZ PAS RENOMMER CETTE CLASSE
 */
public class LegendOfZoe {
    public static void main(String[] args) {
        Game game = new Game(); // Start new game
        Messages.afficherIntro(); // Introduction message

        do {
            Level level = game.getCurrentLevel();
            game.printUI();
            level.printLevel();

            // Play everyone's turn
            for (int i = 0; i < level.getPersonList().size(); i++) {
                Person player = level.getPersonList().get(i);

                if (player instanceof Zoe) {
                    // Play Zoe's turn based on player command
                    Zoe zoe = (Zoe) player;
                    zoe.interact(level, game.getPlayerCmd());

                    // If hexaforce was taken, check if exit is near
                    if (zoe.getHexaforces() == level.getStage()) {
                        game.lookForExit(zoe);
                    }

                } else if (player instanceof Monster) {
                    // Play Monster turn
                    Monster monster = (Monster) player;
                    monster.playTurn(level);
                }

                // Check if any win or loss condition
                if (game.checkConditions() != 0) {
                    // If Zoe won, display win msg else display loss msg
                    switch (game.checkConditions()) {
                        case 1:
                            // Final game screen
                            game.printUI();
                            level.printLevel();
                            Messages.afficherVictoire();
                            break;
                        case -1:
                            Messages.afficherDefaite();
                            break;
                        default:
                            break;
                    }

                    game.setOnGoing(false); // End while loop
                    break; // Exit for loop
                }
            }
        } while (game.getOnGoing());
    }
}