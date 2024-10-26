package com.example;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Vector;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Stores the current order details and keeps track of all values to be displayed to the user.
 * Helps with pricing, allows for removing, and shows general information of the current order
 * that has been built
 * @author Austin Rummel, Haresh Raj, Jack Weltens
 */
public class OrderDetails {
    private Connection conn;
    private GUI gui;
    private InventoryManager runningInventory;

    private Vector<HashMap<String, Integer>> allEntreesMap;     // holds culmination of all entrees that must be listed in a single order
    private Vector<HashMap<String, Integer>> allSidesMap;       // holds culmination of all sides that must be listed in a single order
    private Vector<HashMap<String, Integer>> allExtrasMap;      // holds culmination of all extras that must be listed in a single order
    private Vector<HashMap<String, Integer>> allDrinksMap;      // holds culmination of all drinks that must be listed in a single order
    private Vector<String> allMealSizes;                        // holds culmination of all meal sizes that must be listed in a single order
    private InventoryPage iPage;
    
    /**
     * constructor
     * @param conn this will need to be used to update the database in a future step
     * @param runningInventory this will be used to update the database inventory by batch amounts
     * @param gui this will be used to update the GUI in a future step
     * @param iPage this will be used to update the inventory page in a future step
     */
    public OrderDetails(Connection conn, InventoryManager runningInventory, GUI gui, InventoryPage iPage) {
        this.conn = conn;
        this.gui = gui;
        this.runningInventory = runningInventory;
        this.allEntreesMap = new Vector<>();
        this.allSidesMap = new Vector<>();
        this.allExtrasMap = new Vector<>();
        this.allDrinksMap = new Vector<>();
        this.allMealSizes = new Vector<>();
        this.iPage = iPage;
    }

    
    /**
     * Used to destory all data that has been copied to history in order to prepare for the next order.
     */
    public void OrderDetailsDestructor(){
        allEntreesMap.clear();
        allSidesMap.clear();
        allExtrasMap.clear();
        allDrinksMap.clear();
        allMealSizes.clear();
    }


    /**
     * Adds entrees to the order
     * @param entreeMap the entree to be added
     */
    public void addEntreeMap(HashMap<String, Integer> entreeMap){
        allEntreesMap.add(entreeMap);
    }

    /**
     * Adds sides to the order
     * @param sideMap the side to be added
     */
    public void addSideMap(HashMap<String, Integer> sideMap){
        allSidesMap.add(sideMap);
    }

    /**
     * Adds extras to the order
     * @param extraMap the extra to be added
     */
    public void addExtraMap(HashMap<String, Integer> extraMap){
        allExtrasMap.add(extraMap);
    }

    /**
     * Adds drinks to the order
     * @param drinkMap the drink to be added
     */
    public void addDrinkMap(HashMap<String, Integer> drinkMap){
        allDrinksMap.add(drinkMap);
    }

    /**
     * Adds meal sizes to the order
     * @param mealSize the meal size to be added
     */
    public void addMealSize(String mealSize){
        allMealSizes.add(mealSize);
    }


    /**
     * Removes entrees from the order
     * @param i the index of the entree to be removed
     */
    public void removeFromEntreeMap(int i) {
        allEntreesMap.remove(i);
    }

    /**
     * Removes sides from the order
     * @param i the index of the side to be removed
     */
    public void removeFromSideMap(int i) {
        allSidesMap.remove(i);
    }

    /**
     * Removes extras from the order
     * @param i the index of the extra to be removed
     */
    public void removeFromExtrasMap(int i) {
        allExtrasMap.remove(i);
    }

    /**
     * Removes drinks from the order
     * @param i the index of the drink to be removed
     */
    public void removeFromDrinkMap(int i) {
        allDrinksMap.remove(i);
    }

    /**
     * Removes meal sizes from the order
     * @param i the index of the meal size to be removed
     */
    public void removeFromMealSizesMap(int i) {
        allMealSizes.remove(i);
    }


    
    /**
     * Gets all entrees in the order
     * @return allEntreesMap
     */
    public Vector<HashMap<String, Integer>> getAllEntreesMap() {
        return allEntreesMap;
    }

    /**
     * Gets all sides in the order
     * @return allSidesMap
     */
    public Vector<HashMap<String, Integer>> getAllSidesMap() {
        return allSidesMap;
    }

    /**
     * Gets all extras in the order
     * @return allExtrasMap
     */
    public Vector<HashMap<String, Integer>> getAllExtrasMap() {
        return allExtrasMap;
    }

    /**
     * Gets all drinks in the order
     * @return allDrinksMap
     */
    public Vector<HashMap<String, Integer>> getAllDrinksMap() {
        return allDrinksMap;
    }

