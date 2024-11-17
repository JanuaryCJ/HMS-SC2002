import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AppointmentAction implements AppointmentInterface {
	public AppointmentAction(String Path) {
		this.Path=Path;
	}
	private static String Path;
	public static List<Appointment> ReadPatientAppointment(String Name){
		
		List<Appointment> app=new ArrayList<Appointment>();
    	String line;
        String csvSeparator = ",";  // Define the separator used in the CSV file (usually a comma)

        try (BufferedReader br = new BufferedReader(new FileReader(Path))) {
           
            //line = br.readLine();
            /*if (line != null) {
                System.out.println("Headers: " + line);  // Print headers if present
            }*/

            // Read and display the CSV data line by line
            while ((line = br.readLine()) != null) {
                // Split the line by the separator to get individual values
                String[] values = line.split(csvSeparator);

                // Check if the first column matches the target value
                if (values.length > 0 && values[0].equals(Name)) {
                	Appointment appointment=new Appointment(values[0],values[1],values[2],values[3],values[4],values[5],values[6],values[7],values[8],values[9]);
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
        String csvSeparator = ",";  // Define the separator used in the CSV file (usually a comma)

        try (BufferedReader br = new BufferedReader(new FileReader(Path))) {
           
            //line = br.readLine();
            /*if (line != null) {
                System.out.println("Headers: " + line);  // Print headers if present
            }*/

            // Read and display the CSV data line by line
            while ((line = br.readLine()) != null) {
                // Split the line by the separator to get individual values
                String[] values = line.split(csvSeparator);

                // Check if the first column matches the target value
               
                	Appointment appointment=new Appointment(values[0],values[1],values[2],values[3],values[4],values[5],values[6],values[7],values[8],values[9]);
                	app.add(appointment);
                	
                
            }

        } catch (IOException e) {
            System.err.println("Error reading the CSV file: " + e.getMessage());
        }
        return app;
	}
	public void addAppointment(String[] data) {
		try (FileWriter writer = new FileWriter(Path,true)) { // 'true' enables append mode
            // Convert data array to CSV row format
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

}
