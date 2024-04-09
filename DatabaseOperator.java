import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * The tool that saves data and implements the actions.
 */
public class DatabaseOperator implements DatabaseOperation {

    private ArrayList<User> users = new ArrayList<>();
    private ArrayList<Game> games = new ArrayList<>();
    private ArrayList<Game> tradedGames = new ArrayList<>();

    //the text file with the registered users in the system
    private String userDataPath = "src/database/User.txt";
    //the text file with the registered games in the system
    private String gameDataPath = "src/database/Game.txt";
    //the text file that states if the Auction Sale is currently active
    private String auctionSale_statusPath = "src/database/AuctionSale_status.txt";

    public static DatabaseOperator databaseOperator = null;
    private Boolean auctionSaleStatus = false;
    private Boolean autoUpdate;

    /**
     * Helps read the information of the system's registered games and users on a new day
     * @param userPath string location of the User.txt file
     * @param gamePath string location of the Game.txt file
     * @return the activation of the class
     */
    public static DatabaseOperator getInstance(String userPath, String gamePath) {
        if (databaseOperator == null) {
            databaseOperator = new DatabaseOperator(userPath, gamePath);
            return databaseOperator;
        }
        return databaseOperator;
    }

    /**
     * helps read the line of information in User.txt and Game.txt
     * @param userPath string location of the User.txt file
     * @param gamePath string location of the Game.txt file
     */
    private DatabaseOperator(String userPath, String gamePath) {
        autoUpdate = true;
        this.userDataPath = userPath;
        this.gameDataPath = gamePath;
        this.auctionSale_statusPath = userDataPath.replace("User.txt", "AuctionSale_status.txt");
        File file = new File(userPath);
        try {
            // reads the data from User.txt
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                line.replace("\n", "");
                String[] userInfo = line.split(",");

                String username = userInfo[0];
                String type = userInfo[1];
                Double balance = Double.parseDouble(userInfo[2]);
                //adds the created user into the system
                User user = new User(username, type, balance);
                user.databaseOperator = this;
                users.add(user);
            }

            // reads the data from Game.txt

            file = new File(gamePath);
            scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                line.replace("\n", "");
                String[] userInfo = line.split(",");

                String gameName = userInfo[0];
                String ownerName = userInfo[1];
                Double price = Double.parseDouble(userInfo[2]);
                Boolean is_available = Boolean.parseBoolean(userInfo[3]);
                //adds the created game into the system
                games.add(new Game(gameName, ownerName, price, is_available, this));
            }
            // checks AuctionSale_status.txt
            file = new File(auctionSale_statusPath);
            scanner = new Scanner(file);
            // checks for the next line
            while (scanner.hasNextLine()) {

                String line = scanner.nextLine();
                auctionSaleStatus = !line.equalsIgnoreCase("False");
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Check if the current user can login to the system
     * @param currUser the current user
     * @param username the user's username
     * @param userType the type of account the user has
     * @param credit the user's credit balance
     * @return true iff the current user logging in matches the user in the system, false iff there is information that
     * doesn't match
     */
    public Boolean login(User currUser, String username, String userType, double credit) {
        if (currUser != null && currUser.getIsLogin()) {
            System.out.println("ERROR: Unable to login, " + currUser.getUsername() + " is currently logged in," +
                    " System only support exactly one user logged in at the same time.");
            return false;

        } else if (databaseOperator.getUser(username) == null) {
            System.out.println("ERROR: Unable to login, " + username + " is not in database.");
            return false;

        } else if (!getUser(username).getType().equals(userType)) {
            System.out.println("ERROR: Unable to login, the given user type is incorrect.");
            return false;

        } else if (getUser(username).getBalance() != credit) {
            System.out.println("ERROR: Unable to login, the given balance is incorrect.");
            return false;

        } else {
            for (User user : users) {
                if (username.equals(user.getUsername())) {
                    user.setIsLogin(true);
                    System.out.println(userType + " " + username + " has logged in, current balance is "
                            + credit + ".");
                }
            }
            return true;
        }
    }

    /**
     * Check to see if the user can log out
     * @param currUser the current user
     * @param username the user's username
     * @param userType the type of account the user has
     * @param credit the user's credit balance
     * @return True if the user in the system satisfies all conditions. False iff:
     *
     * 1. The user is currently not logged in
     * 2. There is another user logged in
     * 3. User type given is incorrect
     * 4. User balance given is incorrect
     */
    @Override
    public Boolean logout(User currUser, String username, String userType, double credit) {
        if (currUser == null) {
            System.out.println("ERROR: Unable to logout, No user is currently login.");
            return false;

        } else if (!currUser.getUsername().equals(username)) {
            System.out.println("ERROR: Unable to logout, Please logout " + currUser.getUsername() + " first.");
            return false;

        } else if (!getUser(username).getType().equals(userType)) {
            System.out.println("ERROR: Unable to login, the given user type is incorrect.");
            return false;

        } else if (getUser(username).getBalance() != credit) {
            System.out.println("ERROR: Unable to login, the given balance is incorrect.");
            return false;

        } else {
            currUser.setIsLogin(false);
            System.out.println(userType + " " + currUser.getUsername() + " has logged out.");
            return true;
        }
    }

    /**
     * Sell a game. Method will print out an error if:
     * 1. no such user exists
     * 2. the user is not logged in
     * 3. user does not have permissions to sell games
     * 4. price is invalid
     * 5. user is not logged in
     * @param currUser the current user
     * @param gameName name of the game
     * @param seller name of the seller
     * @param discount percent discount of the game
     * @param salePrice price of the game
     */
    // discount >= 0 && discount < 90
    @Override
    public void sell(User currUser, String gameName, String seller, double discount, double salePrice) {
        if (currUser == null) {
            System.out.println("ERROR: Unable to sell, No user is currently login.");

        } else if (!user_exist(seller)) {
            System.out.println("ERROR: Unable to sell, " + seller + " is not in database.");

        } else if (!currUser.getUsername().equals(seller)) {
            System.out.println("ERROR: Unable to sell, Please logout " + currUser.getUsername() + " first.");

        } else if (getUser(seller).getType().equals("buy-standard")) {
            System.out.println("ERROR: Unable to sell, " + seller + " can not sell games.");

        } else if (salePrice > 999.99) {
            System.out.println("ERROR: ERROR: Unable to sell, given price is over the maximum " +
                    "game price limit (999.99).");

        } else {
            //new game is created
            Game newGame = new Game(gameName, seller, salePrice, true, this);
            games.add(newGame);
            tradedGames.add(newGame);

            newGame.setOwnerName(seller);
            newGame.setDiscount(discount);
            newGame.setIsAvailable(true);

            System.out.println(seller + " has put up " + gameName
                    + " for sale with the price of " + salePrice + " with discount of " + discount + ".");
            databaseOperator.updateDatabase();
        }
    }

    /**
     * User buys a game. Prints an error if:
     * 1. no user is currently logged in
     * 2. seller/buyer/game is not in database
     * 3. the game cannot be sold at the moment
     * 4. the account type does not allow for buying games
     * 5. the account does not have enough credit to buy the game
     * @param currUser the current user
     * @param gameName the name of the game
     * @param seller the seller of the game
     * @param buyer the user who wants to buy the game
     */

    public void buy(User currUser, String gameName, String seller, String buyer) {
        if (currUser == null) {
            System.out.println("ERROR: Unable to buy, No user is currently login.");

        } else if (!currUser.getUsername().equals(buyer)) {
            System.out.println("ERROR: Unable to buy, Please logout " + currUser.getUsername() + " first.");

        } else if (!user_exist(seller)) {
            System.out.println("ERROR: Unable to buy, " + seller + " is not in database.");

        } else if (!user_exist(buyer)) {
            System.out.println("ERROR: Unable to buy, " + buyer + " is not in database.");

        } else if (!game_exist(gameName)) {
            System.out.println("ERROR: Unable to buy, " + gameName + " is not in database.");

        } else if (!getGame(gameName).getIsAvailable()) {
            System.out.println("ERROR: Unable to buy, " + gameName + " is not currently sold.");

        } else if (getUser(seller).getType().equals("sell-standard")) {
            System.out.println("ERROR: Unable to buy, " + seller + " can not buy games.");

        } else if (getUser(buyer).getBalance() < getGame(seller, gameName).getPrice()) {
            System.out.println("ERROR: Unable to buy" + buyer + " has no enough balance to buy "
                    + gameName + ".");
        } else {
            Game currGame = getGame(gameName);
            double currPrice = currGame.getPrice();
            if (getAuctionSaleStatus()) {
                currPrice = currPrice * currGame.getDiscount();
            }

            if (getUser(buyer).getBalance() >= getGame(seller, gameName).getPrice()) {

                getUser(seller).addBalance(currPrice);
                getUser(buyer).reduceBalance(currPrice);
                getGame(gameName).setIsAvailable(false);
                currGame.setOwnerName(buyer);
                tradedGames.add(getGame(gameName));

                System.out.println(buyer + " has bought the game " +
                        gameName + " for " + currPrice + " from " + seller + ", "
                        + buyer + " now have balance of " + getUser(buyer).getBalance() + ".");

                databaseOperator.updateDatabase();
            }
        }
    }


    /**
     * remove a game from the user's inventory. Prints an error if conditions are not met.
     * @param currUser the current user
     * @param gameName the name of the game
     * @param owner the owner of the game
     * @param receiver the recipient of the game
     */
    @Override
    public void removeGame(User currUser, String gameName, String owner, String receiver) {
        if (currUser == null) {
            System.out.println("ERROR: Unable to remove, No user is currently login.");

        } else if (!game_exist(gameName)) {
            System.out.println("ERROR: Unable to remove, " + gameName + " is not in database.");

        } else if (!user_exist(owner)) {
            System.out.println("ERROR: Unable to remove, " + owner + " is not in database.");

        } else if (!currUser.getUsername().equals(owner) && !isAdmin(currUser.getUsername())) {
            System.out.println("ERROR: Unable to remove, " + currUser.getUsername()
                    + " does not have the privilege to remove " + gameName + " from " + owner + "'s inventory.");

        } else if (!getMyGames(owner).contains(getGame(gameName))) {
            System.out.println("ERROR: Unable to remove, " + owner + " does not own " + gameName + ".");

        } else if (tradedGames.contains(getGame(gameName))) {
            System.out.println("ERROR: Unable to remove " + gameName + " from "
                    + owner + "'s inventory, this game is for currently for trading.");
        } else {
            games.remove(getGame(owner, gameName));
            System.out.println(currUser.getUsername() + " has removed game "
                    + gameName + " from " + owner + "'s inventory.");
            databaseOperator.updateDatabase();

        }
    }

    /**
     * Get the username needed
     * @param username the name that needs to be found
     * @return the username of the user iff they are in the system, null iff they are not.
     */
    public User getUser(String username) {
        //checks all the users in the system
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    /**
     * get game by gameName, without specify owner name
     * return null if no such game
     *
     * @param gameName name of the game
     * @return the name of the game iff they are in the system, null iff it is not.
     */
    public Game getGame(String gameName) {
        for (Game game : games) {
            if (game.getGameName().equals(gameName)) {
                return game;
            }
        }
        return null;
    }

    /**
     * find the game that corresponds to the owner. The game must have that specific owner.
     *
     * @param username the owner of the game
     * @param gameName the name of the game
     * @return the name of the game iff it has that specific owner and they are in the system, null iff conditions are
     * not satisfied.
     */
    public Game getGame(String username, String gameName) {
        for (Game game : games) {
            if (game.getGameName().equals(gameName) && game.getOwnerName().equals(username)) {
                return game;
            }
        }
        return null;
    }


    /**
     * add credit to particular user
     * return true if credit added successfully
     * return false if:
     * 1. no such user
     * 2. user is not login
     * 3. credit out of range (should be 0.00 < credit <= 1000.00)
     * 4. if charging over 1000 in today after adding "credit"
     *
     * @param username
     * @param credit
     * @return
     */
    /**
     * add credit to the user. Prints error if these conditions are not met:
     * 1. current user is not logged in
     * 2. username is not in the database
     * 3. account type does not match the user
     * 4. the user does not have permission to add credits
     * 5. the user has reached the daily limit
     * @param currUser the current user
     * @param username the target user's username
     * @param userType the current user's account type
     * @param credit the amount of credit balance the user has
     */
    public void addCredit(User currUser, String username, String userType, double credit) {
        if (currUser == null) {
            System.out.println("ERROR: Unable to add credit, No user is currently login.");

        } else if (!user_exist(username)) {
            System.out.println("ERROR: Unable to add credit, " + username+ " is not in database.");

        } else if (!getUser(username).getType().equals(userType)) {
            System.out.println("ERROR: Unable to add credit, the given user type is incorrect.");

        } else if (!currUser.getUsername().equals(username) && !isAdmin(currUser.getUsername())) {
            System.out.println("ERROR: " + currUser.getUsername() + " does not have the privilege to " +
                    "add credit for " + username);

        } else if ((getUser(username).getTotalAddedCredit() + credit > 1000)) {
            System.out.println("ERROR: Unable to add credit, " + username
                    + " has reach the daily limit. (1000 per day)");

        } else {
            //successfully add credit
            getUser(username).addBalance(credit);
            getUser(username).updateTotalAddedCredit(credit);
            System.out.println(userType + " " + currUser.getUsername() + " has added " + credit
                    + " to " + username + "'s balance.");
        }
    }

    /**
     * See the different games this user has
     * @param username the user's name
     * @return an array of games the user owns
     */
    public ArrayList<Game> getMyGames(String username) {
        if (!user_exist(username)) {
            return null;
        }
        ArrayList<Game> myGames = new ArrayList<>();
        for (Game game : games) {
            if (game.getOwnerName().equals(username)) {
                myGames.add(game);
            }
        }
        return myGames;
    }

    /**
     * Allows users to gift a game to another user. Admins can gift any game to another person. Prints errors if
     * conditions are not met.
     * @param currUser the current user
     * @param gameName the name of the game
     * @param owner the owner of the game
     * @param receiver the recipient of the game
     */
    @Override
    public void gift(User currUser, String gameName, String owner, String receiver) {
        //if user is not logged in
        if (currUser == null) {
            System.out.println("ERROR: Unable to gift, No user is currently login.");
        //if game does not exist in the database
        } else if (!game_exist(gameName)) {
            System.out.println("ERROR: Unable to gift, " + gameName + " is not in database.");
        //if the owner is not in the database
        } else if (!user_exist(owner)) {
            System.out.println("ERROR: Unable to gift, " + owner + " is not in database.");
        //if the user is not an admin, they can't gift any game to another person
        } else if (!currUser.getUsername().equals(owner) && !isAdmin(currUser.getUsername())) {
            System.out.println("ERROR: Unable to gift, " + currUser.getUsername() + " does not have the" +
                    " privilege to send " + gameName + " from " + owner + "'s inventory to "
                    + receiver + "'s.");
        //the user does not own the game that is to be gifted
        } else if (!getMyGames(owner).contains(getGame(gameName)) && !isAdmin(owner)) {
            System.out.println("ERROR: Unable to gift, " + owner + " does not own " + gameName + ".");
        //game is currently for trading
        } else if (tradedGames.contains(getGame(gameName))) {
            System.out.println("ERROR: Unable to send " + gameName + " from "
                    + owner + "'s inventory to " + receiver + ", this game is currently for trading.");
        } else {
            //successfully gifting game
            getGame(gameName).setOwnerName(receiver);
            System.out.println(currUser.getUsername() + " has sent game "
                    + gameName + " from " + owner + "'s inventory to " + receiver + "'s.");
            databaseOperator.updateDatabase();

        }
    }


    /**
     * Check if the user has an Admin account
     * @param adminName name of the user
     * @return true iff the user is an admin and false if:
     *      * 1. user is not exist
     *      * 2. user is not admin
     */
    private Boolean isAdmin(String adminName) {
        return getUser(adminName).getType().equals("admin");
    }

    /**
     * Check if the user is in the database
     *
     * @param username the user name of the user to be checked
     * @return true iff the user is in the database, false if not
     */ 
    private Boolean user_exist(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check if the game is in the databse
     *
     * @param gameName the name of the game
     * @return true if the game exists in the database, false if not
     */
    private Boolean game_exist(String gameName) {
        for (Game game : games) {
            if (game.getGameName().equals(gameName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Register a new User into the system. Prints error if conditions are not met.
     * @param currUser The current user using this command
     * @param username The name of the new user
     * @param userType the account type of the new user
     * @param credit the amount of money in the new user's balance
     */
    @Override
    public void create(User currUser, String username, String userType, double credit) {
        if (currUser == null) {
            System.out.println("ERROR: Unable to create, No user is currently login.");
        //check if the current user is an Admin
        } else if (!isAdmin(currUser.getUsername())) {
            System.out.println("ERROR: Unable to create, " + currUser.getUsername() + " does not have" +
                    "the privilege to create new users.");

        } else if (user_exist(username)) {
            System.out.println("ERROR: Unable to create, " + username + " already exists in database.");

        } else {
            users.add(new User(username, userType, credit));
            System.out.println(userType + " " + currUser.getUsername() + " has created a new "
                    + userType + " user named " + username
                    + " with balance of " + credit + ".");
            databaseOperator.updateDatabase();
        }
    }

    /**
     * Delete an account from the database. Prints errors if conditions are not met.
     * @param currUser the User using this command
     * @param username the username to be deleted
     * @param userType the soon to be deleted user's account type
     * @param credit the balance of the soon to be deleted user
     */
    @Override
    public void delete (User currUser, String username, String userType, double credit) {
        if (currUser == null) {
            System.out.println("ERROR: Unable to delete, No user is currently login.");

        } else if (!isAdmin(currUser.getUsername())) {
            System.out.println("ERROR: Unable to delete, " + currUser.getUsername() + " does not have" +
                    "the privilege to delete any users.");

        } else if (!user_exist(username)) {
            System.out.println("ERROR: Unable to delete, " + username + " is not in database.");

        } else if (currUser.getUsername().equals(username)) {
            System.out.println("ERROR: Unable to delete, " + username + " can not delete himself/herself.");

        } else {
            users.remove(getUser(username));
            System.out.println(userType + " " + currUser.getUsername() + " has deleted a "
                    + userType + " user named " + username + " from database.");
            databaseOperator.updateDatabase();
        }
    }

    /**
     * Refund the user. Error is printed if conditions are not met.
     * @param currUser the user issuing the command
     * @param buyer the user who bought the game, getting the refund
     * @param seller the user who sells the game, returning the money
     * @param credits the amount of money being refunded
     */
    @Override
    public void refund(User currUser, String buyer, String seller, double credits) {
        if (currUser == null) {
            System.out.println("ERROR: Unable to refund, No user is currently login.");

        } else if (!isAdmin(currUser.getUsername())) {
            System.out.println("ERROR: Unable to refund, " + currUser.getUsername() + " does not have" +
                    "the privilege to do the refund for any users.");

        } else if (!user_exist(buyer)) {
            System.out.println("ERROR: Unable to refund, " + seller + " is not in database.");

        } else if (!user_exist(seller)) {
            System.out.println("ERROR: Unable to refund, " + seller + " is not in database.");

        } else if (getUser(buyer).getBalance() == 999999.99) {
            System.out.println("ERROR: Unable to refund, " + seller + " has reached balance limit (999999.99).");

        } else {
            getUser(seller).reduceBalance(credits);
            getUser(buyer).addBalance(credits);
            System.out.println(currUser.getUsername() + " has made the " +
                    credits + " credits refund for buyer " +
                    buyer + " and seller " + seller + ".");

            databaseOperator.updateDatabase();
        }
    }

    /**
     * Turn on or turn off the auction sale. Error occurs if conditions are not met.
     * @param currUser the user toggling the auction sale
     * @param username the current user who is logged in
     * @param userType the current user's account type
     * @param credit the current user's account balance
     */
    @Override
    public void auctionSale(User currUser, String username, String userType, double credit) {
        if (currUser == null) {
            System.out.println("ERROR: Unable to start/close auction sale, No user is currently login.");

        } else if (!user_exist(username)) {
            System.out.println("ERROR: Unable to start/close auction sale, " + username + " is not in database.");

        } else if (!getUser(username).getType().equals(userType)) {
            System.out.println("ERROR: Unable to start/close auction sale, the given user type is incorrect.");

        } else if (!getUser(username).getBalance().equals(credit)) {
            System.out.println("ERROR: Unable to start/close auction sale, the given balance is incorrect.");

        } else if (!currUser.getUsername().equals(username)) {
            System.out.println("ERROR: Unable to start/close auction sale, Please logout "
                    + currUser.getUsername() + " and login " + username + ".");

        } else if (!isAdmin(currUser.getUsername())) {
            System.out.println("ERROR: Sorry, " + currUser.getType() + " " + currUser.getUsername()
                    + " does not have the privilege to deal with auction sale");

        } else if (!user_exist(username)) {
            System.out.println("ERROR: Unable to delete, " + username + " is not in database.");

        } else if (!getAuctionSaleStatus()) {
            auctionSaleStatus = true;
            System.out.println(currUser.getUsername() + " has started the auction sale!");
            databaseOperator.updateDatabase();

        } else if (getAuctionSaleStatus()) {
            auctionSaleStatus = false;
            System.out.println(currUser.getUsername() + " has closed the auction sale!");
            databaseOperator.updateDatabase();
        }
    }

    public Boolean getAuctionSaleStatus() {
        return auctionSaleStatus;
    }

    /**
     * Update the database if autoUpdate is on.
     * <p>
     * autoUpdate is false iff in the testing mode
     * -- don't call this method
     */
    public void updateDatabase() {
        if (!autoUpdate) {
            return;
        }
        try {
            FileWriter fileWriter = new FileWriter(userDataPath);
            for (User user : users) {
                String line = String.format("%s,%s,%s\n", user.getUsername(), user.getType(), user.getBalance());
                fileWriter.write(line);
            }
            fileWriter.close();

            fileWriter = new FileWriter(gameDataPath);
            for (Game game : games) {
                String line = String.format("%s,%s,%s,%s\n",
                        game.getGameName(), game.getOwnerName(), game.getPrice(), game.getIsAvailable());
                fileWriter.write(line);
            }
            fileWriter.close();


            fileWriter = new FileWriter(auctionSale_statusPath);
            fileWriter.write(auctionSaleStatus.toString());
            fileWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}