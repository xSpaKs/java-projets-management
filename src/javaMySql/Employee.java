package javaMySql;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;

public class Employee {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String job;
    
    public Employee() {
    	this.id = -1;
    	this.firstName = "Select";
    	this.lastName = "an employee";
    }
    
    public Employee(int id, String firstName, String lastName, String email, String phoneNumber, String job) {
        this.id = id;
    	this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.job = job;
    }

    public Employee(String firstName, String lastName, String email, String phoneNumber, String job) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.job = job;
    }
    
    public static void createEmployee(String firstName, String lastName, String email, String phoneNumber, String job) throws SQLException {
        String query = "INSERT INTO employees (first_name, last_name, email, phone_number, job) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, firstName);
            stmt.setString(2, lastName);
            stmt.setString(3, email);
            stmt.setString(4, phoneNumber);
            stmt.setString(5, job);
            stmt.executeUpdate();
        }
    }
    
    public static Employee getEmployeeByEmail(String email) throws SQLException {
        String query = "SELECT * FROM employees WHERE email = ?";
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Employee(rs.getInt("id"), rs.getString("first_name"), rs.getString("last_name"),
                        rs.getString("email"), rs.getString("phone_number"), rs.getString("job"));
            }
            return null;
        }
    }

    public static List<Employee> getAllEmployees() throws SQLException {
        List<Employee> employees = new ArrayList<>();
        String query = "SELECT * FROM employees";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Employee employee = new Employee(rs.getInt("id"), rs.getString("first_name"), rs.getString("last_name"),
                        rs.getString("email"), rs.getString("phone_number"), rs.getString("job"));
                employees.add(employee);
            }
        }
        return employees;
    }
    
    public static List<Employee> getAllEmployeesFromProject(int projectId) throws SQLException {
        List<Employee> employees = new ArrayList<>();
        String query = "SELECT * FROM employee_project JOIN employees ON employee_project.employee_id = employees.id WHERE employee_project.project_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, projectId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String email = rs.getString("email");
                String phoneNumber = rs.getString("phone_number");
                String job = rs.getString("job");
                
                Employee employee = new Employee(id, firstName, lastName, email, phoneNumber, job);
                employees.add(employee);
            }
        }
        return employees;
    }
    
    public static List<Employee> getAllEmployeesNotFromProject(int projectId) throws SQLException {
        List<Employee> employees = new ArrayList<>();
        String query = "SELECT * FROM employees WHERE id NOT IN (" +
                       "SELECT employee_id FROM employee_project WHERE project_id = ?" +
                       ")";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, projectId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String email = rs.getString("email");
                String phoneNumber = rs.getString("phone_number");
                String job = rs.getString("job");

                Employee employee = new Employee(id, firstName, lastName, email, phoneNumber, job);
                employees.add(employee);
            }
        }
        return employees;
    }

    public static void updateEmployee(Employee employee) throws SQLException {
        String query = "UPDATE employees SET first_name = ?, last_name = ?, email = ?, phone_number = ?, job = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, employee.getFirstName());
            stmt.setString(2, employee.getLastName());
            stmt.setString(3, employee.getEmail());
            stmt.setString(4, employee.getPhoneNumber());
            stmt.setString(5, employee.getJob());
            stmt.setInt(6, employee.getId());
            stmt.executeUpdate();
        }
    }

    public static void deleteEmployee(int id) throws SQLException {
        String query = "DELETE FROM employees WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
    
    public static Integer selectEmployee(Integer projectId) {
		try {
			List<Employee> employees = getAllEmployeesNotFromProject(projectId);
			JComboBox<Employee> comboBox = new JComboBox<>();
	        for (Employee employee : employees) {
	            comboBox.addItem(employee);
	        }

	        int result = JOptionPane.showConfirmDialog(
	                null,
	                comboBox,
	                "Select an Employee",
	                JOptionPane.OK_CANCEL_OPTION
	        );

	        if (result == JOptionPane.OK_OPTION) {
	            Employee selectedEmployee = (Employee) comboBox.getSelectedItem();
	            return selectedEmployee != null ? selectedEmployee.getId() : -1;
	        }
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
    }
    
    @Override
    public String toString() {
        return this.getFullName();
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public String getJob() { return job; }
    public void setJob(String job) { this.job = job; }
    public String getFullName() { return firstName + " " + lastName; }
}
