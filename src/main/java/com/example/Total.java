package com.example;

import java.util.HashMap;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import javax.swing.JOptionPane;

/**
 * The Total class is responsible for calculating the total price of an order.
 * @author Austin Rummel
 */
public class Total {
    private Connection conn;                                    // connection for accessing the database

    double total;
    OrderDetails totalOrder;

    /**
     * A generic class to hold three related objects.
     *
     * @param <F> the type of the first object
     * @param <S> the type of the second object
     * @param <T> the type of the third object
     */
    public class Triple<F, S, T> {
        private F first;
        private S second;
        private T third;

        /**
         * Constructs a new Triple with the specified values.
         *
         * @param first the first value
         * @param second the second value
         * @param third the third value
         */
        public Triple(F first, S second, T third) {
            this.first = first;
            this.second = second;
            this.third = third;
        }
        
        /**
         * Returns the first value of the Triple.
         *
         * @return the first value of the Triple
         */
        public F getFirst() {
            return first;
        }
        
        /**
         * Returns the second value of the Triple.
         *
         * @return the second value of the Triple
         */
        public S getSecond() {
            return second;
        }

        /**
         * Returns the third value of the Triple.
         *
         * @return the third value of the Triple
         */
        public T getThird() {
            return third;
        }
    }

    Vector<Triple<String, Double, Integer>> itemAndPrice = new Vector<Triple<String, Double, Integer>>(); // For order_details table

    /**
     * Constructs a new Total object with the specified order details and database connection.
     *
     * @param totalOrder the order details
     * @param conn the database connection
     */
    public Total(OrderDetails totalOrder, Connection conn) {
        this.totalOrder = totalOrder;
        this.conn = conn;

        total = calculateTotal();
    }

