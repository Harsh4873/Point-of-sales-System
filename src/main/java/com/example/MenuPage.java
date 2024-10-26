package com.example;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents the Menu page of the application.
 * This class manages the display and manipulation of menu items.
 * @author Harsh Dave
 */
public class MenuPage extends JPanel {

    /**
     * The main GUI of the application
     */
    private GUI mainGUI;
    /**
     * The database connection
     */
    private Connection conn;
    /**
     * List of menu items
     */
    private List<MenuItem> menuItems;
    /**
     * Main panel for displaying menu items
     */
    private JPanel mainPanel;
    /**
     * Side panel for filters
     */
    private JPanel filterPanel;
    /**
     * The current filter applied to menu items
     */
    private String currentFilter;

    /**
     * Constructs a MenuPage with the given main GUI and database connection.
     *
     * @param mainGUI The main GUI of the application.
     * @param conn The database connection.
     */
    public MenuPage(GUI mainGUI, Connection conn) {
        this.mainGUI = mainGUI;
        this.conn = conn;
        this.menuItems = new ArrayList<>();
        setLayout(new BorderLayout());
        add(createTopPanel(), BorderLayout.NORTH);
        filterPanel = createSidePanel();
        add(new JScrollPane(filterPanel), BorderLayout.WEST);
        mainPanel = createMainPanel();
        add(new JScrollPane(mainPanel), BorderLayout.CENTER);
        loadMenuItems();
        loadFilters();
    }

