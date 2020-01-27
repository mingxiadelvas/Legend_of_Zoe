import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class Monster extends Person {
    private String name; // Monster Name
    private String item; // Monster item
    private int strength; // Monster attack strength

    /**
     * Constructor function
     * @param stage Level stage of the monster
     * @param item Item carried by the monster
     * @param x Position on the X axis
     * @param y Position on the Y axis
     */
    public Monster (int stage, String item, int x, int y) {
        super('@', (int) Math.max(0.6 * stage, 1), x, y);
        this.name = newMonsterName();
        this.item = item;
        this.strength = (int) Math.max(0.4 * stage, 1);
    }

    /**
     * Monster standard AI
     * @param level Level Monster is evolving in
     */
    public void playTurn(Level level) {
        // Check if Zoe is around and attack her if she is
        ArrayList<Element> surroundings = level.getSurroundings(getX(), getY());

        for (int i = 0; i < surroundings.size(); i++) {
            if (surroundings.get(i) instanceof Zoe) {
                attack((Person) surroundings.get(i));
                return;
            }
        }

        // If Zoe isn't near, find and move towards her
        findZoe(level);
    }

    /**
     * Find Zoe's position and moves towards her
     * @param level Level Monster is evolving in
     */
    private void findZoe(Level level) {
        Zoe zoe = level.getZoe();
        int targetX;
        int targetY;

        if (zoe.getX() == getX()) {
            targetX = getX();
        } else {
            targetX = (zoe.getX() > getX())
                ? getX() + 1
                : getX() - 1;
        }

        if (zoe.getY() == getY()) {
            targetY = getY();
        } else {
            targetY = (zoe.getY() > getY())
                ? getY() + 1
                : getY() - 1;
        }

        move(level, targetX, targetY);
    }

    /**
     * Attack function to damage target Person
     * @param target Person object to be attacked
     */
    public void attack(Person target) {
        target.setHealthPoints(target.getHealthPoints() - getStrength());

        if (target instanceof Zoe) {
            System.out.println(getName() + " attaque Zoe!");

            if (target.getHealthPoints() <= 0) {
                System.out.println("Zoe meurt!");
            }
        }
    }

    /**
     * Move Monster on the board
     * @param level Level Monster is evolving in
     * @param targetX Target tile on the X axis
     * @param targetY Target tile on the Y axis
     */
    public void move(Level level, int targetX, int targetY) {
        // Update Monster position on the board
        level.updatePosition(this, targetX, targetY);
    }

    /**
     * Method handling the death of a monster
     * @param level Level Monster is evolving in
     */
    public void handleDeath(Level level) {
        System.out.println(getName() + " meurt!"); // Print death msg
        level.getZoe().handleItem(getItem()); // Zoe uses item
        setItem(""); // Removes item
        setSymbol('x'); // Change Symbol to dead monster

        // Get level's person list
        LinkedList<Person> personList = level.getPersonList();

        // Find monster index level's person list and removes him
        for (int i = 0; i < personList.size(); i++) {
            if (personList.get(i) == this) {
                level.rmFromPersonList(i);
                break;
            }
        }
    }

    /**
     * Randomize a name for a monster
     * @return String containing name
     */
    private String newMonsterName() {
        // Prefix and names for monsters
        String[] prefix = { "Wimmy", "Walking", "Evil", "Monstrueux", "Jiminy" };
        String[] names = { "Whooper", "Jimmy", "Jhooper", "Jimmy W.", "Whoopert" };
        Random random = new Random(); // Random object

        // Return randomised full monster name
        return prefix[random.nextInt(prefix.length)]
            + " "
            + names[random.nextInt(names.length)];
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getItem() {
        return this.item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public int getStrength() {
        return this.strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }
}