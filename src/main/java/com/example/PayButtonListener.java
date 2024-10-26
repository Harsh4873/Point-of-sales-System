package com.example;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Vector;
import java.util.Random;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.time.LocalDateTime;

import javax.swing.*;
import com.example.Total.Triple;


/**
 * Takes action when the pay button is pressed to correctly update the order details, history, 
 * and inventory SQL tables
 * @author Jack Weltens, Austin Rummel
 */
public class PayButtonListener implements ActionListener{
    private Connection conn;
    private GUI gui;
    private InventoryManager runningInventory;
    private JFrame frame;                                       // this hold the current GUI frame
    private OrderDetails orderDetails;                          // this holds each order to be added to the history
    private HashMap<String, Double> inventoryMap;               // this holds a map of all menu items put into a form that can read from ingredients
    private HashMap<String, Double> convertedInventoryMap;      // this holds the same map as above but converted to raw material counts
    private InventoryPage iPage;
    /**
     * constructor
     * @param frame
     * @param orderDetails
     * @param conn
     * @param runningInventory
     */
    
    PayButtonListener(JFrame frame, OrderDetails orderDetails, Connection conn, InventoryManager runningInventory, GUI gui, InventoryPage iPage){
        this.frame = frame;
        this.orderDetails = orderDetails;
        this.runningInventory = runningInventory;
        this.conn = conn;
        this.gui = gui;
        this.inventoryMap = new HashMap<>();
        this.convertedInventoryMap = new HashMap<>();
        this.iPage = iPage; 
    }

    
    /**
     * This function is responsible for detecting when a button was pressed and taking the appropriate actions.
     * This will vary fort each type of button.
     * @param e action that occured
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        
        // call function to update order history
        updateOrders();
        
        // call function to determine what exists in the order but on an inventory scale
        determineInventoryCounts();
        
        // call function to convert the menu items into their raw materials
        convertInventoryCounts();
        
        // send converted inventory counts to a class that will keep track and reduce inventory by batch amounts
        runningInventory.updateInventoryMap(convertedInventoryMap);
        runningInventory.checkBatch();
        
        // remove all buttons from order panel and dispose of it
        frame.getContentPane().removeAll();
        frame.repaint();
        frame.dispose();
        
        // destruct order detials to prepare for the next set of items
        orderDetails.OrderDetailsDestructor();
        
        gui.updateTotalLabel();   
        
        // update the inventory page
        iPage.updateInventory();
    }

    
    /**
     * Responsible for converting the ordered list into a SQL format to be sent to the inventory.
     */
    public void determineInventoryCounts(){
        // get all vectors 
        Vector<String> allMealSizes = orderDetails.getAllMealSizes();
        Vector<HashMap<String, Integer>> allEntreesMap = orderDetails.getAllEntreesMap();
        Vector<HashMap<String, Integer>> allSidesMap = orderDetails.getAllSidesMap();
        Vector<HashMap<String, Integer>> allExtrasMap = orderDetails.getAllExtrasMap();
        Vector<HashMap<String, Integer>> allDrinksMap = orderDetails.getAllDrinksMap();
        
        
        int sizeEntree = allEntreesMap.size();
        int sizeExtras = allExtrasMap.size();
        int sizeDrinks = allDrinksMap.size();

        // this will be built and reset to create the string for each database call
        StringBuilder sqlString;

        //add entree/side information to inventoryMap
        for (int i = 0; i < sizeEntree; i++) {
            HashMap<String, Integer> entreeMap = allEntreesMap.get(i);
            HashMap<String, Integer> sideMap = allSidesMap.get(i);
            String mealSize = allMealSizes.get(i);

            // meal size creates button
            if(mealSize.equals("Add Ons")){
                continue;
            }

            sqlString = new StringBuilder();

            // format entrees/sides
            for (HashMap.Entry<String, Integer> entry : entreeMap.entrySet()) {
                if(!entry.getValue().equals(0)){

                    // we must determine how much inventory must be subtracted and this variable is used to do that
                    Double inventoryValue = 0.0;

                    switch(mealSize){
                        case "Small":
                            sqlString.append("S_");
                            inventoryValue = 1.0;
                            break;
                        case "Medium":
                            sqlString.append("M_");
                            inventoryValue = 1.0;
                            break;
                        case "Large":
                            sqlString.append("L_");
                            inventoryValue = 1.0;
                            break;
                        case "Family":
                            sqlString.append("F_");
                            inventoryValue = Double.valueOf(entry.getValue());
                            break;
                        case "Kid":
                            sqlString.append("K_");
                            inventoryValue = 1.0;
                            break;
                        case "Bowl":
                            sqlString.append("N_");
                            inventoryValue = 1.0;
                            break;
                        case "Plate":
                            // in the event of a plate/big_plate we must consider that the amount of an item could be more than one
                            sqlString.append("N_");
                            inventoryValue = Double.valueOf(entry.getValue());
                            break;
                        case "Big_Plate":
                            sqlString.append("N_");
                            inventoryValue = Double.valueOf(entry.getValue());
                            break;
                        default:
                            break;
                    }
                    
                    sqlString.append(entry.getKey());

                    // must add all elements and update counts accordingly
                    if (inventoryMap.containsKey(sqlString.toString())) {
                        inventoryMap.put(sqlString.toString(), inventoryMap.get(sqlString.toString()) + inventoryValue);
                    } else {
                        inventoryMap.put(sqlString.toString(), inventoryValue);
                    }

                    sqlString = new StringBuilder();
                }
                
            }

            // this block is built to determine if the inventory needs to updated based on what kind of meal was ordered (boxes, bowls, etc)

            if(mealSize.equals("Bowl")){

                // we use the same checking method as above for the hashmap
                sqlString.append("bowls");

                if (convertedInventoryMap.containsKey(sqlString.toString())) {
                    convertedInventoryMap.put(sqlString.toString(), convertedInventoryMap.get(sqlString.toString()) + 1.0);
                } else {
                    convertedInventoryMap.put(sqlString.toString(), 1.0);
                }

                // both the bowl and the lid must be added
                sqlString = new StringBuilder();
                sqlString.append("bowl_lids");

                if (convertedInventoryMap.containsKey(sqlString.toString())) {
                    convertedInventoryMap.put(sqlString.toString(), convertedInventoryMap.get(sqlString.toString()) + 1.0);
                } else {
                    convertedInventoryMap.put(sqlString.toString(), 1.0);
                }

                sqlString = new StringBuilder();
            }
            else if(mealSize.equals("Plate")){

                sqlString.append("plates");

                if (convertedInventoryMap.containsKey(sqlString.toString())) {
                    convertedInventoryMap.put(sqlString.toString(), convertedInventoryMap.get(sqlString.toString()) + 1.0);
                } else {
                    convertedInventoryMap.put(sqlString.toString(), 1.0);
                }

                sqlString = new StringBuilder();
            }
            else if(mealSize.equals("Big_Plate")){

                sqlString.append("plates");

                if (convertedInventoryMap.containsKey(sqlString.toString())) {
                    convertedInventoryMap.put(sqlString.toString(), convertedInventoryMap.get(sqlString.toString()) + 1.0);
                } else {
                    convertedInventoryMap.put(sqlString.toString(), 1.0);
                }

                sqlString = new StringBuilder();
                sqlString.append("M_box");

                if (convertedInventoryMap.containsKey(sqlString.toString())) {
                    convertedInventoryMap.put(sqlString.toString(), convertedInventoryMap.get(sqlString.toString()) + 1.0);
                } else {
                    convertedInventoryMap.put(sqlString.toString(), 1.0);
                }

                sqlString = new StringBuilder();
            }

            // end of the block to determine if the inventory needs to updated based on what kind of meal was ordered


            for (HashMap.Entry<String, Integer> entry : sideMap.entrySet()) {
                if(!entry.getValue().equals(0)){

                    // we must determine how much inventory must be subtracted and this variable is used to do that
                    Double inventoryValue = 0.0;

                    // sides are unique because we may be able to have half and half of one in a given entree
                    switch(mealSize){
                        case "Medium":
                            sqlString.append("M_");
                            inventoryValue = 1.0;
                            break;
                        case "Large":
                            sqlString.append("L_");
                            inventoryValue = 1.0;
                            break;
                        // kid, bowl, plate, and big_plate all have the ability to have half sides
                        case "Kid":
                            sqlString.append("K_");
                            if(sideMap.size() > 1){
                                inventoryValue = 0.5;
                            }
                            else{
                                inventoryValue = 1.0;
                            }
                            break;
                        case "Bowl":
                            sqlString.append("N_");
                            if(sideMap.size() > 1){
                                inventoryValue = 0.5;
                            }
                            else{
                                inventoryValue = 1.0;
                            }
                            break;
                        case "Plate":
                            sqlString.append("N_");
                            if(sideMap.size() > 1){
                                inventoryValue = 0.5;
                            }
                            else{
                                inventoryValue = 1.0;
                            }
                            break;
                        case "Big_Plate":
                            sqlString.append("N_");
                            if(sideMap.size() > 1){
                                inventoryValue = 0.5;
                            }
                            else{
                                inventoryValue = 1.0;
                            }
                            break;
                        case "Family":
                            sqlString.append("L_");
                            inventoryValue = Double.valueOf(entry.getValue());
                            break;
                        default:
                            break;
                    }
                    
                    sqlString.append(entry.getKey());

                    if (inventoryMap.containsKey(sqlString.toString())) {
                        inventoryMap.put(sqlString.toString(), inventoryMap.get(sqlString.toString()) + inventoryValue);
                    } else {
                        inventoryMap.put(sqlString.toString(), inventoryValue);
                    }

                    sqlString = new StringBuilder();
                }
                
            }
            
        }

        // add extra information to inventoryMap
        for(int i = 0; i < sizeExtras; i++){
            HashMap<String, Integer> extraMap = allExtrasMap.get(i);

            sqlString = new StringBuilder();

            // formatting for all extra options
            for (HashMap.Entry<String, Integer> entry : extraMap.entrySet()) {
                sqlString = new StringBuilder();
                
                // format extra string based on the product
                if(!entry.getValue().equals(0)){
                    if(entry.getKey().equals("apple_pie_roll")){
                        if(entry.getValue().equals(1)){
                            sqlString.append("S_").append(entry.getKey());
                        }
                        else if(entry.getValue().equals(2)){
                            sqlString.append("M_ ").append(entry.getKey());
                        }
                        else if(entry.getValue().equals(3)){
                            sqlString.append("L_").append(entry.getKey());
                        }
                    }
                    else{
                        if(entry.getValue().equals(1)){
                            sqlString.append("S_").append(entry.getKey());
                        }
                        else if(entry.getValue().equals(2)){
                            sqlString.append("L_").append(entry.getKey());
                        }
                    }
                }

                // add this item if it does not exist to the hashmap otherwise just increase the value
                if (inventoryMap.containsKey(sqlString.toString())) {
                    inventoryMap.put(sqlString.toString(), inventoryMap.get(sqlString.toString()) + 1.0);
                } else {
                    inventoryMap.put(sqlString.toString(), 1.0);
                }
            }
        }

        // add drink information to inventoryMap
        for(int i = 0; i < sizeDrinks; i++){
            HashMap<String, Integer> drinkMap = allDrinksMap.get(i);

            sqlString = new StringBuilder();

            // formatting for drink options
            for (HashMap.Entry<String, Integer> entry : drinkMap.entrySet()) {
                sqlString = new StringBuilder();
                if(!entry.getValue().equals(0)){
                    if(entry.getKey().equals("fountain_drink")){
                        if(entry.getValue().equals(1)){
                            sqlString.append("S_").append(entry.getKey());
                        }
                        else if(entry.getValue().equals(2)){
                            sqlString.append("M_").append(entry.getKey());
                        }
                        else if(entry.getValue().equals(3)){
                            sqlString.append("L_").append(entry.getKey());
                        }
                    }
                    else{
                        sqlString.append(entry.getKey());
                    }
                }
                
                // add this item if it does not exist to the hashmap otherwise just increase the value
                if (inventoryMap.containsKey(sqlString.toString())) {
                    inventoryMap.put(sqlString.toString(), inventoryMap.get(sqlString.toString()) + 1.0);
                } else {
                    inventoryMap.put(sqlString.toString(), 1.0);
                }
            }
        }
    }


    
    /**
     * Responsible for converting the menu items to raw materials.
     */
    public void convertInventoryCounts(){

        // iterate through the order and append to the raw materials map accordingly

        for (HashMap.Entry<String, Double> entry : inventoryMap.entrySet()) {

            // items must be stripped down to their ingredients from the database
            try{
                PreparedStatement stmt = conn.prepareStatement("SELECT * FROM ingredients WHERE product_name = ?");
                stmt.setString(1, entry.getKey());  // set the product_name value

                ResultSet result = stmt.executeQuery();

                while (result.next()) {
                    String ingredientItem = result.getString("inventory_name").toString();
                    Double ingredientValue = result.getDouble("inventory_quantity");
                    Double ingredientCount = entry.getValue();

                    Double ingredientUpdate = ingredientCount * ingredientValue;

                    if (convertedInventoryMap.containsKey(ingredientItem)) {
                        convertedInventoryMap.put(ingredientItem, convertedInventoryMap.get(ingredientItem) + ingredientUpdate);
                    } 
                    else {
                        convertedInventoryMap.put(ingredientItem, ingredientUpdate);
                    }
                }
            }
            catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error accessing Database.");
            }
            
        }
    }

    private void updateOrders() {

        // Create random order ID & test if it's in use
        Random random = new Random();
        int orderID = 0;
        
        while (true) {
            orderID = random.nextInt(1_000_000) + 1;

            try {
                PreparedStatement stmt = conn.prepareStatement("SELECT CASE WHEN EXISTS(SELECT order_id FROM orders WHERE order_id = ?) THEN TRUE ELSE FALSE END");
                stmt.setInt(1, orderID);
                ResultSet result = stmt.executeQuery();

                boolean orderExists = true;
                while (result.next()) {
                    orderExists = result.getBoolean(1); // Runs once
                }

                if (orderExists) {
                    continue;
                } else {
                    break;
                }

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error accessing Database.");
            }
        }

        // Get employee name
        String employeeName = gui.getLogin().getEmployeeName();

        // Get current date and time
        LocalDateTime now = LocalDateTime.now();

        // Get total
        Total total = new Total(orderDetails, conn);
        double orderTotal = total.getTotal();

        // Insert order into orders table
        try {
            PreparedStatement pstmt = conn.prepareStatement(
                            "INSERT INTO orders (order_id, name, total, time_stamp) VALUES (?, ?, ?, ?)");
            pstmt.setInt(1, orderID);
            pstmt.setString(2, employeeName);

            // Ensure total has two decimal places
            BigDecimal totalPrice = new BigDecimal(orderTotal).setScale(2, RoundingMode.HALF_UP);
            pstmt.setBigDecimal(3, totalPrice);

            // Truncate nanoseconds from the LocalDateTime to remove decimals from seconds
            LocalDateTime truncatedDateTime = now.withNano(0);

            pstmt.setTimestamp(4, java.sql.Timestamp.valueOf(truncatedDateTime));
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error accessing Database.");
            
        }

        // order_details table
        Vector<Triple<String, Double, Integer>> itemAndPrice = total.getItemAndPrice();
        for (Triple<String, Double, Integer> entry : itemAndPrice) {
            String itemName = entry.getFirst();
            double itemPrice = entry.getSecond();
            int itemQuantity = entry.getThird();

            try {
                PreparedStatement pstmt = conn.prepareStatement(
                                "INSERT INTO order_details (order_id, product_name, quantity, price) VALUES (?, ?, ?, ?)");
                pstmt.setInt(1, orderID);
                pstmt.setString(2, itemName);
                pstmt.setInt(3, itemQuantity);

                // Ensure total has two decimal places
                BigDecimal itemPriceBD = new BigDecimal(itemPrice).setScale(2, RoundingMode.HALF_UP);
                pstmt.setBigDecimal(4, itemPriceBD);

                pstmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error accessing Database.");
            }
        }
    }
}

