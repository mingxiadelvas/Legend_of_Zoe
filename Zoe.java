import java.util.ArrayList;

public class Zoe extends Person {
    private int hexaforces; // Number of hexaforces carried by Zoe

    /**
     * Constructor function
     * @param x Position on the X axis
     * @param y Position on the Y axis
     */
    public Zoe(int x, int y) {
        super('&', 5, x, y);
    }

    /**
     * Interacts with the level according the command entered by player
     * @param level Level Zoe is evolving in
     * @param cmd Char command entered by player
     */
    public void interact(Level level, char cmd) {
        switch (cmd) {
            // Move up one tile
            case 'w': move(level, getX(), getY() - 1);
                break;
            // Move left one tile
            case 'a': move(level, getX() - 1, getY());
                break;
            // Move down one tile
            case 's': move(level, getX(), getY() + 1);
                break;
            // Move right one tile
            case 'd': move(level, getX() + 1, getY());
                break;
            // Digs surrounding walls
            case 'c': dig(level.getSurroundings(getX(), getY()));
                break;
            // Attack surrounding monsters
            case 'x': attack(level);
                break;
            // Open surrounding treasures
            case 'o': openTreasures(level.getSurroundings(getX(), getY()));
                break;
            // Exit Game
            case 'q': System.exit(0);
            default: System.out.println(cmd + " est une commande invalide");
                break;
        }
    }

    /**
     * Attack function to damage all alive monsters around her
     * @param level Level Zoe is evolving in
     */
    public void attack(Level level) {
        ArrayList<Element> surroundings = level.getSurroundings(getX(), getY());

        for (int i = 0; i < surroundings.size(); i++) {
            // If element is a live monster, attack it
            if (surroundings.get(i).getSymbol() == '@') {
                Monster monster = (Monster) surroundings.get(i);

                System.out.println("Zoe attaque " + monster.getName() + "!");
                attack(monster);

                // If monster died from attack, handle his death
                if (monster.getHealthPoints() <= 0) {
                    monster.handleDeath(level);
                }
            }
        }
    }

    /**
     * Attack function to damage target Person
     * @param target Person object
     */
    public void attack(Person target) {
        target.setHealthPoints(target.getHealthPoints() - 1);
    }

    /**
     * Move Zoe on the board
     * @param level Level Zoe is evolving in
     * @param targetX Target tile on the X axis
     * @param targetY Target tile on the Y axis
     */
    public void move(Level level, int targetX, int targetY) {
        // Update Zoe position on the board
        level.updatePosition(this, targetX, targetY);
    }

    /**
     * Remove walls around Zoe
     * @param surroundings Elements surrounding Zoe, including where she stands
     */
    private void dig(ArrayList<Element> surroundings) {
        for (int i = 0; i < surroundings.size(); i++) {
            if (surroundings.get(i).getSymbol() == '#') {
                surroundings.get(i).setSymbol(' ');
            }
        }
    }

    /**
     * Open treasures
     * @param surroundings Elements surrounding Zoe, including where she stands
     */
    private void openTreasures(ArrayList<Element> surroundings) {
        for (int i = 0; i < surroundings.size(); i++) {
            if (surroundings.get(i) instanceof Treasure) {
                Treasure treasure = (Treasure) surroundings.get(i);
                handleItem(treasure.openTreasure());
            }
        }
    }

    /**
     * Applies item effect to Zoe
     * @param item String representing game item
     */
    public void handleItem(String item) {
        switch (item) {
            // Restore all health points
            case "potionvie":
                setHealthPoints(5);
                System.out.println("Vous trouvez une potion de vie!");
                break;
            // Restore health points by 1
            case "coeur":
                if (getHealthPoints() < 5) {
                    setHealthPoints(getHealthPoints() + 1);
                }
                System.out.println("Vous trouvez un coeur!");
                break;
            // Increase hexaforce count by 1
            case "hexaforce":
                setHexaforces(getHexaforces() + 1);
                System.out.println("Vous trouvez un morceau d'Hexaforce!");
                break;
            default:
                break;
        }
    }

    public int getHexaforces() {
        return this.hexaforces;
    }

    public void setHexaforces(int hexaforces) {
        this.hexaforces = hexaforces;
    }
}