    /**
     * Gets all meal sizes in the order
     * @return allMealSizes
     */
    public Vector<String> getAllMealSizes() {
        return allMealSizes;
    }

    
    /**
     * This function is responsible for creating and managing the order information panel at the appropriate time.
     */
    public void displayContents() {

        gui.updateTotalLabel();

        // create the frame
        JFrame frame = new JFrame("Order Details");
        frame.setSize(500, 500);
        
        // create a panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBackground(Color.WHITE);
        
        // check if all vectors are of the same size
        int sizeEntree = allEntreesMap.size();
        int sizeExtras = allExtrasMap.size();
        int sizeDrinks = allDrinksMap.size();

        // this will be built and reset to create the string for each button
        StringBuilder buttonLabel;

        // set prices for assorted meals
        double bowlPrice = 8.30;
        double platePrice = 9.80;
        double bigPlatePrice = 11.30;
        double kidPrice = 6.60;
        double familyPrice = 43.00;


        // add entree/side information to panel
        for (int i = 0; i < sizeEntree; i++) {
            HashMap<String, Integer> entreeMap = allEntreesMap.get(i);
            HashMap<String, Integer> sideMap = allSidesMap.get(i);
            String mealSize = allMealSizes.get(i);

            // skip "Add Ons" as they are handled separately
            if (mealSize.equals("Add Ons")) {
                continue;
            }
            buttonLabel = new StringBuilder(mealSize + " ");

            switch(mealSize) {
                case "Bowl":
                    buttonLabel.append(" $").append(String.format("%.2f", bowlPrice));
                    break;
                case "Plate":
                    buttonLabel.append(" $").append(String.format("%.2f", platePrice));
                    break;
                case "Big_Plate":
                    buttonLabel.append(" $").append(String.format("%.2f", bigPlatePrice));
                    break;
                case "Kid":
                    buttonLabel.append(" $").append(String.format("%.2f", kidPrice));
                    break;
                case "Family":
                    buttonLabel.append(" $").append(String.format("%.2f", familyPrice));
                    break;
            }

            // format entrees/sides
            for (HashMap.Entry<String, Integer> entry : entreeMap.entrySet()) {
                if(!entry.getValue().equals(0)){
                    double price = getItemPrice(mealSize.substring(0,1) + "_" + entry.getKey());
                    buttonLabel.append("<pre>").append(entry.getValue()).append(" ").append(entry.getKey());
                    if (!mealSize.substring(0,1).equals("P") && !mealSize.substring(0,1).equals("B") && !mealSize.substring(0,1).equals("K") && !mealSize.substring(0,1).equals("F")) {
                        buttonLabel.append(String.format(" $%.2f", price));
                    }
                }
                
            }
            for (HashMap.Entry<String, Integer> entry : sideMap.entrySet()) {
                if(!entry.getValue().equals(0)){
                    double price = getItemPrice(mealSize.substring(0,1) + "_" + entry.getKey());
                    buttonLabel.append("<pre>").append(entry.getValue()).append(" ").append(entry.getKey());
                    if (!mealSize.substring(0,1).equals("P") && !mealSize.substring(0,1).equals("B") && !mealSize.substring(0,1).equals("K") && !mealSize.substring(0,1).equals("F")) {
                        buttonLabel.append(String.format(" $%.2f", price));
                    }
                }
            }
        

            // create a button with the combined label
            JButton mealSizeButton = new JButton("<html>" + buttonLabel.toString().replace("\n", "<br>") + "</html>");
            mealSizeButton.setFont(new Font("Arial", Font.PLAIN, 14));

            // allignment and padding
            mealSizeButton.setHorizontalAlignment(SwingConstants.LEFT);
            mealSizeButton.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            mealSizeButton.addActionListener(new CurrentOrderButtonListener(frame, buttonPanel, this, mealSizeButton, i, "entreeSide"));
            buttonPanel.add(mealSizeButton);
            
        }

        // add extras information to panel
        for(int i = 0; i < sizeExtras; i++){
            HashMap<String, Integer> extraMap = allExtrasMap.get(i);

            buttonLabel = new StringBuilder();

            // formatting for all extra options
            for (HashMap.Entry<String, Integer> entry : extraMap.entrySet()) {
                buttonLabel = new StringBuilder();
                if(!entry.getValue().equals(0)){
                    if(entry.getKey().equals("apple_pie_roll")){
                        if(entry.getValue().equals(1)){
                            double price = getItemPrice("S_" + entry.getKey());
                            buttonLabel.append("Small ").append(entry.getKey()).append(" $").append(String.format("%.2f", price)).append("\n");
                        }
                        else if(entry.getValue().equals(2)){
                            double price = getItemPrice("M_" + entry.getKey());
                            buttonLabel.append("Medium ").append(entry.getKey()).append(" $").append(String.format("%.2f", price)).append("\n");
                        }
                        else if(entry.getValue().equals(3)){
                            double price = getItemPrice("L_" + entry.getKey());
                            buttonLabel.append("Large ").append(entry.getKey()).append(" $").append(String.format("%.2f", price)).append("\n");
                        }
                    }
                    else{
                        if(entry.getValue().equals(1)){
                            double price = getItemPrice("S_" + entry.getKey());
                            buttonLabel.append("Small ").append(entry.getKey()).append(" $").append(String.format("%.2f", price)).append("\n");
                        }
                        else if(entry.getValue().equals(2)){
                            double price = getItemPrice("L_" + entry.getKey());
                            buttonLabel.append("Large ").append(entry.getKey()).append(" $").append(String.format("%.2f", price)).append("\n");
                        }
                    }

                    // create a button with the combined label
                    JButton mealSizeButton = new JButton("<html>" + buttonLabel.toString().replace("\n", "<br>") + "</html>");
                    mealSizeButton.setFont(new Font("Arial", Font.PLAIN, 14));

                    // allignment and padding
                    mealSizeButton.setHorizontalAlignment(SwingConstants.LEFT);
                    mealSizeButton.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                    mealSizeButton.addActionListener(new CurrentOrderButtonListener(frame, buttonPanel, this, mealSizeButton, i, "extra"));
                    buttonPanel.add(mealSizeButton);
                }
            }

            
        }

        // add drinks information to the panel
        for(int i = 0; i < sizeDrinks; i++){
            HashMap<String, Integer> drinkMap = allDrinksMap.get(i);

            buttonLabel = new StringBuilder();

            // formatting for drink options
            for (HashMap.Entry<String, Integer> entry : drinkMap.entrySet()) {
                buttonLabel = new StringBuilder();
                if(!entry.getValue().equals(0)){
                    if(entry.getKey().equals("fountain_drink")){
                        if(entry.getValue().equals(1)){
                            double price = getItemPrice("S_" + entry.getKey());
                            buttonLabel.append("Small ").append(entry.getKey()).append(" $").append(String.format("%.2f", price)).append("\n");
                        }
                        else if(entry.getValue().equals(2)){
                            double price = getItemPrice("M_" + entry.getKey());
                            buttonLabel.append("Medium ").append(entry.getKey()).append(" $").append(String.format("%.2f", price)).append("\n");
                        }
                        else if(entry.getValue().equals(3)){
                            double price = getItemPrice("L_" + entry.getKey());
                            buttonLabel.append("Large ").append(entry.getKey()).append(" $").append(String.format("%.2f", price)).append("\n");
                        }
                    }
                    else{
                        double price = getItemPrice("N_" + entry.getKey());
                        buttonLabel.append(entry.getKey()).append(" $").append(String.format("%.2f", price)).append("\n");
                    }
                    // create a button with the combined label
                    JButton mealSizeButton = new JButton("<html>" + buttonLabel.toString().replace("\n", "<br>") + "</html>");
                    mealSizeButton.setFont(new Font("Arial", Font.PLAIN, 14));

                    // allignment and padding
                    mealSizeButton.setHorizontalAlignment(SwingConstants.LEFT);
                    mealSizeButton.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                    mealSizeButton.addActionListener(new CurrentOrderButtonListener(frame, buttonPanel, this, mealSizeButton, i, "drink"));
                    buttonPanel.add(mealSizeButton);
                }
            }
        }
        

        JButton payButton = new JButton("PAY");

        // add action listeners
        payButton.addActionListener(new PayButtonListener(frame, this, conn, runningInventory, gui, iPage));

        // create a panel for the buttons
        JPanel controlPanel = new JPanel();
        controlPanel.add(payButton);

        // makes order scrollable for large orders
        JScrollPane scrollPane = new JScrollPane(buttonPanel);
        
        frame.setLayout(new BorderLayout());
        frame.add(scrollPane);
        frame.add(controlPanel, BorderLayout.SOUTH);
        
        // make the frame visible
        frame.setVisible(true);
    }

    private double getItemPrice(String itemName) {
        double price = 0.0;
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT price FROM menu WHERE product_name = ?");
            stmt.setString(1, itemName);
            ResultSet result = stmt.executeQuery();
    
            if (result.next()) {
                price = result.getDouble("price");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error accessing Database.");
        }
        return price;
    }
    
}
