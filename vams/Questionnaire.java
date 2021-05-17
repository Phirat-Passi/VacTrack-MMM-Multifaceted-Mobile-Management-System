package vaccinesystem;

public class Questionnaire {
    
    // Class Properties
    protected String questionnaireName;
    protected Boolean questionnaireCOVID14Days;
    protected Boolean questionnaireVaccine14Days;
    protected Boolean questionnaireIsPregnant;
    protected Boolean questionnaireHadAnaphylaxis;
    protected Boolean questionnaireHasExistingConditions;
    protected String questionnaireExistingConditions;
    protected String medicalStatus;
    
    /**
     * @return the questionnaireName
     */
    public String getQuestionnaireName() {
        return questionnaireName;
    }

    /**
     * @param questionnaireName the questionnaireName to set
     */
    public void setQuestionnaireName(String questionnaireName) {
        this.questionnaireName = questionnaireName;
    }

    /**
     * @return the questionnaireCOVID14Days
     */
    public Boolean getQuestionnaireCOVID14Days() {
        return questionnaireCOVID14Days;
    }

    /**
     * @param questionnaireCOVID14Days the questionnaireCOVID14Days to set
     */
    public void setQuestionnaireCOVID14Days(Boolean questionnaireCOVID14Days) {
        this.questionnaireCOVID14Days = questionnaireCOVID14Days;
    }

    /**
     * @return the questionnaireVaccine14Days
     */
    public Boolean getQuestionnaireVaccine14Days() {
        return questionnaireVaccine14Days;
    }

    /**
     * @param questionnaireVaccine14Days the questionnaireVaccine14Days to set
     */
    public void setQuestionnaireVaccine14Days(Boolean questionnaireVaccine14Days) {
        this.questionnaireVaccine14Days = questionnaireVaccine14Days;
    }

    /**
     * @return the questionnaireIsPregnant
     */
    public Boolean getQuestionnaireIsPregnant() {
        return questionnaireIsPregnant;
    }

    /**
     * @param questionnaireIsPregnant the questionnaireIsPregnant to set
     */
    public void setQuestionnaireIsPregnant(Boolean questionnaireIsPregnant) {
        this.questionnaireIsPregnant = questionnaireIsPregnant;
    }

    /**
     * @return the questionnaireHadAnaphylaxis
     */
    public Boolean getQuestionnaireHadAnaphylaxis() {
        return questionnaireHadAnaphylaxis;
    }

    /**
     * @param questionnaireHadAnaphylaxis the questionnaireHadAnaphylaxis to set
     */
    public void setQuestionnaireHadAnaphylaxis(Boolean questionnaireHadAnaphylaxis) {
        this.questionnaireHadAnaphylaxis = questionnaireHadAnaphylaxis;
    }

    /**
     * @return the questionnaireHasExistingConditions
     */
    public Boolean getQuestionnaireHasExistingConditions() {
        return questionnaireHasExistingConditions;
    }

    /**
     * @param questionnaireHasExistingConditions the questionnaireHasExistingConditions to set
     */
    public void setQuestionnaireHasExistingConditions(Boolean questionnaireHasExistingConditions) {
        this.questionnaireHasExistingConditions = questionnaireHasExistingConditions;
    }

    /**
     * @return the questionnaireExistingConditions
     */
    public String getQuestionnaireExistingConditions() {
        return questionnaireExistingConditions;
    }

    /**
     * @param questionnaireExistingConditions the questionnaireExistingConditions to set
     */
    public void setQuestionnaireExistingConditions(String questionnaireExistingConditions) {
        this.questionnaireExistingConditions = questionnaireExistingConditions;
    }
    
    /**
     * @return the questionnaireMedicalStatus
     */
    public String getQuestionnaireMedicalStatus() {
        return medicalStatus;
    }

    /**
     * @param questionnaireMedicalStatus the questionnaireMedicalStatus to set
     */
    public void setQuestionnaireMedicalStatus(String questionnaireMedicalStatus) {
        this.medicalStatus = questionnaireMedicalStatus;
    }

    // Constructor
    public Questionnaire(String questionnaireName, Boolean questionnaireCOVID14Days, Boolean questionnaireVaccine14Days, Boolean questionnaireIsPregnant, Boolean questionnaireHadAnaphylaxis, Boolean questionnaireHasExistingConditions, String questionnaireExistingConditions, String questionnaireMedicalStatus) {
        this.questionnaireName = questionnaireName;
        this.questionnaireCOVID14Days = questionnaireCOVID14Days;
        this.questionnaireVaccine14Days = questionnaireVaccine14Days;
        this.questionnaireIsPregnant = questionnaireIsPregnant;
        this.questionnaireHadAnaphylaxis = questionnaireHadAnaphylaxis;
        this.questionnaireHasExistingConditions = questionnaireHasExistingConditions;
        this.questionnaireExistingConditions = questionnaireExistingConditions;
        this.medicalStatus = questionnaireMedicalStatus;
    }

    // To String
    @Override
    public String toString() {
        return questionnaireName + "«" + questionnaireCOVID14Days.toString() + "«" + questionnaireVaccine14Days.toString() + "«" + questionnaireIsPregnant.toString() + "«" + questionnaireHadAnaphylaxis.toString() + "«" + questionnaireHasExistingConditions.toString() + "«" + questionnaireExistingConditions + "«" + medicalStatus;
    }
    
    
}
