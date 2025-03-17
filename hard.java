import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// Model
class Student {
    private int studentID;
    private String name;
    private String department;
    private double marks;

    public Student(int studentID, String name, String department, double marks) {
        this.studentID = studentID;
        this.name = name;
        this.department = department;
        this.marks = marks;
    }

    public int getStudentID() { return studentID; }
    public String getName() { return name; }
    public String getDepartment() { return department; }
    public double getMarks() { return marks; }
}

// Controller
class StudentController {
    private static final String URL = "jdbc:mysql://localhost:3306/your_database";
    private static final String USER = "your_username";
    private static final String PASSWORD = "your_password";
    private Connection conn;

    public StudentController() throws SQLException {
        conn = DriverManager.getConnection(URL, USER, PASSWORD);
        conn.setAutoCommit(true);
    }

    public void addStudent(Student student) throws SQLException {
        String sql = "INSERT INTO Student (StudentID, Name, Department, Marks) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, student.getStudentID());
            ps.setString(2, student.getName());
            ps.setString(3, student.getDepartment());
            ps.setDouble(4, student.getMarks());
            ps.executeUpdate();
        }
    }

    public List<Student> getAllStudents() throws SQLException {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM Student";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                students.add(new Student(rs.getInt("StudentID"), rs.getString("Name"), rs.getString("Department"), rs.getDouble("Marks")));
            }
        }
        return students;
    }

    public void updateStudent(int id, double marks) throws SQLException {
        String sql = "UPDATE Student SET Marks=? WHERE StudentID=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, marks);
            ps.setInt(2, id);
            ps.executeUpdate();
        }
    }

    public void deleteStudent(int id) throws SQLException {
        String sql = "DELETE FROM Student WHERE StudentID=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}

// Main View
public class hard {
    public static void main(String[] args) {
        try {
            StudentController controller = new StudentController();
            Scanner sc = new Scanner(System.in);
            int choice;
            do {
                System.out.println("\n1. Add Student\n2. View Students\n3. Update Marks\n4. Delete Student\n5. Exit");
                choice = sc.nextInt();
                switch (choice) {
                    case 1 -> {
                        System.out.println("Enter ID, Name, Department, Marks:");
                        Student s = new Student(sc.nextInt(), sc.next(), sc.next(), sc.nextDouble());
                        controller.addStudent(s);
                    }
                    case 2 -> controller.getAllStudents().forEach(s -> System.out.println(s.getStudentID() + " " + s.getName() + " " + s.getDepartment() + " " + s.getMarks()));
                    case 3 -> {
                        System.out.println("Enter StudentID and new Marks:");
                        controller.updateStudent(sc.nextInt(), sc.nextDouble());
                    }
                    case 4 -> {
                        System.out.println("Enter StudentID to delete:");
                        controller.deleteStudent(sc.nextInt());
                    }
                }
            } while (choice != 5);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
