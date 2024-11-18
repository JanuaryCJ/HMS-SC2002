import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Pharmacist extends User {

    protected String staffName;

    public Pharmacist(String hospitalID)
    {
        super(hospitalID);
    }

    private void viewAppointmentOutcomeRecord() {
        Inventory menu = new Inventory();
        menu.viewAppointmentRecords(); 
    }

    private void updatePrescriptionStatus() {
        Inventory menu = new Inventory();
        System.out.println("Updating prescription status...");
        menu.viewAndUpdatePrescription();
    }

    //private void viewMedicationInventory() {
        //Inventory menu = new Inventory();
        //System.out.println("Viewing medication inventory...");
        //menu.viewMedicationInventory();
    //}

    private void viewMedicationInventory() {
        String CSV_FILE_PATH = "C:/Users/mingh/OneDrive/Desktop/Y2S1/sc2002 oop/PROJECT_2002_v2/HMS-SC2002/Data/Medicine_List.csv";
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

    private void submitReplenishmentRequest() {
        Inventory menu = new Inventory();
        System.out.println("Submitting replenishment request...");
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Enter the medicine name: ");
            String medicineName = scanner.nextLine();
            System.out.print("Enter the replenishment amount: ");
            int amount = scanner.nextInt();
            menu.submitReplenishmentRequest(medicineName, amount);
            menu.updateMedicineList();
            System.out.println("Replenishment request submitted...");
        }
        catch (Exception e) {
            System.out.println("Something went wrong.");
        }
    }
    private void addNewMedicine(){
        Inventory menu = new Inventory();
        System.out.println("Adding new medicine to inventory...");
        menu.addNewMedicine();
    }

    private void deleteMedicine(){
        Inventory menu = new Inventory();
        System.out.println("Removing medicine from inventory...");
        menu.deleteMedicine();
    }

    //for approval
    //public void approveReplenishment() {
        //System.out.println("Checking replenishment request...");
        //menu.updateMedicineList();
        //menu.viewMedicationInventory();
        //Scanner scanner = new Scanner(System.in);
        //System.out.print("Enter the medicine name: ");
        //String medicineName = scanner.nextLine();

        //System.out.print("Enter approval status (Approved/Rejected): ");
        //String approvalStatus = scanner.nextLine();

        //menu.approveSubmissionRequest(medicineName, approvalStatus);
        //System.out.println("Replenishment request Approved/Rejected...");
        //scanner.close();
    //}

    protected void displayMenu() {

    	Hospital hospital = new Hospital();
        staffName = hospital.getName(hospitalID, this);
        System.out.println("Welcome Pharmacist, " + staffName);
        

        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("---- Pharmacist Menu ----");
            System.out.println("1. View Appointment Outcome Record");
            System.out.println("2. Update Prescription Status");
            System.out.println("3. View Medication Inventory");
            System.out.println("4. Submit Replenishment Request");
            System.out.println("5. Add New Medicine From Inventory");
            System.out.println("6. Remove Medicine From Inventory");
            System.out.println("7. Logout");

            int choice = sc.nextInt();
            sc.close();
            switch (choice) {
                case 1:
                    viewAppointmentOutcomeRecord();
                    break;
                case 2:
                    updatePrescriptionStatus();
                    break;
                case 3:
                    viewMedicationInventory();
                    break;
                case 4:
                    submitReplenishmentRequest();
                    break;
                case 5:
                    addNewMedicine();
                    break;
                case 6:
                    deleteMedicine();
                    break;
                case 7:
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
    
}
