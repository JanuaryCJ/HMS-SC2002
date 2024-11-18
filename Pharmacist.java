import java.util.Scanner;

public class Pharmacist extends User {

    protected String staffName;

    public Pharmacist(String hospitalID)
    {
        super(hospitalID);
    }

    private void viewAppointmentOutcomeRecord() {
        AppointmentAction apmenu = new AppointmentAction("C:/Users/mingh/OneDrive/Desktop/Y2S1/sc2002 oop/PROJECT_2002_v2/HMS-SC2002/Data/AppointmentRecord1.csv");
        apmenu.viewAppointmentRecords(); 
    }

    private void updatePrescriptionStatus() {
        PrescriptionManagement prescription = new PrescriptionManagement();
        prescription.managePatientPrescription();
    }

    private void viewMedicationInventory() {
        PrescriptionManagement prescription = new PrescriptionManagement();
        prescription.viewMedicationInventory();
    }

    private void submitReplenishmentRequest() {
        PrescriptionManagement prescription = new PrescriptionManagement();
        System.out.println("Submitting replenishment request...");
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Enter the medicine name: ");
            String medicineName = scanner.nextLine();
            System.out.print("Enter the replenishment amount: ");
            int amount = scanner.nextInt();
            prescription.submitReplenishmentRequest(medicineName, amount);
            System.out.println("Replenishment request submitted...");
        }
        catch (Exception e) {
            System.out.println("Something went wrong.");
        }
    }
    
    private void addNewMedicine(){
        PrescriptionManagement prescription = new PrescriptionManagement();
        System.out.println("Adding new medicine to inventory...");
        prescription.addNewMedicine();
    }

    private void deleteMedicine(){
        PrescriptionManagement prescription = new PrescriptionManagement();
        System.out.println("Removing medicine from inventory...");
        prescription.deleteMedicine();
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
