import java.io.*;
import java.util.*;

public class TEST {

    private static final String APPOINTMENT_CSV = "C:/Users/mingh/OneDrive/Desktop/Y2S1/sc2002 oop/PROJECT_2002_v1/HMS-SC2002/Data/AppointmentRecord.csv";
    private static final String MEDICINE_CSV = "C:/Users/mingh/OneDrive/Desktop/Y2S1/sc2002 oop/PROJECT_2002_v1/HMS-SC2002/Data/Medicine_List.csv";
    

    public static void managePatientAppointment() {
        Scanner scanner = new Scanner(System.in);

        // Validate patient name
        String patientName = null;
        boolean isValidPatient = false;
        while (!isValidPatient) {
            System.out.print("Enter the name of the patient: ");
            patientName = scanner.nextLine();

            if (isPatientValid(patientName)) {
                isValidPatient = true;
            } else {
                System.out.println("Invalid patient name. Please try again.");
            }
        }

        List<String[]> appointmentRecords = new ArrayList<>();
        boolean hasPendingAppointments = false;

        // Step 1: Read AppointmentRecord.csv and retrieve "Pending" appointments for the patient
        try (BufferedReader br = new BufferedReader(new FileReader(APPOINTMENT_CSV))) {
            String line;
            boolean isFirstLine = true;

            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false; // Skip the header row
                    continue;
                }

                String[] values = line.split(",");
                if (values.length == 11 && values[0].equalsIgnoreCase(patientName) && values[9].equalsIgnoreCase("Pending")) {
                    hasPendingAppointments = true;
                    appointmentRecords.add(values);

                    // Display the appointment
                    System.out.println("\nPending Appointment:");
                    System.out.println("    Date of Appointment: " + values[2]);
                    System.out.println("    Time of Appointment: " + values[3]);
                    System.out.println("    Appointment Status: " + values[4]);
                    System.out.println("    Patient Diagnosis: " + values[5]);
                    System.out.println("    Patient Treatment: " + values[6]);
                    System.out.println("    Prescribed Medication: " + values[7]);
                    System.out.println("    Medication Quantity: " + values[8]);
                    System.out.println("    Prescription Status: " + values[9]);
                    System.out.println("    Remarks: " + values[10]);

                    // Step 2: Prompt the user for prescription details
                    boolean validMedication = false;
                    String medicationName = null;
                    int medicationQuantity = -1;

                    while (!validMedication) {
                        System.out.print("Enter Medication Name: ");
                        medicationName = scanner.nextLine();

                        if (isMedicationValid(medicationName)) {
                            validMedication = true;
                        } else {
                            System.out.println("Invalid medication name. Would you like to retry or exit? (retry/exit): ");
                            String choice = scanner.nextLine();
                            if (choice.equalsIgnoreCase("exit")) {
                                return;
                            }
                        }
                    }

                    boolean validQuantity = false;
                    while (!validQuantity) {
                        System.out.print("Enter Medication Quantity: ");
                        try {
                            medicationQuantity = Integer.parseInt(scanner.nextLine());
                            validQuantity = true;
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid quantity. Please enter a numeric value.");
                        }
                    }

                    System.out.print("Enter Prescription Status (Prescribed/Pending): ");
                    String prescriptionStatus = scanner.nextLine();

                    // Update Prescription Status in AppointmentRecord.csv
                    values[7] = medicationName;
                    values[8] = String.valueOf(medicationQuantity);
                    values[9] = prescriptionStatus;

                    if (prescriptionStatus.equalsIgnoreCase("Prescribed")) {
                        // Step 3: Update the Medicine_List.csv for "Prescribed"
                        boolean stockUpdated = updateMedicineStock(medicationName, medicationQuantity);
                        if (!stockUpdated) {
                            System.out.println("Out of Stock, Submit Replenishment Request. Exiting.");
                            return;
                        }
                    } else if (prescriptionStatus.equalsIgnoreCase("Pending")) {
                        System.out.println("Prescription status set to Pending. Exiting.");
                        return;
                    } else {
                        System.out.println("Invalid prescription status. Exiting.");
                        return;
                    }
                }
            }

            if (!hasPendingAppointments) {
                System.out.println("No pending appointments found for the specified patient.");
                return;
            }

            // Step 4: Write updated appointment records back to AppointmentRecord.csv
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(APPOINTMENT_CSV))) {
                bw.write("Name of Patient,Name of Doctor,Date of Appointment,Time of Appointment,Appointment Status,Patient Diagnosis,Patient Treatment,Prescribed Medication,Medication Quantity,Prescription Status,Remarks");
                bw.newLine();
                for (String[] record : appointmentRecords) {
                    bw.write(String.join(",", record));
                    bw.newLine();
                }
            } catch (IOException e) {
                System.out.println("Error writing to the appointment file: " + e.getMessage());
            }

        } catch (IOException e) {
            System.out.println("Error reading the appointment file: " + e.getMessage());
        }
    }

    private static boolean isPatientValid(String patientName) {
        try (BufferedReader br = new BufferedReader(new FileReader(APPOINTMENT_CSV))) {
            String line;
            boolean isFirstLine = true;

            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false; // Skip the header row
                    continue;
                }

                String[] values = line.split(",");
                if (values.length >= 1 && values[0].equalsIgnoreCase(patientName)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading the appointment file: " + e.getMessage());
        }
        return false;
    }

    private static boolean isMedicationValid(String medicationName) {
        try (BufferedReader br = new BufferedReader(new FileReader(MEDICINE_CSV))) {
            String line;
            boolean isFirstLine = true;

            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false; // Skip the header row
                    continue;
                }

                String[] values = line.split(",");
                if (values.length >= 1 && values[0].equalsIgnoreCase(medicationName)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading the medicine file: " + e.getMessage());
        }
        return false;
    }

    private static boolean updateMedicineStock(String medicationName, int medicationQuantity) {
        List<String[]> medicineRecords = new ArrayList<>();
        boolean medicineFound = false;

        try (BufferedReader br = new BufferedReader(new FileReader(MEDICINE_CSV))) {
            String line;
            boolean isFirstLine = true;

            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false; // Skip the header row
                    continue;
                }

                String[] values = line.split(",");
                if (values.length >= 3 && values[0].equalsIgnoreCase(medicationName)) {
                    medicineFound = true;
                    int currentStock = Integer.parseInt(values[2]);

                    if (currentStock >= medicationQuantity) {
                        // Update the current stock
                        currentStock -= medicationQuantity;
                        values[2] = String.valueOf(currentStock);
                        System.out.println("Medication updated successfully. New stock: " + currentStock);
                    } else {
                        System.out.println("Out of Stock.");
                        return false;
                    }
                }
                medicineRecords.add(values);
            }
        } catch (IOException e) {
            System.out.println("Error reading the medicine file: " + e.getMessage());
            return false;
        }

        if (!medicineFound) {
            System.out.println("Medication not found in the inventory.");
            return false;
        }

        // Write the updated medicine records back to the file
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(MEDICINE_CSV))) {
            bw.write("Medicine Name,Initial Stock,Current Stock,Low Stock Level Alert,Request Replenishment,Replenishment Approved,Last Updated");
            bw.newLine();
            for (String[] record : medicineRecords) {
                bw.write(String.join(",", record));
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error writing to the medicine file: " + e.getMessage());
            return false;
        }

        return true;
    }

    public static void main(String[] args) {
        managePatientAppointment();
    }
}