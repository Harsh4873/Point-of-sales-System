package com.example;
import java.util.ArrayList;
import java.util.List;

/**
 * Class to store an extensive list of order details for formatting and math
 * @author Jack Weltens
 */
public class Order {
    private List<MenuItem> entrees = new ArrayList<>();     // stores all entrees that have been pressed
    private List<MenuItem> sides = new ArrayList<>();       // stores all sides that have been pressed
    private List<MenuItem> extras = new ArrayList<>();      // stores all extras that have been pressed
    private List<MenuItem> drinks = new ArrayList<>();      // stores all drinks that have been pressed
    private Integer entreeCount;                            // keeps a running total of the actively selected entrees
    private Integer sideCount;                              // keeps a running total of the actively selected sides
    private Integer extraCount;                             // keeps a running total of the actively selected extras
    private Integer drinkCount;                             // keeps a running total of the actively selected drinks
    private boolean kid;                                    // boolean to detect if the kid attribute is selected
    private boolean family;                                 // boolean to detect if the family attribute is selected
    private String mealSize;                                // Kid, Family, Bowl, Plate, Big_Plate, Small, Medium, Large


    /**
     * constructor
     */
    public Order(){
        entreeCount = 0;
        sideCount = 0;
        extraCount = 0;
        drinkCount = 0;
        kid = false;
        family = false;
        mealSize = "";
    }


    /**
     * Adds an entree to the order
     * @param item the MenuItem to be added
     */
    public void addEntree(MenuItem item) {
        entrees.add(item);
    }

    /**
     * Adds a side to the order
     * @param item the MenuItem to be added
     */
    public void addSide(MenuItem item) {
        sides.add(item);
    }

    /**
     * Adds an extra to the order
     * @param item the MenuItem to be added
     */
    public void addExtra(MenuItem item) {
        extras.add(item);
    }

    /**
     * Adds a drink to the order
     * @param item the MenuItem to be added
     */
    public void addDrink(MenuItem item) {
        drinks.add(item);
    }


    /**
     * Gets the list of entrees
     * @return entrees
     */
    public List<MenuItem> getEntree() {
        return entrees;
    }

    /**
     * Gets the list of sides
     * @return sides
     */
    public List<MenuItem> getSide() {
        return sides;
    }

    /**
     * Gets the list of extras
     * @return extras
     */
    public List<MenuItem> getExtra() {
        return extras;
    }

    /**
     * Gets the list of drinks
     * @return drinks
     */
    public List<MenuItem> getDrink() {
        return drinks;
    }

    /**
     * Gets the total number of entrees
     * @return entreeCount
     */
    public Integer getEntreeCount(){
        return entreeCount;
    }

    /**
     * Gets the total number of sides
     * @return sideCount
     */
    public Integer getSideCount(){
        return sideCount;
    }

    /**
     * Gets the total number of extras
     * @return extraCount
     */
    public Integer getExtraCount(){
        return extraCount;
    }

    /**
     * Gets the total number of drinks
     * @return drinkCount
     */
    public Integer getDrinkCount(){
        return drinkCount;
    }

    /**
     * Gets the kid boolean
     * @return kid
     */
    public boolean getKid(){
        return kid;
    }

    /**
     * Gets the family boolean
     * @return family
     */
    public boolean getFamily(){
        return family;
    }

    /**
     * Gets the meal size
     * @return mealSize
     */
    public String getMealSize() {
        return mealSize;
    }


    /**
     * Updates the entree count
     * @param newValue the value to be added to the entree count
     */
    public void updateEntreeCount(Integer newValue){
        entreeCount += newValue;
    }

    /**
     * Updates the side count
     * @param newValue the value to be added to the side count
     */
    public void updateSideCount(Integer newValue){
        sideCount += newValue;
    }

    /**
     * Updates the extra count
     * @param newValue the value to be added to the extra count
     */
    public void updateExtraCount(Integer newValue){
        extraCount += newValue;
    }

    /**
     * Updates the drink count
     * @param newValue the value to be added to the drink count
     */
    public void updateDrinkCount(Integer newValue){
        drinkCount += newValue;
    }

    /**
     * Updates the kid boolean
     * @param newValue the value of the kid boolean
     */
    public void updateKid(boolean newValue){
        kid = newValue;
    }

    /**
     * Updates the family boolean
     * @param newValue the value of the family boolean
     */
    public void updateFamily(boolean newValue){
        family = newValue;
    }

    /**
     * Updates the meal size
     * @param newValue the value of the meal size
     */
    public void setMealSize(String newValue) {
        mealSize = newValue;
    }
}
