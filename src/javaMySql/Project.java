package javaMySql;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Project {
    private int id;
    private String title;
    private String description;
    private String startDate;
    private String endDate;
    
    public Project() {
    	this.id = -1;
    	this.title = "Select a project";
    }
    
    public Project(int id, String title, String description, String startDate, String endDate) {
    	this.id = id;
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Project(String title, String description, String startDate, String endDate) {
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
    }
    
    public static void createProject(String title, String description, String startDate, String endDate) throws SQLException {
        String query = "INSERT INTO projects (title, description, start_date, end_date) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, title);
            stmt.setString(2, description);
            stmt.setString(3, startDate);
            stmt.setString(4, endDate);
            stmt.executeUpdate();
        }
    }

    public static Project getProjectByTitle(String title) throws SQLException {
        String query = "SELECT * FROM projects WHERE title = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, title);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Project(rs.getInt("id"), rs.getString("title"), rs.getString("description"),
                        rs.getString("start_date"), rs.getString("end_date"));
            }
            return null;
        }
    }

    public static List<Project> getAllProjects() throws SQLException {
        List<Project> projects = new ArrayList<>();
        String query = "SELECT * FROM projects";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Project project = new Project(rs.getInt("id"), rs.getString("title"), rs.getString("description"),
                        rs.getString("start_date"), rs.getString("end_date"));
                projects.add(project);
            }
        }
        return projects;
    }
    
    public static List<Project> getAllProjectsFromEmployee(int employeeId) throws SQLException {
        List<Project> projects = new ArrayList<>();
        String query = "SELECT * FROM employee_project JOIN projects ON employee_project.project_id = projects.id WHERE employee_project.employee_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, employeeId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String description = rs.getString("description");
                String startDate = rs.getString("start_date");
                String endDate = rs.getString("end_date");
                
                Project project = new Project(id, title, description, startDate, endDate);
                projects.add(project);
            }
        }
        return projects;
    }

    public static void updateProject(Project project) throws SQLException {
        String query = "UPDATE projects SET title = ?, description = ?, start_date = ?, end_date = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, project.getTitle());
            stmt.setString(2, project.getDescription());
            stmt.setString(3, project.getStartDate());
            stmt.setString(4, project.getEndDate());
            stmt.setInt(5, project.getId());
            stmt.executeUpdate();
        }
    }

    public static void deleteProject(int id) throws SQLException {
        String query = "DELETE FROM projects WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
    
    @Override
    public String toString() {
        return this.getTitle();
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }
    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }
}
