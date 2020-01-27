public class Element {

    private char symbol; // Character on the board

    /**
     * Constructor of Element
     * @param symbol Symbol representing Element on the board
     */
    public Element(char symbol){
        this.symbol = symbol;
    }

    public char getSymbol() {
        return this.symbol;
    }

    public void setSymbol(char symbol) {
        this.symbol = symbol;
    }
}
