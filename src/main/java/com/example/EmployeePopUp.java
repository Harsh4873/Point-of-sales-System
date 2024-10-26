package com.example;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;
import java.util.List;
import java.util.ArrayList;

/**
 * EmployeePopUp class extends JDialog to manage employee details and actions.
 * @author Harsh Dave
 */
public class EmployeePopUp extends JDialog {
    /**
     * The employee to be managed
     */
    private Employee employee;
    /**
     * Text field for employee name
     */
    private JTextField nameField;
    /**
     * Text field for employee role
     */
    private JTextField roleField;
    /**
     * Consumer action on employee removal
     */
    private Consumer<Employee> onEmployeeRemoved;
    /**
     * Consumer action on employee update
     */
    private Consumer<Employee> onEmployeeUpdated;
    private static List<Employee> terminatedEmployees = new ArrayList<>();  // List of terminated employees

    /**
     * Constructor for EmployeePopUp.
     * @param employee The employee to be managed.
     * @param onEmployeeRemoved Consumer action on employee removal.
     * @param onEmployeeUpdated Consumer action on employee update.
     */
    public EmployeePopUp(Employee employee, Consumer<Employee> onEmployeeRemoved, Consumer<Employee> onEmployeeUpdated) {
        this.employee = employee;
        this.onEmployeeRemoved = onEmployeeRemoved;
        this.onEmployeeUpdated = onEmployeeUpdated;

        setTitle("Employee Details");
        setModal(true);
        setSize(300, 200);
        setLayout(new BorderLayout());

        JPanel mainPanel = createMainPanel();
        add(mainPanel, BorderLayout.CENTER);

        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
    }

    /**
     * Creates the main panel with employee details.
     * @return JPanel with employee details.
     */
    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        mainPanel.add(new JLabel("Name:"));
        nameField = new JTextField(employee.getName());
        mainPanel.add(nameField);

        mainPanel.add(new JLabel("Role:"));
        roleField = new JTextField(employee.getRole());
        mainPanel.add(roleField);

        return mainPanel;
    }

    /**
     * Creates the button panel with actions.
     * @return JPanel with action buttons.
     */
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton terminateButton = new JButton("Terminate");
        terminateButton.setForeground(Color.RED);
        terminateButton.addActionListener(e -> terminateEmployee());
        buttonPanel.add(terminateButton);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> saveChanges());
        buttonPanel.add(saveButton);

        return buttonPanel;
    }

    /**
     * Handles the termination of an employee.
     */
    private void terminateEmployee() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to terminate this employee? This action cannot be undone.",
                "Confirm Termination",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            terminatedEmployees.add(employee);
            onEmployeeRemoved.accept(employee);
            JOptionPane.showMessageDialog(this, "Employee terminated and removed from the system.");
            dispose();
        }
    }

    /**
     * Saves changes made to the employee details.
     */
    private void saveChanges() {
        employee.setName(nameField.getText());
        employee.setRole(roleField.getText());
        onEmployeeUpdated.accept(employee);
        dispose();
    }

    /**
     * Static method to create and show dialog for adding a new employee.
     * @param employees List of current employees.
     * @param onEmployeeAdded Consumer action on employee addition.
     */
    public static void showAddEmployeeDialog(List<Employee> employees, Consumer<Employee> onEmployeeAdded) {
        JDialog addDialog = new JDialog((Frame)null, "Add New Employee", true);
        addDialog.setSize(300, 200);
        addDialog.setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField nameField = new JTextField();
        JTextField roleField = new JTextField();

        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Role:"));
        panel.add(roleField);

        addDialog.add(panel, BorderLayout.CENTER);

        JButton addButton = new JButton("Add Employee");
        addButton.addActionListener(e -> {
            String name = nameField.getText();
            String role = roleField.getText();
            if (!name.isEmpty() && !role.isEmpty()) {
                Employee newEmployee = new Employee(generateUniqueId(employees), name, role, null, null, false);
                onEmployeeAdded.accept(newEmployee);
                addDialog.dispose();
            } else {
                JOptionPane.showMessageDialog(addDialog, "Please fill all fields.");
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(addButton);
        addDialog.add(buttonPanel, BorderLayout.SOUTH);

        addDialog.setLocationRelativeTo(null);
        addDialog.setVisible(true);
    }

    /**
     * Generates a unique ID for a new employee.
     * @param employees List of current employees to check against.
     * @return int unique ID.
     */
    public static int generateUniqueId(List<Employee> employees) {
        int uniqueId;
        boolean idExists;
        do {
            uniqueId = (int) (Math.random() * 10000);
            idExists = false;
            for (Employee emp : employees) {
                if (emp.getEmployeeId() == uniqueId) {
                    idExists = true;
                    break;
                }
            }
        } while (idExists);
        return uniqueId;
    }

    /**
     * Retrieves the list of terminated employees.
     * @return List of terminated employees.
     */
    public static List<Employee> getTerminatedEmployees() {
        return terminatedEmployees;
    }
}
