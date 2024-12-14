// Import necessary classes for file operations and collection framework
import java.io.*; // For file input/output operations
import java.util.*; // For collections like HashMap and Scanner

// Define the main public class for the Student Management System
public class StudentManagementSystem
{
    // Map to store student records with student ID as key and Student object as value
    private Map<String, Student> studentMap = new HashMap<>();

    // Constant for the file name used to store student data
    private static final String FILE_NAME = "students.dat"; // Using ".dat" for binary file

    // Nested static class representing a student
    public static class Student implements Serializable
    {
            private String id;
            private String name;
            private int age;
            private String course;

        // Constructor to initialize a new Student object
        public Student(String id, String name, int age, String course)
        {
            this.id = id;
            this.name = name;
            this.age = age;
            this.course = course;
        }

        public String getId() {
            return id;
        }
        public void setId(String id) {
            this.id = id;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public int getAge() {
            return age;
        }
        public void setAge(int age) {
            this.age = age;
        }
        public String getCourse() {
            return course;
        }
        public void setCourse(String course) {
            this.course = course;
        }
        // Override toString method to return a string representation of the Student object
        @Override
        public String toString() {
            return String.format("ID: %s, Name: %s, Age: %d, Course: %s", id, name, age, course);
        }
    }

    // Nested static class for validation utilities
    public static class ValidationUtil {
        public static boolean isValidAge(String ageStr) {
            try {
                int age = Integer.parseInt(ageStr); // Attempt to parse the string to an integer
                return age > 0 && age < 100; // Valid if age is between 1 and 99
            }
            catch (NumberFormatException e) { // If parsing fails
                return false; // Not a valid age
            }
        }

        // Method to check if the provided ID is valid
        public static boolean isValidId(String id) {
            return id != null && !id.trim().isEmpty(); // ID should not be null or empty
        }

        // Method to check if the provided string is valid
        public static boolean isValidString(String str) {
            return str != null && !str.trim().isEmpty(); // String should not be null or empty
        }
    }

    public void addStudent() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("[LCID – Add Student] Enter Student ID: ");
        String id = scanner.nextLine();
        if (!ValidationUtil.isValidId(id)) {
            System.out.println("\nInvalid ID.");
            return;
        }
        if (studentMap.containsKey(id)) {
            System.out.println("\nID already exists.");
            return;
        }
        System.out.print("Enter Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Age: ");
        String ageStr = scanner.nextLine();
        if (!ValidationUtil.isValidAge(ageStr)) {
            System.out.println("\nInvalid age.");
            return;
        }
        int age = Integer.parseInt(ageStr); // Convert age to integer
        System.out.print("Enter Course: ");
        String course = scanner.nextLine();
        // Create a new Student object and add it to the studentMap
        Student student = new Student(id, name, age, course);
        studentMap.put(id, student);
        saveToFile(); // Save the updated student data to file
        System.out.println("\nStudent added successfully!");
    }

    public void updateStudent() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("[LCID – Update Student] Enter Student ID to update: ");
        String id = scanner.nextLine();
        if (studentMap.containsKey(id)) {
            System.out.print("Enter new Name: ");
            String name = scanner.nextLine();
            System.out.print("Enter new Age: ");
            String ageStr = scanner.nextLine();
            if (!ValidationUtil.isValidAge(ageStr)) {
                System.out.println("\nInvalid age.");
                return;
            }
            int age = Integer.parseInt(ageStr); // Convert new age to integer
            System.out.print("Enter new Course: ");
            String course = scanner.nextLine();
            // Retrieve the student object from the map and update its details
            Student student = studentMap.get(id);
            student.setName(name);
            student.setAge(age);
            student.setCourse(course);
            saveToFile(); // Save the updated student data to file
            System.out.println("\nStudent updated successfully!");
        } else {
            System.out.println("\nStudent not found!");
        }
    }

    public void viewStudents() {
        System.out.println("[LCID– View Students]");
        if (studentMap.isEmpty()) {
            System.out.println("\nNo students to display.");
        } else {
            studentMap.values().forEach(System.out::println);
        }
    }

    public void deleteStudent() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("[LCID – Delete Student] Enter Student ID to delete: ");
        String id = scanner.nextLine();
        // Check if the student ID exists
        if (studentMap.containsKey(id)) {
            studentMap.remove(id); // Remove the student from the map
            saveToFile(); // Save the updated student data to file
            System.out.println("\nStudent deleted successfully!");
        } else {
            System.out.println("\nStudent not found!");
        }
    }

    private void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(studentMap); // Write the student map to the file
        }
        catch (IOException e) {
            System.out.println("Error saving to file: " + e.getMessage());
        }
    }

    // Method to load student data from a file
    @SuppressWarnings("unchecked") // Suppress warnings related to unchecked type casting
    private void loadFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            studentMap = (Map<String, Student>) ois.readObject(); // Read the student map from the file
        }
        catch (FileNotFoundException e) {
            System.out.println("\nNo existing data file found.");
            studentMap = new HashMap<>(); // Initialize empty map
        }
        catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading from file: " + e.getMessage());
            studentMap = new HashMap<>(); // Initialize empty map
        }
    }

    // Main method to run the Student Management System
    public void run() {
        loadFromFile(); // Load student data from file
        Scanner scanner = new Scanner(System.in);
        boolean running = true; // Flag to control the loop

        while (running) {
            System.out.println("\n==============================");
            System.out.println("Welcome to Student Management MIS");
            System.out.println("==============================");
            System.out.println("1. Add Information");
            System.out.println("2. Update Information");
            System.out.println("3. View Inventory");
            System.out.println("4. Delete Information");
            System.out.println("5. Exit");
            System.out.println("==============================");
            System.out.print("Please enter your choice (1-5): ");

            try {
                int choice = Integer.parseInt(scanner.nextLine()); // Read and parse the user's choice
                switch (choice) {
                    case 1:
                        addStudent();
                        break;
                    case 2:
                        updateStudent();
                        break;
                    case 3:
                        viewStudents();
                        break;
                    case 4:
                        deleteStudent();
                        break;
                    case 5:
                        running = false; // Exit the loop
                        System.out.println("\nExiting...");
                        break;
                    default:
                        System.out.println("\nInvalid choice! Please enter a number between 1 and 5.");
                }
                clearConsole();
            }
            catch (NumberFormatException e) {
                System.out.println("\nInvalid input format. Please enter a number.");
            }
        }
    }

    private void clearConsole() {
        System.out.print("\033[H\033[2J"); // ANSI escape code to clear the screen
        System.out.flush(); // Flush the output stream
    }

    // Main method to start the application
    public static void main(String[] args) {
        StudentManagementSystem system = new StudentManagementSystem();
        system.run();
    }
}