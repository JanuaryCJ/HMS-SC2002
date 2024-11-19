import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class AppointmentAction implements AppointmentInterface {
	public AppointmentAction(String Path) {
		this.Path=Path;
	}
	private static String Path;
	public static List<Appointment> ReadPatientAppointment(String Name){
		
		List<Appointment> app=new ArrayList<Appointment>();
    	String line;
        String csvSeparator = ",";  

        try (BufferedReader br = new BufferedReader(new FileReader(Path))) {
           
           
            while ((line = br.readLine()) != null) {
                
                String[] values = line.split(csvSeparator);

                
                if (values.length > 0 && values[0].equals(Name)) {
                	Appointment appointment=new Appointment(values[0],values[1],values[2],values[3],values[4],values[5],values[6],values[7],values[8],values[9],values[10]);
                	app.add(appointment);
                	
                }
            }

        } catch (IOException e) {
            System.err.println("Error reading the CSV file: " + e.getMessage());
        }
        return app;
	}
    public List<Appointment> ReadPatientAppointment(){
		
		List<Appointment> app=new ArrayList<Appointment>();
    	String line;
        String csvSeparator = ",";  

        try (BufferedReader br = new BufferedReader(new FileReader(Path))) {
           
           
            while ((line = br.readLine()) != null) {
                
                String[] values = line.split(csvSeparator);

                
               
                	Appointment appointment=new Appointment(values[0],values[1],values[2],values[3],values[4],values[5],values[6],values[7],values[8],values[9],values[10]);
                	app.add(appointment);
                	
                
            }

        } catch (IOException e) {
            System.err.println("Error reading the CSV file: " + e.getMessage());
        }
        return app;
	}
	public void addAppointment(String[] data) {
		try (FileWriter writer = new FileWriter(Path,true)) { 
            
            String row = String.join(",", data);
            writer.append(row).append("\n");
            System.out.println("Appointment booked");
            return;
        } catch (IOException e) {
            e.printStackTrace();
        }
        
	}
	public boolean cancelAppointment(Appointment app) {
		String line;
	    String csvSeparator = ",";  
	    List<String> lines = new ArrayList<>();

	        try (BufferedReader br = new BufferedReader(new FileReader(Path))) {
	           
	            while ((line = br.readLine()) != null) {
	                String[] values = line.split(csvSeparator);             
	                if (values.length > 0 && values[0].equals(app.getPatient())&& values[1].equals(app.getDoctor())&& values[2].equals(app.getDate())&& values[3].equals(app.getTime())&& values[4].equals(app.getStatus()))   {
	                	values[4]="Canceled";
	                	
	                	
	                }
	                lines.add(String.join(",", values));
	            }

	        } catch (IOException e) {
	            System.err.println("Error reading the CSV file: " + e.getMessage());
	            return false;
	        }
	        try (BufferedWriter writer = new BufferedWriter(new FileWriter(Path))) {
	            for (String updatedLine  : lines) {
	                writer.write(updatedLine );
	                writer.newLine();
	            }
	        }catch (IOException e) {
	            System.err.println("Error writing the CSV file: " + e.getMessage());
	            return false;
	        }
	        return true;
	}
	
	public List<Appointment> getValidAppointments(String doctorName) {
        List<Appointment> validAppointments = new ArrayList<>();
        String line;
        String csvSeparator = ",";

        try (BufferedReader br = new BufferedReader(new FileReader(Path))) {
            while ((line = br.readLine()) != null) {
                String[] values = line.split(csvSeparator);

                
                if (values.length > 4 &&
                    (values[4].equalsIgnoreCase("Booked") || values[4].equalsIgnoreCase("Confirmed")) &&
                    !values[0].equalsIgnoreCase("NIL") && 
                    values[1].equalsIgnoreCase(doctorName)) {
                    
                    Appointment appointment = new Appointment(
                        values[0], 
                        values[1], 
                        values[2], 
                        values[3], 
                        values[4], 
                        values.length > 5 ? values[5] : "", 
                        values.length > 6 ? values[6] : "", 
                        values.length > 7 ? values[7] : "", 
                        values.length > 8 ? values[8] : "", 
                        values.length > 9 ? values[9] : "", 
                        values.length > 10 ? values[10] : "" 
                    );
                    validAppointments.add(appointment);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading appointments file: " + e.getMessage());
        }

        return validAppointments;
    }


	public List<String> getAvailableSlots(String doctorName) {
        List<String> unavailableSlots = new ArrayList<>();
        List<String> availableSlots = new ArrayList<>();
        String line;
        String csvSeparator = ",";
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("d/MM/yyyy");

        try (BufferedReader br = new BufferedReader(new FileReader(Path))) {
            while ((line = br.readLine()) != null) {
                String[] values = line.split(csvSeparator);

                if (values.length > 4 && values[1].equals(doctorName) &&
                        (values[4].equals("Booked") || values[4].equals("Confirmed"))) {
                    LocalDate date = LocalDate.parse(values[2], inputFormatter);
                    String formattedDate = date.format(outputFormatter);
                    unavailableSlots.add(formattedDate + "," + values[3]); 
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading appointments: " + e.getMessage());
        }

        LocalDate today = LocalDate.now();
       	
        
        
        
        for (int i = 0; i <= 7; i++) {
            
        	LocalDate date = today.plusDays(i);
            for (int time = 1000; time <= 1700; time += 100) {
                String slotKey = date + "," + time;
                if (!unavailableSlots.contains(slotKey)) {
                    availableSlots.add("Date: " + date + ", Time: " + time);
                }
            }
        }


        return availableSlots;
    }

    
    public List<String[]> getDoctorUnavailableSlots(String doctorName) {
        List<String[]> doctorBookedSlots = new ArrayList<>();
        String line;
        String csvSeparator = ",";

        try (BufferedReader br = new BufferedReader(new FileReader(Path))) {
            while ((line = br.readLine()) != null) {
                String[] values = line.split(csvSeparator);
                if (values.length > 4 && values[0].equals("NIL") && values[1].equals(doctorName) && values[4].equals("Confirmed")) {
                    doctorBookedSlots.add(values);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading appointments: " + e.getMessage());
        }

        return doctorBookedSlots;
    }

    
    public boolean cancelDoctorSlot(String[] slotToCancel) {
        List<String> updatedLines = new ArrayList<>();
        String line;
        String csvSeparator = ",";

        try (BufferedReader br = new BufferedReader(new FileReader(Path))) {
            while ((line = br.readLine()) != null) {
                String[] values = line.split(csvSeparator);
                if (values[0].equals(slotToCancel[0]) && values[1].equals(slotToCancel[1]) &&
                        values[2].equals(slotToCancel[2]) && values[3].equals(slotToCancel[3])) {
                    values[4] = "Canceled"; 
                }
                updatedLines.add(String.join(",", values));
            }
        } catch (IOException e) {
            System.err.println("Error reading appointments: " + e.getMessage());
            return false;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(Path))) {
            for (String updatedLine : updatedLines) {
                writer.write(updatedLine);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error updating appointments: " + e.getMessage());
            return false;
        }

        return true;
    }

    
    public boolean addDoctorSlot(String doctorName, String date, String time) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(Path, true))) {
            writer.write("NIL," + doctorName + "," + date + "," + time + ",Confirmed,NIL,NIL,NIL,NIL,NIL\n");
            return true;
        } catch (IOException e) {
            System.err.println("Error adding slot: " + e.getMessage());
            return false;
        }
    }
    
    public boolean updateAppointmentStatus(Appointment appointment, String newStatus) {
        List<String> updatedLines = new ArrayList<>();
        String line;
        String csvSeparator = ",";

        try (BufferedReader br = new BufferedReader(new FileReader(Path))) {
            while ((line = br.readLine()) != null) {
                String[] values = line.split(csvSeparator);

                
                if (values[0].equals(appointment.getPatient()) &&
                    values[1].equals(appointment.getDoctor()) &&
                    values[2].equals(appointment.getDate()) &&
                    values[3].equals(appointment.getTime()) &&
                    values[4].equals(appointment.getStatus())) {
                    values[4] = newStatus; 
                }

                updatedLines.add(String.join(",", values));
            }
        } catch (IOException e) {
            System.err.println("Error reading appointments: " + e.getMessage());
            return false;
        }

        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(Path))) {
            for (String updatedLine : updatedLines) {
                writer.write(updatedLine);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error updating appointments: " + e.getMessage());
            return false;
        }

        return true;
    }
    
    private boolean updatedFlag = false; 

    public void updateAppointmentRecord(String doctorName) {
        List<String[]> confirmedAppointments = new ArrayList<>();
        String line;

        
        try (BufferedReader br = new BufferedReader(new FileReader(Path))) {
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length > 4 && values[4].equals("Confirmed") && values[1].equals(doctorName) && !values[0].equals("NIL")) {
                    confirmedAppointments.add(values);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading appointments: " + e.getMessage());
            return;
        }

        if (confirmedAppointments.isEmpty()) {
            System.out.println("No confirmed appointments to update.");
            updatedFlag = false; 
            return;
        }

        
        System.out.println("Confirmed Appointments:");
        for (int i = 0; i < confirmedAppointments.size(); i++) {
            String[] app = confirmedAppointments.get(i);
            System.out.println((i + 1) + ": Patient: " + app[0] + ", Date: " + app[2] + ", Time: " + app[3]);
        }

       
        Scanner sc = new Scanner(System.in);
        System.out.println("Select an appointment to update (1, 2, 3, ...):");
        int choice = sc.nextInt() - 1;
        sc.nextLine(); 

        if (choice < 0 || choice >= confirmedAppointments.size()) {
            System.out.println("Invalid choice.");
            updatedFlag = false; 
            return;
        }

        String[] selectedAppointment = confirmedAppointments.get(choice);

        
        List<String> updatedLines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(Path))) {
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");

                
                if (values.length > 4 && values[0].equals(selectedAppointment[0]) &&
                        values[1].equals(selectedAppointment[1]) &&
                        values[2].equals(selectedAppointment[2]) &&
                        values[3].equals(selectedAppointment[3])) {

                    
                    while (values.length < 11) { 
                        String[] extendedValues = new String[11];
                        System.arraycopy(values, 0, extendedValues, 0, values.length);
                        values = extendedValues;
                    }

                    
                    System.out.println("Enter new Diagnosis:");
                    values[5] = sc.nextLine();

                    System.out.println("Enter new Type of Service:");
                    values[6] = sc.nextLine();

                    System.out.println("Enter new Prescribed Medications:");
                    values[7] = sc.nextLine();

                    System.out.println("Enter new Quantity of Medications:");
                    values[8] = sc.nextLine();

                    System.out.println("Enter new Medication Status (e.g., Pending, Dispensed):");
                    values[9] = sc.nextLine();

                    System.out.println("Enter new Notes:");
                    values[10] = sc.nextLine();

                    
                    values[4] = "Completed";
                    updatedFlag = true; 

                    
                    System.out.println("Updated Record:");
                    System.out.println("Diagnosis: " + values[5]);
                    System.out.println("Type of Service: " + values[6]);
                    System.out.println("Prescribed Medications: " + values[7]);
                    System.out.println("Quantity of Medications: " + values[8]);
                    System.out.println("Medication Status: " + values[9]);
                    System.out.println("Notes: " + values[10]);
                    System.out.println("Status: " + values[4]);
                }

                
                updatedLines.add(String.join(",", values));
            }
        } catch (IOException e) {
            System.err.println("Error reading appointments: " + e.getMessage());
            return;
        }

        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(Path))) {
            for (String updatedLine : updatedLines) {
                writer.write(updatedLine);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error updating appointments: " + e.getMessage());
            return;
        }

        if (updatedFlag) {
            System.out.println("Appointment record updated successfully and marked as Completed.");
        } else {
            System.out.println("No updates were made.");
        }
    }

    
    public boolean isUpdatedFlag() {
        return updatedFlag;
    }

    public void viewAppointmentRecords() {
        System.out.println("Viewing appointment records...\n");

        
        Map<String, List<String[]>> groupedRecords = new TreeMap<>(); 

        try (BufferedReader br = new BufferedReader(new FileReader(Path))) {
            String line;
            boolean isFirstLine = true;

            
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false; 
                    continue;
                }

                String[] values = line.split(",");
                if (values.length == 11) {
                    String patientName = values[0]; 
                    groupedRecords.putIfAbsent(patientName, new ArrayList<>());
                    groupedRecords.get(patientName).add(values);
                } else {
                    System.out.println("Invalid row format: " + line);
                }
            }

            
            for (String patientName : groupedRecords.keySet()) {
                System.out.println("\nName of Patient: " + patientName);
                System.out.println("Appointments: ");

                
                for (String[] record : groupedRecords.get(patientName)) {
                    System.out.println("    Name of Doctor: " + record[1]);
                    System.out.println("    Date of Appointment: " + record[2]);
                    System.out.println("    Time of Appointment: " + record[3]);
                    System.out.println("    Appointment Status: " + record[4]);
                    System.out.println("    Patient Diagnosis: " + record[5]);
                    System.out.println("    Patient Treatment: " + record[6]);
                    System.out.println("    Prescribed Medication: " + record[7]);
                    System.out.println("    Medication Quantity: " + record[8]);
                    System.out.println("    Prescription Status: " + record[9]);
                    System.out.println("    Remarks: " + record[10]);
                    System.out.println(); 
                }
            }

        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        }
    }
}