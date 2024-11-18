
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class TEST {

    private static final String CSV_FILE_PATH = "C:/Users/mingh/OneDrive/Desktop/Y2S1/sc2002 oop/PROJECT_2002_v2/HMS-SC2002/Data/Medicine_List.csv";

    public void viewMedicationInventory() {
        System.out.println("Viewing medication inventory...\n");
        System.out.printf("%-15s %-15s %-15s %-20s %-20s %-20s%n",
                "Medicine Name", "Initial Stock", "Current Stock",
                "Low Stock Level Alert", "Request Replenishment", "Replenishment Approved");

        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE_PATH))) {
            String line;
            boolean isFirstLine = true;

            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false; // Skip the header row
                    continue;
                }

                String[] values = line.split(",");
                if (values.length == 6) {
                    System.out.printf("%-15s %-15s %-15s %-20s %-20s %-20s%n",
                            values[0], values[1], values[2], values[3], values[4], values[5]);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        TEST menu = new TEST();
        menu.viewMedicationInventory();
    }
}
