package vaccinesystem;

import java.io.*;
import java.util.ArrayList;
    

public class UserManagement {
    
    // ArrayList to store patient details in application
    public static ArrayList<Patient> patientsArray = new ArrayList<>();
    public static ArrayList<Clinician> cliniciansArray = new ArrayList<>();
    public static ArrayList<Administrator> administrationArray = new ArrayList<>();
    public static ArrayList<Questionnaire> questionnaireArray = new ArrayList<>();
    
    // ------------------------------------------------------------------------
    //                           Patient Management
    // ------------------------------------------------------------------------
    // Reads patients from text file and stores them as objects in arraylist
    static void refreshPatients() {
        try {
            patientsArray.clear();
            FileReader fstream = new FileReader("patients.txt");
            BufferedReader reader = new BufferedReader(fstream);
            String line = reader.readLine();
            while(line != null) {
                String[] details = line.split("«", 0);
                patientsArray.add(new Patient(details[0], details[1], details[2], details[3], details[4], details[5], details[6], details[7], details[8], Boolean.parseBoolean(details[9])));
                line = reader.readLine();
            }
            reader.close();
            System.out.println("PATIENTS LOADED SUCCESSFULLY");
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    
    // Adds patient to text file then refreshes arraylist
    static void addPatient(String name, String dateofbirth, String town, String eircode, String number, String ppsNumber, String email, String password, String status, Boolean questionnaireDone) {
        try { 
            FileWriter fstream = new FileWriter("patients.txt", true);
            BufferedWriter writer = new BufferedWriter(fstream);
            PrintWriter out = new PrintWriter(writer);
            out.println(name + "«" + dateofbirth + "«" + town + "«" + eircode + "«" + number + "«" + ppsNumber + "«" + email + "«" + password + "«" + status + "«" + questionnaireDone.toString());
            out.close();
            refreshPatients();
         } catch(IOException e) {
             e.printStackTrace();
         } 
    }
    
    // Checks if any object in arraylist has defined name attribute
    // removes object from arraylist then re-creates textfile with updated list
    static void removePatient(String name) {
        Patient patientToDelete = null;
        for(Patient patient:patientsArray){
            if(patient.getName().equals(name))
            patientToDelete = patient;
        }
        
        if(patientToDelete==null) {
            System.out.println("No patient found");
        } else {
            patientsArray.remove(patientToDelete);
        }
        refreshPatientFile();
    }
    
    // Deletes file and recreates for refresh
    static void refreshPatientFile() {
        try { 
            File patients = new File("patients.txt");
            patients.delete();
            patients.createNewFile();
            FileWriter fstream = new FileWriter("patients.txt", true);
            PrintWriter out = new PrintWriter(fstream);
            patientsArray.forEach(user -> {
                out.println(user.getName() + "«" + user.getDateofbirth() + "«" + user.getTown() + "«" + user.getEircode() + "«" + user.getNumber() + "«" + user.getPpsNumber() + "«" + user.getEmail() + "«" + user.getPassword() + "«" + user.getStatus() + "«" + user.getQuestionnaireDone().toString());
            });
            out.close();
            refreshPatients(); 
        } catch(IOException e) {
             e.printStackTrace();
        } 
    }
    
    // ------------------------------------------------------------------------
    //                           Clinician Management
    // ------------------------------------------------------------------------
    // Reads patients from text file and stores them as objects in arraylist
    static void refreshClinicians() {
        try {
            cliniciansArray.clear();
            FileReader fstream = new FileReader("clinicians.txt");
            BufferedReader reader = new BufferedReader(fstream);
            String line = reader.readLine();
            while(line != null) {
                String[] details = line.split("«", 0);
                cliniciansArray.add(new Clinician(details[0], details[1], details[2], details[3], details[4], details[5], Boolean.parseBoolean(details[6])));
                line = reader.readLine();
            }
            reader.close();
            System.out.println("CLINICIANS LOADED SUCCESSFULLY");
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    
    /* ------------------------------------------------------------------------
                           Healthcare Provider Management
      ------------------------------------------------------------------------ */
    static void refreshAdmins() {
        try {
            administrationArray.clear();
            FileReader fstream = new FileReader("admins.txt");
            BufferedReader reader = new BufferedReader(fstream);
            String line = reader.readLine();
            while(line != null) {
                String[] details = line.split("«", 0);
                administrationArray.add(new Administrator(details[0], details[1], details[2], details[3]));
                line = reader.readLine();
            }
            reader.close();
            System.out.println("ADMINISTRATION LOADED SUCCESSFULLY");
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    
    /* ------------------------------------------------------------------------
                           Questionnaire Management
      ------------------------------------------------------------------------ */
    static void refreshQuestionnaires() {
        try {
            questionnaireArray.clear();
            FileReader fstream = new FileReader("questionnaires.txt");
            BufferedReader reader = new BufferedReader(fstream);
            String line = reader.readLine();
            while(line != null) {
                String[] details = line.split("«", 0);
                questionnaireArray.add(new Questionnaire(details[0], Boolean.parseBoolean(details[1]), Boolean.parseBoolean(details[2]), Boolean.parseBoolean(details[3]), Boolean.parseBoolean(details[4]),Boolean.parseBoolean(details[5]), details[6], details[7]));
                line = reader.readLine();
            }
            reader.close();
            System.out.println("QUESTIONNAIRES LOADED SUCCESSFULLY");
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    
    // Add questionnaire to text file
    static void addQuestionnaire(String questionnaireName, Boolean questionnaireCOVID14Days, Boolean questionnaireVaccine14Days, Boolean questionnaireIsPregnant, Boolean questionnaireHadAnaphylaxis, Boolean questionnaireHasExistingConditions, String questionnaireExistingConditions) {
        try { 
            FileWriter fstream = new FileWriter("questionnaires.txt", true);
            BufferedWriter writer = new BufferedWriter(fstream);
            PrintWriter out = new PrintWriter(writer);
            out.println(questionnaireName + "«" + questionnaireCOVID14Days.toString() + "«" + questionnaireVaccine14Days.toString() + "«" + questionnaireIsPregnant.toString() + "«" + questionnaireHadAnaphylaxis.toString() + "«" + questionnaireHasExistingConditions.toString() + "«" + questionnaireExistingConditions + "«" + "PENDING");
            out.close();
            refreshQuestionnaires();
         } catch(IOException e) {
             e.printStackTrace();
         } 
    }
    
    // Deletes file and recreates for refresh
    static void refreshQuestionnaireFile() {
        try { 
            File questionnaires = new File("questionnaires.txt");
            questionnaires.delete();
            questionnaires.createNewFile();
            FileWriter fstream = new FileWriter("questionnaires.txt", true);
            PrintWriter out = new PrintWriter(fstream);
            questionnaireArray.forEach(user -> {
                out.println(user.getQuestionnaireName() + "«" + user.getQuestionnaireCOVID14Days().toString() + "«" + user.getQuestionnaireVaccine14Days().toString() + "«" + user.getQuestionnaireIsPregnant().toString() + "«" + user.getQuestionnaireHadAnaphylaxis().toString() + "«" + user.getQuestionnaireHasExistingConditions().toString() + "«" + user.getQuestionnaireExistingConditions() + "«" + user.getQuestionnaireMedicalStatus());
            });
            out.close();
            refreshQuestionnaires();
        } catch(IOException e) {
             e.printStackTrace();
        } 
    }
}
