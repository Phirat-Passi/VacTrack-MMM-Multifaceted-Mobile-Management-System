package vaccinesystem;

public class Patient extends User {
    
    // Class Properties
    protected String patientDOB;
    protected String patientTown;
    protected String patientEircode;
    protected String patientPPS;
    protected String patientEmail;
    protected String patientVaccinationStatus;
    protected Boolean patientQuestionnaireDone;
    
    /**
     * @return the patientDOB
     */
    public String getDateofbirth() {
        return patientDOB;
    }

    /**
     * @param patientDOB the patientDOB to set
     */
    public void setDateofbirth(String patientDOB) {
        this.patientDOB = patientDOB;
    }

    /**
     * @return the patientAddress
     */
    public String getTown() {
        return patientTown;
    }

    /**
     * @param patientTown
     */
    public void setTown(String patientTown) {
        this.patientTown = patientTown;
    }

    /**
     * @return the patientEircode
     */
    public String getEircode() {
        return patientEircode;
    }

    /**
     * @param patientEircode the patientEircode to set
     */
    public void setEircode(String patientEircode) {
        this.patientEircode = patientEircode;
    }

    /**
     * @return the patientPPS
     */
    public String getPpsNumber() {
        return patientPPS;
    }

    /**
     * @param patientPPS the patientPPS to set
     */
    public void setPpsNumber(String patientPPS) {
        this.patientPPS = patientPPS;
    }

    /**
     * @return the patientEmail
     */
    public String getEmail() {
        return patientEmail;
    }

    /**
     * @param patientEmail the patientEmail to set
     */
    public void setEmail(String patientEmail) {
        this.patientEmail = patientEmail;
    }

    /**
     * @return the patientVaccinationStatus
     */
    public String getStatus() {
        return patientVaccinationStatus;
    }

    /**
     * @param patientVaccinationStatus the patientVaccinationStatus to set
     */
    public void setStatus(String patientVaccinationStatus) {
        this.patientVaccinationStatus = patientVaccinationStatus;
    }
    
    /**
     * @return the patientVaccinationStatus
     */
    public Boolean getQuestionnaireDone() {
        return patientQuestionnaireDone;
    }

    /**
     * @param patientQuestionnaireDone
     */
    public void setQuestionnaireDone(Boolean patientQuestionnaireDone) {
        this.patientQuestionnaireDone = patientQuestionnaireDone;
    }

    // Constructor
    public Patient(String name, String patientDOB, String patientTown, String patientEircode, String number, String patientPPS, String patientEmail, String password, String patientVaccinationStatus, Boolean patientQuestionnaireDone) {
        super(name, password, number);
        this.patientDOB = patientDOB;
        this.patientTown = patientTown;
        this.patientEircode = patientEircode;
        this.patientPPS = patientPPS;
        this.patientEmail = patientEmail;
        this.patientVaccinationStatus = patientVaccinationStatus;
        this.patientQuestionnaireDone = patientQuestionnaireDone;
    }

    // To Sring
    @Override
    public String toString() {
        return super.toString() + "«" + patientDOB + "«" + patientTown + "«" + patientEircode + "«" + patientPPS + "«" + patientEmail + "«" + patientVaccinationStatus + "«" + patientQuestionnaireDone.toString();
    }
}
