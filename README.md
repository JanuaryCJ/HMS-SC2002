![HMS](https://github.com/user-attachments/assets/e1ac17b9-137f-4074-84b2-2097622ebaac)
#**Java OOP Project for Hospital Management System** 

**Contributors:** 
Sun Yifei,
Wu Chengrui,
Tang Ruixuan,
Jan Chen Jie,
Alvin Ong Minghui.

The project organizes classes into packages based on their functionality and roles, such as user, administrator, appointment_data, medical_record_data, and inventory_data, ensuring proper encapsulation.

**Class Design:**

- The main class runs the system and relies on UserCredential for login functionality, forming a dependency relationship.
- The User abstract class interacts with UserCredential and has subclasses: Patient, Doctor, Pharmacist, and Administrator.
- Patient and Doctor access appointments and medical records through associated Appointment and MedicalRecord classes. Action classes (AppointmentAction, MedicalRecordAction) operate on these objects using dependency relationships and implement interfaces for basic operations.
- The Pharmacist class manages inventory, with Medicine and Medical Equipment aggregated as part of the inventory. PrescriptionManagement helps pharmacists manage medications.
- The Administrator class manages StaffManage, UserManage, and PatientManage via composition, using the AdministratorManageFunction interface for polymorphism. It interacts with the Hospital class to manage user data in CSV files.

**Additional Features:**

- A login system identifies users' roles (e.g., patient, doctor) to display role-specific menus and actions. Users can update their credentials for security.
- The Inventory class manages medicine and equipment stocks individually. The design allows for extensibility, such as adding a HospitalDirector class.
- Organized classes within packages improve code readability and maintainability.

**Design Considerations:**

- Applied the SOLID principles: Single Responsibility Principle for Separation of Appointment and AppointmentAction classes.
- Applied the SOLID principles: Interface Segregation and Dependency Inversion Principles for Usage of AdministratorManageFunction interface.
- Carefully designed relationships (association, dependency, aggregation, and composition) between classes.
- Encapsulation is enforced with appropriate access modifiers.
- Java naming conventions and package structures enhance readability and extensibility.
- Polymorphism is applied through the User abstract class and AdministratorManageFunction interface, allowing flexibility, reusability, and extensibility.
