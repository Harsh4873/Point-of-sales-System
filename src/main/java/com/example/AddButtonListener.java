package com.example;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import javax.swing.JOptionPane;


/**
 * Represents the backbone of the ADD button which is responsible for putting 
 * employee input onto the current order window based on what is selected
 * @author Jack Weltens, Austin Rummel
 */
public class AddButtonListener implements ActionListener{
    private OrderDetails totalOrder;
    private Order orderManager;
    private GUI gui;
    private HashMap<String, Integer> entreeMap;     // stores all entrees/sides
    private HashMap<String, Integer> sideMap;       // stores all drinks/extras
    private HashMap<String, Integer> extraMap;      // stores all entrees/sides
    private HashMap<String, Integer> drinkMap;      // stores all drinks/extras


    /**
     * constructor
     * @param orderManager current iteration of one part of a single order
     * @param gui link to GUI interface
     * @param totalOrder stores all order information
     */
    public AddButtonListener(Order orderManager, GUI gui, OrderDetails totalOrder) {
        this.orderManager = orderManager;
        this.gui = gui;
        this.entreeMap = new HashMap<>();
        this.sideMap = new HashMap<>();
        this.extraMap = new HashMap<>();
        this.drinkMap = new HashMap<>();
        this.totalOrder = totalOrder;
    }


    /**
     * This function is responsible for detecting when a button was pressed and taking the appropriate actions.
     * This will vary fort each type of button.
     * @param e action that occured
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        // populates both hashmaps with the correct number of each item
        entreeMap.clear();
        sideMap.clear();
        extraMap.clear();
        drinkMap.clear();

        entreeMap = entreeMap();
        sideMap = sideMap();
        extraMap = extraMap();
        drinkMap = drinkMap();

        // validate meal ensures that the selected combination is correct
        boolean validatedMeal = validateMeal();

        if (!validatedMeal) {
            gui.resetGUI(orderManager);
            entreeMap.clear();
            sideMap.clear();
            extraMap.clear();
            drinkMap.clear();
            return;
        }
        
        // add to current order list (must create deep copies due to object deconstruction)
        totalOrder.addEntreeMap(deepCopyMap(entreeMap));
        totalOrder.addSideMap(deepCopyMap(sideMap));
        totalOrder.addExtraMap(deepCopyMap(extraMap));
        totalOrder.addDrinkMap(deepCopyMap(drinkMap));
        totalOrder.addMealSize(orderManager.getMealSize());
        
        // clear board
        gui.resetGUI(orderManager);
        entreeMap.clear();
        sideMap.clear();
        extraMap.clear();
        drinkMap.clear();

        // calls the total label function to ensure proper updates
        gui.updateTotalLabel();

        return;
    }

    
    /**
     * This function populates a hashmap that will store all of the entrees for a given ADD iteration.
     * @return a HashMap that stores the entrees
     */
    public HashMap<String, Integer> entreeMap() {

        for (MenuItem item : orderManager.getEntree()) {
            if (entreeMap.containsKey(item.getName())) {
                entreeMap.put(item.getName(), entreeMap.get(item.getName()) + 1);
            } else {
                entreeMap.put(item.getName(), 1);
            }
        }

        // any modulus operators are present to allow the GUI to loop through items to deselect them
        for (HashMap.Entry<String, Integer> entry : entreeMap.entrySet()) {
            Integer temp = entry.getValue();
            temp %= 4;
            entry.setValue(temp);
        }

        return entreeMap;
    }


