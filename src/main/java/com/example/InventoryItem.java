package com.example;

/**
 * Represents an item in the inventory system.
 * This class encapsulates the properties of an inventory item.
 * @author Harsh Dave
 */
public class InventoryItem {

    private String name;
    private double quantity;
    private double recommendedQuantity;
    private double gamedayQuantity;
    private String inventoryType;
    private double batch;

    /**
     * Constructs a new InventoryItem with the specified properties.
     * @param name The name of the item.
     * @param quantity The current quantity of the item.
     * @param recommendedQuantity The recommended quantity to keep in stock.
     * @param gamedayQuantity The quantity needed for game day.
     * @param inventoryType The type or category of the inventory item.
     * @param batch The batch size of the item.
     */
    public InventoryItem(String name, double quantity, double recommendedQuantity, 
                         double gamedayQuantity, String inventoryType, double batch) {
        this.name = name;
        this.quantity = quantity;
        this.recommendedQuantity = recommendedQuantity;
        this.gamedayQuantity = gamedayQuantity;
        this.inventoryType = inventoryType;
        this.batch = batch;
    }

    /**
     * Gets the name of the item.
     * @return The name of the item.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the current quantity of the item.
     * @return The current quantity.
     */
    public double getQuantity() {
        return quantity;
    }

    /**
     * Gets the recommended quantity to keep in stock.
     * @return The recommended quantity.
     */
    public double getRecommendedQuantity() {
        return recommendedQuantity;
    }

    /**
     * Gets the quantity needed for game day.
     * @return The game day quantity.
     */
    public double getGamedayQuantity() {
        return gamedayQuantity;
    }

    /**
     * Gets the type or category of the inventory item.
     * @return The inventory type.
     */
    public String getInventoryType() {
        return inventoryType;
    }

    /**
     * Sets the name of the item.
     * @param name The new name for the item.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the current quantity of the item.
     * @param quantity The new quantity.
     */
    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    /**
     * Sets the recommended quantity to keep in stock.
     * @param recommendedQuantity The new recommended quantity.
     */
    public void setRecommendedQuantity(double recommendedQuantity) {
        this.recommendedQuantity = recommendedQuantity;
    }

    /**
     * Sets the quantity needed for game day.
     * @param gamedayQuantity The new game day quantity.
     */
    public void setGamedayQuantity(double gamedayQuantity) {
        this.gamedayQuantity = gamedayQuantity;
    }

    /**
     * Sets the type or category of the inventory item.
     * @param inventoryType The new inventory type.
     */
    public void setInventoryType(String inventoryType) {
        this.inventoryType = inventoryType;
    }

    /**
     * Sets the batch size of the item.
     * @param batch The new batch size.
     */
    public void setBatch(double batch) {
        this.batch = batch;
    }

    /**
     * Gets the batch size of the item.
     * @return The batch size.
     */
    public double getBatch()
    {
        return batch;
    }
}
