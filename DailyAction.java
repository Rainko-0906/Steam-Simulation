public class DailyAction {

    // for all action type
    private String action_type;
    private int actionCase;

    // for login, create, delete, add_credit, logout
    private String username;
    private String usertype;
    private double available_credit;

    //for refund
    private String buyerName; //(sell, refund, buy)
    private String sellerName; //(refund, buy, sell)
    private double refund_credit;

    //for sell
    private String gameName; //(buy,sell, removegame, gift)
    private double discount;
    private double salePrice;

    // for removegame and gift

    private String ownerName;
    private String receiverName;


    public DailyAction(){
        //what kind of transaction type
        this.action_type = "";
        //which case corresponds with the transaction type
        this.actionCase = 0;
        //username of the user
        this.username = "";
        //type of account this username has
        this.usertype = "";
        //the balance available on the user's account
        this.available_credit = 0.00;
        //username of the buyer's account
        this.buyerName = "";
        //username of the seller's account
        this.sellerName = "";
        //the amount of credit to be refunded
        this.refund_credit = 0.00;
        //the name of the game
        this.gameName = "";
        //percent discount
        this.discount = 0.00;
        //the price of the product
        this.salePrice = 0.00;
        //username of the owner
        this.ownerName = "";
        //username of the recipient
        this.receiverName = "";

    }


    //a series of simple methods that conveniently calls various attributes of DailyAction

    //setter for action_type
    public void setAction_type(String action_type) {
        this.action_type = action_type;
    }
    //getter for action_type
    public String getAction_type() {
        return this.action_type;
    }
    //setter for actionCase
    public void setActionCase(int actionCase){
        this.actionCase = actionCase;
    }
    //setter for username
    public void setUsername(String Username){
        this.username = Username;
    }
    //getter for username
    public String getUsername(){
        return this.username;
    }
    //setter for usertype
    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }
    //getter for usertype
    public String getUsertype() {
        return this.usertype;
    }
    //setter for available_credit
    public void setAvailable_credit(double available_credit) {
        this.available_credit = available_credit;
    }
    //getter for available_credit
    public double getAvailable_credit() {
        return this.available_credit;
    }
    //setter for buyerName
    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }
    //getter for buyerName
    public String getBuyerName() {
        return this.buyerName;
    }
    //setter for sellerName
    public void setSellerName(String sellerName){
        this.sellerName = sellerName;
    }
    //getter for sellerName
    public String getSellerName(){
        return this.sellerName;
    }
    //setter for refund_credit
    public void setRefund_credit(double refund_credit) {
        this.refund_credit = refund_credit;
    }
    //getter for refund_credit
    public double getRefund_credit() {
        return this.refund_credit;
    }
    //setter for gameName
    public void setGameName(String gameName) {
        this.gameName = gameName;
    }
    //getter for gameName
    public String getGameName() {
        return this.gameName;
    }
    //setter for discount
    public void setDiscount(double discount){
        this.discount = discount;
    }
    //getter for discount
    public double getDiscount(){
        return this.discount;
    }
    //setter for salePrice
    public void setSalePrice(double salePrice) {
        this.salePrice = salePrice;
    }
    //getter for SalePrice
    public double getSalePrice() {
        return this.salePrice;
    }
    //getter for ownerName
    public void setOwnerName(String ownerName){
        this.ownerName = ownerName;
    }
    //getter for ownerName
    public String getOwnerName(){
        return this.ownerName;
    }
    //setter for receiverName
    public void setReceiverName(String receiverName){
        this.receiverName = receiverName;
    }
    //getter for receiverName
    public String getReceiverName(){
        return this.receiverName;
    }
}