    /**
     * This function populates a hashmap that will store all of the sides for a given ADD iteration.
     * @return a HashMap that stores the sides
     */
    public HashMap<String, Integer> sideMap() {

        for (MenuItem item : orderManager.getSide()) {
            if (sideMap.containsKey(item.getName())) {
                sideMap.put(item.getName(), sideMap.get(item.getName()) + 1);
            } else {
                sideMap.put(item.getName(), 1);
            }
        }

        // any modulus operators are present to allow the GUI to loop through items to deselect them
        for (HashMap.Entry<String, Integer> entry : sideMap.entrySet()) {
            Integer temp = entry.getValue();
            temp %= 3;
            entry.setValue(temp);
        }

        return sideMap;
    }

    
    /**
     * This function is responsible for populating a hash map that keeps track of all extras in one ADD iteration.
     * @return a HashMap with the extras
     */
    public HashMap<String, Integer> extraMap() {

        for (MenuItem item : orderManager.getExtra()) {
            if (extraMap.containsKey(item.getName())) {
                extraMap.put(item.getName(), extraMap.get(item.getName()) + 1);
            } else {
                extraMap.put(item.getName(), 1);
            }
        }

        // any modulus operators are present to allow the GUI to loop through items to deselect them
        for (HashMap.Entry<String, Integer> entry : extraMap.entrySet()) {
            Integer temp = entry.getValue();
            if(entry.getKey().equals("apple_pie_roll")){
                temp %= 4;
            }
            else{
                temp %= 3;
            }
            entry.setValue(temp);
        }

        return extraMap;
    }

    
    /**
     * This function is responsible for populating a hash map that keeps track of all drinks in one ADD iteration
     * @return a HashMap with the drinks
     */
    public HashMap<String, Integer> drinkMap() {

        for (MenuItem item : orderManager.getDrink()) {
            if (drinkMap.containsKey(item.getName())) {
                drinkMap.put(item.getName(), drinkMap.get(item.getName()) + 1);
            } else {
                drinkMap.put(item.getName(), 1);
            }
        }

        // any modulus operators are present to allow the GUI to loop through items to deselect them
        for (HashMap.Entry<String, Integer> entry : drinkMap.entrySet()) {
            Integer temp = entry.getValue();
            if(entry.getKey().equals("fountain_drink")){
                temp %= 4;
            }
            else{
                temp %= 2;
            }
            entry.setValue(temp);
        }

        return drinkMap;
    }

    
    /**
     * This is an error checking function that will let the user know if a combination that they entered is invalid.
     * @return a boolean that indicates whether the combination is valid
     */
    public boolean validateMeal() {
        String errorMessage = "";

        // checks for the existence of either
        boolean extrasExist = validateExtras(extraMap);
        boolean drinksExist = validateDrinks(drinkMap);

        // if we only see extras or drinks
        if((extrasExist || drinksExist) && orderManager.getSideCount() == 0 && orderManager.getEntreeCount() == 0){
            orderManager.setMealSize("Add Ons");
            return true;
        }

        // validate meal categories clicks
        if (orderManager.getKid() && orderManager.getFamily()) {
            errorMessage = "You cannot select both Kid and Family meals.";
            showErrorDialog(errorMessage);
            return false;
        } else if (orderManager.getKid()) {
            if (orderManager.getEntreeCount() != 1 || orderManager.getSideCount() > 2 || orderManager.getSideCount() == 0) {
                errorMessage = "For a Kid's meal, you must select 1 entree and between 1 and 2 sides.";
                showErrorDialog(errorMessage);
                return false;
            }
            orderManager.setMealSize("Kid");

        } else if (orderManager.getFamily()) {
            if (orderManager.getEntreeCount() != 3 || orderManager.getSideCount() != 2) {
                errorMessage = "For a Family meal, you must select 3 entrees and 2 sides.";
                showErrorDialog(errorMessage);
                return false;
            }
            orderManager.setMealSize("Family");
        } else {

            // A LA Carte Invalids
            if (orderManager.getSideCount() == 0 || orderManager.getEntreeCount() == 0) {

                // these two loops need to exist to filter out items that have a value of zero
                Integer entreeMapSize = 0;
                Integer sideMapSize = 0;
                for(HashMap.Entry<String, Integer> entry : entreeMap.entrySet()){
                    if(entry.getValue() > 0){
                        entreeMapSize++;
                    }
                }
                for(HashMap.Entry<String, Integer> entry : sideMap.entrySet()){
                    if(entry.getValue() > 0){
                        sideMapSize++;
                    }
                }

                if (entreeMapSize != 1 && sideMapSize != 1) {
                    errorMessage = "For A La Carte, select exactly 1 item.";
                    showErrorDialog(errorMessage);
                    return false;
                }

                // Get size of A La Carte Item
                if(entreeMapSize == 1){
                    // this allows for skipping zero items
                    Integer aLaCarteSize = 0;
                    for(HashMap.Entry<String, Integer> entry : entreeMap.entrySet()){
                        if(entry.getValue() > 0){
                            aLaCarteSize = entry.getValue();
                        }
                    }
                    
                    switch (aLaCarteSize) {
                        case 1:
                            orderManager.setMealSize("Small");
                            break;
                        case 2:
                            orderManager.setMealSize("Medium");
                            break;
                        case 3:
                            orderManager.setMealSize("Large");
                            break;                        
                        default:
                            break;
                    }
                    return true;
                }
                else{
                    // this allows for zero items to be skipped
                    Integer aLaCarteSize = 0;
                    for(HashMap.Entry<String, Integer> entry : sideMap.entrySet()){
                        if(entry.getValue() > 0){
                            aLaCarteSize = entry.getValue();
                        }
                    }

                    switch (aLaCarteSize) {
                        case 1:
                            orderManager.setMealSize("Medium");
                            break;
                        case 2:
                            orderManager.setMealSize("Large");
                            break;                        
                        default:
                            break;
                    }
                    return true;
                }
                
            }

            // Normal category Invalid
            if (orderManager.getEntreeCount() > 3 || orderManager.getSideCount() > 2) {
                errorMessage = "You cannot select more than 3 entrees or more than 2 sides.";
                showErrorDialog(errorMessage);
                return false;
            }
            switch (orderManager.getEntreeCount()) {
                case 1:
                    orderManager.setMealSize("Bowl");
                    break;
                case 2:
                    orderManager.setMealSize("Plate");
                    break;
                case 3:
                    orderManager.setMealSize("Big_Plate");
                    break;                        
                default:
                    break;
            }
            return true;
        }
       return true; 
    }

    
    /**
     * This will be a helper function for validateMeal() that is capable of determining if extras exist and passing that back.
     * @param extraMap  holds all extras in the order
     * @return  whether the combination is valid
     */
    public boolean validateExtras(HashMap<String, Integer> extraMap){
        for (HashMap.Entry<String, Integer> entry : extraMap.entrySet()) {
            Integer temp = entry.getValue();
            if(temp > 0){
                return true;
            }
        }
        return false;
    }

    
    /**
     * This will be a helper function for validateMeal() that is capable of determining if drinks exist and passing that back.
     * @param drinkMap  holds all drinks in a HashMap
     * @return  whether the combination is valid
     */
    public boolean validateDrinks(HashMap<String, Integer> drinkMap){
        for (HashMap.Entry<String, Integer> entry : drinkMap.entrySet()) {
            Integer temp = entry.getValue();
            if(temp > 0){
                return true;
            }
        }
        return false;
    }

    
    /**
     * This pops up an error message if the input is invalid.
     * @param message message to pass to the dialogue box
     */
    private void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(null, message, "Input Validation Error", JOptionPane.ERROR_MESSAGE);
    }

    
    /**
     * We must create deep copies to prevent the issues that come with populating the OrderDetails vectors
     * @param original a HashMap to be deep copied
     * @return the deep copy
     */
    public static HashMap<String, Integer> deepCopyMap(HashMap<String, Integer> original) {
        return new HashMap<>(original);
    }
    
}