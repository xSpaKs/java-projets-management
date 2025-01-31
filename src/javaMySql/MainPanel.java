package javaMySql;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainPanel extends JFrame {
    private EmployeePanel employeePanel;
    private ProjectPanel projectPanel;

    public MainPanel() {
        setTitle("Managements of employees and projects");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        employeePanel = new EmployeePanel();
        projectPanel = new ProjectPanel();

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Employees", employeePanel);
        tabbedPane.addTab("Projects", projectPanel);

        add(tabbedPane);
    }

    public static void main(String[] args) {
    	new MainPanel().setVisible(true);
    }
}