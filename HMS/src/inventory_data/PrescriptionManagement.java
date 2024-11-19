package inventory_data;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Manages the prescription and inventory data in a hospital.
 * Includes functionalities for adding, deleting, viewing, and updating medication inventory,
 * as well as handling replenishment requests and managing patient prescriptions.
 * 
 * @author Alvin Ong Minghui
 * @version 3.0.1
 * @since 2024-11-19
 */
public class PrescriptionManagement {

    private static final String MEDICINE_CSV = "./Medicine_List/Medicine_List.csv";
    private static final String APPOINTMENT_CSV = "./Appointment_Record/AppointmentRecord.csv";

    /**
     * Adds a new medicine to the inventory.
     * Prompts the user to input the medicine name, initial stock, and low stock alert level.
     * If the medicine already exists, it prevents duplication.
     */
    public void addNewMedicine() {
        Scanner scanner = new Scanner(System.in);

        try {
            System.out.print("Enter Medicine Name: ");
            String name = scanner.nextLine().trim();

            if (isMedicineValid(name)) {
                System.out.println("Medicine already exists in the list.");
                return;
            }

            int initialStock = getValidIntegerInput(scanner, "Enter Initial Stock: ");
            int lowStockLevelAlert = getValidIntegerInput(scanner, "Enter Low Stock Level Alert: ");

            Medicine newMedicine = new Medicine(name, initialStock, lowStockLevelAlert);

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(MEDICINE_CSV, true))) {
                bw.write(newMedicine.toCSVRow());
                bw.newLine();
            }

            System.out.println("New medicine '" + name + "' has been added successfully!");
        } catch (IOException e) {
            System.out.println("Error updating the file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Deletes a specified medicine from the inventory.
     * Prompts the user to input the name of the medicine to delete and updates the inventory file accordingly.
     */
    public void deleteMedicine() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter Medicine Name to delete: ");
        String medicineName = scanner.nextLine().trim();

        File inputFile = new File(MEDICINE_CSV);
        File tempFile = new File(inputFile.getParent(), "Temp_" + inputFile.getName());

        boolean medicineFound = false;

        try (BufferedReader br = new BufferedReader(new FileReader(inputFile));
             BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            boolean isFirstLine = true;

            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    bw.write(line);
                    bw.newLine();
                    isFirstLine = false;
                    continue;
                }

                String[] values = line.split(",");
                if (values.length >= 1 && values[0].equalsIgnoreCase(medicineName)) {
                    medicineFound = true;
                    System.out.println("Medicine '" + medicineName + "' found and deleted.");
                } else {
                    bw.write(line);
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            System.out.println("Error processing the file: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        if (medicineFound) {
            if (inputFile.delete()) {
                if (tempFile.renameTo(inputFile)) {
                    System.out.println("Medicine list updated successfully.");
                } else {
                    System.out.println("Error renaming the temp file.");
                }
            } else {
                System.out.println("Error deleting the original file.");
            }
        } else {
            System.out.println("Medicine '" + medicineName + "' not found in the list.");
            tempFile.delete();
        }
    }

    /**
     * Displays the medication inventory.
     * Reads data from the inventory file and prints each medicine's details in a formatted view.
     */
    public void viewMedicationInventory() {
        System.out.println("Viewing medication inventory...\n");

        try (BufferedReader br = new BufferedReader(new FileReader(MEDICINE_CSV))) {
            String line;
            boolean isFirstLine = true;

            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                String[] values = line.split(",");
                if (values.length == 7) { 
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

    /**
     * Submits a replenishment request for a medicine.
     * Updates the inventory file to set the requested quantity and marks the status as "Pending".
     */
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

    /**
     * Validates if a medicine exists in the inventory file.
     *
     * @param medicineName The name of the medicine to validate.
     * @return true if the medicine exists, false otherwise.
     */
    private boolean isMedicineValid(String medicineName) {
        try (BufferedReader br = new BufferedReader(new FileReader(MEDICINE_CSV))) {
            String line;
            boolean isFirstLine = true;

            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                String[] values = line.split(",");
                if (values.length >= 1 && values[0].equalsIgnoreCase(medicineName)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println("Error validating medicine: " + e.getMessage());
        }
        return false;
    }

    /**
     * Ensures the input is a valid integer.
     *
     * @param scanner The Scanner object to read user input.
     * @param prompt  The prompt message to display to the user.
     * @return The validated integer input.
     */
    private int getValidIntegerInput(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
            }
        }
    }
}