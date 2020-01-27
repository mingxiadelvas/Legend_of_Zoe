abstract class Person extends Element {
    private int healthPoints; // Person healthPoints
    private int x; // Person position on the X axis
    private int y; // Person position on the Y axis

    /**
     * Constructor function
     * @param symbol Symbol representing Person on the board
     * @param healthPoints Health points of the Person
     * @param x Position on the X axis
     * @param y Position on the Y axis
     */
    public Person(char symbol, int healthPoints, int x, int y) {
        super(symbol);
        this.healthPoints = healthPoints;
        this.x = x;
        this.y = y;
    }

    /**
     * Attack function to damage another Person
     * @param target Target Person
     */
    abstract void attack(Person target);

    /**
     * Move function on the board
     * @param level Level the person is evolving in
     * @param targetX Target tile on the X axis
     * @param targetY Target tile on the Y axis
     */
    abstract void move(Level level, int targetX, int targetY);

    public int getHealthPoints() {
        return this.healthPoints;
    }

    public void setHealthPoints(int healthPoints) {
        this.healthPoints = healthPoints;
    }

    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
