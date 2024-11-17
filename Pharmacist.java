import java.util.Scanner;

public class Pharmacist extends User {

    protected String staffName;
    private final Inventory menu;
    private final AppointmentAction record;

    public Pharmacist(String hospitalID)
    {
        super(hospitalID);
        this.menu = new Inventory(); // Association with Inventory
        this.record = new AppointmentAction("./Data/Medicine_List.csv"); 
    }

    //private String displayStaffName(Administrator admin)
    //{
        //return admin.getStaffName(hospitalID, this);
    //}
    
    @Override
    protected void displayMenu() {
        //Administrator admin = new Administrator(hospitalID);
        //staffName = displayStaffName(admin);
        //System.out.println("Welcome Pharmacist, " + staffName);
        System.out.println("---- Pharmacist Menu ----");
        System.out.println("1. View Appointment Outcome Record");
        System.out.println("2. Update Prescription Status");
        System.out.println("3. View Medication Inventory");
        System.out.println("4. Submit Replenishment Request");
        System.out.println("5. Add New Medicine From Inventory");
        System.out.println("6. Remove Medicine From Inventory");
        System.out.println("7. Logout");

        try (Scanner scanner = new Scanner(System.in)) {
            boolean isRunning = true;
            
            while (isRunning) {
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();
                
                switch (choice) {
                    case 1 -> viewAppointmentOutcomeRecord();
                    case 2 -> updatePrescriptionStatus();
                    case 3 -> viewMedicationInventory();
                    case 4 -> submitReplenishmentRequest();
                    case 5 -> addNewMedicine();
                    case 6 -> deleteMedicine();
                    case 7 -> {
                        System.out.println("Logging out...");
                        isRunning = false;
                    }
                    default -> System.out.println("Invalid choice. Please try again.");
                }
            }
        }
        catch (Exception e) {
            System.out.println("Something went wrong.");
        }
    }

    // Placeholder methods to demonstrate functionality
    private void viewAppointmentOutcomeRecord() {
        System.out.println("Viewing appointment outcome record...");
        record.ReadPatientAppointment();
    }

    private void updatePrescriptionStatus() {
        System.out.println("Updating prescription status...");
        record.viewAndUpdatePrescription();

    }

    private void viewMedicationInventory() {
        System.out.println("Viewing medication inventory...");
        menu.updateMedicineList();
        menu.viewMedicationInventory();
    }

    private void submitReplenishmentRequest() {
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
        System.out.println("Adding new medicine to inventory...");
        menu.addNewMedicine();
    }

    private void deleteMedicine(){
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
    
}
