package javaMySql;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ProjectPanel extends JPanel {
	private JPanel projectInfoPanel;
	private JComboBox<Project> projectComboBox;
	private List<Project> projects;

	public ProjectPanel() {
	    setLayout(new BorderLayout());

	    projectInfoPanel = new JPanel();
	    projectInfoPanel.setLayout(new BoxLayout(projectInfoPanel, BoxLayout.Y_AXIS));

        refreshProjectComboBox(); 
	}
    
    public JPanel createProjectPanel(Project project, List<Employee> assignedEmployees) {
    	JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(new JLabel("Title: " + project.getTitle()));
        panel.add(new JLabel("Description: " + project.getDescription()));
        panel.add(new JLabel("Start date: " + project.getStartDate()));
        panel.add(new JLabel("End date: " + project.getEndDate()));
        
        JPanel employeesPanel = new JPanel();
        employeesPanel.setLayout(new BoxLayout(employeesPanel, BoxLayout.Y_AXIS));
        employeesPanel.setBorder(BorderFactory.createTitledBorder("Employees"));

        if (assignedEmployees.isEmpty()) {
            employeesPanel.add(new JLabel("No employees are working on this project."));
        } else {
            for (Employee employee : assignedEmployees) {            
                JButton removeEmployeeButton = new JButton("Remove employee from project");
                removeEmployeeButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
        					removeEmployeeFromProject(project.getId(), employee.getId());
        				} catch (SQLException e1) {
        					// TODO Auto-generated catch block
        					e1.printStackTrace();
        				}
                    }
                });
                
                employeesPanel.add(new JLabel(" - " + employee.getFullName() + " (ID: " + employee.getId() + ")"));
                employeesPanel.add(removeEmployeeButton);
            }
        }

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        
        JButton addEmployeeButton = new JButton("Add an employee");
        addEmployeeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
					addEmployeeToProject(project.getId());
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            }
        });

        JButton modifyButton = new JButton("Modify informations");
        modifyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modifyProject(project);
            }
        });
        
        JButton deleteButton = new JButton("Delete the project");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteProject(project, panel);
            }
        });
        
        buttonPanel.add(addEmployeeButton);
        buttonPanel.add(modifyButton);
        buttonPanel.add(deleteButton);
        
        panel.add(employeesPanel);
        panel.add(buttonPanel);
        
        return panel;
    }
    
    public void refreshProjectComboBox() {
    	try {
			this.projects = Project.getAllProjects();
			
			Project defaultProject = new Project();
	        this.projects.add(0, defaultProject);
	        
	        projectComboBox = new JComboBox<>(projects.toArray(new Project[0]));
	        projectComboBox.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                Project selectedProject = (Project) projectComboBox.getSelectedItem(); 	                
					try {
						List<Employee> assignedEmployees = Employee.getAllEmployeesFromProject(selectedProject.getId());
						if (selectedProject != null && selectedProject.getTitle() != "Select a project") {
		                	 JPanel newPanel = createProjectPanel(selectedProject, assignedEmployees);
		                     projectInfoPanel.removeAll();
		                     projectInfoPanel.add(newPanel);
		                     projectInfoPanel.revalidate(); 
		                     projectInfoPanel.repaint();
		                }
					} catch (SQLException e1) {						
						e1.printStackTrace();
					}	                	                
	            }
	        });

		    JPanel comboBoxPanel = new JPanel();
		    comboBoxPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		    comboBoxPanel.add(projectComboBox);

		    JButton createButton = new JButton("Create a project");
		    createButton.addActionListener(new ActionListener() {
		        @Override
		        public void actionPerformed(ActionEvent e) {
		            addProject();
		        }
		    });

		    JPanel buttonPanel = new JPanel();
		    buttonPanel.add(createButton);

		    removeAll();
            add(comboBoxPanel, BorderLayout.NORTH);
            add(buttonPanel, BorderLayout.SOUTH);
            add(projectInfoPanel, BorderLayout.CENTER);

            revalidate(); 
            repaint();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
    public void addProject() {
    	String title = JOptionPane.showInputDialog(this, "Enter new Title:");
    	if (title == null || title.trim().isEmpty()) {
    	    JOptionPane.showMessageDialog(null, "Title cannot be empty");
    	    return;
    	} 
    	
		try {
			Project projectExists = Project.getProjectByTitle(title);
			if (projectExists != null ) {
	    		JOptionPane.showMessageDialog(null, "This project already exists, please choose another title");
	    	    return;
	    	}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	

    	String description = JOptionPane.showInputDialog(this, "Enter new Description:");
    	if (description == null || description.trim().isEmpty()) {
    	    JOptionPane.showMessageDialog(null, "Description cannot be empty");
    	    return;
    	}

    	String startDate = JOptionPane.showInputDialog(this, "Enter new Start date:");
    	if (startDate == null || startDate.trim().isEmpty()) {
    	    JOptionPane.showMessageDialog(null, "Start date cannot be empty");
    	    return;
    	}

    	String endDate = JOptionPane.showInputDialog(this, "Enter new End date:");
    	if (endDate == null || endDate.trim().isEmpty()) {
    	    JOptionPane.showMessageDialog(null, "End date cannot be empty");
    	    return;
    	}
    	
    	try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date start = dateFormat.parse(startDate);
			Date end = dateFormat.parse(endDate);
	
			if (start.after(end)) {
				JOptionPane.showMessageDialog(null, "Start date cannot be after End date");
				return;
			}
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
       
        try {                
			Project.createProject(title, description, startDate, endDate);
			refreshProjectComboBox();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
    public void addEmployeeToProject(int projectId) throws SQLException {
    	int employeeId = Employee.selectEmployee(projectId);
    	
    	if (employeeId > 0) {
    		EmployeeProject.createEmployeeProject(employeeId, projectId); 
    		refreshProjectComboBox();
			
			projectInfoPanel.removeAll();
			projectInfoPanel.revalidate(); 
			projectInfoPanel.repaint();
			
			JOptionPane.showMessageDialog(null, "Employee added to project successfully");
    	}
    }
    
    public void removeEmployeeFromProject(int projectId, int employeeId) throws SQLException {
    	try {
    		EmployeeProject.deleteEmployeeProject(employeeId, projectId);
    		refreshProjectComboBox();
			
			projectInfoPanel.removeAll();
			projectInfoPanel.revalidate(); 
			projectInfoPanel.repaint();
			
			JOptionPane.showMessageDialog(null, "Employee removed from project successfully");
    	} catch(SQLException e1) {
    		e1.printStackTrace();
    	}
    }
    
    public void modifyProject(Project project) {
    	String title = JOptionPane.showInputDialog(this, "Enter new Title:");
    	if (title == null || title.trim().isEmpty()) {
    	    JOptionPane.showMessageDialog(null, "Title cannot be empty");
    	    return;
    	} 
    	
		try {
			Project projectExists = Project.getProjectByTitle(title);
			if (projectExists != null ) {
	    		JOptionPane.showMessageDialog(null, "This project already exists, please choose another title");
	    	    return;
	    	}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    	String description = JOptionPane.showInputDialog(this, "Enter new Description:");
    	if (description == null || description.trim().isEmpty()) {
    	    JOptionPane.showMessageDialog(null, "Description cannot be empty");
    	    return;
    	}

    	String startDate = JOptionPane.showInputDialog(this, "Enter new Start date:");
    	if (startDate == null || startDate.trim().isEmpty()) {
    	    JOptionPane.showMessageDialog(null, "Start date cannot be empty");
    	    return;
    	}

    	String endDate = JOptionPane.showInputDialog(this, "Enter new End date:");
    	if (endDate == null || endDate.trim().isEmpty()) {
    	    JOptionPane.showMessageDialog(null, "End date cannot be empty");
    	    return;
    	}
    	
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date start = dateFormat.parse(startDate);
			Date end = dateFormat.parse(endDate);
	
			if (start.before(end)) {
				JOptionPane.showMessageDialog(null, "Start date cannot be after End date");
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
            
    	try {
        	project.setTitle(title);
        	project.setDescription(description);
        	project.setStartDate(startDate);
        	project.setEndDate(endDate);
            
			Project.updateProject(project);
			refreshProjectComboBox();
			
			projectInfoPanel.removeAll();
			projectInfoPanel.revalidate(); 
			projectInfoPanel.repaint();
			
			JOptionPane.showMessageDialog(null, "Project modified successfully");
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
    public void deleteProject(Project project, JPanel panel) {
    	try {
			Project.deleteProject(project.getId());
            refreshProjectComboBox();
            projectInfoPanel.removeAll();
            projectInfoPanel.revalidate(); 
            projectInfoPanel.repaint();
            
            JOptionPane.showMessageDialog(null, "Project deleted successfully");
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
    }
}

