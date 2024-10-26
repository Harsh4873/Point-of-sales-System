package com.example;

/**
 * Represents a menu item in the restaurant management system.
 * @author Harsh Dave
 */
public class MenuItem {
    private String name;
    private double price;
    private String size;
    private String baseItem;
    private String type;

    /**
     * Constructs a MenuItem with specified name, price, and type.
     * 
     * @param name  The full name of the product (including size prefix).
     * @param price The price of the menu item.
     * @param type  The type of the menu item.
     */
    public MenuItem(String name, double price, String type) {
        this.name = name;
        this.price = price;
        this.type = type;
    }

    /**
     * Constructs a MenuItem with specified name and price, parsing the name for size and base item.
     * 
     * @param name  The full name of the product.
     * @param price The price of the menu item.
     */
    public MenuItem(String name, double price) {
        this.name = name;
        this.price = price;
        parseName(size, baseItem);
    }

    /**
     * Constructs a MenuItem with specified name.
     * 
     * @param name The full name of the product.
     */
    public MenuItem(String name) {
        this.name = name;
    }

    /**
     * Parses the name to extract size and base item.
     */
    private void parseName(String size, String baseItem) {
        String[] parts = name.split("_", 2);
        this.size = "";
        this.baseItem = name;
        if (parts.length == 2) {
            this.size = parts[0];
            this.baseItem = parts[1];
        }
    }

    /**
     * Gets the name of the menu item.
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the menu item.
     * @param name The new name of the menu item.
     */
    public void setName(String name) {
        this.name = name;
        parseName(size, baseItem);
    }

    /**
     * Gets the price of the menu item.
     * @return price
     */
    public double getPrice() {
        return price;
    }

    /**
     * Sets the price of the menu item.
     * @param price The new price of the menu item.
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Gets the type of the menu item.
     * @return type
     */
    public String getType() {
        return type;
    }

    /**
     * Gets the base item of the menu item.
     * @return name The name of the base item.
     */
    public String getBaseItem() {
        if (name.contains("_")) {
            return name.substring(name.indexOf('_') + 1).replace('_', ' ');
        }
        return name; // Return the original name if no underscore is found
    }

    /**
     * Gets the size of the menu item.
     * @return name.substring(0, name.indexOf('_'))
     */
    public String getSize() {
        if (name.contains("_")) {
            return name.substring(0, name.indexOf('_'));
        }
        return ""; // Return empty if no size prefix is present
    }

    /**
     * Returns a display-friendly version of the base item's name.
     * @return The formatted name of the base item.
     */
    public String getDisplayName() {
        String base = getBaseItem().replace('_', ' ');
        if (base.equalsIgnoreCase("cc rangoon")) {
            return "Cream Cheese Rangoon";
        }
        return base.substring(0, 1).toUpperCase() + base.substring(1);
    }
}