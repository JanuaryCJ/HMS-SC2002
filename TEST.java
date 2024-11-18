import java.io.*;
import java.util.*;

public class TEST {
    
    private static final String CSV_FILE_PATH = "C:/Users/mingh/OneDrive/Desktop/Y2S1/sc2002 oop/PROJECT_2002_v1/HMS-SC2002/Data/AppointmentRecord.csv";

    public static void viewAppointmentRecords() {
        System.out.println("Viewing appointment records...\n");

        // A map to group records by patient names
        Map<String, List<String[]>> groupedRecords = new TreeMap<>(); // TreeMap to ensure sorted order by patient name

        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE_PATH))) {
            String line;
            boolean isFirstLine = true;

            // Read each line and group by patient name
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false; // Skip header row if it exists
                    continue;
                }

                String[] values = line.split(",");
                if (values.length == 11) {
                    String patientName = values[0]; // Assuming the first column is the patient's name
                    groupedRecords.putIfAbsent(patientName, new ArrayList<>());
                    groupedRecords.get(patientName).add(values);
                } else {
                    System.out.println("Invalid row format: " + line);
                }
            }

            // Print grouped records
            for (String patientName : groupedRecords.keySet()) {
                System.out.println("\nName of Patient: " + patientName);
                System.out.println("Appointments:");

                // Print the records for the patient in the desired format
                for (String[] record : groupedRecords.get(patientName)) {
                    System.out.println("    Name of Doctor: " + record[1]);
                    System.out.println("    Date of Appointment: " + record[2]);
                    System.out.println("    Time of Appointment: " + record[3]);
                    System.out.println("    Appointment Status: " + record[4]);
                    System.out.println("    Patient Diagnosis: " + record[5]);
                    System.out.println("    Patient Treatment: " + record[6]);
                    System.out.println("    Prescribed Medication: " + record[7]);
                    System.out.println("    Medication Quantity: " + record[8]);
                    System.out.println("    Prescription Status: " + record[9]);
                    System.out.println("    Remarks: " + record[10]);
                    System.out.println(); // Blank line for better separation
                }
            }

        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        viewAppointmentRecords();
    }
}
