package com.example;

import java.io.File;
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the Employees page of the application.
 * This class manages the display and manipulation of employee data.
 * @author Harsh Dave
 */
public class EmployeesPage extends JPanel {
    /**
     * The main GUI of the application
     */
    private GUI mainGUI;
    /**
     * The database connection
     */
    private Connection conn;
    /**
     * List of employees
     */
    private List<Employee> employees;
    /**
     * Main panel for displaying employee cards
     */
    private JPanel mainPanel;

    /**
     * Constructs an EmployeesPage with the given main GUI and database connection.
     * @param mainGUI The main GUI of the application
     * @param conn The database connection
     */
    public EmployeesPage(GUI mainGUI, Connection conn) {
        this.mainGUI = mainGUI;
        this.conn = conn;
        setLayout(new BorderLayout());
        add(createTopPanel(), BorderLayout.NORTH);
        add(createSidePanel(), BorderLayout.WEST);
        mainPanel = createMainPanel();
        add(new JScrollPane(mainPanel), BorderLayout.CENTER);
        loadEmployees();
    }

    /**
     * Creates the top panel of the Employees page.
     * @return JPanel containing the top panel elements
     */
    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout());
        JButton returnButton = new JButton("Return");
        returnButton.addActionListener(e -> mainGUI.switchToMainPage());
        topPanel.add(returnButton, BorderLayout.WEST);
        topPanel.add(new JLabel("Employees", SwingConstants.CENTER), BorderLayout.CENTER);

        JButton addEmployeeButton = new JButton("Add Employee");
        addEmployeeButton.addActionListener(e -> EmployeePopUp.showAddEmployeeDialog(employees, this::addNewEmployee));
        topPanel.add(addEmployeeButton, BorderLayout.EAST);

        return topPanel;
    }

    /**
     * Creates the side panel with filter buttons.
     * @return JPanel containing the side panel elements
     */
    private JPanel createSidePanel() {
        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
        sidePanel.setBorder(BorderFactory.createTitledBorder("FILTERS"));

        JButton cashiersButton = new JButton("Cashiers");
        JButton managersButton = new JButton("Managers");
        JButton activeButton = new JButton("Active");
        JButton inactiveButton = new JButton("Inactive");
        JButton terminatedButton = new JButton("Terminated");

        cashiersButton.addActionListener(e -> filterEmployees("Cashier"));
        managersButton.addActionListener(e -> filterEmployees("Manager"));
        activeButton.addActionListener(e -> filterEmployeesByStatus(true));
        inactiveButton.addActionListener(e -> filterEmployeesByStatus(false));
        terminatedButton.addActionListener(e -> displayTerminatedEmployees());

        sidePanel.add(cashiersButton);
        sidePanel.add(managersButton);
        sidePanel.add(activeButton);
        sidePanel.add(inactiveButton);
        sidePanel.add(terminatedButton);
        return sidePanel;
    }

    /**
     * Creates the main panel for displaying employee cards.
     * @return JPanel configured for displaying employee cards
     */
    private JPanel createMainPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return panel;
    }

    /**
     * Loads employees from the database and displays them.
     */
    public void loadEmployees() {
        employees = new ArrayList<>();
        try {
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery("SELECT employee_id, name, role, clock_in, clock_out, active_status FROM Employees");

            while (result.next()) {
                int employeeId = result.getInt("employee_id");
                String name = result.getString("name");
                String role = result.getString("role");
                Timestamp clockIn = result.getTimestamp("clock_in");
                Timestamp clockOut = result.getTimestamp("clock_out");
                boolean activeStatus = result.getBoolean("active_status");

                if (!role.equals("Z_Report")) {
                    employees.add(new Employee(employeeId, name, role, clockIn, clockOut, activeStatus));
                }

            }
            displayEmployees(employees);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading employees: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Displays the given list of employees in the main panel.
     * @param employeesToDisplay List of employees to display
     */
    private void displayEmployees(List<Employee> employeesToDisplay) {
        mainPanel.removeAll();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.NORTHWEST;

        for (Employee employee : employeesToDisplay) {
            mainPanel.add(createEmployeeCard(employee), gbc);
            gbc.gridx++;
            if (gbc.gridx > 2) {
                gbc.gridx = 0;
                gbc.gridy++;
            }
        }

        while (gbc.gridx <= 2) {
            mainPanel.add(new JPanel(), gbc);
            gbc.gridx++;
        }

        mainPanel.revalidate();
        mainPanel.repaint();
    }

    /**
     * Filters employees based on their role.
     * @param role The role to filter by
     */
    private void filterEmployees(String role) {
        List<Employee> filteredEmployees = new ArrayList<>();
        for (Employee employee : employees) {
            if (role.equals("Cashier") && !employee.getRole().equals("Manager")) {
                filteredEmployees.add(employee);
            } else if (employee.getRole().equals(role)) {
                filteredEmployees.add(employee);
            }
        }
        displayEmployees(filteredEmployees);
    }

    /**
     * Filters employees based on their active status.
     * @param activeStatus The active status to filter by
     */
    private void filterEmployeesByStatus(boolean activeStatus) {
        List<Employee> filteredEmployees = new ArrayList<>();
        for (Employee employee : employees) {
            if (employee.isActiveStatus() == activeStatus) {
                filteredEmployees.add(employee);
            }
        }
        displayEmployees(filteredEmployees);
    }

    /**
     * Creates a visual card representing an employee.
     * @param employee The employee to create a card for
     * @return JPanel containing the employee's information
     */
    private JPanel createEmployeeCard(Employee employee) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        card.setPreferredSize(new Dimension(300, 200));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        if (!EmployeePopUp.getTerminatedEmployees().contains(employee)) {
            JButton terminateButton = new JButton("Terminate");
            terminateButton.setForeground(Color.RED);
            terminateButton.addActionListener(e -> confirmTermination(employee));
            topPanel.add(terminateButton);
        }
        card.add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout(10, 0));

        JPanel imagePanel = new JPanel();
        imagePanel.setPreferredSize(new Dimension(100, 100));
        String basePath = new File("").getAbsolutePath();
        String imagePath = basePath + File.separator + "pics" + File.separator + employee.getName().replace(" ", "") + ".jpeg";
        File imageFile = new File(imagePath);
        if (imageFile.exists()) {
            ImageIcon imageIcon = new ImageIcon(imagePath);
            Image image = imageIcon.getImage();
            Image newimg = image.getScaledInstance(100, 100, java.awt.Image.SCALE_SMOOTH);
            imageIcon = new ImageIcon(newimg);
            JLabel picLabel = new JLabel(imageIcon);
            imagePanel.add(picLabel);
        } else {
            imagePanel.add(new JLabel("pic", SwingConstants.CENTER));
            imagePanel.setBorder(BorderFactory.createLineBorder(Color.RED));
        }
        centerPanel.add(imagePanel, BorderLayout.WEST);

        JPanel detailsPanel = new JPanel(new GridLayout(4, 1, 0, 5));
        detailsPanel.add(new JLabel("Name: " + employee.getName()));
        detailsPanel.add(new JLabel("Role: " + employee.getRole()));
        if (!EmployeePopUp.getTerminatedEmployees().contains(employee)) {
            detailsPanel.add(new JLabel("Status: " + (employee.isActiveStatus() ? "Active" : "Inactive")));
        }
        centerPanel.add(detailsPanel, BorderLayout.CENTER);

        card.add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        if (!EmployeePopUp.getTerminatedEmployees().contains(employee)) {
            JButton editButton = new JButton("Edit");
            editButton.addActionListener(e -> new EmployeePopUp(employee, this::removeEmployee, this::updateEmployee).setVisible(true));    
            bottomPanel.add(editButton);
        }

        card.add(bottomPanel, BorderLayout.SOUTH);

        return card;
    }

    /**
     * Displays a confirmation dialog for terminating an employee.
     * @param employee The employee to be terminated
     */
    private void confirmTermination(Employee employee) {
        int result = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to terminate " + employee.getName() + "?",
            "Confirm Termination",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (result == JOptionPane.YES_OPTION) {
            EmployeePopUp.getTerminatedEmployees().add(employee);
            removeEmployee(employee);
        }
    }

    /**
     * Removes an employee from the list and updates the display.
     * @param employee The employee to be removed
     */
    private void removeEmployee(Employee employee) 
    {
        employees.remove(employee);
        try {
            PreparedStatement pstmt = conn.prepareStatement(
                "DELETE FROM Employees WHERE employee_id = ?");
            pstmt.setInt(1, employee.getEmployeeId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error removing employee: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
        displayEmployees(employees);
    }

    /**
     * Adds a new employee to the list and updates the display.
     * @param newEmployee The new employee to be added
     */
    private void addNewEmployee(Employee newEmployee) {
        int uniqueId = EmployeePopUp.generateUniqueId(employees);
        employees.add(new Employee(uniqueId, newEmployee.getName(), newEmployee.getRole(), null, null, newEmployee.isActiveStatus()));
        try {
            PreparedStatement pstmt = conn.prepareStatement(
                "INSERT INTO Employees (employee_id, name, role, clock_in, clock_out, active_status) VALUES (?, ?, ?, '2023-09-01 10:00:00', '2023-09-01 17:00:00', ?)");
            pstmt.setInt(1, uniqueId);
            pstmt.setString(2, newEmployee.getName());
            pstmt.setString(3, newEmployee.getRole());
            pstmt.setBoolean(4, newEmployee.isActiveStatus());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding employee: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
        displayEmployees(employees);
    }

    /**
     * Updates an existing employee's name and role in the list and refreshes the display.
     * @param updatedEmployee The employee with updated information
     */
    public void updateEmployee(Employee updatedEmployee) {
        try {
            PreparedStatement pstmt = conn.prepareStatement(
                "UPDATE Employees SET name = ?, role = ? WHERE employee_id = ?");
            pstmt.setString(1, updatedEmployee.getName());
            pstmt.setString(2, updatedEmployee.getRole());
            pstmt.setInt(3, updatedEmployee.getEmployeeId());
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                for (int i = 0; i < employees.size(); i++) {
                    if (employees.get(i).getEmployeeId() == updatedEmployee.getEmployeeId()) {
                        employees.set(i, updatedEmployee);
                        break;
                    }
                }
                displayEmployees(employees);
                JOptionPane.showMessageDialog(this, "Employee updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                throw new SQLException("Employee update failed, no rows affected.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating employee: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Displays the list of terminated employees.
     */
    private void displayTerminatedEmployees() {
        displayEmployees(EmployeePopUp.getTerminatedEmployees());
    }
}