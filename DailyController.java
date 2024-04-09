import java.io.*;
import java.util.ArrayList;

//main controller of the backend
public class DailyController {

    // For using this system, please enter your valid path of daily.txt, user.txt and game.txt here! :)
    String dailyPath = "src/database/Daily.txt";
    String userPath = "src/database/User.txt";
    String gamePath = "src/database/game.txt";

    private User currUser;

    DailyView dailyView;
    DatabaseOperator databaseOperator;

    public DailyController() {
        this.currUser = null;
        this.dailyView = new DailyView();
        this.databaseOperator = DatabaseOperator.getInstance(userPath, gamePath);
    }

    /**
     * the main method that runs the daily.txt file. This will help dissect every transaction code line for line
     * and perform the necessary actions given.
     */
    public void run() {
        System.out.println("Welcome!\n");

        // starts to read the daily.txt file
        try {
            File file = new File(dailyPath);
            InputStreamReader reader = new InputStreamReader(new FileInputStream(file));
            BufferedReader bufferedReader = new BufferedReader(reader);

            // every time it while loops, the transactions will renew
            String transaction = "";
            transaction = bufferedReader.readLine();

            // Start reading the daily.txt file line by line until it is finished
            while (transaction != null) {

                // starts to read the transactions line by line
                DailyAction dailyAction = dailyView.getAction(transaction);
                //if the transaction code is to login
                if ((dailyAction.getAction_type().equals("login")) &&
                (databaseOperator.login(currUser, dailyAction.getUsername(),
                            dailyAction.getUsertype(), dailyAction.getAvailable_credit()))) {

                    this.currUser = databaseOperator.getUser(dailyAction.getUsername());
                //if the transaction code is to logout
                } else if (dailyAction.getAction_type().equals("logout") &&
                        (databaseOperator.logout(currUser, dailyAction.getUsername(),
                                dailyAction.getUsertype(), dailyAction.getAvailable_credit()))) {

                    this.currUser = null;
                //if the transaction code is to toggle auctionsale
                } else if (dailyAction.getAction_type().equals("auctionsale")) {

                    databaseOperator.auctionSale(currUser, dailyAction.getUsername(),
                            dailyAction.getUsertype(), dailyAction.getAvailable_credit());
                //if the transaction code is to delete a user
                } else if (dailyAction.getAction_type().equals("delete")) {

                    databaseOperator.delete(currUser, dailyAction.getUsername(), dailyAction.getUsertype(),
                            dailyAction.getAvailable_credit());
                //if the transaction code is to register a new user
                } else if (dailyAction.getAction_type().equals("create")) {

                    databaseOperator.create(currUser, dailyAction.getUsername(), dailyAction.getUsertype(),
                        dailyAction.getAvailable_credit());
                //if the transaction code is to add credit to user balance
                } else if (dailyAction.getAction_type().equals("addcredit")) {
                    databaseOperator.addCredit(currUser, dailyAction.getUsername(), dailyAction.getUsertype(),
                            dailyAction.getAvailable_credit());

                //if the transaction code is to refund a game
                } else if (dailyAction.getAction_type().equals("refund")) {

                    databaseOperator.refund(currUser, dailyAction.getBuyerName(),
                        dailyAction.getSellerName(), dailyAction.getRefund_credit());
                //if the transaction code is to sell a game
                } else if (dailyAction.getAction_type().equals("sell")) {

                    databaseOperator.sell(currUser, dailyAction.getGameName(),
                            dailyAction.getSellerName(), dailyAction.getDiscount(), dailyAction.getSalePrice());
                //if the transaction code is to
                } else if (dailyAction.getAction_type().equals("buy")) {

                    databaseOperator.buy(currUser, dailyAction.getGameName(),
                        dailyAction.getSellerName(), dailyAction.getBuyerName());
                //if the transaction code is to remove a game from the user's inventory
                } else if ((dailyAction.getAction_type().equals("removegame"))) {

                    databaseOperator.removeGame(currUser, dailyAction.getGameName(),
                            dailyAction.getOwnerName(), dailyAction.getReceiverName());
                //if the transaction code is to gift a user a game
                } else if (dailyAction.getAction_type().equals("gift")) {
                    databaseOperator.gift(currUser, dailyAction.getGameName(),
                            dailyAction.getOwnerName(), dailyAction.getReceiverName());

                //if the transaction code given is outside of available functions
                } else if (currUser == null) {
                System.out.println("ERROR: No user is currently logged in, unable to handle transactions.");
            }
            transaction = bufferedReader.readLine();
        }

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("\nThanks for using!");
    }
    //starts running this system
    public static void main(String[] args) {

        DailyController dc = new DailyController();
        dc.run();
    }

}

