package javaMySql;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class EmployeePanel extends JPanel {
	private JPanel employeeInfoPanel;
	private JComboBox<Employee> employeeComboBox;
	private List<Employee> employees;

	public EmployeePanel() {
	    setLayout(new BorderLayout());

	    employeeInfoPanel = new JPanel();
	    employeeInfoPanel.setLayout(new BoxLayout(employeeInfoPanel, BoxLayout.Y_AXIS));

        refreshEmployeeComboBox(); 
	}
    
    public JPanel createEmployeePanel(Employee employee, List<Project> assignedProjects) {
    	JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(new JLabel("First Name: " + employee.getFirstName()));
        panel.add(new JLabel("Last Name: " + employee.getLastName()));
        panel.add(new JLabel("Email: " + employee.getEmail()));
        panel.add(new JLabel("Phone Number: " + employee.getPhoneNumber()));
        panel.add(new JLabel("Job: " + employee.getJob()));
        
        JPanel projectsPanel = new JPanel();
        projectsPanel.setLayout(new BoxLayout(projectsPanel, BoxLayout.Y_AXIS));
        projectsPanel.setBorder(BorderFactory.createTitledBorder("Projects"));

        if (assignedProjects.isEmpty()) {
        	projectsPanel.add(new JLabel("This employee does not work on any project."));
        } else {
            for (Project project : assignedProjects) {
                projectsPanel.add(new JLabel(" - " + project.getTitle() + " (ID: " + project.getId() + ")"));
            }
        }

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JButton modifyButton = new JButton("Modify");
        modifyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modifyEmployee(employee);
            }
        });
        
        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteEmployee(employee, panel);
            }
        });

        buttonPanel.add(modifyButton);
        buttonPanel.add(deleteButton);
        
        panel.add(projectsPanel);
        panel.add(buttonPanel);
        
        return panel;
    }
    
    public void refreshEmployeeComboBox() {
    	try {
			this.employees = Employee.getAllEmployees();
			
			Employee defaultEmployee = new Employee();
	        this.employees.add(0, defaultEmployee);
	        
	        employeeComboBox = new JComboBox<>(employees.toArray(new Employee[0]));
	        employeeComboBox.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                Employee selectedEmployee = (Employee) employeeComboBox.getSelectedItem();  
					try {
						List<Project> assignedProjects = Project.getAllProjectsFromEmployee(selectedEmployee.getId());
						if (selectedEmployee != null && selectedEmployee.getId() != -1) {
		                	 JPanel newPanel = createEmployeePanel(selectedEmployee, assignedProjects);
		                     employeeInfoPanel.removeAll();
		                     employeeInfoPanel.add(newPanel);
		                     employeeInfoPanel.revalidate(); 
		                     employeeInfoPanel.repaint();
		                }
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
	                
	            }
	        });

		    JPanel comboBoxPanel = new JPanel();
		    comboBoxPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		    comboBoxPanel.add(employeeComboBox);

		    JButton createButton = new JButton("Create an employee");
		    createButton.addActionListener(new ActionListener() {
		        @Override
		        public void actionPerformed(ActionEvent e) {
		            addEmployee();
		        }
		    });

		    JPanel buttonPanel = new JPanel();
		    buttonPanel.add(createButton);

		    removeAll();
            add(comboBoxPanel, BorderLayout.NORTH);
            add(buttonPanel, BorderLayout.SOUTH);
            add(employeeInfoPanel, BorderLayout.CENTER);

            revalidate(); 
            repaint();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
    public void addEmployee() {
    	String firstName = JOptionPane.showInputDialog(this, "Enter new First Name:");
    	if (firstName == null || firstName.trim().isEmpty()) {
    	    JOptionPane.showMessageDialog(null, "First Name cannot be empty");
    	    return;
    	}

    	String lastName = JOptionPane.showInputDialog(this, "Enter new Last Name:");
    	if (lastName == null || lastName.trim().isEmpty()) {
    	    JOptionPane.showMessageDialog(null, "Last Name cannot be empty");
    	    return;
    	}

    	String email = JOptionPane.showInputDialog(this, "Enter new Email:");
    	if (email == null || email.trim().isEmpty()) {
    	    JOptionPane.showMessageDialog(null, "Email cannot be empty");
    	    return;
    	} 
    	
    	if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            JOptionPane.showMessageDialog(null, "Invalid email address");
            return;
        }
    	
    	try {
			Employee employeeExists = Employee.getEmployeeByEmail(email);	
			if (employeeExists != null) {
				JOptionPane.showMessageDialog(null, "Email already exists, please choose another one");
	    	    return;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    	String phoneNumber = JOptionPane.showInputDialog(this, "Enter new Phone Number:");
    	if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
    	    JOptionPane.showMessageDialog(null, "Phone Number cannot be empty");
    	    return;
    	}
    	
    	if (!phoneNumber.matches("\\d{10}")) {
            JOptionPane.showMessageDialog(null, "Phone Number must be exactly 10 digits");
            return;
        }

    	String job = JOptionPane.showInputDialog(this, "Enter new Job:");
    	if (job == null || job.trim().isEmpty()) {
    	    JOptionPane.showMessageDialog(null, "Job cannot be empty");
    	    return;
    	}
        
        try {                
			Employee.createEmployee(firstName, lastName, email, phoneNumber, job);
			refreshEmployeeComboBox();
			
			JOptionPane.showMessageDialog(null, "Employee added successfully");
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
    public void modifyEmployee(Employee employee) {
    	String firstName = JOptionPane.showInputDialog(this, "Enter new First Name:");
    	if (firstName == null || firstName.trim().isEmpty()) {
    	    JOptionPane.showMessageDialog(null, "First Name cannot be empty");
    	    return;
    	}

    	String lastName = JOptionPane.showInputDialog(this, "Enter new Last Name:");
    	if (lastName == null || lastName.trim().isEmpty()) {
    	    JOptionPane.showMessageDialog(null, "Last Name cannot be empty");
    	    return;
    	}

    	String email = JOptionPane.showInputDialog(this, "Enter new Email:");
    	if (email == null || email.trim().isEmpty()) {
    	    JOptionPane.showMessageDialog(null, "Email cannot be empty");
    	    return;
    	} 
    	
    	if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            JOptionPane.showMessageDialog(null, "Invalid email address");
            return;
        }
    	
    	try {
			Employee employeeExists = Employee.getEmployeeByEmail(email);	
			if (employeeExists != null) {
				JOptionPane.showMessageDialog(null, "Email already exists, please choose another one");
	    	    return;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    	String phoneNumber = JOptionPane.showInputDialog(this, "Enter new Phone Number:");
    	if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
    	    JOptionPane.showMessageDialog(null, "Phone Number cannot be empty");
    	    return;
    	}
    	
    	if (!phoneNumber.matches("\\d{10}")) {
            JOptionPane.showMessageDialog(null, "Phone Number must be exactly 10 digits");
            return;
        }

    	String job = JOptionPane.showInputDialog(this, "Enter new Job:");
    	if (job == null || job.trim().isEmpty()) {
    	    JOptionPane.showMessageDialog(null, "Job cannot be empty");
    	    return;
    	}
        
    	try {
        	employee.setFirstName(firstName);
            employee.setLastName(lastName);
            employee.setEmail(email);
            employee.setPhoneNumber(phoneNumber);
            employee.setJob(job);
            
			Employee.updateEmployee(employee);
			refreshEmployeeComboBox();
			
			employeeInfoPanel.removeAll();
            employeeInfoPanel.revalidate(); 
            employeeInfoPanel.repaint();
            
            JOptionPane.showMessageDialog(null, "Employee modified successfully");
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
    public void deleteEmployee(Employee employee, JPanel panel) {
    	try {
			Employee.deleteEmployee(employee.getId());
            refreshEmployeeComboBox();
            employeeInfoPanel.removeAll();
            employeeInfoPanel.revalidate(); 
            employeeInfoPanel.repaint();
            
            JOptionPane.showMessageDialog(null, "Employee deleted successfully");
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
    }
}
