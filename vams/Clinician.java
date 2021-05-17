package vaccinesystem;

public class Clinician extends User {

    // Class Properties
    protected String role;
    protected String clinicLocation;
    protected Boolean activelyVaccinating;
    protected String email;
    
    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }
    /**
     * @return the role
     */
    public String getRole() {
        return role;
    }

    /**
     * @param role the role to set
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * @return the location
     */
    public String getClinicLocation() {
        return clinicLocation;
    }

    /**
     * @param location the location to set
     */
    public void setClinicLocation(String location) {
        this.clinicLocation = location;
    }

    /**
     * @return the activelyVaccinating
     */
    public Boolean getActivelyVaccinating() {
        return activelyVaccinating;
    }

    /**
     * @param activelyVaccinating the activelyVaccinating to set
     */
    public void setActivelyVaccinating(Boolean activelyVaccinating) {
        this.activelyVaccinating = activelyVaccinating;
    }

    
    // Constructor
    public Clinician(String name, String password, String number, String email, String role, String location, Boolean activelyVaccinating) {
        super(name, password, number);
        this.email = email;
        this.role = role;
        this.clinicLocation = location;
        this.activelyVaccinating = activelyVaccinating;
    }
    
    
    // To String
    @Override
    public String toString() {
        return super.toString() + "«" + email + "«" + role + "«" + clinicLocation + "«" + activelyVaccinating ;
    }
    
    
}
