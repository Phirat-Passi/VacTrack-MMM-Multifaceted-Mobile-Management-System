package vaccinesystem;

public class Vaccine {
    
    // Class properties
    protected String vaccineName;
    protected String vaccineDescription;
    protected String vaccineStorageConditions;
    protected String vaccineExpiryDate;
    protected int vaccineQuantity;
    
    /**
     * @return the vaccineName
     */
    public String getVaccineName() {
        return vaccineName;
    }

    /**
     * @param vaccineName the vaccineName to set
     */
    public void setVaccineName(String vaccineName) {
        this.vaccineName = vaccineName;
    }

    /**
     * @return the vaccineDescription
     */
    public String getVaccineDescription() {
        return vaccineDescription;
    }

    /**
     * @param vaccineDescription the vaccineDescription to set
     */
    public void setVaccineDescription(String vaccineDescription) {
        this.vaccineDescription = vaccineDescription;
    }

    /**
     * @return the vaccineStorageConditions
     */
    public String getVaccineStorageConditions() {
        return vaccineStorageConditions;
    }

    /**
     * @param vaccineStorageConditions the vaccineStorageConditions to set
     */
    public void setVaccineStorageConditions(String vaccineStorageConditions) {
        this.vaccineStorageConditions = vaccineStorageConditions;
    }

    /**
     * @return the vaccineExpiryDate
     */
    public String getVaccineExpiryDate() {
        return vaccineExpiryDate;
    }

    /**
     * @param vaccineExpiryDate the vaccineExpiryDate to set
     */
    public void setVaccineExpiryDate(String vaccineExpiryDate) {
        this.vaccineExpiryDate = vaccineExpiryDate;
    }

    /**
     * @return the vaccineQuantity
     */
    public int getVaccineQuantity() {
        return vaccineQuantity;
    }

    /**
     * @param vaccineQuantity the vaccineQuantity to set
     */
    public void setVaccineQuantity(int vaccineQuantity) {
        this.vaccineQuantity = vaccineQuantity;
    }

    
    // Constructor
    public Vaccine(String vaccineName, String vaccineDescription, String vaccineStorageConditions, String vaccineExpiryDate, int vaccineQuantity) {
        this.vaccineName = vaccineName;
        this.vaccineDescription = vaccineDescription;
        this.vaccineStorageConditions = vaccineStorageConditions;
        this.vaccineExpiryDate = vaccineExpiryDate;
        this.vaccineQuantity = vaccineQuantity;
    }

    // To String
    @Override
    public String toString() {
        return vaccineName + "«" + vaccineDescription + "«" + vaccineStorageConditions + "«" + vaccineExpiryDate + "«" + vaccineQuantity;
    }
    
    
}
