/**
 * Creates a game that can be owned by multiple people and be sold at different prices with different sellers.
 */
public class Game {
    //name of the game
    private final String gameName;
    //username of the owner of the game
    private String ownerName;
    //price of the game
    private double price;
    //percentage discount during an auction sale
    private double discount;
    //the availability of the game
    private Boolean is_available;
    DatabaseOperator databaseOperator;

    /**
     * Creates the game
     * @param gameName name of the game
     * @param ownerName name of the owner of the game
     * @param price price of the game
     * @param is_available the availability of the game
     * @param databaseOperator the class for implementing all the actions
     */
    public Game(String gameName, String ownerName, Double price, Boolean is_available, DatabaseOperator databaseOperator) {
        this.gameName = gameName;
        this.ownerName = ownerName;
        this.price = price;
        this.is_available = is_available;
        this.databaseOperator = databaseOperator;
        this.discount = 0;
    }
    //gets the name of the game
    public String getGameName() {
        return gameName;
    }
    //gets the owner of the game
    public String getOwnerName() {
        return ownerName;
    }
    //sets the owner of the game
    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }
    //gets the price of the game
    public Double getPrice() {
        return price;
    }
    //sets the price of the game
    public void setPrice(Double price) {
        this.price = price;
    }
    //check if the game is available
    public Boolean getIsAvailable() {
        return is_available;
    }
    //set whether or not the game is available
    public void setIsAvailable(Boolean is_available) {
        this.is_available = is_available;
    }
    //sets the discount of the game
    public void setDiscount(double discount) {
        this.discount = discount;
    }
    //get the percent discount of the game
    public double getDiscount() {
        return (1 - (discount * 0.01));
    }
}

