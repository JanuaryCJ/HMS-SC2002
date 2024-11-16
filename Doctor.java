import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class Doctor extends User {

    protected String staffName;
    private String appointmentsPath = "D:/Java_hw/HMS/src/Data/AppointmentRecord.csv";
    private String medicalRecordsPath = "D:/Java_hw/HMS/src/Data/MedicalRecords.csv";
    private String patientListPath = "D:/Java_hw/HMS/src/Data/Patient_List.csv";
    
    public Doctor(String hospitalID) {
        super(hospitalID);
    }

    private String displayStaffName(Administrator admin) {
        return admin.getStaffName(hospitalID, this);
    }

    public void viewPatientMedicalRecords() {
        List<String> patientsUnderCare = new ArrayList<>();
        String line;
        String csvSeparator = ",";

        try (BufferedReader br = new BufferedReader(new FileReader(medicalRecordsPath))) {
            while ((line = br.readLine()) != null) {
                String[] values = line.split(csvSeparator);

                // Check if column 8 (Doctor ID) matches the logged-in doctor (hospitalID)
                if (values.length > 8 && values[8].equals(hospitalID) && !patientsUnderCare.contains(values[1])) {
                    patientsUnderCare.add(values[1]); // Add unique patient names under care
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading medical records: " + e.getMessage());
            return;
        }

        if (patientsUnderCare.isEmpty()) {
            System.out.println("No patients under your care.");
            return;
        }

        // List all unique patients under this doctor's care
        System.out.println("Select a patient to view medical records:");
        for (int i = 0; i < patientsUnderCare.size(); i++) {
            System.out.println((i + 1) + ": " + patientsUnderCare.get(i));
        }

        Scanner sc = new Scanner(System.in);
        int choice = sc.nextInt() - 1;

        if (choice >= 0 && choice < patientsUnderCare.size()) {
            String selectedPatient = patientsUnderCare.get(choice);
            System.out.println("Medical Records for " + selectedPatient + ":");

            try (BufferedReader br = new BufferedReader(new FileReader(medicalRecordsPath))) {
                while ((line = br.readLine()) != null) {
                    String[] values = line.split(csvSeparator);

                    // Check if column 1 (Patient Name) matches the selected patient
                    if (values.length > 1 && values[1].equals(selectedPatient)) {
                        // Print all fields for the selected patient
                        System.out.println("Patient ID: " + values[0]);
                        System.out.println("Name: " + values[1]);
                        System.out.println("Date of Birth: " + values[2]);
                        System.out.println("Gender: " + values[3]);
                        System.out.println("Blood Type: " + values[4]);
                        System.out.println("Contact Info: " + values[5]);
                        System.out.println("Contact Info: " + values[6]);
                        System.out.println("Appointment Date: " + values[7]);
                        System.out.println("Doctor ID: " + values[8]);
                        System.out.println("Doctor Name: " + values[9]);
                        System.out.println("Time: " + values[10]);
                        System.out.println("Diagnosis: " + values[11]);
                        System.out.println("Treatment: " + values[12]);
                        System.out.println("Prescription: " + values[13]);
                        System.out.println("Notes: " + values[14]);
                        System.out.println("------------------------------------");
                    }
                }
            } catch (IOException e) {
                System.err.println("Error reading medical records: " + e.getMessage());
            }
        } else {
            System.out.println("Invalid choice.");
        }
    }


    public void updatePatientMedicalRecords() {
        List<String> patientsUnderCare = new ArrayList<>();
        String line;
        String csvSeparator = ",";

        // Step 1: Identify patients under the doctor's care
        try (BufferedReader br = new BufferedReader(new FileReader(medicalRecordsPath))) {
            while ((line = br.readLine()) != null) {
                String[] values = line.split(csvSeparator);

                // Check if the record belongs to the logged-in doctor
                if (values.length > 8 && values[8].equals(hospitalID) && !patientsUnderCare.contains(values[1])) {
                    patientsUnderCare.add(values[1]); // Add unique patient names
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading medical records: " + e.getMessage());
            return;
        }

        if (patientsUnderCare.isEmpty()) {
            System.out.println("No patients under your care.");
            return;
        }

        // Step 2: Let the doctor select a patient
        System.out.println("Select a patient to update medical records:");
        for (int i = 0; i < patientsUnderCare.size(); i++) {
            System.out.println((i + 1) + ": " + patientsUnderCare.get(i));
        }

        Scanner sc = new Scanner(System.in);
        int choice = sc.nextInt() - 1;
        sc.nextLine(); // Consume newline

        if (choice >= 0 && choice < patientsUnderCare.size()) {
            String selectedPatient = patientsUnderCare.get(choice);
            System.out.println("Medical Records for " + selectedPatient + ":");

            List<String[]> recordsToEdit = new ArrayList<>();
            List<String> allLines = new ArrayList<>();

            // Step 3: Load all records and identify editable ones
            try (BufferedReader br = new BufferedReader(new FileReader(medicalRecordsPath))) {
                while ((line = br.readLine()) != null) {
                    String[] values = line.split(csvSeparator);
                    allLines.add(line); // Add all lines for later rewriting

                    // Check if the record belongs to the selected patient and doctor
                    if (values.length > 8 && values[1].equals(selectedPatient) && values[8].equals(hospitalID)) {
                        recordsToEdit.add(values);
                        System.out.println((recordsToEdit.size()) + ": ");
                        System.out.println("Patient ID: " + values[0]);
                        System.out.println("Name: " + values[1]);
                        System.out.println("Appointment Date: " + values[7]);
                        System.out.println("Time: " + values[10]);
                        System.out.println("Diagnosis: " + values[11]);
                        System.out.println("Treatment: " + values[12]);
                        System.out.println("Prescription: " + values[13]);
                        System.out.println("Notes: " + values[14]);
                        System.out.println("------------------------------------");
                    }
                }
            } catch (IOException e) {
                System.err.println("Error reading medical records: " + e.getMessage());
                return;
            }

            if (recordsToEdit.isEmpty()) {
                System.out.println("No medical records available for editing.");
                return;
            }

            // Step 4: Let the doctor select a specific record
            System.out.println("Select a record to edit (1, 2, ...):");
            int recordChoice = sc.nextInt() - 1;
            sc.nextLine(); // Consume newline

            if (recordChoice >= 0 && recordChoice < recordsToEdit.size()) {
                String[] selectedRecord = recordsToEdit.get(recordChoice);

                while (true) {
                    System.out.println("What do you want to update?");
                    System.out.println("1: Diagnosis");
                    System.out.println("2: Treatment");
                    System.out.println("3: Prescription");
                    System.out.println("4: Notes");
                    System.out.println("5: Return to Doctor Menu");

                    int updateChoice = sc.nextInt();
                    sc.nextLine(); // Consume newline

                    switch (updateChoice) {
                        case 1:
                            System.out.println("Enter new diagnosis:");
                            selectedRecord[11] = sc.nextLine();
                            break;
                        case 2:
                            System.out.println("Enter new treatment:");
                            selectedRecord[12] = sc.nextLine();
                            break;
                        case 3:
                            System.out.println("Enter new prescription:");
                            selectedRecord[13] = sc.nextLine();
                            break;
                        case 4:
                            System.out.println("Enter new notes:");
                            selectedRecord[14] = sc.nextLine();
                            break;
                        case 5:
                            System.out.println("Returning to Doctor Menu...");
                            break;
                        default:
                            System.out.println("Invalid choice. Try again.");
                            continue;
                    }

                    if (updateChoice == 5) break;

                    System.out.println("Updated record:");
                    System.out.println("Date: " + selectedRecord[7] + ", Diagnosis: " + selectedRecord[11] +
                            ", Treatment: " + selectedRecord[12] + ", Prescription: " + selectedRecord[13] +
                            ", Notes: " + selectedRecord[14]);
                }

                // Step 5: Replace only the selected record in allLines
                for (int i = 0; i < allLines.size(); i++) {
                    String[] currentRecord = allLines.get(i).split(csvSeparator);

                    // Ensure all key fields match before replacing the record
                    if (currentRecord[0].equals(selectedRecord[0]) && currentRecord[7].equals(selectedRecord[7]) &&
                            currentRecord[10].equals(selectedRecord[10]) && currentRecord[8].equals(hospitalID)) {
                        allLines.set(i, String.join(",", selectedRecord));
                        break; // Replace only the first matching record
                    }
                }

                // Step 6: Write allLines back to the file
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(medicalRecordsPath))) {
                    for (String updatedLine : allLines) {
                        writer.write(updatedLine);
                        writer.newLine();
                    }
                } catch (IOException e) {
                    System.err.println("Error writing medical records: " + e.getMessage());
                }
            } else {
                System.out.println("Invalid record selection.");
            }
        } else {
            System.out.println("Invalid patient selection.");
        }
    }




    public void viewPersonalSchedule() {
        List<String[]> unavailableAppointments = new ArrayList<>();
        List<String> unavailableSlots = new ArrayList<>();
        String line;
        String csvSeparator = ",";
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("d/M/yyyy");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");

        try (BufferedReader br = new BufferedReader(new FileReader(appointmentsPath))) {
            while ((line = br.readLine()) != null) {
                String[] values = line.split(csvSeparator);

                // Mark slots as unavailable for both booked and confirmed appointments
                if (values.length > 4  && (values[4].equals("Booked") || values[4].equals("Confirmed"))) {
                    unavailableAppointments.add(values);

                    // Standardize the date format for unavailable slots
                    LocalDate date = LocalDate.parse(values[2], inputFormatter);
                    String formattedDate = date.format(outputFormatter);
                    unavailableSlots.add(formattedDate + "," + values[3]); // Combine date and time as a key
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading appointments: " + e.getMessage());
        }

        // Print unavailable appointments
        System.out.println("Your Upcoming Appointments (Status: Booked or Confirmed):");
        for (String[] app : unavailableAppointments) {
        	if(!app[0].equals("NIL"))
        		System.out.println("Patient: " + app[0] + ", Date: " + app[2] + ", Time: " + app[3] + ", Status: " + app[4]);
        }

        // Print unavailable slots
        

        // Print available slots from 2024/11/2 to 2024/11/8
        System.out.println("\nAvailable Slots:");
        for (int i = 2; i <= 8; i++) { 
            String date = "2024/11/" + (i < 10 ? "0" + i : i);
            for (int time = 1000; time <= 1700; time += 100) {
                String slotKey = date + "," + time;
                if (!unavailableSlots.contains(slotKey)) {
                    System.out.println("Date: " + date + ", Time: " + time);
                }
            }
        }
    }


    public void setAvailability() {
        Scanner sc = new Scanner(System.in);

        System.out.println("1. View Available Slots and Update");
        System.out.println("2. View Unavailable Slots and Update");
        int choice = sc.nextInt();
        sc.nextLine(); // Consume newline

        if (choice == 1) {
            // View available slots and book a slot
            List<String> availableSlots = new ArrayList<>();
            List<String> unavailableSlots = new ArrayList<>();
            String line;

            // Collect unavailable slots from the CSV
            try (BufferedReader br = new BufferedReader(new FileReader(appointmentsPath))) {
                while ((line = br.readLine()) != null) {
                    String[] values = line.split(",");
                    if (values.length > 4 && (values[4].equals("Booked") || values[4].equals("Confirmed"))) {
                        unavailableSlots.add(values[2] + "," + values[3]); // Use date and time directly
                    }
                }
            } catch (IOException e) {
                System.err.println("Error reading appointments: " + e.getMessage());
            }

            // Display available slots
            System.out.println("Available Slots:");
            for (int i = 2; i <= 8; i++) {
                String date = i + "/11/2024"; // Use the `DD/MM/YYYY` format directly
                for (int time = 1000; time <= 1700; time += 100) {
                    String slotKey = date + "," + time;
                    if (!unavailableSlots.contains(slotKey)) {
                        availableSlots.add(slotKey);
                        System.out.println("Date: " + date + ", Time: " + time);
                    }
                }
            }

            // Book a slot
            if (availableSlots.isEmpty()) {
                System.out.println("No available slots.");
                return;
            }

            System.out.println("Enter date (DD/MM/YYYY):");
            String date = sc.nextLine();

            System.out.println("Enter time (e.g., 1000 for 10:00 AM):");
            String time = sc.nextLine();

            String slotToBook = date + "," + time;

            if (availableSlots.contains(slotToBook)) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(appointmentsPath, true))) {
                    writer.write("NIL," + staffName + "," + date + "," + time + ",Confirmed\n");
                    System.out.println("Slot updated successfully.");
                } catch (IOException e) {
                    System.err.println("Error booking slot: " + e.getMessage());
                }
            } else {
                System.out.println("Invalid slot selection.");
            }
        } else if (choice == 2) {
            // View slots booked by doctor and cancel
            List<String[]> doctorBookedSlots = new ArrayList<>();
            String line;

            try (BufferedReader br = new BufferedReader(new FileReader(appointmentsPath))) {
                while ((line = br.readLine()) != null) {
                    String[] values = line.split(",");
                    // Filter by patient name = NIL and doctor name matches logged-in doctor
                    if (values.length > 4 && values[0].equals("NIL") && values[1].equals(staffName) && values[4].equals("Confirmed")) {
                        doctorBookedSlots.add(values);
                    }
                }
            } catch (IOException e) {
                System.err.println("Error reading appointments: " + e.getMessage());
            }

            if (doctorBookedSlots.isEmpty()) {
                System.out.println("No unavailable slots.");
                return;
            }

            // Display slots booked by doctor
            System.out.println("Unavailable Slots:");
            for (int i = 0; i < doctorBookedSlots.size(); i++) {
                String[] slot = doctorBookedSlots.get(i);
                System.out.println((i + 1) + ": Date: " + slot[2] + ", Time: " + slot[3]);
            }
            System.out.println((doctorBookedSlots.size() + 1) + ": return to the Doctor Menu");

            // Cancel a slot or return to menu
            System.out.println("Select a slot to update:");
            int slotChoice = sc.nextInt() - 1;

            if (slotChoice == doctorBookedSlots.size()) {
                System.out.println("Returning to Doctor Menu...");
                return;
            }

            if (slotChoice >= 0 && slotChoice < doctorBookedSlots.size()) {
                String[] slotToCancel = doctorBookedSlots.get(slotChoice);
                List<String> updatedLines = new ArrayList<>();

                // Update the CSV
                try (BufferedReader br = new BufferedReader(new FileReader(appointmentsPath))) {
                    while ((line = br.readLine()) != null) {
                        String[] values = line.split(",");
                        if (values[0].equals(slotToCancel[0]) && values[1].equals(slotToCancel[1]) &&
                                values[2].equals(slotToCancel[2]) && values[3].equals(slotToCancel[3])) {
                            values[4] = "Canceled"; // Mark the slot as canceled
                        }
                        updatedLines.add(String.join(",", values));
                    }
                } catch (IOException e) {
                    System.err.println("Error reading appointments: " + e.getMessage());
                    return;
                }

                try (BufferedWriter writer = new BufferedWriter(new FileWriter(appointmentsPath))) {
                    for (String updatedLine : updatedLines) {
                        writer.write(updatedLine);
                        writer.newLine();
                    }
                } catch (IOException e) {
                    System.err.println("Error updating appointments: " + e.getMessage());
                }

                System.out.println("Slot updated successfully.");
            } else {
                System.out.println("Invalid slot selection.");
            }
        } else {
            System.out.println("Invalid choice.");
        }
    }




    public void acceptOrDeclineRequests() {
        List<String[]> bookedAppointments = new ArrayList<>();
        String line;

        // Collect booked appointments
        try (BufferedReader br = new BufferedReader(new FileReader(appointmentsPath))) {
            while ((line = br.readLine()) != null) {
                // Skip empty lines or lines that do not have sufficient columns
                if (line.trim().isEmpty() || line.split(",").length < 5) {
                    continue;
                }

                String[] values = line.split(",");
                // Only add appointments with "Booked" status
                if (values[4].equalsIgnoreCase("Booked")) {
                    bookedAppointments.add(values);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading appointments: " + e.getMessage());
            return;
        }

        if (bookedAppointments.isEmpty()) {
            System.out.println("No booked appointments to respond to.");
            return;
        }

        // Display booked appointments
        System.out.println("Booked Appointments:");
        for (int i = 0; i < bookedAppointments.size(); i++) {
            String[] app = bookedAppointments.get(i);
            System.out.println((i + 1) + ": Patient: " + app[0] + ", Date: " + app[2] + ", Time: " + app[3] + ", Status: " + app[4]);
        }

        Scanner sc = new Scanner(System.in);
        System.out.println("Select an appointment to respond to (1, 2, 3, ...):");
        int choice = sc.nextInt() - 1;

        if (choice >= 0 && choice < bookedAppointments.size()) {
            String[] selectedAppointment = bookedAppointments.get(choice);
            System.out.println("1: Confirm, 2: Cancel");
            int decision = sc.nextInt();

            // Update the appointment status
            String newStatus = (decision == 1) ? "Confirmed" : "Canceled";

            List<String> updatedLines = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new FileReader(appointmentsPath))) {
                while ((line = br.readLine()) != null) {
                    // Skip empty lines or lines with insufficient columns
                    if (line.trim().isEmpty() || line.split(",").length < 5) {
                        updatedLines.add(line);
                        continue;
                    }

                    String[] values = line.split(",");
                    if (values[0].equals(selectedAppointment[0]) && 
                        values[1].equals(selectedAppointment[1]) &&
                        values[2].equals(selectedAppointment[2]) && 
                        values[3].equals(selectedAppointment[3])) {
                        values[4] = newStatus; // Update status
                    }
                    updatedLines.add(String.join(",", values));
                }
            } catch (IOException e) {
                System.err.println("Error reading appointments: " + e.getMessage());
                return;
            }

            // Write updated CSV
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(appointmentsPath))) {
                for (String updatedLine : updatedLines) {
                    writer.write(updatedLine);
                    writer.newLine();
                }
            } catch (IOException e) {
                System.err.println("Error updating appointments: " + e.getMessage());
            }

            System.out.println("Appointment status updated to: " + newStatus);
        } else {
            System.out.println("Invalid choice. Returning to menu.");
        }
    }

    public void viewUpcomingAppointment() {
        List<String[]> unavailableAppointments = new ArrayList<>();
        List<String> unavailableSlots = new ArrayList<>();
        String line;
        String csvSeparator = ",";
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("d/M/yyyy");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");

        try (BufferedReader br = new BufferedReader(new FileReader(appointmentsPath))) {
            while ((line = br.readLine()) != null) {
                String[] values = line.split(csvSeparator);

                // Mark slots as unavailable for both booked and confirmed appointments
                if (values.length > 4 && values[4].equals("Confirmed")) {
                    unavailableAppointments.add(values);

                    // Standardize the date format for unavailable slots
                    LocalDate date = LocalDate.parse(values[2], inputFormatter);
                    String formattedDate = date.format(outputFormatter);
                    unavailableSlots.add(formattedDate + "," + values[3]); // Combine date and time as a key
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading appointments: " + e.getMessage());
        }

        // Print unavailable appointments
        System.out.println("Your Upcoming Appointments (Status: Confirmed):");
        for (String[] app : unavailableAppointments) {
        	if(!app[0].equals("NIL"))
        		System.out.println("Patient: " + app[0] + ", Date: " + app[2] + ", Time: " + app[3] + ", Status: " + app[4]);
        }

        // Print unavailable slots
        

        
    }
    
    public void updateAppointmentRecord() {
        List<String[]> confirmedAppointments = new ArrayList<>();
        String line;

        // Collect confirmed appointments
        try (BufferedReader br = new BufferedReader(new FileReader(appointmentsPath))) {
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length > 4 && values[4].equals("Confirmed") && !values[0].equals("NIL")) { // Check if status is confirmed
                    confirmedAppointments.add(values);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading appointments: " + e.getMessage());
            return;
        }

        if (confirmedAppointments.isEmpty()) {
            System.out.println("No confirmed appointments to update.");
            return;
        }

        // Display confirmed appointments
        System.out.println("Confirmed Appointments:");
        for (int i = 0; i < confirmedAppointments.size(); i++) {
            String[] app = confirmedAppointments.get(i);
            System.out.println((i + 1) + ": Patient: " + app[0] + ", Date: " + app[2] + ", Time: " + app[3]);
        }

        // Select an appointment to update
        Scanner sc = new Scanner(System.in);
        System.out.println("Select an appointment to update (1, 2, 3, ...):");
        int choice = sc.nextInt() - 1;
        sc.nextLine(); // Consume newline

        if (choice < 0 || choice >= confirmedAppointments.size()) {
            System.out.println("Invalid choice.");
            return;
        }

        String[] selectedAppointment = confirmedAppointments.get(choice);

        // Begin updating the selected appointment
        List<String> updatedLines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(appointmentsPath))) {
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");

                // Match the selected appointment
                if (values.length > 4 && values[0].equals(selectedAppointment[0]) &&
                        values[1].equals(selectedAppointment[1]) &&
                        values[2].equals(selectedAppointment[2]) &&
                        values[3].equals(selectedAppointment[3])) {

                    // Ensure all required indices exist
                    while (values.length < 10) {
                        String[] extendedValues = new String[10];
                        System.arraycopy(values, 0, extendedValues, 0, values.length);
                        values = extendedValues;
                    }

                    // Update each field one by one
                    System.out.println("Enter new Diagnosis:");
                    values[5] = sc.nextLine();

                    System.out.println("Enter new Type of Service:");
                    values[6] = sc.nextLine();

                    System.out.println("Enter new Prescribed Medications:");
                    values[7] = sc.nextLine();

                    System.out.println("Enter new Medication Status (e.g., Pending, Dispensed):");
                    values[8] = sc.nextLine();

                    System.out.println("Enter new Notes:");
                    values[9] = sc.nextLine();

                    // Mark as completed
                    values[4] = "Completed";
                    
                    recordToMedicalRecords(values); 

                    // Print updated record for confirmation
                    System.out.println("Updated Record:");
                    System.out.println("Diagnosis: " + values[5]);
                    System.out.println("Type of Service: " + values[6]);
                    System.out.println("Prescribed Medications: " + values[7]);
                    System.out.println("Medication Status: " + values[8]);
                    System.out.println("Notes: " + values[9]);
                    System.out.println("Status: " + values[4]);
                }

                // Add the (possibly updated) line back to the list of updated lines
                updatedLines.add(String.join(",", values));
            }
        } catch (IOException e) {
            System.err.println("Error reading appointments: " + e.getMessage());
            return;
        }

        // Write the updated lines back to the CSV
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(appointmentsPath))) {
            for (String updatedLine : updatedLines) {
                writer.write(updatedLine);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error updating appointments: " + e.getMessage());
        }

        System.out.println("Appointment record updated successfully and marked as Completed.");
    }
        
    public void recordToMedicalRecords(String[] updatedAppointment) {
        // Load patient info from Patient_List.csv
        String[] patientInfo = null;
        try (BufferedReader br = new BufferedReader(new FileReader(patientListPath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length > 1 && values[1].equals(updatedAppointment[0])) { // Match patient name
                    patientInfo = values;
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading patient list: " + e.getMessage());
            return;
        }

        if (patientInfo == null) {
            System.err.println("Patient not found in Patient_List.csv.");
            return;
        }

        // Create a new record for MedicalRecords.csv
        String newRecord = String.join(",", 
                patientInfo[0],  // Patient ID
                patientInfo[1],  // Patient Name
                patientInfo[2],  // Date of Birth
                patientInfo[3],  // Gender
                patientInfo[4],  // Blood Type
                patientInfo[5],  // Contact Info
                updatedAppointment[2], // Appointment Date
                hospitalID, // Doctor's Hospital ID
                staffName,  // Doctor's Name
                updatedAppointment[3], // Appointment Time
                updatedAppointment[5], // Diagnosis
                updatedAppointment[6], // Treatment
                updatedAppointment[8]  // Notes
        );

        // Append the record to MedicalRecords.csv
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(medicalRecordsPath, true))) {
            writer.write(newRecord);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error writing to medical records: " + e.getMessage());
        }

        System.out.println("Medical record updated successfully.");
    }



    
   
    
    protected void displayMenu() {
        Administrator admin = new Administrator(hospitalID, false);
        staffName = displayStaffName(admin);
        System.out.println("Welcome Doctor, " + staffName);

        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("---- Doctor Menu ----");
            System.out.println("1. View Patient Medical Records");
            System.out.println("2. Update Patient Medical Records");
            System.out.println("3. View Personal Schedule");
            System.out.println("4. Set Availability for Appointments");
            System.out.println("5. Accept or Decline Appointment Requests");
            System.out.println("6. View Upcoming Appointments");
            System.out.println("7. Record Appointment Outcome");
            System.out.println("8. Logout");

            int choice = sc.nextInt();
            switch (choice) {
                case 1:
                    viewPatientMedicalRecords();
                    break;
                case 2:
                    updatePatientMedicalRecords();
                    break;
                case 3:
                    viewPersonalSchedule();
                    break;
                case 4:
                    setAvailability();
                    break;
                case 5:
                    acceptOrDeclineRequests();
                    break;
                case 6:
                	viewUpcomingAppointment();
                    break;
                case 7:
                	updateAppointmentRecord();
                    break;
                case 8:
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
}