    /**
     * Calculates the total price of the order.
     *
     * @return the total price of the order
     */
    public double calculateTotal() {
        double total = 0.0;

        // Base prices for different meal sizes
        double bowlPrice = 8.30;
        double platePrice = 9.80;
        double bigPlatePrice = 11.30;
        double kidPrice = 6.60;
        double familyPrice = 43.00;


        // Loop through meal sizes, check for premium entrees (non extras/drinks)
        for (int i = 0; i < totalOrder.getAllMealSizes().size(); i++) {
            String mealSize = totalOrder.getAllMealSizes().get(i);
            if (mealSize != "Add Ons") {
                String prefix = "";
                double basePrice = 0.0;
                Integer numItems = 0;
                
                // Add base price and set size prefix
                switch (mealSize) {
                    case "Bowl":
                        basePrice = bowlPrice;
                        prefix = "N_";
                        numItems = 2;  
                        break;
                    case "Plate":
                        basePrice = platePrice;
                        prefix = "N_";
                        numItems = 3;
                        break;
                    case "Big_Plate":
                        basePrice = bigPlatePrice;
                        prefix = "N_";
                        numItems = 4;
                        break;
                    case "Kid":
                        basePrice = kidPrice;
                        prefix = "K_";
                        numItems = 2;
                        break;
                    case "Family":
                        basePrice = familyPrice;
                        prefix = "F_";
                        numItems = 6;
                        break;
                    case "Small" :
                        prefix = "S_";
                        break;
                    case "Medium":
                        prefix = "M_";
                        break;
                    case "Large":
                        prefix = "L_";
                        break;
                    default:
                        break;
                }

                total += basePrice;

                // Add SQL Price
                // For each entree in particular entree map
                for (HashMap.Entry<String, Integer> entree : totalOrder.getAllEntreesMap().get(i).entrySet()) {
                    String entreeName = prefix + entree.getKey();
                    
                    // Get price of entree
                    try {
                        PreparedStatement stmt = conn.prepareStatement("SELECT price FROM menu WHERE product_name = ?");
                        stmt.setString(1, entreeName);
                        ResultSet result = stmt.executeQuery();

                        double premiumPrice = 0.0;
                        while (result.next()) {
                            premiumPrice = result.getDouble("price"); // Runs once
                        }

                        if (!(mealSize == "Small" || mealSize == "Medium" || mealSize == "Large")) {
                            premiumPrice *= entree.getValue();
                            itemAndPrice.add(new Triple<String, Double, Integer>(entreeName, ((basePrice / numItems) + premiumPrice) * entree.getValue(), entree.getValue()));
                        } else {
                            itemAndPrice.add(new Triple<String, Double, Integer>(entreeName, premiumPrice, 1));
                        }

                        total += premiumPrice;
                        
                    } catch (SQLException e) {
                        JOptionPane.showMessageDialog(null, "Error accessing Database.");
                    }
                }

                // For each side in particular side map
                for (HashMap.Entry<String, Integer> side : totalOrder.getAllSidesMap().get(i).entrySet()) {
                    String sidePrefix = prefix;

                    
                    if (mealSize == "Family") {
                        sidePrefix = "L_";
                    }
                    String sideName = sidePrefix + side.getKey();

                    int sideCount = totalOrder.getAllSidesMap().get(i).size();
                    
                    // Get price of side
                    try {
                        PreparedStatement stmt = conn.prepareStatement("SELECT price FROM menu WHERE product_name = ?");
                        stmt.setString(1, sideName);
                        ResultSet result = stmt.executeQuery();

                        // There are no premium sides
                        double premiumPrice = 0.0;
                        while (result.next()) {
                            premiumPrice = result.getDouble("price"); // Runs once
                        }

                        if (!(mealSize == "Medium" || mealSize == "Large" || mealSize == "Family")) {
                            premiumPrice *= side.getValue();
                            itemAndPrice.add(new Triple<String, Double, Integer>(sideName, (basePrice / numItems) / sideCount, side.getValue()));
                        } else if (mealSize == "Family") {
                            itemAndPrice.add(new Triple<String, Double, Integer>(sideName, (basePrice / numItems) * side.getValue(), side.getValue()));
                        } else {
                            itemAndPrice.add(new Triple<String, Double, Integer>(sideName, premiumPrice, 1));
                        }

                        if (mealSize != "Family") {
                            total += premiumPrice;
                        }

                    } catch (SQLException e) {
                        JOptionPane.showMessageDialog(null, "Error accessing Database.");
                    }
                }


                
            }
        }

        // Extras
        for (int i = 0; i < totalOrder.getAllExtrasMap().size(); i++) {
            for (HashMap.Entry<String, Integer> extra : totalOrder.getAllExtrasMap().get(i).entrySet()) {
                String extraName = extra.getKey();

                // Set prefix
                if(!extra.getValue().equals(0)){
                    if(extra.getKey().equals("apple_pie_roll")){
                        if(extra.getValue().equals(1)){
                            extraName = "S_" + extraName;                            
                        }
                        else if(extra.getValue().equals(2)){
                            extraName = "M_" + extraName;
                        }
                        else if(extra.getValue().equals(3)){
                            extraName = "L_" + extraName;
                        }
                    }
                    else{
                        if(extra.getValue().equals(1)){
                            extraName = "S_" + extraName;
                        }
                        else if(extra.getValue().equals(2)){
                            extraName = "L_" + extraName;
                        }
                    }
                }

                try {
                    PreparedStatement stmt = conn.prepareStatement("SELECT price FROM menu WHERE product_name = ?");
                    stmt.setString(1, extraName);
                    ResultSet result = stmt.executeQuery();

                    double price = 0.0;
                    while (result.next()) {
                        price = result.getDouble("price"); // Runs once
                    }

                    total += price;
                    
                    itemAndPrice.add(new Triple<String, Double, Integer>(extraName, price, 1));
                    

                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, "Error accessing Database.");
                }
            }
        }

        // Drinks
        for (int i = 0; i < totalOrder.getAllDrinksMap().size(); i++) {
            for (HashMap.Entry<String, Integer> drink : totalOrder.getAllDrinksMap().get(i).entrySet()) {
                String drinkName = drink.getKey();

                // Set prefix
                if(!drink.getValue().equals(0)){
                    if(drink.getKey().equals("fountain_drink")){
                        if(drink.getValue().equals(1)){
                            drinkName = "S_" + drinkName;                            
                        }
                        else if(drink.getValue().equals(2)){
                            drinkName = "M_" + drinkName;
                        }
                        else if(drink.getValue().equals(3)){
                            drinkName = "L_" + drinkName;
                        }
                    }
                    else{
                        drinkName = "N_" + drinkName;
                    }
                }

                try {
                    PreparedStatement stmt = conn.prepareStatement("SELECT price FROM menu WHERE product_name = ?");
                    stmt.setString(1, drinkName);
                    ResultSet result = stmt.executeQuery();

                    double price = 0.0;
                    while (result.next()) {
                        price = result.getDouble("price"); // Runs once
                    }

                    total += price;
                    
                    itemAndPrice.add(new Triple<String, Double, Integer>(drinkName, price, 1));
                    

                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, "Error accessing Database.");
                }
            }
        }

        return total;
    }

    /**
     * Returns the total price of the order.
     *
     * @return the total price of the order
     */
    public double getTotal() {
        return total;
    }

    /**
     * Returns a vector of items and their prices.
     *
     * @return a vector of items and their prices
     */
    public Vector<Triple<String, Double, Integer>> getItemAndPrice() {
        return itemAndPrice;
    }
    
}