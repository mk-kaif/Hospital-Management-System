package Hospital_Mannegement_System;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Patient {
    private Connection connection;
    private Scanner scanner;

    public Patient(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }

    public void addPatient() {
        System.out.print("ENTER PATIENT NAME: ");
        String name = scanner.nextLine();
        System.out.print("ENTER PATIENT AGE: ");
        int age = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.print("ENTER PATIENT GENDER: ");
        String gender = scanner.nextLine();

        String query = "INSERT INTO patients(name, age, gender) VALUES(?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, age);
            preparedStatement.setString(3, gender);
            int affectedRows = preparedStatement.executeUpdate();
            System.out.println(affectedRows > 0 ? "Patient added successfully" : "Failed to add patient");
        } catch (SQLException e) {
            System.err.println("Error adding patient: " + e.getMessage());
        }
    }

    public void viewPatient() {
        String query = "SELECT * FROM patients";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            System.out.println("+------------+--------------------+----------+----------+");
            System.out.println("| Patient id | Name               | Age      | Gender   |");
            System.out.println("+------------+--------------------+----------+----------+");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                String gender = resultSet.getString("gender");
                System.out.printf("|%-12d|%-20s|%-10d|%-10s|\n", id, name, age, gender);
                System.out.println("+------------+--------------------+----------+----------+");
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving patients: " + e.getMessage());
        }
    }

    public boolean getPatientId(int id) {
        String query = "SELECT * FROM patients WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            System.err.println("Error checking patient ID: " + e.getMessage());
        }
        return false;
    }
}
