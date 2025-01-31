package javaMySql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class EmployeeProject {
	private int employeeId;
	private String employeeName;
	private int projectId;
	private String projectTitle;
	
	
	public EmployeeProject(int employeeId, int projectId) {
		this.employeeId = employeeId;
		this.projectId = projectId;
	}
	
	public static List<EmployeeProject> getAllEmployeeProjects() throws SQLException {
        List<EmployeeProject> employeeProjects = new ArrayList<>();
        String query = "SELECT * FROM employee_project";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                EmployeeProject employeeProject = new EmployeeProject(rs.getInt("employee_id"), rs.getInt("project_id"));
                employeeProjects.add(employeeProject);
            }
        }
        return employeeProjects;
    }
	
	public static void createEmployeeProject(int employeeId, int projectId ) throws SQLException {
        String query = "INSERT INTO employee_project (employee_id, project_id) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, employeeId);
            stmt.setInt(2, projectId);
            stmt.executeUpdate();
        }
	}
	
    public static void deleteEmployeeProject(int employeeId, int projectId) throws SQLException {
        String query = "DELETE FROM employee_project WHERE employee_id = ? AND project_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, employeeId);
            stmt.setInt(2, projectId);
            stmt.executeUpdate();
        }
    }

	public int getEmployeeId() { return employeeId; }
	public void setEmployeeId(int employeeId) { this.employeeId = employeeId; }
	public int getProjectId() { return projectId; }
	public void setProjectId(int projectId) { this.projectId = projectId; }

}
