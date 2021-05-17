package vaccinesystem;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class VaccineManagement {
    public static ArrayList<Vaccine> vaccinesArray = new ArrayList<>();
    public static ArrayList<VaccinationCentre> vaccineCentresArray = new ArrayList<>();
    
    /* ------------------------------------------------------------------------
                               Vaccine Management
      ------------------------------------------------------------------------ */
    static void refreshVaccines() {
        try {
            vaccinesArray.clear();
            FileReader fstream = new FileReader("vaccines.txt");
            BufferedReader reader = new BufferedReader(fstream);
            String line = reader.readLine();
            while(line != null) {
                String[] details = line.split("«", 0);
                vaccinesArray.add(new Vaccine(details[0], details[1], details[2], details[3], Integer.parseInt(details[4])));
                line = reader.readLine();
            }
            reader.close();
            System.out.println("VACCINES LOADED SUCCESSFULLY");
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    
    // Add vaccine to text file
    static void addVaccine(String vaccineName, String vaccineDescription, String vaccineStorageConditions, String vaccineExpiryDate, int vaccineQuantity) {
        try { 
            FileWriter fstream = new FileWriter("vaccines.txt", true);
            PrintWriter out = new PrintWriter(fstream);
            out.println(vaccineName + "«" + vaccineDescription + "«" + vaccineStorageConditions + "«" + vaccineExpiryDate + "«" + Integer.toString(vaccineQuantity));
            out.close();
            refreshVaccines();
         } catch(IOException e) {
             e.printStackTrace();
         } 
    }
    
    // Deletes file and recreates for refresh
    static void refreshVaccinesFile() {
        try { 
            File vaccines = new File("vaccines.txt");
            vaccines.delete();
            vaccines.createNewFile();
            FileWriter fstream = new FileWriter("vaccines.txt", true);
            PrintWriter out = new PrintWriter(fstream);
            vaccinesArray.forEach(vaccine -> {
                out.println(vaccine.getVaccineName() + "«" + vaccine.getVaccineDescription() + "«" + vaccine.getVaccineStorageConditions() + "«" + vaccine.getVaccineExpiryDate() + "«" + Integer.toString(vaccine.getVaccineQuantity()));
            });
            out.close();
            refreshVaccines();
        } catch(IOException e) {
             e.printStackTrace();
        } 
    }
    
    /* ------------------------------------------------------------------------
                               Vaccine Centre Management
      ------------------------------------------------------------------------ */
    // Populate vaccine centres array from text file
    static void refreshVaccineCentres() {
        try {
            vaccineCentresArray.clear();
            FileReader fstream = new FileReader("vaccinecentres.txt");
            BufferedReader reader = new BufferedReader(fstream);
            String line = reader.readLine();
            while(line != null) {
                String[] details = line.split("«", 0);
                vaccineCentresArray.add(new VaccinationCentre(details[0], details[1], Boolean.parseBoolean(details[2])));
                line = reader.readLine();
            }
            reader.close();
            System.out.println("VACCINATION CENTRES LOADED SUCCESSFULLY");
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    
    // Add vaccine centre to text file
    static void addVaccineCentre(String vcName, String vcLocation, Boolean vcIsActive) {
        try { 
            FileWriter fstream = new FileWriter("vaccinecentres.txt", true);
            PrintWriter out = new PrintWriter(fstream);
            out.println(vcName + "«" + vcLocation + "«" + vcIsActive.toString());
            out.close();
            refreshVaccineCentres();
         } catch(IOException e) {
             e.printStackTrace();
         } 
    }
    
    // Deletes file and recreates for refresh
    static void refreshVaccineCentresFile() {
        try { 
            File vaccinecentres = new File("vaccinecentres.txt");
            vaccinecentres.delete();
            vaccinecentres.createNewFile();
            FileWriter fstream = new FileWriter("vaccinecentres.txt", true);
            PrintWriter out = new PrintWriter(fstream);
            vaccineCentresArray.forEach(vaccinecentre -> {
                out.println(vaccinecentre.getVcName() + "«" + vaccinecentre.getVcLocation() + "«" + vaccinecentre.getVcIsActive().toString());
            });
            out.close();
            refreshVaccineCentres();
        } catch(IOException e) {
             e.printStackTrace();
        } 
    }
}
