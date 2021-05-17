package vaccinesystem;

public class VaccinationCentre {
    
    // Class properties
    protected String vcName;
    protected String vcLocation;
    protected Boolean vcIsActive;
    
    /**
     * @return the vcName
     */
    public String getVcName() {
        return vcName;
    }

    /**
     * @param vcName the vcName to set
     */
    public void setVcName(String vcName) {
        this.vcName = vcName;
    }

    /**
     * @return the vcLocation
     */
    public String getVcLocation() {
        return vcLocation;
    }

    /**
     * @param vcLocation the vcLocation to set
     */
    public void setVcLocation(String vcLocation) {
        this.vcLocation = vcLocation;
    }

    /**
     * @return the vcIsActive
     */
    public Boolean getVcIsActive() {
        return vcIsActive;
    }

    /**
     * @param vcIsActive the vcIsActive to set
     */
    public void setVcIsActive(Boolean vcIsActive) {
        this.vcIsActive = vcIsActive;
    }

    
    // Constructor
    public VaccinationCentre(String vcName, String vcLocation, Boolean vcIsActive) {
        this.vcName = vcName;
        this.vcLocation = vcLocation;
        this.vcIsActive = vcIsActive;
    }

    
    // To String
    @Override
    public String toString() {
        return vcName + "«" + vcLocation + "«" + vcIsActive;
    }
    
    
}
