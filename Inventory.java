import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Inventory {

    private static final String CSV_FILE_PATH = "./Data/Medicine_List.csv";
    private static final String CSV_FILE_PATH2 = "./Data/Medical_Equipment_List.csv";

    public void addNewMedicine() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Adding a new medicine to the inventory...");

        System.out.print("Enter Medicine Name: ");
        String name = scanner.nextLine();

        System.out.print("Enter Initial Stock: ");
        int initialStock = Integer.parseInt(scanner.nextLine());

        System.out.print("Enter Low Stock Level Alert: ");
        int lowStockLevelAlert = Integer.parseInt(scanner.nextLine());

        // Create a new Medicine object
        Medicine newMedicine = new Medicine(name, initialStock, lowStockLevelAlert);

        scanner.close();

        // Append the new medicine entry to the CSV file
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CSV_FILE_PATH, true))) {
            bw.write(newMedicine.toCsvFormat());
            bw.newLine();
            System.out.println("New medicine added successfully.");
        } 
        
        catch (IOException e) {
        System.out.println("Error adding new medicine to file: " + e.getMessage());
        }
    }

    public void addNewMedicalEquipment() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Adding a new medical equipment to the inventory...");

        System.out.print("Enter Equipment Name: ");
        String name = scanner.nextLine();

        System.out.print("Enter Initial Stock: ");
        int initialStock = Integer.parseInt(scanner.nextLine());

        System.out.print("Enter Low Stock Level Alert: ");
        int lowStockLevelAlert = Integer.parseInt(scanner.nextLine());

        // Create a new MedicalEquipment object
        MedicalEquipment newEquipment = new MedicalEquipment(name, initialStock, lowStockLevelAlert);

        scanner.close();

        // Append the new equipment entry to the CSV file
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CSV_FILE_PATH2, true))) {
            bw.write(newEquipment.toCsvFormat());
            bw.newLine();
            System.out.println("New medical equipment added successfully.");
        }

        catch (IOException e) {
        System.out.println("Error adding new medical equipment to file: " + e.getMessage());
        }
    }

    public void deleteMedicine() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the Medicine Name to remove from inventory: ");
        String medicineNameToDelete = scanner.nextLine();

        List<String> updatedData = new ArrayList<>();
        boolean medicineFound = false;

        scanner.close();

        // Read the existing data
        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE_PATH))) {
            String line;
            boolean isFirstLine = true;

            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    // Add the header to the updated data
                    updatedData.add(line);
                    isFirstLine = false;
                    continue;
                }

                String[] values = line.split(",");
                String medicineName = values[0];

                if (!medicineName.equalsIgnoreCase(medicineNameToDelete)) {
                    // Add line to updated data if it's not the medicine to delete
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

        // Write the updated data back to the file
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CSV_FILE_PATH))) {
            for (String dataLine : updatedData) {
                bw.write(dataLine);
                bw.newLine();
            }
        } 
        
        catch (IOException e) {
            System.out.println("Error writing to the file: " + e.getMessage());
        }
    }

    public void deleteMedicalEquipment() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the Equipment Name to remove from inventory: ");
        String equipmentNameToDelete = scanner.nextLine();

        List<String> updatedData = new ArrayList<>();
        boolean equipmentFound = false;

        scanner.close();

        // Read the existing data
        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE_PATH2))) {
            String line;
            boolean isFirstLine = true;

            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    // Add the header to the updated data
                    updatedData.add(line);
                    isFirstLine = false;
                    continue;
                }

                String[] values = line.split(",");
                String equipmentName = values[0];

                if (!equipmentName.equalsIgnoreCase(equipmentNameToDelete)) {
                    // Add line to updated data if it's not the equipment to delete
                    updatedData.add(line);
                } else {
                    equipmentFound = true;
                    System.out.println("Equipment '" + equipmentNameToDelete + "' has been deleted from the inventory.");
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
            return;
        }

        if (!equipmentFound) {
            System.out.println("Equipment '" + equipmentNameToDelete + "' not found in the inventory.");
            return;
        }

        // Write the updated data back to the file
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CSV_FILE_PATH2))) {
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
        System.out.printf("%-15s %-15s %-15s %-20s %-20s %-20s %-25s%n",
                "Medicine Name", "Initial Stock", "Current Stock",
                "Low Stock Level Alert", "Request Replenishment", "Replenishment Approval Status", "Last Update");
    
        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE_PATH))) {
            String line;
            boolean isFirstLine = true;
    
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false; // Skip the header row
                    continue;
                }
    
                String[] values = line.split(",");
                if (values.length >= 7) { // Check for 7 columns
                    System.out.printf("%-15s %-15s %-15s %-20s %-20s %-20s %-25s%n",
                            values[0], values[1], values[2], values[3], values[4], values[5], values[6]);
                }
            }
        } 
        
        catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        }
    }

    public void viewEquipmentInventory() {
        System.out.println("Viewing equipment inventory...\n");
        System.out.printf("%-15s %-15s %-15s %-20s %-20s %-20s %-25s%n",
                "Equipment Name", "Initial Stock", "Current Stock",
                "Low Stock Level Alert", "Request Replenishment", "Replenishment Approval Status", "Last Update");

        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE_PATH2))) {
            String line;
            boolean isFirstLine = true;

            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false; // Skip the header row
                    continue;
                }

                String[] values = line.split(",");
                if (values.length >= 7) { // Check for 7 columns
                    System.out.printf("%-15s %-15s %-15s %-20s %-20s %-20s %-25s%n",
                            values[0], values[1], values[2], values[3], values[4], values[5], values[6]);
                }
            }
        }

        catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        }
    }

    public void submitReplenishmentRequest(String medicineName, int replenishmentAmount) {
        List<String[]> csvData = new ArrayList<>();
        boolean medicineFound = false;

        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE_PATH))) {
            String line;
            boolean isFirstLine = true;

            // Read all lines from the file
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                
                // Check if it's the header row
                if (isFirstLine) {
                    isFirstLine = false;
                } else {
                    // Check if the current row matches the medicine name
                    if (values[0].equalsIgnoreCase(medicineName)) {
                        values[4] = String.valueOf(replenishmentAmount); // Update the fifth column
                        medicineFound = true;
                        System.out.println("Replenishment request updated for " + medicineName + " to " + replenishmentAmount);
                    }
                }

                // Add row to in-memory data structure
                csvData.add(values);
            }
        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
            return;
        }

        if (!medicineFound) {
            System.out.println("Medicine " + medicineName + " not found in inventory.");
            return;
        }

        // Write updated data back to the file
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CSV_FILE_PATH))) {
            for (String[] row : csvData) {
                bw.write(String.join(",", row));
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error writing to the file: " + e.getMessage());
        }
    }

    public void approveSubmissionRequest(String medicineName, String approvalStatus) {
        List<String[]> csvData = new ArrayList<>();
        boolean medicineFound = false;

        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE_PATH))) {
            String line;
            boolean isFirstLine = true;

            // Read all lines from the file
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                
                // Check if it's the header row
                if (isFirstLine) {
                    isFirstLine = false;
                } else {
                    // Check if the current row matches the medicine name
                    if (values[0].equalsIgnoreCase(medicineName)) {
                        values[5] = approvalStatus; // Update the sixth column
                        medicineFound = true;
                        System.out.println("Replenishment request for " + medicineName + " marked as " + approvalStatus);
                    }
                }

                // Add row to in-memory data structure
                csvData.add(values);
            }
        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
            return;
        }

        if (!medicineFound) {
            System.out.println("Medicine " + medicineName + " not found in inventory.");
            return;
        }

        // Write updated data back to the file
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CSV_FILE_PATH))) {
            for (String[] row : csvData) {
                bw.write(String.join(",", row));
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error writing to the file: " + e.getMessage());
        }
    }

    public void updateMedicineList() {
        List<String[]> csvData = new ArrayList<>();
        boolean updated = false;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE_PATH))) {
            String line;
            boolean isFirstLine = true;

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");

                // Check if it's the header row
                if (isFirstLine) {
                    isFirstLine = false;
                } else {
                    // Check if Replenishment Approved is marked as "Approved" or "Rejected"
                    String approvalStatus = values[5].trim();
                    if ("Approved".equalsIgnoreCase(approvalStatus)) {
                        int currentStock = Integer.parseInt(values[2].trim());
                        int requestedReplenishment = Integer.parseInt(values[4].trim());

                        // Update Initial Stock (column 2) to be the sum of current stock and requested replenishment
                        int newInitialStock = currentStock + requestedReplenishment;
                        values[1] = String.valueOf(newInitialStock);

                        // Update Current Stock (column 3) to match the new Initial Stock
                        values[2] = String.valueOf(newInitialStock);

                        // Update column 7 with "Approved" and the current date and time
                        values[6] = "Approved on " + LocalDateTime.now().format(formatter);

                        // Zero out Request Replenishment (column 5) and Replenishment Approved (column 6)
                        values[4] = "0";
                        values[5] = "-";

                        updated = true;
                        System.out.println("Updated replenishment for medicine: " + values[0]);

                    } else if ("Rejected".equalsIgnoreCase(approvalStatus)) {
                        // Update column 7 with "Rejected" and the current date and time
                        values[6] = "Rejected on " + LocalDateTime.now().format(formatter);

                        // Zero out Request Replenishment (column 5) and Replenishment Approved (column 6)
                        values[4] = "0";
                        values[5] = "-";

                        updated = true;
                        System.out.println("Replenishment request rejected for medicine: " + values[0]);
                    }
                }

                // Add the row (updated or not) to the list
                csvData.add(values);
            }
        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
            return;
        }

        if (!updated) {
            System.out.println("No updates were made, as no medicines had approved or rejected replenishment.");
            return;
        }

        // Write updated data back to the file
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CSV_FILE_PATH))) {
            for (String[] row : csvData) {
                bw.write(String.join(",", row));
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error writing to the file: " + e.getMessage());
        }
    }
}
