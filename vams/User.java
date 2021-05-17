package vaccinesystem;

public abstract class User {
    
    // Class Properties
    protected String userName;
    protected String userPassword;
    protected String userNumber;
    
    /**
     * @return the userName
     */
    public String getName() {
        return userName;
    }

    /**
     * @param userName the userName to set
     */
    public void setName(String userName) {
        this.userName = userName;
    }

    /**
     * @return the userNumber
     */
    public String getPassword() {
        return userPassword;
    }
    
    /**
     * @param userNumber the userNumber to set
     */
    public void setPassword(String userPassword) {
        this.userPassword = userPassword;
    }
/**
     * @return the userNumber
     */
    public String getNumber() {
        return userNumber;
    }
    
    /**
     * @param userNumber the userNumber to set
     */
    public void setNumber(String userNumber) {
        this.userNumber = userNumber;
    }

    // Constructor
    public User(String userName, String userPassword, String userNumber) {
        this.userName = userName;
        this.userPassword = userPassword;
        this.userNumber = userNumber;
    }

    // To String
    @Override
    public String toString() {
        return userName + "«" + userPassword + "«" + userNumber;
    }
     
}
