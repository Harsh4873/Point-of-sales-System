package com.example;

import java.util.HashMap;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;


/**
 * Manages all of the unit conversions associated with updating the inventory based on what ingredients were used
 * in a completed order. Also manages the opening hour batches
 * @author Jack Weltens
 */
public class InventoryManager {
    private Connection conn;
    private HashMap<String, Double> cumulativeInventoryMap;     // map that does not get cleared until the entire system is restarted


    /**
     * constructor
     * @param conn connection for accessing the database for quantity updates
     */
    public InventoryManager(Connection conn){
        this.conn = conn;
        this.cumulativeInventoryMap = new HashMap<>();
    }


    /**
     * This function is responsible for adding to the cumulative map each time a transaction goes through
     * @param convertedMap  holds the new items added by the newest transaction
     */
    public void updateInventoryMap(HashMap<String, Double> convertedMap){
        for (HashMap.Entry<String, Double> entry : convertedMap.entrySet()) {
            if (cumulativeInventoryMap.containsKey(entry.getKey())) {
                cumulativeInventoryMap.put(entry.getKey(), cumulativeInventoryMap.get(entry.getKey()) + entry.getValue());
            } else {
                cumulativeInventoryMap.put(entry.getKey(), entry.getValue());
            }
        }

    }


    /**
     * This function is responsible for detecting if the cumulative values have gone over the batch limit. It then updates the inventory quantity accordingly
     */
    public void checkBatch(){
        for (HashMap.Entry<String, Double> entry : cumulativeInventoryMap.entrySet()) {
            try{
                PreparedStatement stmt = conn.prepareStatement("SELECT * FROM inventory WHERE inventory_name = ?");
                stmt.setString(1, entry.getKey());  // set the product_name value

                ResultSet result = stmt.executeQuery();

                while (result.next()) {
                    String inventoryType = result.getString("inventory_type").toString();
                    String inventoryName = result.getString("inventory_name").toString();
                    Double batchQuantity = result.getDouble("batch_quantity");
                    Double quantity = result.getDouble("quantity");

                    // this allows for unit conversions from ingredients to inventory quantities
                    Double unitConvertedValue = 0.0;

                    // this switch statement is responsible for taking care of unit conversions
                    switch(inventoryType){
                        case "protein":
                        case "sides":
                        case "produce":
                        case "other":
                            // ounces to pounds conversion
                            unitConvertedValue = entry.getValue() * 0.0625;
                            break;
                        case "consumables":
                        case "supplies":
                        case "drinks":
                            // these are all stored in units so no conversion needed
                            unitConvertedValue = entry.getValue();
                            break;
                        case "sauces":
                            // fluid ounces to gallons conversion
                            unitConvertedValue = entry.getValue() * 0.0078125;
                            break;
                        default:
                            break; 
                    }

                    // if the unitValue happens to be higher then we need to decrease inventory
                    if(unitConvertedValue >= batchQuantity){
                        while(unitConvertedValue >= batchQuantity){
                            quantity -= batchQuantity;
                            unitConvertedValue -= batchQuantity;
    
                            // update cumulative amount based on how we just changed the quantity
                            cumulativeInventoryMap.put(entry.getKey(), unitConvertedValue);
                        }

                        // this allows us to update the inventory in large batch amounts
                        PreparedStatement updateStatement = conn.prepareStatement("UPDATE inventory SET quantity = ? WHERE inventory_name = ?");
                        updateStatement.setDouble(1, quantity);
                        updateStatement.setString(2, inventoryName);

                        updateStatement.executeUpdate();
                    }  
                }
            }
            catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error accessing Database.");
            }
        }

        // this block is for decreasing constant items that go down with each order
        try {
            // bags update
            PreparedStatement updateStatement = conn.prepareStatement("UPDATE inventory SET quantity = quantity - 1 WHERE inventory_name = ?");
            updateStatement.setString(1, "bags");
            updateStatement.executeUpdate();

            // napkins update
            updateStatement.setString(1, "napkins");
            updateStatement.executeUpdate();

            // flatware update
            updateStatement.setString(1, "flatware");
            updateStatement.executeUpdate();

            // fortune cookies update
            updateStatement.setString(1, "fortune_cookies");
            updateStatement.executeUpdate();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error accessing Database.");
        }
    }


    /**
     * This function is responsible for decreasing the inventory at the start of the day when everything is made.
     */
    public void openingCycle(){
        try {
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery("SELECT inventory_name, inventory_type, quantity, batch_quantity FROM inventory");

            while (result.next()) {
                String inventoryName = result.getString("inventory_name");
                String inventoryType = result.getString("inventory_type");
                Double quantity = result.getDouble("quantity");
                Double batchQuantity = result.getDouble("batch_quantity");

                if(inventoryType.equals("protein") || inventoryType.equals("sides") || inventoryType.equals("sauces") || inventoryType.equals("produce") || inventoryType.equals("other")){
                    if(inventoryName.equals("chicken")){
                        // branch for chicken and beef (we want to decrease them by more)
                        quantity = quantity - (3 * batchQuantity);

                        PreparedStatement updateStatement = conn.prepareStatement("UPDATE inventory SET quantity = ? WHERE inventory_name = ?");
                        updateStatement.setDouble(1, quantity);
                        updateStatement.setString(2, inventoryName);

                        updateStatement.executeUpdate();
                    }
                    else{
                        // branch for everything
                        quantity = quantity - batchQuantity;

                        PreparedStatement updateStatement = conn.prepareStatement("UPDATE inventory SET quantity = ? WHERE inventory_name = ?");
                        updateStatement.setDouble(1, quantity);
                        updateStatement.setString(2, inventoryName);

                        updateStatement.executeUpdate();
                    }
                }
                if(inventoryType.equals("consumables")){
                    if(inventoryName.equals("cc_rangoons") || inventoryName.equals("wonton_wrappers") || inventoryName.equals("pastry")){
                        // only inculde these items from the consumable section
                        quantity = quantity - batchQuantity;

                        PreparedStatement updateStatement = conn.prepareStatement("UPDATE inventory SET quantity = ? WHERE inventory_name = ?");
                        updateStatement.setDouble(1, quantity);
                        updateStatement.setString(2, inventoryName);

                        updateStatement.executeUpdate();
                    }
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error accessing Database.");
        }
    }



}
