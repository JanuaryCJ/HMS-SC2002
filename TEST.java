import java.io.*;
import java.time.LocalDateTime;
import java.util.*;
    
public class TEST {
    
    private static final String APPOINTMENT_CSV = "C:/Users/mingh/OneDrive/Desktop/Y2S1/sc2002 oop/PROJECT_2002_v2/HMS-SC2002/Data/AppointmentRecord1.csv";
    private static final String MEDICINE_CSV = "C:/Users/mingh/OneDrive/Desktop/Y2S1/sc2002 oop/PROJECT_2002_v2/HMS-SC2002/Data/Medicine_List1.csv";    


    public void approveReplenishmentRequest() {
        Scanner scanner = new Scanner(System.in);

        try (BufferedReader br = new BufferedReader(new FileReader(MEDICINE_CSV))) {
            // Read the entire CSV file and store its content
            StringBuilder updatedFileContent = new StringBuilder();
            String line;
            boolean isFirstLine = true;

            System.out.println("Medicines with 'Pending' Replenishment Requests:\n");
            System.out.printf("%-15s %-15s %-15s %-20s %-20s %-20s %-20s%n",
                    "Medicine Name", "Initial Stock", "Current Stock",
                    "Low Stock Level Alert", "Request Replenishment",
                    "Replenishment Approved", "Last Update");

            boolean hasPendingRequests = false;

            // Iterate through the file to find medicines with "Pending" status
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (isFirstLine) {
                    updatedFileContent.append(line).append("\n");
                    isFirstLine = false;
                    continue;
                }

                if (values.length == 7 && values[5].equalsIgnoreCase("Pending")) {
                    hasPendingRequests = true;
                    // Print the pending replenishment row
                    System.out.printf("%-15s %-15s %-15s %-20s %-20s %-20s %-20s%n",
                            values[0], values[1], values[2], values[3], values[4], values[5], values[6]);
                }

                // Add to updated content for later processing
                updatedFileContent.append(line).append("\n");
            }

            if (!hasPendingRequests) {
                System.out.println("\nNo pending replenishment requests found.");
                return;
            }

            // Get user input for medicine name
            String inputMedicine;
            do {
                System.out.print("\nEnter the Medicine Name to process: ");
                inputMedicine = scanner.nextLine().trim();

                if (!isMedicineValid(inputMedicine)) {
                    System.out.println("Invalid Medicine Name.");
                    System.out.print("Do you want to retry or quit? (Type 'Retry' to try again or 'Quit' to exit): ");
                    String choice = scanner.nextLine().trim();
                    if (choice.equalsIgnoreCase("Quit")) {
                        System.out.println("Exiting replenishment approval process.");
                        return;
                    }
                } else {
                    break;
                }
            } while (true);

            // Process approval or rejection
            boolean validInput = false;
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

            // Update the file
            StringBuilder finalFileContent = new StringBuilder();
            String[] rows = updatedFileContent.toString().split("\n");
            for (String row : rows) {
                String[] values = row.split(",");
                if (values.length == 7 && values[0].equalsIgnoreCase(inputMedicine)) {
                    if (approvalStatus.equalsIgnoreCase("Approve")) {
                        values[6] = "Approved on " + LocalDateTime.now(); // Update with date and time
                        values[1] = String.valueOf(Integer.parseInt(values[2]) + Integer.parseInt(values[4])); // Update Initial Stock
                        values[2] = String.valueOf(Integer.parseInt(values[2]) + Integer.parseInt(values[4])); // Sync Current Stock
                        values[4] = "0"; // Reset Request Replenishment
                        values[5] = "NIL"; // Reset Replenishment Approved
                    } else if (approvalStatus.equalsIgnoreCase("Reject")) {
                        values[6] = "Rejected on " + LocalDateTime.now(); // Update with date and time
                        values[4] = "0"; // Reset Request Replenishment
                        values[5] = "NIL"; // Reset Replenishment Approved
                    }
                }
                finalFileContent.append(String.join(",", values)).append("\n");
            }

            // Write the updated content back to the CSV file
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(MEDICINE_CSV))) {
                bw.write(finalFileContent.toString());
            }

            System.out.println("Replenishment request for '" + inputMedicine + "' has been " + approvalStatus + ".");
        } catch (IOException e) {
            System.out.println("Error accessing the file: " + e.getMessage());
            e.printStackTrace();
        }
    }

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

    public static void main(String[] args) {
        TEST approval = new TEST();
        approval.approveReplenishmentRequest();
    }
}
