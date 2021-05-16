package vaccinesystem;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class AppointmentManagement {
    public static ArrayList<Appointment> appointmentsArray = new ArrayList<>();
    
    
    // Populate appointments array from text file
    static void refreshAppointments() {
        try {
            appointmentsArray.clear();
            FileReader fstream = new FileReader("appointments.txt");
            BufferedReader reader = new BufferedReader(fstream);
            String line = reader.readLine();
            while(line != null) {
                String[] details = line.split("«", 0);
                appointmentsArray.add(new Appointment(details[0], details[1], details[2], details[3], details[4], details[5], Integer.parseInt(details[6]), Boolean.parseBoolean(details[7]), details[8]));
                line = reader.readLine();
            }
            reader.close();
            System.out.println("APPOINTMENTS LOADED SUCCESSFULLY");
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    
    
    // Add a new appointment to textfile
    static void addAppointment(String appointmentEmail, String appointmentName, String appointmentDate, String appointmentTime, String appointmentLocation, String vaccineType, Integer doseNo, Boolean noShow, String taskStatus) {
        try { 
            FileWriter fstream = new FileWriter("appointments.txt", true);
            PrintWriter out = new PrintWriter(fstream);
            out.println(appointmentEmail + "«" + appointmentName + "«" + appointmentDate + "«" + appointmentTime + "«" + appointmentLocation + "«" + vaccineType + "«" + doseNo + "«" + noShow.toString() + "«" + taskStatus);
            out.close();
            refreshAppointments();
         } catch(IOException e) {
             e.printStackTrace();
         } 
    }
    
    // Deletes file and recreates for refresh
    static void refreshAppointmentFile() {
        try { 
            File patients = new File("appointments.txt");
            patients.delete();
            patients.createNewFile();
            FileWriter fstream = new FileWriter("appointments.txt", true);
            PrintWriter out = new PrintWriter(fstream);
            appointmentsArray.forEach(appointment -> {
                out.println(appointment.getAppointmentEmail() + "«" + appointment.getAppointmentName() + "«" + appointment.getAppointmentDate() + "«" + appointment.getAppointmentTime() + "«" + appointment.getAppointmentLocation() + "«" + appointment.getVaccineType() + "«" + appointment.getDoseNo() + "«" + appointment.getNoShow().toString() + "«" + appointment.getTaskStatus());
            });
            out.close();
            refreshAppointments();
        } catch(IOException e) {
             e.printStackTrace();
        } 
    }
}
