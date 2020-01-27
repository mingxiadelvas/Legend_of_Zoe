import java.util.ArrayList;
import java.util.LinkedList;

public class Level  {
    private int stage; // Current stage of the game
    private LinkedList<Person> personList; // Person list for turn management
    private ArrayList<ArrayList<ArrayList<Element>>> board; //3D table of level

    /**
     * Constructor function
     * @param stage Level stage of the game
     */
    public Level(int stage) {
        this.stage = stage;
        this.personList = new LinkedList<Person>();

        // Initialize 3D board
        // Y axis
        this.board = new ArrayList<>(LevelGenerator.HAUTEUR);

        for (int i = 0; i < LevelGenerator.HAUTEUR; i++) {
            // X axis
            this.board.add(
                new ArrayList<ArrayList<Element>>(LevelGenerator.LARGEUR)
            );

            for (int j = 0; j < LevelGenerator.LARGEUR; j++) {
                // Z axis
                this.board.get(i).add(new ArrayList<Element>(1));
            }
        }

        // Generate level data
        Paire<boolean[][], String[]> levelData = LevelGenerator
            .generateLevel(stage);
        // Place walls on board
        placeWalls(levelData.getKey());
        // Place objects on the board
        placeObjects(levelData.getValue());
    }

    /**
     * Method that places the walls and empty spaces on the board
     * @param walls Array of booleans with true meaning a wall
     */
    private void placeWalls(boolean[][] walls) {
        for (int y = 0; y < LevelGenerator.HAUTEUR; y++) {
            for (int x = 0; x < LevelGenerator.LARGEUR; x++) {
                if (walls[y][x] == true) {
                    addToTile(x, y, new Element('#')); // Wall
                } else {
                    addToTile(x, y, new Element(' ')); // Empty space
                }
            }
        }
    }

    /**
     * Method that places the objects at the beginning of the game
     * @param objects String array containing item and person informations
     */
    private void placeObjects(String[] objects){
        for (int i = 0; i < objects.length; i++) {
            String[] splitArray = objects[i].split(":"); // Object's properties
            int x; // Position on the board X axis
            int y; // Position on the board Y axis

            switch (splitArray[0]) {
                case "tresor":
                    x = Integer.parseInt(splitArray[2]);
                    y = Integer.parseInt(splitArray[3]);

                    // Create treasure with his item and add it to its tile
                    addToTile(x, y, new Treasure(splitArray[1]));
                    break;
                case "monstre":
                    x = Integer.parseInt(splitArray[2]);
                    y = Integer.parseInt(splitArray[3]);

                    // Create new monster
                    Monster tempMonster = new Monster(
                        getStage(), splitArray[1], x, y
                    );
                    addToTile(x, y, tempMonster);
                    addToPersonList(tempMonster); // Add reference to personList
                    break;
                case "sortie":
                    x = Integer.parseInt(splitArray[1]);
                    y = Integer.parseInt(splitArray[2]);

                    // Create Exit element and add it to its tile
                    addToTile(x, y, new Element('E'));
                    break;
                case "zoe":
                    x = Integer.parseInt(splitArray[1]);
                    y = Integer.parseInt(splitArray[2]);

                    // Create Zoe and add her to her tiles
                    Zoe tempZoe = new Zoe(x, y);
                    addToTile(x, y, tempZoe);
                    addFirstToPersonList(tempZoe); // Zoe always plays 1st
                    break;
                default: break;
            }
        }
    }

    /**
     * Get Zoe from this level
     * @return Zoe object from this level
     */
    public Zoe getZoe() {
        return (Zoe) getPersonList().getFirst();
    }

    /**
     * Prints this level to console
     */
    public void printLevel() {
        String output = "";

        for (int y = 0; y < getBoardHeight(); y++) {
            for (int x = 0; x < getBoardLength(y); x++) {
                // If Zoe is on tile, get her Symbol, else get last element's
                output += (getZoe().getX() == x && getZoe().getY() == y)
                    ? getZoe().getSymbol()
                    : getElement(x, y, getTile(x, y).size() - 1).getSymbol();
            }
            output += "\n";
        }

        System.out.print(output);
    }

    /**
     * Updates position for a person and on the board
     * @param person Person instance to move
     * @param targetX Move target on the X axis
     * @param targetY Move target on the Y axis
     */
    public void updatePosition(Person person, int targetX, int targetY) {
        // If targetY is on the board
        if (targetY >= 0 && targetY < getBoardHeight()) {
            // If targetX is on the board
            if (targetX >= 0 && targetX < getBoardLength(targetY)) {
                // If can't move to the target location, exit
                if (!(canMove(getTile(targetX, targetY)))) { return; }

                // Person's current tile
                ArrayList<Element> tile = getTile(person.getX(), person.getY());

                // Find person index on current tile
                for (int i = 0; i < tile.size(); i++) {
                    if (tile.get(i) == person) {
                        // Remove from current tile
                        rmFromTile(person.getX(), person.getY(), i);

                        // Add to new tile
                        addToTile(targetX, targetY, person);

                        // Update Person attributes
                        person.setX(targetX);
                        person.setY(targetY);
                        break;
                    }
                }
            }
        }
    }

    /**
     * Return true if move is possible
     * @param tile elements of a given tile
     * @return boolean
     */
    private boolean canMove(ArrayList<Element> tile) {
        // Find if at least one element on tile prevents movement
        for (int i = 0; i < tile.size(); i++) {
            switch (tile.get(i).getSymbol()) {
                case '#': return false; // Wall
                case '$': return false; // Closed treasure
                case '_': return false; // Open treasure
                case 'E': return false; // Exit
            }
        }

        return true;
    }

    /**
     * Get Elements of all surroundings tiles, including tile at given location
     * @param x Position on the X axis
     * @param y Position on the Y axis
     * @return Elements from all the tiles
     */
    public ArrayList<Element> getSurroundings(int x, int y){
        ArrayList<Element> surroundings = new ArrayList<>();

        for (int i = y - 1; i <= y + 1; i++) {
            // If outside of Y axis bounds, next iteration
            if (i < 0 || i >= getBoardHeight()) { continue; }

            for (int j = x - 1; j <= x + 1; j++) {
                // If outside of X axis bounds, next iteration.
                if (j < 0 || j >= getBoardLength(i)) { continue; }

                for (int k = 0; k < getTile(j, i).size(); k++) {
                    // Add all tile elements to surroundings ArrayList
                    surroundings.add(getElement(j, i, k));
                }
            }
        }

        return surroundings;
    }

    public int getBoardLength(int y) {
        return this.board.get(y).size();
    }

    public int getBoardHeight() {
        return this.board.size();
    }

    public ArrayList<Element> getTile(int x, int y) {
        return this.board.get(y).get(x);
    }

    public void addToTile(int x, int y, Element element) {
        this.board.get(y).get(x).add(element);
    }

    public void rmFromTile(int x, int y, int z) {
        this.board.get(y).get(x).remove(z);
    }

    public Element getElement(int x, int y, int z) {
       return this.board.get(y).get(x).get(z);
    }

    public LinkedList<Person> getPersonList() {
        return this.personList;
    }

    public void addToPersonList(Person person) {
        this.personList.add(person);
    }

    public void addFirstToPersonList(Person person) {
        this.personList.addFirst(person);
    }

    public void rmFromPersonList(int index) {
        this.personList.remove(index);
    }

    public int getStage() {
        return this.stage;
    }

    public void setStage(int stage) {
        this.stage = stage;
    }
}