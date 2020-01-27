import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public class Game {
    private boolean onGoing; // Game is active or not
    private Level currentLevel; // Current level
    private LinkedList<Character> cmdQueue; // Commands from player

    /**
     * Constructor function
     */
    public Game() {
        this.onGoing = true;
        this.currentLevel = new Level(1);
        this.cmdQueue = new LinkedList<Character>();
    }

    /**
     * Creates next level and copies old Zoe values to the new one
     * @param healthPoints Previous Zoe's healthpoints
     * @param hexaforces Previous Zoe's heaxforces
     */
    public void nextLevel(int healthPoints, int hexaforces) {
        // Create new level
        setCurrentLevel(new Level(getCurrentLevel().getStage() + 1));

        // Update new Zoe with old Zoe attributes
        Zoe newZoe = getCurrentLevel().getZoe();
        newZoe.setHealthPoints(healthPoints);
        newZoe.setHexaforces(hexaforces);

        System.out.println("Vous entrez dans le niveau "
            + getCurrentLevel().getStage()
        );
    }

    /**
     * Gets command from player for Zoe's turn
     * @return Char representing user command
     */
    public char getPlayerCmd() {
        Character cmd = getCmdQueue().pollFirst(); // Get latest command

        // If no command stored, get user input
        if (cmd == null) {
            Scanner reader = new Scanner(System.in);
            String input = reader.nextLine(); // Take user input as a string

            // Insert string's characters in cmdQueue
            for (int i = 0; i < input.length(); i++) {
                addToCmdQueue(input.charAt(i));
            }

            return getPlayerCmd();
        } else {
            return cmd;
        }
    }

    /**
     * Returns true if winning condition is met
     * @return boolean signifying victory or not
     */
    private boolean checkWin() {
        // Win if Zoe has 6 hexaforces
        return (getCurrentLevel().getZoe().getHexaforces() == 6);
    }

    /**
     * Returns true if losing condition is met
     * @return boolean signifying lost or not
     */
    private boolean checkLost() {
        // Lose if Zoe's HP is 0 or less
        return (getCurrentLevel().getZoe().getHealthPoints() <= 0);
    }

    /**
     * Checks win and loss conditions, returns 1 for a win, -1 for a loss and
     * 0 for neither
     * @return int
     */
    public int checkConditions() {
        int gameStatus = 0;

        if (checkWin()) {
            gameStatus = 1;
        } else if (checkLost()) {
            gameStatus = -1;
        }

        return gameStatus;
    }

    /**
     * Checks if Exit near Zoe and takes it
     * @param Zoe Zoe object
     */
    public void lookForExit(Zoe zoe) {
        // Get surrounding elements around Zoe (including her tile)
        ArrayList<Element> surroundings = getCurrentLevel()
            .getSurroundings(zoe.getX(), zoe.getY());

        // If one of them is an exit, take it and go to next level
        for (int j = 0; j < surroundings.size(); j++) {
            if (surroundings.get(j).getSymbol() == 'E') {
                nextLevel(zoe.getHealthPoints(), zoe.getHexaforces());
                break;
            }
        }
    }

    /**
     * Prints healthPoints and hexaforce count
     */
    public void printUI() {
        Zoe zoe = getCurrentLevel().getZoe();

        String output = "Zoe: ";

        // Print hearts
        for (int i = 0; i < zoe.getHealthPoints(); i++) {
            output += "\u2661 "; // Full hearts
        }
        for (int i = 0; i < (5 - zoe.getHealthPoints()); i++) {
            output += "_ "; // Missing hearts
        }

        output += "| ";

        // Print hexaforces
        for (int i = 0; i < zoe.getHexaforces(); i++) {
            output += "\u25B3 "; // Full hexaforces
        }
        for (int i = 0; i < (6 - zoe.getHexaforces()); i++) {
            output += "_ "; // Missing hexaforces
        }

        System.out.println(output);
    }

    public boolean getOnGoing() {
        return this.onGoing;
    }

    public void setOnGoing(boolean onGoing) {
        this.onGoing = onGoing;
    }

    public Level getCurrentLevel() {
        return this.currentLevel;
    }

    public void setCurrentLevel(Level currentLevel) {
        this.currentLevel = currentLevel;
    }

    public LinkedList<Character> getCmdQueue() {
        return this.cmdQueue;
    }

    public void setCmdQueue(LinkedList<Character> cmdQueue) {
        this.cmdQueue = cmdQueue;
    }

    public void addToCmdQueue(char cmd) {
        this.cmdQueue.add(cmd);
    }
}