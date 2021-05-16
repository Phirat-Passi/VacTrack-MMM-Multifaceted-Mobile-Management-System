package vaccinesystem;

public class Appointment {
    
    // Class Properties
    protected String appointmentEmail;
    protected String appointmentName;
    protected String appointmentDate;
    protected String appointmentTime;
    protected String appointmentLocation;
    protected String vaccineType;
    protected Integer doseNo;
    protected Boolean noShow;
    protected String taskStatus;
    
    /**
     * @return the appointmentEmail
     */
    public String getAppointmentEmail() {
        return appointmentEmail;
    }

    /**
     * @param appointmentEmail the appointmentEmail to set
     */
    public void setAppointmentEmail(String appointmentEmail) {
        this.appointmentEmail = appointmentEmail;
    }

    /**
     * @return the appointmentName
     */
    public String getAppointmentName() {
        return appointmentName;
    }

    /**
     * @param appointmentName the appointmentName to set
     */
    public void setAppointmentName(String appointmentName) {
        this.appointmentName = appointmentName;
    }

    /**
     * @return the appointmentDate
     */
    public String getAppointmentDate() {
        return appointmentDate;
    }

    /**
     * @param appointmentDate the appointmentDate to set
     */
    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    /**
     * @return the appointmentTime
     */
    public String getAppointmentTime() {
        return appointmentTime;
    }

    /**
     * @param appointmentTime the appointmentTime to set
     */
    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    /**
     * @return the appointmentLocation
     */
    public String getAppointmentLocation() {
        return appointmentLocation;
    }

    /**
     * @param appointmentLocation the appointmentLocation to set
     */
    public void setAppointmentLocation(String appointmentLocation) {
        this.appointmentLocation = appointmentLocation;
    }

    /**
     * @return the vaccineType
     */
    public String getVaccineType() {
        return vaccineType;
    }

    /**
     * @param vaccineType the vaccineType to set
     */
    public void setVaccineType(String vaccineType) {
        this.vaccineType = vaccineType;
    }

    /**
     * @return the doseNo
     */
    public Integer getDoseNo() {
        return doseNo;
    }

    /**
     * @param doseNo the doseNo to set
     */
    public void setDoseNo(Integer doseNo) {
        this.doseNo = doseNo;
    }
    
    /**
     * @return the noShow
     */
    public Boolean getNoShow() {
        return noShow;
    }

    /**
     * @param noShow the noShow to set
     */
    public void setNoShow(Boolean noShow) {
        this.noShow = noShow;
    }

    /**
     * @return the taskStatus
     */
    public String getTaskStatus() {
        return taskStatus;
    }

    /**
     * @param taskStatus the taskStatus to set
     */
    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }
    
    
    // Constructor
    public Appointment(String appointmentEmail, String appointmentName, String appointmentDate, String appointmentTime, String appointmentLocation, String vaccineType, Integer doseNo, Boolean noShow, String taskStatus) {
        this.appointmentEmail = appointmentEmail;
        this.appointmentName = appointmentName;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.appointmentLocation = appointmentLocation;
        this.vaccineType = vaccineType;
        this.doseNo = doseNo;
        this.noShow = noShow;
        this.taskStatus = taskStatus;
    }

    
    // To String
    @Override
    public String toString() {
        return appointmentEmail + "«" + appointmentName + "«" + appointmentDate + "«" + appointmentTime + "«" + appointmentLocation + "«" + vaccineType + "«" + doseNo + "«" + noShow.toString() + "«" + taskStatus;
    }
    
    
}
