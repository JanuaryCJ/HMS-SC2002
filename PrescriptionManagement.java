import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PrescriptionManagement {

    private static final String MEDICINE_CSV = "C:/Users/mingh/OneDrive/Desktop/Y2S1/sc2002 oop/PROJECT_2002_v2/HMS-SC2002/Data/Medicine_List1.csv";
    private static final String APPOINTMENT_CSV = "C:/Users/mingh/OneDrive/Desktop/Y2S1/sc2002 oop/PROJECT_2002_v2/HMS-SC2002/Data/AppointmentRecord1.csv";

    public void addNewMedicine() {
        Medicine newMedicine;
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Adding a new medicine to the inventory...");
            System.out.print("Enter Medicine Name: ");
            String name = scanner.nextLine();
            System.out.print("Enter Initial Stock: ");
            int initialStock = Integer.parseInt(scanner.nextLine());
            System.out.print("Enter Low Stock Level Alert: ");
            int lowStockLevelAlert = Integer.parseInt(scanner.nextLine());
            newMedicine = new Medicine(name, initialStock, lowStockLevelAlert);
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(MEDICINE_CSV, true))) {
            bw.write(newMedicine.toCsvFormat());
            bw.newLine();
            System.out.println("New medicine added successfully.");
        } 
        
        catch (IOException e) {
        System.out.println("Error adding new medicine to file: " + e.getMessage());
        }
    }

    public void deleteMedicine() {
        String medicineNameToDelete;
        List<String> updatedData;
        boolean medicineFound;
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Enter the Medicine Name to remove from inventory: ");
            medicineNameToDelete = scanner.nextLine();
            updatedData = new ArrayList<>();
            medicineFound = false;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(MEDICINE_CSV))) {
            String line;
            boolean isFirstLine = true;

            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    updatedData.add(line);
                    isFirstLine = false;
                    continue;
                }

                String[] values = line.split(",");
                String medicineName = values[0];

                if (!medicineName.equalsIgnoreCase(medicineNameToDelete)) {
                    updatedData.add(line);
                } else {
                    medicineFound = true;
                    System.out.println("Medicine '" + medicineNameToDelete + "' has been deleted from the inventory.");
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
            return;
        }

        if (!medicineFound) {
            System.out.println("Medicine '" + medicineNameToDelete + "' not found in the inventory.");
            return;
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(MEDICINE_CSV))) {
            for (String dataLine : updatedData) {
                bw.write(dataLine);
                bw.newLine();
            }
        } 
        
        catch (IOException e) {
            System.out.println("Error writing to the file: " + e.getMessage());
        }
    }

    public void viewMedicationInventory() {
        System.out.println("Viewing medication inventory...\n");
    
        try (BufferedReader br = new BufferedReader(new FileReader(MEDICINE_CSV))) {
            String line;
            boolean isFirstLine = true;
    
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false; // Skip the header row
                    continue;
                }
    
                String[] values = line.split(",");
                if (values.length == 7) { // Expecting 7 columns
                    // Print each row in the desired format
                    System.out.println("Medicine Name: " + values[0]);
                    System.out.println("Initial Stock: " + values[1]);
                    System.out.println("Current Stock: " + values[2]);
                    System.out.println("Low Stock Level Alert: " + values[3]);
                    System.out.println("Request Replenishment: " + values[4]);
                    System.out.println("Replenishment Approved: " + values[5]);
                    System.out.println("Last Update: " + values[6]);
                    System.out.println("---------------------------------------------------");
                } else {
                    System.out.println("Invalid row format: " + line);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + MEDICINE_CSV);
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void submitReplenishmentRequest() {
        Scanner scanner = new Scanner(System.in);
        boolean validInput = false;

        try (BufferedReader br = new BufferedReader(new FileReader(MEDICINE_CSV))) {
            StringBuilder updatedFileContent = new StringBuilder();
            String line;
            boolean isFirstLine = true;

            System.out.println("Submit Replenishment Request:");
            System.out.print("Enter Medicine Name: ");
            String inputMedicine = scanner.nextLine().trim();

            int inputQuantity = 0;
            while (!validInput) {
                System.out.print("Enter Quantity: ");
                String quantityStr = scanner.nextLine().trim();
                try {
                    inputQuantity = Integer.parseInt(quantityStr);
                    if (inputQuantity <= 0) {
                        System.out.println("Quantity must be a positive number.");
                    } else {
                        validInput = true;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Quantity must be a numeric value.");
                }
            }

            boolean medicineFound = false;

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (isFirstLine) {
                    updatedFileContent.append(line).append("\n");
                    isFirstLine = false;
                    continue;
                }

                if (values[0].equalsIgnoreCase(inputMedicine)) {
                    medicineFound = true;

                    values[4] = String.valueOf(inputQuantity); 
                    values[5] = "Pending"; 

                    updatedFileContent.append(String.join(",", values)).append("\n");
                } else {
                    updatedFileContent.append(line).append("\n");
                }
            }

            if (!medicineFound) {
                System.out.println("Error: Medicine not found in the inventory.");
                return;
            }

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(MEDICINE_CSV))) {
                bw.write(updatedFileContent.toString());
            }

            System.out.println("Replenishment request submitted successfully!");

        } catch (IOException e) {
            System.out.println("Error accessing the file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public class MedicineReplenishmentApproval {

        private static final String MEDICINE_CSV = "Medicine_List.csv";
    
        public void approveReplenishmentRequest() {
            Scanner scanner = new Scanner(System.in);
            boolean validInput;
    
            try (BufferedReader br = new BufferedReader(new FileReader(MEDICINE_CSV))) {
                StringBuilder updatedFileContent = new StringBuilder();
                String line;
                boolean isFirstLine = true;
    
                System.out.println("Medicines with 'Pending' Replenishment Requests:\n");
                System.out.printf("%-15s %-15s %-15s %-20s %-20s %-20s %-20s%n",
                        "Medicine Name", "Initial Stock", "Current Stock", 
                        "Low Stock Level Alert", "Request Replenishment", 
                        "Replenishment Approved", "Last Update");
    
                boolean hasPendingRequests = false;
    
                while ((line = br.readLine()) != null) {
                    String[] values = line.split(",");
                    if (isFirstLine) {
                        updatedFileContent.append(line).append("\n");
                        isFirstLine = false;
                        continue;
                    }
    
                    if (values.length == 7 && values[5].equalsIgnoreCase("Pending")) {
                        hasPendingRequests = true;
                        System.out.printf("%-15s %-15s %-15s %-20s %-20s %-20s %-20s%n",
                                values[0], values[1], values[2], values[3], values[4], values[5], values[6]);
                    }
    
                    updatedFileContent.append(line).append("\n");
                }
    
                if (!hasPendingRequests) {
                    System.out.println("\nNo pending replenishment requests found.");
                    return;
                }
    
                System.out.print("\nEnter the Medicine Name to process: ");
                String inputMedicine = scanner.nextLine().trim();
    
                validInput = false;
                String approvalStatus = "";
                while (!validInput) {
                    System.out.print("Approve or Reject Replenishment Request (Type 'Approve' or 'Reject'): ");
                    approvalStatus = scanner.nextLine().trim();
                    if (approvalStatus.equalsIgnoreCase("Approve") || approvalStatus.equalsIgnoreCase("Reject")) {
                        validInput = true;
                    } else {
                        System.out.println("Invalid input. Please type 'Approve' or 'Reject'.");
                    }
                }
    
                StringBuilder finalFileContent = new StringBuilder();
                String[] rows = updatedFileContent.toString().split("\n");
                for (String row : rows) {
                    String[] values = row.split(",");
                    if (values.length == 7 && values[0].equalsIgnoreCase(inputMedicine)) {
                        if (approvalStatus.equalsIgnoreCase("Approve")) {
                            values[6] = "Approved, " + java.time.LocalDateTime.now();
                            values[1] = String.valueOf(Integer.parseInt(values[1]) + Integer.parseInt(values[4]));
                            values[2] = values[1];
                            values[4] = "0";
                            values[5] = "NIL";
                        } else if (approvalStatus.equalsIgnoreCase("Reject")) {
                            values[6] = "Rejected, " + java.time.LocalDateTime.now();
                            values[4] = "0";
                            values[5] = "NIL";
                        }
                    }
                    finalFileContent.append(String.join(",", values)).append("\n");
                }
    
                try (BufferedWriter bw = new BufferedWriter(new FileWriter(MEDICINE_CSV))) {
                    bw.write(finalFileContent.toString());
                }
    
                System.out.println("Replenishment request for '" + inputMedicine + "' has been " + approvalStatus + ".");
            } catch (IOException e) {
                System.out.println("Error accessing the file: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public void managePatientPrescription() {
        Scanner scanner = new Scanner(System.in);

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

        List<String[]> allAppointments = new ArrayList<>();
        boolean hasPendingAppointments = false;

        try (BufferedReader br = new BufferedReader(new FileReader(APPOINTMENT_CSV))) {
            String line;
            boolean isFirstLine = true;

            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    allAppointments.add(line.split(","));
                    continue;
                }

                String[] values = line.split(",");
                allAppointments.add(values);

                if (values.length == 11 && values[0].equalsIgnoreCase(patientName) && values[9].equalsIgnoreCase("Pending")) {
                    hasPendingAppointments = true;

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

                    boolean validMedication = false;
                    while (!validMedication) {
                        System.out.print("Enter Medication Name: ");
                        String medicationName = scanner.nextLine();

                        if (!medicationName.equalsIgnoreCase(values[7])) {
                            System.out.println("Incorrect Prescribed Medication. Would you like to retry or exit? (retry/exit): ");
                            String choice = scanner.nextLine();
                            if (choice.equalsIgnoreCase("exit")) {
                                return;
                            }
                        } else {
                            validMedication = true;

                            boolean validQuantity = false;
                            int medicationQuantity = -1;
                            while (!validQuantity) {
                                System.out.print("Enter Medication Quantity: ");
                                try {
                                    medicationQuantity = Integer.parseInt(scanner.nextLine());
                                    validQuantity = true;
                                } catch (NumberFormatException e) {
                                    System.out.println("Invalid quantity. Please enter a numeric value.");
                                }
                            }

                            System.out.print("Enter Prescription Status (Dispensed/Pending): ");
                            String prescriptionStatus = scanner.nextLine();

                            values[8] = String.valueOf(medicationQuantity);
                            values[9] = prescriptionStatus;

                            if (prescriptionStatus.equalsIgnoreCase("Dispensed")) {
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
                }
            }

            if (!hasPendingAppointments) {
                System.out.println("No pending appointments found for the specified patient.");
                return;
            }

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(APPOINTMENT_CSV))) {
                for (String[] record : allAppointments) {
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

    private boolean isPatientValid(String patientName) {
        try (BufferedReader br = new BufferedReader(new FileReader(APPOINTMENT_CSV))) {
            String line;
            boolean isFirstLine = true;

            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
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

    private boolean updateMedicineStock(String medicationName, int medicationQuantity) {
        List<String[]> medicineRecords = new ArrayList<>();
        boolean medicineFound = false;

        try (BufferedReader br = new BufferedReader(new FileReader(MEDICINE_CSV))) {
            String line;
            boolean isFirstLine = true;

            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                String[] values = line.split(",");
                if (values.length >= 3 && values[0].equalsIgnoreCase(medicationName)) {
                    medicineFound = true;
                    int currentStock = Integer.parseInt(values[2]);

                    if (currentStock >= medicationQuantity) {
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

}