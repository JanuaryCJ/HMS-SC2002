
public class Appointment {
	private String Patient;
	private String Doctor;
	private String Date;
	private String Time;
	private String Status;
	private String Outcome;
	private String Diagnosis;

	public Appointment(String Patient, String Doctor, String Date, String Time, String status, String outcome) {
		this.Patient = Patient;
		this.Doctor = Doctor;
		this.Date = Date;
		this.Time = Time;
		this.Status = status;
		this.Outcome = outcome;
	}

	public String getPatient() {
		return Patient;
	}

	public String getDoctor() {
		return Doctor;
	}

	public String getDate() {
		return Date;
	}

	public String getTime() {
		return Time;
	}

	public String getStatus() {
		return Status;
	}

	public String getOutcome() {
		return Outcome;
	}
}
