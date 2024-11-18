import java.io.*;
import java.util.*;

    
public class TEST {
    
    private static final String APPOINTMENT_CSV = "C:/Users/mingh/OneDrive/Desktop/Y2S1/sc2002 oop/PROJECT_2002_v2/HMS-SC2002/Data/AppointmentRecord1.csv";
    private static final String MEDICINE_CSV = "C:/Users/mingh/OneDrive/Desktop/Y2S1/sc2002 oop/PROJECT_2002_v2/HMS-SC2002/Data/Medicine_List1.csv";    

    
    public void submitReplenishmentRequest() {
            Scanner scanner = new Scanner(System.in);
            boolean validInput = false;
    
            try (BufferedReader br = new BufferedReader(new FileReader(MEDICINE_CSV))) {
                // Read the entire CSV into a list
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
    
                // Iterate through the CSV
                while ((line = br.readLine()) != null) {
                    String[] values = line.split(",");
                    if (isFirstLine) {
                        updatedFileContent.append(line).append("\n");
                        isFirstLine = false;
                        continue;
                    }
    
                    if (values[0].equalsIgnoreCase(inputMedicine)) {
                        medicineFound = true;
    
                        // Update the "Request Replenishment" and "Replenishment Approved" columns
                        values[4] = String.valueOf(inputQuantity); // Update quantity in the 5th column
                        values[5] = "Pending"; // Update status to "Pending"
    
                        // Append the updated row
                        updatedFileContent.append(String.join(",", values)).append("\n");
                    } else {
                        // Append unmodified rows
                        updatedFileContent.append(line).append("\n");
                    }
                }
    
                if (!medicineFound) {
                    System.out.println("Error: Medicine not found in the inventory.");
                    return;
                }
    
                // Write the updated content back to the file
                try (BufferedWriter bw = new BufferedWriter(new FileWriter(MEDICINE_CSV))) {
                    bw.write(updatedFileContent.toString());
                }
    
                System.out.println("Replenishment request submitted successfully!");
    
            } catch (IOException e) {
                System.out.println("Error accessing the file: " + e.getMessage());
                e.printStackTrace();
            }
        }
    
    public static void main(String[] args) {
            TEST replenishment = new TEST();
            replenishment.submitReplenishmentRequest();
        }
}
