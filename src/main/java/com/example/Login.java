package com.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Handles the login functionality for the application.
 * @author Harsh Dave, Haresh Raj, Austin Rummel 
 */
public class Login {
    private static final Color BUTTON_COLOR = new Color(70, 130, 180);
    private static final Color BUTTON_TEXT_COLOR = Color.WHITE;
    private static final Font BUTTON_FONT = new Font("Arial", Font.BOLD, 18);
    private static final int BUTTON_SIZE = 60;

    private String employeeName = "";

    /**
     * Constructor
     */
    public Login() {}

    /**
     * Creates the pin pad interface for user login.
     * @param conn Connection to the database
     * @param inventoryButton Button for inventory
     * @param employeesButton Button for employees
     * @param menuButton Button for menu
     * @param f Main application frame
     */
    public void pinpad(Connection conn, JButton inventoryButton, JButton employeesButton, JButton menuButton, JFrame f) {
        inventoryButton.setEnabled(false);
        employeesButton.setEnabled(false);
        menuButton.setEnabled(false);
        setEmployeeName("");

        

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(240, 240, 240));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPasswordField pinField = new JPasswordField();
        pinField.setFont(new Font("Arial", Font.PLAIN, 24));
        pinField.setHorizontalAlignment(JTextField.CENTER);
        pinField.setEditable(true);
        pinField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) && c != KeyEvent.VK_BACK_SPACE) {
                    e.consume();
                }
            }
        });

        panel.add(pinField, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(4, 3, 10, 10));
        buttonPanel.setBackground(new Color(240, 240, 240));

        for (int i = 1; i <= 9; i++) {
            addButton(buttonPanel, pinField, String.valueOf(i));
        }

        addButton(buttonPanel, pinField, "0");

        JButton clearButton = createStyledButton("C");
        clearButton.addActionListener(e -> pinField.setText(""));
        buttonPanel.add(clearButton);

        JButton backspaceButton = createStyledButton("←");
        backspaceButton.addActionListener(e -> {
            String currentText = new String(pinField.getPassword());
            if (currentText.length() > 0) {
                pinField.setText(currentText.substring(0, currentText.length() - 1));
            }
        });
        buttonPanel.add(backspaceButton);

        panel.add(buttonPanel, BorderLayout.CENTER);

        JButton loginButton = new JButton("Login");
        loginButton.setFont(BUTTON_FONT);
        loginButton.setBackground(new Color(46, 204, 113));
        loginButton.setForeground(Color.WHITE);
        loginButton.addActionListener(e -> handleLogin(conn, pinField, inventoryButton, employeesButton, menuButton, f));
        panel.add(loginButton, BorderLayout.SOUTH);

        JOptionPane pane = new JOptionPane(panel, JOptionPane.PLAIN_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[]{}, null);
        JDialog dialog = pane.createDialog("Enter PIN");
        dialog.getRootPane().setDefaultButton(loginButton); 
        dialog.setVisible(true);
    }

    /**
     * Confirmation dialog for login
     * @param conn Connection to the database
     * @param pinField Field containing the entered PIN
     * @param inventoryButton Button for inventory
     * @param employeesButton Button for employees
     * @param menuButton Button for menu
     * @param f Main application frame
     */
    private void handleLogin(Connection conn, JPasswordField pinField, JButton inventoryButton, JButton employeesButton, JButton menuButton, JFrame f) {
        String enteredPin = new String(pinField.getPassword());
        if (checkPinInDatabase(conn, enteredPin, inventoryButton, employeesButton, menuButton)) {           
            f.setEnabled(true);
            SwingUtilities.getWindowAncestor(pinField).dispose();
        } else {
            JOptionPane.showMessageDialog(null, "Invalid PIN.", "Error", JOptionPane.ERROR_MESSAGE);
            pinField.setText("");
        }
    }

    /**
     * Checks the entered PIN against the database
     * @param conn Connection to the database
     * @param pin PIN to check
     * @param inventoryButton Button for inventory
     * @param employeesButton Button for employees
     * @param menuButton Button for menu
     * @return true if the PIN is valid, false otherwise
     */
    private boolean checkPinInDatabase(Connection conn, String pin, JButton inventoryButton, JButton employeesButton, JButton menuButton) {
        String query = "SELECT role, name FROM Employees WHERE employee_id = " + pin;
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                String role = rs.getString("role");
                String employeeName = rs.getString("name");
                setEmployeeName(employeeName);
                boolean isManager = "Manager".equals(role);
                inventoryButton.setEnabled(isManager);
                employeesButton.setEnabled(isManager);
                menuButton.setEnabled(isManager);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Adds a button to the specified panel
     * @param panel Panel to add the button to
     * @param pinField Field for entering the PIN
     * @param label Label for the button
     */
    private static void addButton(JPanel panel, JPasswordField pinField, String label) {
        JButton button = createStyledButton(label);
        button.addActionListener(e -> pinField.setText(new String(pinField.getPassword()) + label));
        panel.add(button);
    }

    /**
     * Creates a styled button with specified label.
     * Improve the look of the login screen
     * @param label Label for the button
     * @return Styled JButton
     */
    private static JButton createStyledButton(String label) {
        JButton button = new JButton(label);
        button.setFont(BUTTON_FONT);
        button.setForeground(BUTTON_TEXT_COLOR);
        button.setBackground(BUTTON_COLOR);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(BUTTON_SIZE, BUTTON_SIZE));
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(BUTTON_COLOR.brighter());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(BUTTON_COLOR);
            }
        });
        return button;
    }

    /**
     * Sets the name of the employee.
     * @param employeeName Name of the employee
     */
    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    /**
     * Returns the name of the employee.
     * @return Name of the employee
     */
    public String getEmployeeName() {
        return employeeName;
    }
}