import java.util.Arrays;
import java.util.List;

public class DailyView {
    //different cases depends on the input transaction codes
    private final List<String> case1 = Arrays.asList("00", "02", "07", "10");
    private final List<String> case5 = Arrays.asList("08", "09");

    //case1: XX UUUUUUUUUUUUUUU TT CCCCCCCCC              "00", "01", "02", "06", "07", "10"
    //case2: XX UUUUUUUUUUUUUUU SSSSSSSSSSSSSSS CCCCCCCCC          05
    //case3: XX IIIIIIIIIIIIIIIIIII SSSSSSSSSSSSS DDDDD PPPPPP          03
    //case4: XX IIIIIIIIIIIIIIIIIII SSSSSSSSSSSSSSS UUUUUUUUUUUUUU        04
    //case5: XX IIIIIIIIIIIIIIIIIII UUUUUUUUUUUUUUU SSSSSSSSSSSSSSS    08 09

    /**
     * gathers and assigns information of which type of action the transaction is.
     * @param transaction each line of text that comes from daily.txt
     * @return a DailyAction class with new information about its transaction type and ActionCase
     */
    public DailyAction getAction(String transaction) {

        DailyAction result = new DailyAction();

        String action_type = transaction.substring(0,2);
        if (case1.contains(action_type)){
            result.setActionCase(1);
            check_case1(transaction, result);
        } else if (action_type.equals("05")) {
            result.setActionCase(2);
            result.setAction_type("refund");
            check_refund(transaction, result);
        } else if (action_type.equals("03")) {
            result.setActionCase(3);
            result.setAction_type("sell");
            check_sell(transaction, result);
        } else if (action_type.equals("04")) {
            result.setActionCase(4);
            result.setAction_type("buy");
            check_buy(transaction, result);
        } else if (case5.contains(action_type)){
            result.setActionCase(5);
            check_case5(transaction, result);
        } else if (action_type.equals("01")) {
            result.setActionCase(6);
            result.setAction_type("create");
            check_case1(transaction, result);
        } else if (action_type.equals("06")) {
            result.setActionCase(6);
            result.setAction_type("addcredit");
            check_case1(transaction, result);
        }
        return result;
    }

    /**
     * assigns the type of action and user account the user is doing and has, respectively. All according the to the
     * tracsaction given.
     * @param transaction the line of string given which gives us details on the transaction
     * @param result DailyAction class that contains information of the action
     */
    private void check_case1(String transaction, DailyAction result){

        String action_type = transaction.substring(0,2);
        String username = transaction.substring(3,18);
        String user_type = transaction.substring(19,21);
        double available_credit = Double.parseDouble(transaction.substring(22));
        //checks the first two index's (XX) to see what kind of transaction it is
        switch (action_type) {
            case "00":
                //since 00 means login, the action type is set to "login"
                result.setAction_type("login");
                break;
            case "01":
                result.setAction_type("create");
                break;
            case "02":
                result.setAction_type("delete");
                break;
            case "06":
                result.setAction_type("addcredit");
                break;
            case "07":
                result.setAction_type("auctionsale");
                break;
            case "10":
                result.setAction_type("logout");
                break;
        }

        result.setUsername(username.strip());

        //user_type would tells us what kind of account the user has.
        switch (user_type) {
            //AA is the code that tells us the user has an admin account
            case "AA":
                result.setUsertype("admin");
                break;
            case "FS":
                result.setUsertype("full-standard");
                break;
            case "BS":
                result.setUsertype("buy-standard");
                break;
            case "SS":
                result.setUsertype("sell-standard");
                break;
        }
        result.setAvailable_credit(available_credit);
    }

    /**
     * Assigns the necessary information needed when completing the refund transaction
     * @param transaction the line of string given which gives us details on the transaction
     * @param result DailyAction class that contains information of the action
     */
    public void check_refund(String transaction, DailyAction result){
        //clean up any spaces
        String buyerName = transaction.substring(3,18).trim();
        String sellerName = transaction.substring(19,34).trim();
        double refund_credit = Double.parseDouble(transaction.substring(35));
        result.setBuyerName(buyerName);
        result.setSellerName(sellerName);
        result.setRefund_credit(refund_credit);
    }

    /**
     * Assigns the necessary information needed when completing the selling transaction
     * @param transaction the line of string given which gives us details on the transaction
     * @param result DailyAction class that contains information of the action
     */
    public void check_sell(String transaction, DailyAction result){

        String gameName = transaction.substring(3, 22).trim();
        String seller_Name = transaction.substring(23, 36).trim();
        double discount = Double.parseDouble(transaction.substring(37, 42));
        double sale_price = Double.parseDouble(transaction.substring(44));

        result.setGameName(gameName);
        result.setSellerName(seller_Name);
        result.setDiscount(discount);
        result.setSalePrice(sale_price);
    }

    /**
     * Assigns the necessary information needed when completing the buying transaction
     * @param transaction the line of string given which gives us details on the transaction
     * @param result DailyAction class that contains information of the action
     */
    public void check_buy(String transaction, DailyAction result){
        String game_name = transaction.substring(3, 22).trim();
        String sellerName = transaction.substring(23, 38).trim();
        String buyerName = transaction.substring(39).trim();

        result.setGameName(game_name);
        result.setSellerName(sellerName);
        result.setBuyerName(buyerName);
    }

    /**
     * gathers and assigns the information of which type of action the transaction is.
     * @param transaction each line of text that comes from daily.txt
     * @param result DailyAction class with information about its transactions
     */
    public void check_case5(String transaction, DailyAction result){
        //remove unnecessary spaces
        String action_type = transaction.substring(0, 2).trim();
        String game_Name = transaction.substring(3, 22).trim();
        String ownerName = transaction.substring(23, 38).trim();
        String receiverName = transaction.substring(39).trim();
        //check which type of transaction it is
        switch (action_type) {
            case "08":
                result.setAction_type("removegame");
                break;
            case "09":
                result.setAction_type("gift");
                break;
        }
        //add information of the DailyAction
        result.setGameName(game_Name);
        result.setOwnerName(ownerName);
        result.setReceiverName(receiverName);
    }
}