    /**
     * Creates the top panel of the MenuPage.
     *
     * @return JPanel containing the top panel elements.
     */
    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout());

        JButton returnButton = new JButton("Return");
        returnButton.addActionListener(e -> mainGUI.switchToMainPage());
        topPanel.add(returnButton, BorderLayout.WEST);

        topPanel.add(new JLabel("Menu", SwingConstants.CENTER), BorderLayout.CENTER);

        JButton addItemButton = new JButton("Add Item");
        addItemButton.addActionListener(e -> showAddItemDialog());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(addItemButton);
        topPanel.add(buttonPanel, BorderLayout.EAST);

        return topPanel;
    }

    /**
     * Creates the side panel for filters.
     *
     * @return JPanel containing the filter options.
     */
    private JPanel createSidePanel() {
        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
        sidePanel.setBorder(BorderFactory.createTitledBorder("FILTERS"));
        sidePanel.setPreferredSize(new Dimension(150, getHeight()));
        return sidePanel;
    }

    /**
     * Creates the main panel for displaying menu items.
     *
     * @return JPanel for displaying menu items.
     */
    private JPanel createMainPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return panel;
    }

    /**
     * Loads and displays filter options in the side panel.
     */
    private void loadFilters() {
        filterPanel.removeAll();

        Set<String> types = menuItems.stream()
                .map(MenuItem::getType)
                .collect(Collectors.toSet());

        addFilterButton("All Items", null);

        for (String type : types) {
            String displayName = formatTypeForDisplay(type);
            addFilterButton(displayName, type);
        }

        filterPanel.add(Box.createVerticalGlue());
        filterPanel.revalidate();
        filterPanel.repaint();
    }

    /**
     * Adds a filter button to the side panel.
     *
     * @param displayName The name to display on the button.
     * @param filterValue The filter value associated with the button.
     */
    private void addFilterButton(String displayName, String filterValue) {
        JButton filterButton = new JButton(displayName);
        filterButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        filterButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, filterButton.getPreferredSize().height));

        filterButton.addActionListener(e -> {
            currentFilter = filterValue;
            if (filterValue == null) {
                displayMenuItems(menuItems);
            } else {
                filterByType(filterValue);
            }
        });

        filterPanel.add(filterButton);
        filterPanel.add(Box.createRigidArea(new Dimension(0, 5)));
    }

    /**
     * Formats the type string for display.
     *
     * @param type The type string to format.
     * @return Formatted type string.
     */
    private String formatTypeForDisplay(String type) {
        switch (type.toLowerCase()) {
            case "entree":
                return "Entrees";
            case "side":
                return "Sides";
            case "drink":
                return "Drinks";
            case "appetizer":
                return "Appetizers";
            default:
                return type.substring(0, 1).toUpperCase() + type.substring(1) + "s";
        }
    }

    /**
     * Filters and displays menu items by type.
     *
     * @param type The type to filter by.
     */
    private void filterByType(String type) {
        List<MenuItem> filteredItems = menuItems.stream()
                .filter(item -> item.getType().equalsIgnoreCase(type))
                .collect(Collectors.toList());
        displayMenuItems(filteredItems);
    }

    /**
     * Loads menu items from the database.
     */
    private void loadMenuItems() 
    {
        menuItems.clear();
        try {
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(
                    "SELECT product_name, price, type FROM menu");

            while (result.next()) {
                String productName = result.getString("product_name");
                double price = result.getDouble("price");
                String type = result.getString("type");

                MenuItem item = new MenuItem(productName, price, type);
                menuItems.add(item);
            }
            displayMenuItems(menuItems);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading menu items: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
        mainGUI.updateMenuPanels(conn);
    }

    /**
     * Displays the given menu items in the main panel.
     *
     * @param itemsToDisplay List of menu items to display.
     */
    private void displayMenuItems(List<MenuItem> itemsToDisplay) {
        mainPanel.removeAll();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.NORTHWEST;

        Map<String, List<MenuItem>> groupedItems = itemsToDisplay.stream()
                .collect(Collectors.groupingBy(MenuItem::getBaseItem));

        for (Map.Entry<String, List<MenuItem>> entry : groupedItems.entrySet()) {
            mainPanel.add(createMenuItemCard(entry.getKey(), entry.getValue()), gbc);
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
     * Creates a card for displaying a menu item.
     *
     * @param baseItem The base item name.
     * @param items List of menu items with different sizes.
     * @return JPanel representing the menu item card.
     */
    private JPanel createMenuItemCard(String baseItem, List<MenuItem> items) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        card.setPreferredSize(new Dimension(300, 200));

        JPanel detailsPanel = new JPanel(new GridLayout(0, 1, 0, 5));
        detailsPanel.add(new JLabel(items.get(0).getDisplayName(), SwingConstants.CENTER));

        for (MenuItem item : items) {
            if (item.getPrice() > 0) {
                detailsPanel.add(new JLabel(item.getSize() + ": $" + String.format("%.2f", item.getPrice())));
            }
        }

        card.add(detailsPanel, BorderLayout.CENTER);

        JButton editButton = new JButton("Edit");
        editButton.addActionListener(e -> showEditItemDialog(items));
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(editButton);

        JButton deleteButton = new JButton("Delete");
        deleteButton.setForeground(Color.RED);
        deleteButton.addActionListener(e -> confirmDeletion(items));
        buttonPanel.add(deleteButton);

        card.add(buttonPanel, BorderLayout.SOUTH);

        return card;
    }

    /**
     * Displays a confirmation dialog for deleting menu items.
     *
     * @param items List of menu items to delete.
     */
    private void confirmDeletion(List<MenuItem> items) {
        int result = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete " + items.get(0).getDisplayName() + "?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (result == JOptionPane.YES_OPTION) {
            removeItems(items);

            try {
                for (MenuItem item : items) {
                    PreparedStatement deleteStmt = conn.prepareStatement(
                            "DELETE FROM menu WHERE product_name = ?");
                    deleteStmt.setString(1, item.getName());
                    deleteStmt.executeUpdate();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error deleting items: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        mainGUI.updateMenuPanels(conn);
    }

    /**
     * Removes the given items from the menu and updates the display.
     *
     * @param items List of menu items to remove.
     */
    private void removeItems(List<MenuItem> items) {
        menuItems.removeAll(items);
        displayMenuItems(menuItems);
        loadFilters();
    }

    /**
     * Displays a dialog for adding a new menu item.
     */
        /**
     * Displays a dialog for adding a new menu item with mandatory price fields for all sizes.
     */
    private void showAddItemDialog() {
        JTextField nameField = new JTextField();
        Map<String, JTextField> priceFields = new HashMap<>();
        String[] sizes = {"S", "M", "L", "K", "N", "F"};
        JPanel panel = new JPanel(new GridLayout(0, 2));
        panel.add(new JLabel("Name:"));
        panel.add(nameField);

        for (String size : sizes) {
            JLabel label = new JLabel("Price (" + size + "):");
            JTextField priceField = new JTextField("0.0");  // Default value set to 0.0
            panel.add(label);
            panel.add(priceField);
            priceFields.put(size, priceField);
        }

        panel.add(new JLabel("Type:"));
        JComboBox<String> typeComboBox = new JComboBox<>(new String[]{"Entree", "Side", "Drink", "Appetizer"});
        panel.add(typeComboBox);

        int result = JOptionPane.showConfirmDialog(null, panel, "Add New Menu Item",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            try {
                String baseName = nameField.getText().toLowerCase().replace(" ", "_");
                String type = ((String) typeComboBox.getSelectedItem()).toLowerCase();

                for (String size : sizes) {
                    double price = Double.parseDouble(priceFields.get(size).getText());
                    String productName = size.toUpperCase() + "_" + baseName;

                    PreparedStatement pstmt = conn.prepareStatement(
                            "INSERT INTO menu (product_name, price, type) VALUES (?, ?, ?)");
                    pstmt.setString(1, productName);
                    pstmt.setDouble(2, price);
                    pstmt.setString(3, type);
                    pstmt.executeUpdate();
                }

                loadMenuItems();
                loadFilters();
            } catch (SQLException | NumberFormatException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error adding item: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        mainGUI.updateMenuPanels(conn);
    }

    /**
     * Displays a dialog for editing menu item prices.
     *
     * @param items List of menu items to edit.
     */
    private void showEditItemDialog(List<MenuItem> items) {
        JPanel panel = new JPanel(new GridLayout(0, 2));
        Map<String, JTextField> priceFields = new HashMap<>();

        for (MenuItem item : items) {
            panel.add(new JLabel(item.getSize() + ":"));
            JTextField priceField = new JTextField(String.valueOf(item.getPrice()));
            panel.add(priceField);
            priceFields.put(item.getSize(), priceField);
        }

        int result = JOptionPane.showConfirmDialog(null, panel, "Edit Menu Item Prices",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            try {
                for (MenuItem item : items) {
                    double newPrice = Double.parseDouble(priceFields.get(item.getSize()).getText());
                    PreparedStatement pstmt = conn.prepareStatement(
                            "UPDATE menu SET price = ? WHERE product_name = ?");
                    pstmt.setDouble(1, newPrice);
                    pstmt.setString(2, item.getName());
                    pstmt.executeUpdate();
                }

                loadMenuItems();
                if (currentFilter != null) {
                    filterByType(currentFilter);
                }
            } catch (SQLException | NumberFormatException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error updating item: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Gets the list of menu items.
     *
     * @return List of MenuItem objects.
     */
    public List<MenuItem> getMenuItems() {
        return menuItems;
    }
}
