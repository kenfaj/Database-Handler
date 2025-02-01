package com.mycompany.project;

import java.io.File;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;

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
        private Date dateOfEnrollment;
        private String department;
        private int units;
        private String address;

        public Student(String id, String firstName, String lastName, String middleName, String sex, String dateOfBirth,
                Date dateOfEnrollment, String department, int units, String address) {
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
    }

    public DatabaseHandler(String database) {
        this.con = null;
        try {
            this.con = DriverManager.getConnection(DB_URL);
            System.out.println("Connection to SQLite has been established using url: " + DB_URL);
        } catch (Exception e) {
            // TODO: handle exception
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    void initializeStudents() {

    }

    public static void main(String[] args) {
        System.out.println("Database Path: " + DB_PATH);
        new DatabaseHandler(DEFAULT_DATABASE);
    }

}
