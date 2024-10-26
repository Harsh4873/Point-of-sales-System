package com.example;

import javax.swing.*;

import java.util.Vector;

import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Represents the Inventory page of the application.
 * This class manages the display and manipulation of inventory data.
 * @author Harsh Dave, Jarren Tobias, Jack Weltens, Haresh Raj
 */
public class InventoryPage extends JPanel {

    /**
     * The main GUI of the application
     */
    private GUI mainGUI;
    /**
     * The database connection
     */
    private Connection conn;
    /**
     * List of inventory items
     */
    private List<InventoryItem> inventoryItems;
    /**
     * The main panel of the Inventory page
     */
    private JPanel mainPanel;
    /**
     * The side panel containing filter buttons
     */
    private JPanel filterPanel;
    /**
     * The InventoryManager for managing inventory data
     */
    private InventoryManager inventoryManager;
    /**
     * The OrderHistory for displaying product usage
     */
    private OrderHistory orderHistory;
    /**
     * The X_Report for sales per hour for the current day
     */
    private X_report xReport;
    /**
     * The Z_Report for end of day sales totals
     */
    private Z_Report zReport;
    /**
     * Stores the current inventory type selected
     */
    private String currentInventoryType;

    /**
     * Constructs an InventoryPage with the given main GUI and database connection.
     *
     * @param mainGUI The main GUI of the application.
     * @param conn    The database connection.
     */
    public InventoryPage(GUI mainGUI, Connection conn) {
        this.mainGUI = mainGUI;
        this.conn = conn;
        this.inventoryItems = new ArrayList<>();
        this.inventoryManager = new InventoryManager(conn);
        setLayout(new BorderLayout());
        add(createTopPanel(), BorderLayout.NORTH);
        filterPanel = createSidePanel();
        add(filterPanel, BorderLayout.WEST);
        mainPanel = createMainPanel();
        add(new JScrollPane(mainPanel), BorderLayout.CENTER);
        loadInventoryItems();
        loadFilters();
    }


