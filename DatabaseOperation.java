//Important method interface
public interface DatabaseOperation {

    //case1 methods
    Boolean login(User currUser, String username, String userType, double credit);
    void create(User currUser, String username, String userType, double credit);
    void delete(User currUser, String username, String userType, double credit);
    void addCredit(User currUser, String username, String userType, double credit);
    void auctionSale(User currUser, String username, String userType, double credit);
    Boolean logout(User currUser, String username, String userType, double credit);

    // case2 methods
    void refund(User currUser, String buyerUsername, String sellerUsername, double credits);

    // case3 methods
    void sell(User currUser, String gameName, String seller, double discount, double salePrice);

    // case4 methods
    void buy(User currUser, String gameName, String seller, String buyer);

    // case5 methods
    void removeGame(User currUser, String gameName, String owner, String receiver);
    void gift(User currUser, String gameName, String owner, String receiver);
}
