package vaccinesystem;

public class Administrator extends User {
    
    // Class properties
    protected String address;
    
    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }
    
    // Constructor
    public Administrator(String name, String password, String number, String address) {
        super(name, password, number);
        this.address = address;
    }

    
    // To String
    @Override
    public String toString() {
        return super.toString() + "«" + address;
    }
    
    
}
