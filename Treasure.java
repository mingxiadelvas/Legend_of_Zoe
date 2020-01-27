public class Treasure extends Element{
    private String item; // Item contained in treasure

    /**
     * Constructor of Treasure
     * @param item String of what is inside of treasure
     */
    public Treasure(String item) {
        super('$');
        this.item = item;
    }

    /**
     * Open treasure
     * @return String item from inside the treasure
     */
    public String openTreasure() {
        String itemFound = getItem();

        if (!itemFound.equals("")) {
            setSymbol('_');
            setItem("");
        }

        return itemFound;
    }

    public String getItem() {
        return this.item;
    }

    public void setItem(String item) {
        this.item = item;
    }
}
