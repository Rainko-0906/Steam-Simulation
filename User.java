/**
 * Creates the various users in this system.
 */

public class User {
    //username of the user
    private String username;
    //the type of account the user has: Admin, Seller, Buyer, or Full-Standard
    private String type;
    //the amount of money/credit the user account has
    private Double balance;
    //whether the account is logged in
    private Boolean isLogin = false;
    DatabaseOperator databaseOperator;
    private Double addCreditLimit = 0.0;

    /**
     * Creates a new abstract User class with a username, type, balance, and an inventory with their games.
     *
     * @param username the user's username
     * @param type     the type of account the user owns
     * @param balance  the user's current credit balance
     */
    public User(String username, String type, Double balance) {

        this.username = username;
        this.type = type;
        this.balance = balance;
    }

    /**
     * Get the balance of the user
     * @return a double of the balance of the user
     */
    public Double getBalance() {
        return balance;
    }

    /**
     * check if user has logged in
     *
     * @return true if logged in, false if not
     */
    public boolean getIsLogin() {
        return this.isLogin;
    }

    /**
     * Changes the properties of isLogin
     */
    public void setIsLogin(boolean isLogin) {
        this.isLogin = isLogin;
    }

    /**
     * Get the user's account name
     *
     * @return the username of the user
     */
    public String getUsername() {
        return username;
    }

    /**
     * finds what account type the current user has(Admin,Buyer,Seller,Full-Standard)
     *
     * @return type the type of account the user has.
     */
    public String getType() {
        return type;
    }

    /**
     * adds credit to the user's account iff the resulting amount doesn't exceed a million
     * @param addBalance the amount of credit being added to the user's account balance
     */
    public void addBalance(double addBalance) {
        this.balance += addBalance;
        if (balance > 999999.99) {
            balance = 999999.99;
        }
    }

    /**
     * subtracts credit from the user's account
     * @param reduceBalance the amount of credit to be subtracted from the user's account balance
     */
    public void reduceBalance(double reduceBalance) {
        this.balance -= reduceBalance;
        if (balance < 0) {
            balance = 0.00;
        }
    }
    /**
     * updates the total amount of credit added
     * @param credit amount of money added
     */
    public void updateTotalAddedCredit(double credit) {
        this.addCreditLimit += credit;
    }

    /**
     * gets the amount of money added
     * @return the amount of credit balance this user has
     */
    public double getTotalAddedCredit() {
        return this.addCreditLimit;
    }
}

