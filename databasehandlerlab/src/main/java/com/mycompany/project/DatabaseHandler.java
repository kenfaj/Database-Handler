package com.mycompany.project;

import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class DatabaseHandler {

    private Connection con;

    public static final String DEFAULT_DATABASE = "StudentDatabase.db";
    private static final String DB_FOLDER = Paths.get("databasehandlerlab/src/main/resources").toAbsolutePath()
            .toString();
    private static final String DB_PATH = Paths.get(DB_FOLDER, DEFAULT_DATABASE).toString();
    private static final String DB_URL = "jdbc:sqlite:" + DB_PATH;
    private final String[] departments = { "College of Accountancy", "College of Architecture",
            "Faculty of Arts and Letters", "Faculty of Civil Law", "College of Commerce and Business Administration",
            "College of Education", "Faculty of Engineering", "College of Fine Arts and Design", "Graduate School",
            "Graduate School of Law", "College of Information and Computing Sciences",
            "Faculty of Medicine and Surgery", "Conservatory of Music", "College of Nursing", "Faculty of Pharmacy",
            "Institute of Physical Education and Athletics", "College of Rehabilitation Sciences", "College of Science",
            "College of Tourism and Hospitality Management", "Faculty of Canon Law", "Faculty of Philosophy",
            "Faculty of Sacred Theology", "Senior High School", "Junior High School", "Education High School" };

    public class Student {
        private String id;
        private String firstName;
        private String lastName;
        private String middleName;
        private String sex;
        private String dateOfBirth;
        private int dateOfEnrollment;
        private String department;
        private int units;
        private String address;

        public Student(String id, String firstName, String lastName, String middleName, String sex, String dateOfBirth,
                int dateOfEnrollment, String department, int units, String address) {
            {
                this.id = id;
                this.firstName = firstName;
                this.lastName = lastName;
                this.middleName = middleName;
                this.sex = sex;
                this.dateOfBirth = dateOfBirth;
                this.dateOfEnrollment = dateOfEnrollment;
                this.department = department;
                this.units = units;
                this.address = address;
            }
        }

        @Override
        public String toString() {
            return "Student [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", middleName="
                    + middleName + ", sex=" + sex + ", dateOfBirth=" + dateOfBirth + ", dateOfEnrollment="
                    + dateOfEnrollment + ", department=" + department + ", units=" + units + ", address=" + address
                    + "]";
        }
    }

    public DatabaseHandler(String database) {
        this.con = null;
        try {
            this.con = DriverManager.getConnection(DB_URL);
            System.out.println("Connection to SQLite has been established using url: " + DB_URL);
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    void initializeStudents() {
        // Ensure the database connection is not null
        if (con == null) {
            System.err.println("Database connection is not established.");
            return;
        }

        // Build a string of the departments for SQL
        StringBuilder departmentString = new StringBuilder();
        for (int i = 0; i < departments.length; i++) {
            departmentString.append("'").append(departments[i]).append("'");
            if (i < departments.length - 1) {
                departmentString.append(",");
            }
        }

        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(
                    "CREATE TABLE students (" + "student_id TEXT PRIMARY KEY CHECK (student_id LIKE '____010____'), "
                            + "student_fname TEXT NOT NULL, " + "student_mname TEXT NOT NULL, "
                            + "student_lname TEXT NOT NULL, " + "student_sex TEXT CHECK (student_sex IN ('M', 'F')), "
                            + "student_birth TEXT CHECK (student_birth LIKE '____-__-__'), "
                            + "student_start INT CHECK (student_start BETWEEN 1900 AND 2100), "
                            + "student_department TEXT CHECK (student_department IN (" + departmentString + ")),"
                            + "student_units INT CHECK (student_units > 0), " + "student_address TEXT NOT NULL" + ")");
            stmt.executeUpdate();
            // tester
            System.out.println("\nCommand executed/initialized: " + stmt.toString());
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    Student getStudent(String studentNumber) {
        try {
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM students WHERE student_id = ?");
            stmt.setString(1, studentNumber);
            ResultSet rs = stmt.executeQuery();
            // tester
            System.out.println("\nCommand executed/getstudent: " + stmt.toString());
            if (rs.next()) {
                return new Student(studentNumber, rs.getString("student_fname"), rs.getString("student_mname"),
                        rs.getString("student_lname"), rs.getString("student_sex"), rs.getString("student_birth"),
                        rs.getInt("student_start"), rs.getString("student_department"), rs.getInt("student_units"),
                        rs.getString("student_address"));
            }
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + "/getstudent: " + e.getMessage());
        }
        return null;
    }

    Student getStudent(String studentFname, String studentMname, String studentLname) {
        try {
            PreparedStatement stmt = con.prepareStatement(
                    "SELECT * FROM students WHERE student_fname = ? AND student_mname = ? AND student_lname = ?");
            stmt.setString(1, studentFname);
            stmt.setString(2, studentMname);
            stmt.setString(3, studentLname);
            ResultSet rs = stmt.executeQuery();
            // tester
            System.out.println("\nCommand executed/getstudent: " + stmt.toString());
            if (rs.next()) {
                return new Student(rs.getString("student_id"), rs.getString("student_fname"),
                        rs.getString("student_mname"), rs.getString("student_lname"), rs.getString("student_sex"),
                        rs.getString("student_birth"), rs.getInt("student_start"), rs.getString("student_department"),
                        rs.getInt("student_units"), rs.getString("student_address"));
            }
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + "/getstudent: " + e.getMessage());
        }
        return null;
    }

    ArrayList<Student> getStudents() {
        ArrayList<Student> students = new ArrayList<>();
        try {
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM students");
            ResultSet rs = stmt.executeQuery();
            // tester
            System.out.println("\nCommand executed: " + stmt.toString());
            while (rs.next()) {
                Student student = new Student(rs.getString("student_id"), rs.getString("student_fname"),
                        rs.getString("student_mname"), rs.getString("student_lname"), rs.getString("student_sex"),
                        rs.getString("student_birth"), rs.getInt("student_start"), rs.getString("student_department"),
                        rs.getInt("student_units"), rs.getString("student_address"));
                students.add(student);
            }
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + "/getstudents: " + e.getMessage());
        }
        return students;
    }

    Boolean removeStudent(String studentNumber) {
        try {
            PreparedStatement stmt = con.prepareStatement("DELETE FROM students WHERE student_id = ?");
            stmt.setString(1, studentNumber);
            stmt.executeUpdate();
            // tester
            System.out.println("\nCommand executed/removeStudent: " + stmt.toString());
            return true;
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + "/removestudent: " + e.getMessage());
            return false;
        }
    }

    ArrayList<Student> getStudentsByYear(int year) {
        ArrayList<Student> students = new ArrayList<>();
        try {
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM students WHERE student_start = ?");
            stmt.setInt(1, year);
            ResultSet rs = stmt.executeQuery();
            // tester
            System.out.println("\nCommand executed/getStudentsByYear: " + stmt.toString());
            while (rs.next()) {
                Student student = new Student(rs.getString("student_id"), rs.getString("student_fname"),
                        rs.getString("student_mname"), rs.getString("student_lname"), rs.getString("student_sex"),
                        rs.getString("student_birth"), rs.getInt("student_start"), rs.getString("student_department"),
                        rs.getInt("student_units"), rs.getString("student_address"));
                students.add(student);
            }
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + "/getstudents: " + e.getMessage());
        }
        return students;
    }

    Boolean updateStudentInfo(String studentNumber, Student studentInfo) {
        try {
            PreparedStatement stmt = con.prepareStatement("UPDATE students SET student_fname = ?, student_mname = ?, "
                    + "student_lname = ?, student_sex = ?, student_birth = ?, student_start = ?, student_department = ?, "
                    + "student_units = ?, student_address = ? WHERE student_id = ?");
            stmt.setString(1, studentInfo.firstName);
            stmt.setString(2, studentInfo.middleName);
            stmt.setString(3, studentInfo.lastName);
            stmt.setString(4, studentInfo.sex);
            stmt.setString(5, studentInfo.dateOfBirth);
            stmt.setInt(6, studentInfo.dateOfEnrollment);
            stmt.setString(7, studentInfo.department);
            stmt.setInt(8, studentInfo.units);
            stmt.setString(9, studentInfo.address);
            stmt.setString(10, studentNumber);
            stmt.executeUpdate();
            // tester
            System.out.println("\nCommand executed/updatestudentinfo: " + stmt.toString());
            return true;
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + "/updatestudentinfo: " + e.getMessage());
            return false;
        }
    }

    Boolean updateStudentUnits(String studentNumber, int subtractedUnits) {
        try {
            PreparedStatement stmt = con.prepareStatement("UPDATE students SET student_units = ? WHERE student_id = ?");
            int originalUnits = getStudent(studentNumber).units;
            stmt.setInt(1, originalUnits - subtractedUnits);
            stmt.setString(2, studentNumber);
            stmt.executeUpdate();
            // tester
            System.out.println("\nCommand executed/updateStudentUnits: " + stmt.toString());
            return true;
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + "/updatestudentinfo: " + e.getMessage());
            return false;
        }
    }

    Boolean insertStudent(Student newStudent) {
        try {
            PreparedStatement stmt = con.prepareStatement("INSERT INTO students VALUES (?,?,?,?,?,?,?,?,?,?)");
            stmt.setString(1, newStudent.id);
            stmt.setString(2, newStudent.firstName);
            stmt.setString(3, newStudent.middleName);
            stmt.setString(4, newStudent.lastName);
            stmt.setString(5, newStudent.sex);
            stmt.setString(6, newStudent.dateOfBirth);
            stmt.setInt(7, newStudent.dateOfEnrollment);
            stmt.setString(8, newStudent.department);
            stmt.setInt(9, newStudent.units);
            stmt.setString(10, newStudent.address);
            stmt.executeUpdate();
            // tester
            System.out.println("\nCommand executed/insertstudent: " + stmt.toString());
            return true;
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + "/insertstudent: " + e.getMessage());
            return false;
        }
    }

    // test all methods
    public static void main(String[] args) {
        System.out.println("Database Path: " + DB_PATH);
        DatabaseHandler db = new DatabaseHandler(DEFAULT_DATABASE);

        // init
        db.initializeStudents();
        // insert
        db.insertStudent(db.new Student("11110102222", "John", "Doe", "Doe", "M", "2000-01-01", 2017,
                "College of Accountancy", 3, "123 Main St"));
        db.insertStudent(db.new Student("33330104444", "Jane", "", "Doe", "F", "1990-01-01", 2017,
                "College of Information and Computing Sciences", 3, "456 Main St"));
        db.insertStudent(db.new Student("22220203333", "Alice", "Marie", "Smith", "F", "1995-02-15", 2016,
                "Faculty of Arts and Letters", 4, "789 Main St"));
        // updatestudentunits
        db.updateStudentUnits("11110102222", 1);
        // getstudents
        db.getStudents().forEach(System.out::println);
        // getstudentsbyyear
        db.getStudentsByYear(2017).forEach(System.out::println);
        // getstudent
        DatabaseHandler.Student s1 = db.getStudent("John", "Doe", "Doe");
        DatabaseHandler.Student s2 = db.getStudent("33330104444");
        System.out.println("Student: " + s1);
        System.out.println("Student: " + s2);
        // removestudent
        db.removeStudent("11110102222");
        db.getStudents().forEach(System.out::println);
        // updatestudentinfo
        db.updateStudentInfo("33330104444", db.new Student("33330104444", "Jane", "Doe", "Doe", "F", "1990-01-01", 2018,
                "College of Information and Computing Sciences", 6, "789 Main St"));
        System.out.println("newStudent: " + db.getStudent("33330104444"));

    }

}