    /**
     * Creates the top panel of the Inventory page.
     *
     * @return JPanel containing the top panel elements.
     */
    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout());

        JButton returnButton = new JButton("Return");
        returnButton.addActionListener(e -> mainGUI.switchToMainPage());
        topPanel.add(returnButton, BorderLayout.WEST);

        topPanel.add(new JLabel("Inventory", SwingConstants.CENTER), BorderLayout.CENTER);

        JButton addItemButton = new JButton("Add Item");
        addItemButton.addActionListener(e -> showAddItemDialog());

        JButton orderHistoryButton = new JButton("Product Usage");

        JButton salesReportButton = new JButton("Sales Report");
        salesReportButton.addActionListener(e -> showSalesReportDialog());

        JButton xReportButton = new JButton("X-Report");

        JButton zReportButton = new JButton("Z-Report");

        JButton startDayButton = new JButton("Start Day");

        // Add action to display the correct chart based on selected filter
        orderHistoryButton.addActionListener(e -> {
            if (currentInventoryType != null && !currentInventoryType.equals("Low Stock")) {
                openOrderHistory(currentInventoryType); // Pass the selected inventory type to show the correct chart
            } else {
                JOptionPane.showMessageDialog(this, "Please select a filter first(not low stock)", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        xReportButton.addActionListener(e -> openXReport());

        zReportButton.addActionListener(e -> openZReport(startDayButton));

        startDayButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Confirm Start Day? This will update the inventory.", "Confirm Start Day", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                inventoryManager.openingCycle();
                startDayButton.setEnabled(false);
            }
            loadInventoryItems();
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(addItemButton);
        buttonPanel.add(orderHistoryButton);
        buttonPanel.add(xReportButton);
        buttonPanel.add(zReportButton);
        buttonPanel.add(salesReportButton);
        buttonPanel.add(startDayButton);
        topPanel.add(buttonPanel, BorderLayout.EAST);

        return topPanel;
    }


    /**
     * Creates the side panel with filter buttons.
     *
     * @return JPanel containing the side panel elements.
     */
    private JPanel createSidePanel() {
        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
        sidePanel.setBorder(BorderFactory.createTitledBorder("FILTERS"));
        return sidePanel;
    }


    /**
     * Creates the main panel for displaying inventory item cards.
     *
     * @return JPanel configured for displaying inventory item cards.
     */
    private JPanel createMainPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return panel;
    }


    /**
     * Loads dynamic filters based on inventory types from the database.
     */
    private void loadFilters() {
        filterPanel.removeAll();

        try {
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery("SELECT DISTINCT inventory_type FROM inventory");

            while (result.next()) {
                String filterType = result.getString("inventory_type");
                String capitalizedFilterType = filterType.substring(0, 1).toUpperCase() + filterType.substring(1);
                JButton filterButton = new JButton(capitalizedFilterType);

                // Update currentInventoryType when filter is clicked
                filterButton.addActionListener(e -> {
                    currentInventoryType = filterType; // Set the current selected inventory type
                    filterByType(filterType); // Display filtered items
                });

                filterPanel.add(filterButton);
            }
            
            addFilterButton("Low Stock", () -> {
                currentInventoryType = "Low Stock"; // Set the current selected inventory type to "Low Stock"
                filterLowStockItems(); // Display low stock items
            });

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading filters: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }

        filterPanel.revalidate();
        filterPanel.repaint();
    }


    /**
     * Adds a filter button to the filter panel.
     *
     * @param buttonText The text to display on the button.
     * @param action     The action to perform when the button is clicked.
     */
    private void addFilterButton(String buttonText, Runnable action) {
        JButton button = new JButton(buttonText);
        button.addActionListener(e -> action.run());
        filterPanel.add(button);
    }

        
    /**
     * Filters inventory items by type and updates the display.
     * @param type The inventory type to filter by.
     */
    private void filterByType(String type) {
        List<InventoryItem> filteredItems = inventoryItems.stream()
                .filter(item -> item.getInventoryType().equals(type))
                .collect(Collectors.toList());
        displayInventoryItems(filteredItems);
    }

    
    /**
     * Updates the inventory page by clearing the current inventory and loading new items.
     */
    public void updateInventory() {
        clearMenuPanels();
        loadInventoryItems();
        revalidate();
        repaint();
    }


    /**
     * Clears the main panel and resets the inventory items list.
     */
    private void clearMenuPanels() {
        mainPanel.removeAll();
        inventoryItems.clear();
    }


    /**
     *  Recieves input from the user on the time frame under study to be used for 
     *  sales history and the product report
     */
    private Vector<String> inputTimeFrame(){

        // initialize vector to store the start and stop times
        Vector<String> startAndStop = new Vector<>();

        // create input fields for start time
        JTextField startYearField = new JTextField(4);

        JComboBox<String> startMonthComboBox = new JComboBox<>(new String[]{
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
        });

        JTextField startDateField = new JTextField(2);

        JComboBox<String> startHourComboBox = new JComboBox<>(new String[]{
                "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"
        });

        JComboBox<String> startAmPmComboBox = new JComboBox<>(new String[]{"AM", "PM"});

        // create input fields for end time
        JTextField endYearField = new JTextField(4);

        JComboBox<String> endMonthComboBox = new JComboBox<>(new String[]{
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
        });

        JTextField endDateField = new JTextField(2);

        JComboBox<String> endHourComboBox = new JComboBox<>(new String[]{
                "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"
        });
        JComboBox<String> endAmPmComboBox = new JComboBox<>(new String[]{"AM", "PM"});

        // panel for layout
        JPanel panel = new JPanel(new GridLayout(2, 8, 10, 5));  // 2 rows, 8 columns for the layout

        // adding start time components to the first row
        panel.add(new JLabel("Start Year:"));
        panel.add(startYearField);
        panel.add(new JLabel("Start Month:"));
        panel.add(startMonthComboBox);
        panel.add(new JLabel("Start Date:"));
        panel.add(startDateField);
        panel.add(new JLabel("Start Hour:"));
        panel.add(startHourComboBox);
        panel.add(new JLabel("AM/PM:"));
        panel.add(startAmPmComboBox);

        // adding end time components to the second row
        panel.add(new JLabel("End Year:"));
        panel.add(endYearField);
        panel.add(new JLabel("End Month:"));
        panel.add(endMonthComboBox);
        panel.add(new JLabel("End Date:"));
        panel.add(endDateField);
        panel.add(new JLabel("End Hour:"));
        panel.add(endHourComboBox);
        panel.add(new JLabel("AM/PM:"));
        panel.add(endAmPmComboBox);

        // show dialog for user input
        int result = JOptionPane.showConfirmDialog(null, panel, "Input Timeframe", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {

            try{
                if(startYearField.getText().trim().isEmpty() || endYearField.getText().trim() == null){
                    throw new NullPointerException("");
                }

                if(startMonthComboBox.getSelectedItem() == null || endMonthComboBox.getSelectedItem() == null){
                    throw new NullPointerException("");
                }

                if(startDateField.getText() == null || endDateField.getText() == null){
                    throw new NullPointerException("");
                }

                if(startHourComboBox.getSelectedItem() == null || endHourComboBox.getSelectedItem() == null){
                    throw new NullPointerException("");
                }

                if(startAmPmComboBox.getSelectedItem() == null || endAmPmComboBox.getSelectedItem() == null){
                    throw new NullPointerException("");
                }

            }
            catch(NullPointerException e){
                JFrame frame = new JFrame("Error in date");
                JOptionPane.showMessageDialog(frame, "Error: All fields must be filled out.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return startAndStop;
            }

            // gather start time input
            String startYear = startYearField.getText();
            String startMonth = String.format("%02d", startMonthComboBox.getSelectedIndex() + 1); // Convert month to 2-digit format
            String startDate = String.format("%02d", Integer.parseInt(startDateField.getText())); // Ensure date is 2-digit
            String startHour = (String) startHourComboBox.getSelectedItem();
            String startAmPm = (String) startAmPmComboBox.getSelectedItem();

            // convert to military time
            int startHourMilitary = 0;
            if(startAmPm.equals("PM") && !startHour.equals("12")){
                startHourMilitary = Integer.parseInt(startHour) + 12;
            }
            else{
                startHourMilitary = Integer.parseInt(startHour);
            }
            if(startAmPm.equals("AM") && startHour.equals("12")){
                startHourMilitary = 0;
            }
            else{
                startHourMilitary = Integer.parseInt(startHour);
            }

            // gather end time input
            String endYear = endYearField.getText();
            String endMonth = String.format("%02d", endMonthComboBox.getSelectedIndex() + 1); // Convert month to 2-digit format
            String endDate = String.format("%02d", Integer.parseInt(endDateField.getText())); // Ensure date is 2-digit
            String endHour = (String) endHourComboBox.getSelectedItem();
            String endAmPm = (String) endAmPmComboBox.getSelectedItem();

            // convert to military time
            int endHourMilitary = 0;
            if(endAmPm.equals("PM") && !endHour.equals("12")){
                endHourMilitary = Integer.parseInt(endHour) + 12;
            }
            else{
                endHourMilitary = Integer.parseInt(endHour);
            }
            if(endAmPm.equals("AM") && endHour.equals("12")){
                endHourMilitary = 0;
            }
            else{
                endHourMilitary = Integer.parseInt(endHour);
            }

            // this is an error checking block to ensure that the dates requested are sequential in the year
            if(Integer.parseInt(startYear) > Integer.parseInt(endYear)){
                JFrame frame = new JFrame("Error in date");
                JOptionPane.showMessageDialog(frame, "Error: not a valid range.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return startAndStop;
            }
            else if(Integer.parseInt(startYear) == Integer.parseInt(endYear)){
                if(Integer.parseInt(startMonth) > Integer.parseInt(endMonth)){
                    JFrame frame = new JFrame("Error in date");
                    JOptionPane.showMessageDialog(frame, "Error: not a valid range.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return startAndStop;
                }
                else if(Integer.parseInt(startMonth) == Integer.parseInt(endMonth)){
                    if(Integer.parseInt(startDate) > Integer.parseInt(endDate)){
                        JFrame frame = new JFrame("Error in date");
                        JOptionPane.showMessageDialog(frame, "Error: not a valid range.", "Input Error", JOptionPane.ERROR_MESSAGE);
                        return startAndStop;
                    }
                    else if(Integer.parseInt(startDate) == Integer.parseInt(endDate)){
                        if(startHourMilitary > endHourMilitary){
                            JFrame frame = new JFrame("Error in date");
                            JOptionPane.showMessageDialog(frame, "Error: not a valid range.", "Input Error", JOptionPane.ERROR_MESSAGE);
                            return startAndStop;
                        }
                    }
                }
            }
            

            // format the start time
            StringBuilder startTimeBuilder = new StringBuilder();
            startTimeBuilder.append(startYear).append("-")
                    .append(startMonth).append("-")
                    .append(startDate).append(" ")
                    .append(String.format("%02d", startHourMilitary)).append(":00:00");


            // format the end time
            StringBuilder endTimeBuilder = new StringBuilder();
            endTimeBuilder.append(endYear).append("-")
                    .append(endMonth).append("-")
                    .append(endDate).append(" ")
                    .append(String.format("%02d", endHourMilitary)).append(":00:00");

            // add the results to the time vector
            startAndStop.add(startTimeBuilder.toString());
            startAndStop.add(endTimeBuilder.toString());
        }
        // this vector holds the start and end time for return
        return startAndStop;
    }


    /**
     * Opens the Product Usage page.
     */
    private void openOrderHistory(String inventoryType) {

        // prompt the user to input a timeframe that will allow for specific elements of the inventory to be accessed
        Vector<String> startAndStop = inputTimeFrame();

        // if this is empty then the user did not input the correct data
        if(startAndStop.isEmpty()){
            return;
        }

        // create a new instance of OrderHistory and pass the inventory type
        orderHistory = new OrderHistory(conn, inventoryType, startAndStop);

        JFrame frame = new JFrame("Product Usage for " + inventoryType);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.add(orderHistory);
        frame.setSize(850, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void openXReport() {
        xReport = new X_report(conn);
        JFrame frame = new JFrame("X-Report");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.add(xReport);
        frame.setSize(850, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void openZReport(JButton startDayButton) {
        zReport = new Z_Report(conn, startDayButton);
        JFrame frame = new JFrame("Z-Report");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.add(zReport);
        frame.setSize(850, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }


    /**
     * Loads inventory items from the database and displays them.
     */
    public void loadInventoryItems() {
        inventoryItems.clear();
        try {
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(
                    "SELECT inventory_name, quantity, recommended_quantity, gameday_quantity, inventory_type, batch_quantity FROM inventory");

            while (result.next()) {
                String inventoryName = result.getString("inventory_name");
                double quantity = result.getDouble("quantity");
                double recommendedQuantity = result.getDouble("recommended_quantity");
                double gamedayQuantity = result.getDouble("gameday_quantity");
                String inventoryType = result.getString("inventory_type");
                double batch = result.getDouble("batch_quantity");

                InventoryItem item = new InventoryItem(inventoryName, quantity, recommendedQuantity,
                        gamedayQuantity, inventoryType, batch);
                inventoryItems.add(item);
            }
            displayInventoryItems(inventoryItems);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading inventory items: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    /**
     * Displays the given list of inventory items in the main panel.
     *
     * @param itemsToDisplay List of inventory items to display.
     */
    private void displayInventoryItems(List<InventoryItem> itemsToDisplay) {
        mainPanel.removeAll();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.NORTHWEST;

        for (InventoryItem item : itemsToDisplay) {
            mainPanel.add(createInventoryItemCard(item), gbc);
            gbc.gridx++;
            if (gbc.gridx > 2) {
                gbc.gridx = 0;
                gbc.gridy++;
            }
        }

        mainPanel.revalidate();
        mainPanel.repaint();
    }


    /**
     * Creates a visual card representing an inventory item.
     *
     * @param item The inventory item to create a card for.
     * @return JPanel containing the inventory item's information.
     */
    private JPanel createInventoryItemCard(InventoryItem item) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        card.setPreferredSize(new Dimension(300, 350));
    
        // Changed GridLayout to have 1 column instead of 2
        JPanel detailsPanel = new JPanel(new GridLayout(0, 1, 0, 5)); // 0 rows, 1 column
        detailsPanel.add(new JLabel("Name: " + item.getName()));
        detailsPanel.add(new JLabel("Type: " + item.getInventoryType()));
        detailsPanel.add(new JLabel("Quantity: " + item.getQuantity()));
        detailsPanel.add(new JLabel("Recommended: " + item.getRecommendedQuantity()));
        detailsPanel.add(new JLabel("Gameday: " + item.getGamedayQuantity()));
        detailsPanel.add(new JLabel("Batch: " + item.getBatch()));
    
        card.add(detailsPanel, BorderLayout.CENTER);
    
        JButton editButton = new JButton("Edit");
        editButton.addActionListener(e -> showEditItemDialog(item));
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(editButton);
        card.add(buttonPanel, BorderLayout.SOUTH);
    
        JButton deleteButton = new JButton("Delete");
        deleteButton.setForeground(Color.RED);
        deleteButton.addActionListener(e -> confirmDeletion(item));
        buttonPanel.add(deleteButton);
    
        return card;
    }


    /**
     * Confirms the deletion of an inventory item.
     *
     * @param item The inventory item to be deleted.
     */
    private void confirmDeletion(InventoryItem item) {
        int result = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete item " + item.getName() + "?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (result == JOptionPane.YES_OPTION) {
            removeItem(item);

            try {
                PreparedStatement deleteStmt = conn.prepareStatement(
                        "DELETE FROM inventory WHERE inventory_name = ? AND inventory_type = ?");
                deleteStmt.setString(1, item.getName());
                deleteStmt.setString(2, item.getInventoryType());
                deleteStmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error deleting item: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    /**
     * Removes an inventory item from the list and updates the display.
     *
     * @param item The inventory item to be removed.
     */
    private void removeItem(InventoryItem item) {
        inventoryItems.remove(item);
        displayInventoryItems(inventoryItems);
    }


    /**
     * Shows a dialog for adding a new inventory item.
     */
    private void showAddItemDialog() {
        JTextField nameField = new JTextField();
        JTextField quantityField = new JTextField();
        JTextField recommendedField = new JTextField();
        JTextField gamedayField = new JTextField();
        JTextField typeField = new JTextField();
        JTextField batchField = new JTextField();
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Quantity:"));
        panel.add(quantityField);
        panel.add(new JLabel("Recommended Quantity:"));
        panel.add(recommendedField);
        panel.add(new JLabel("Gameday Quantity:"));
        panel.add(gamedayField);
        panel.add(new JLabel("Type:"));
        panel.add(typeField);
        panel.add(new JLabel("Batch Quantity:"));
        panel.add(batchField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Add New Item",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            try {
                String name = nameField.getText();
                double quantity = Double.parseDouble(quantityField.getText());
                double recommended = Double.parseDouble(recommendedField.getText());
                double gameday = Double.parseDouble(gamedayField.getText());
                String type = typeField.getText();
                double batch = Double.parseDouble(batchField.getText());

                PreparedStatement pstmt = conn.prepareStatement(
                        "INSERT INTO inventory (inventory_name, quantity, recommended_quantity, gameday_quantity, inventory_type, batch_quantity) VALUES (?, ?, ?, ?, ?, ?)");
                pstmt.setString(1, name);
                pstmt.setDouble(2, quantity);
                pstmt.setDouble(3, recommended);
                pstmt.setDouble(4, gameday);
                pstmt.setString(5, type);
                pstmt.setDouble(6, batch);
                pstmt.executeUpdate();

                loadInventoryItems();
                loadFilters();
            } catch (SQLException | NumberFormatException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error adding item: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    /**
     * Shows a dialog for editing an existing inventory item.
     *
     * @param item The inventory item to be edited.
     */
    private void showEditItemDialog(InventoryItem item) {
        JTextField nameField = new JTextField(item.getName());
        JTextField quantityField = new JTextField(String.valueOf(item.getQuantity()));
        JTextField recommendedField = new JTextField(String.valueOf(item.getRecommendedQuantity()));
        JTextField gamedayField = new JTextField(String.valueOf(item.getGamedayQuantity()));
        JTextField typeField = new JTextField(item.getInventoryType());
        JTextField batchField = new JTextField(String.valueOf(item.getBatch()));
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Quantity:"));
        panel.add(quantityField);
        panel.add(new JLabel("Recommended Quantity:"));
        panel.add(recommendedField);
        panel.add(new JLabel("Gameday Quantity:"));
        panel.add(gamedayField);
        panel.add(new JLabel("Type:"));
        panel.add(typeField);
        panel.add(new JLabel("Batch Quantity:"));
        panel.add(batchField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Edit Item",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            try {
                String name = nameField.getText();
                double quantity = Double.parseDouble(quantityField.getText());
                double recommended = Double.parseDouble(recommendedField.getText());
                double gameday = Double.parseDouble(gamedayField.getText());
                String type = typeField.getText();
                double batch = Double.parseDouble(batchField.getText());

                PreparedStatement pstmt = conn.prepareStatement(
                        "UPDATE inventory SET quantity = ?, recommended_quantity = ?, gameday_quantity = ?, inventory_type = ?, batch_quantity = ? WHERE inventory_name = ?");
                pstmt.setDouble(1, quantity);
                pstmt.setDouble(2, recommended);
                pstmt.setDouble(3, gameday);
                pstmt.setString(4, type);
                pstmt.setDouble(5, batch);
                pstmt.setString(6, name);
                pstmt.executeUpdate();

                loadInventoryItems();
                loadFilters();
            } catch (SQLException | NumberFormatException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error updating item: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    /**
     * Retrieves sales data for products within a given time window.
     *
     * @param startDate The start date of the time window.
     * @param endDate   The end date of the time window.
     * @return List of sales data for products.
     */
    public List<Object[]> getSalesByProduct(String startDate, String endDate) {
        List<Object[]> salesData = new ArrayList<>();
        String query = "SELECT od.product_name, SUM(od.quantity * od.price) AS total_sales " +
                       "FROM order_details od " +
                       "JOIN orders o ON od.order_id = o.order_id " +
                       "WHERE o.time_stamp BETWEEN ? AND ? " +
                       "GROUP BY od.product_name " +
                       "ORDER BY total_sales DESC";
    
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date parsedStartDate = dateFormat.parse(startDate);
            Date parsedEndDate = dateFormat.parse(endDate);
            
            pstmt.setTimestamp(1, new java.sql.Timestamp(parsedStartDate.getTime()));
            pstmt.setTimestamp(2, new java.sql.Timestamp(parsedEndDate.getTime()));
            
            ResultSet rs = pstmt.executeQuery();
    
            while (rs.next()) {
                String productName = rs.getString("product_name");
                double totalSales = rs.getDouble("total_sales");
                salesData.add(new Object[]{productName, totalSales});
            }
        } catch (SQLException | java.text.ParseException e) {
            JOptionPane.showMessageDialog(this, "Error retrieving sales data: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
        return salesData;
    }
    

    /**
     * Displays a sales report for products within a given time window.
     *
     * @param salesData List of sales data for products.
     * @param startDate The start date of the time window.
     * @param endDate   The end date of the time window.
     */
    private void displaySalesReport(List<Object[]> salesData, String startDate, String endDate) {
        String[] columnNames = {"Product Name", "Total Sales"};
        Object[][] data = new Object[salesData.size()][2];
    
        for (int i = 0; i < salesData.size(); i++) {
            data[i][0] = salesData.get(i)[0];
            double totalSales = (double) salesData.get(i)[1];
            data[i][1] = String.format("$%.2f", totalSales);
        }
    
        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);
    
        JFrame frame = new JFrame("Sales Report from " + startDate + " to " + endDate);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.add(scrollPane);
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    

    /**
     * Shows a dialog for entering a time window and displaying a sales report.
     */
    private void showSalesReportDialog() {
        Vector<String> timeFrame = inputTimeFrame(); 
        if (timeFrame.size() == 2) {
            String startDate = timeFrame.get(0);
            String endDate = timeFrame.get(1);
            
            List<Object[]> salesData = getSalesByProduct(startDate, endDate);
            
            displaySalesReport(salesData, startDate, endDate);
        } else {
            JOptionPane.showMessageDialog(null, "Timeframe input was invalid.", 
                                          "Input Invalid", JOptionPane.WARNING_MESSAGE);
        }
    }
    

    /**
     * Filters and displays low stock items.
     */
    private void filterLowStockItems() {
        List<InventoryItem> lowStockItems = inventoryItems.stream()
                .filter(item -> item.getQuantity() < item.getRecommendedQuantity())
                .collect(Collectors.toList());
        displayInventoryItems(lowStockItems);
    }


    /**
     * Retrieves the list of inventory items.
     *
     * @return List of inventory items.
     */
    public List<InventoryItem> getInventoryItems() {
        return inventoryItems;
    }
}
