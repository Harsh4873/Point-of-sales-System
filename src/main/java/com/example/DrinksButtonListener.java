package com.example;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import java.util.HashMap;
import java.util.Map;


/**
 * Represents the backbone of the drinks buttons that allows them to be pressed and 
 * visually show how many times they have been pressed
 * @author Jack Weltens
 */
public class DrinksButtonListener implements ActionListener{
    private Order orderManager;
    private Map<JButton, Integer> buttonPressCount;     // track button press counts


    /**
     * constructor
     * @param orderManager keeps track of the parent order
     */
    public DrinksButtonListener(Order orderManager) {
        this.orderManager = orderManager;
        this.buttonPressCount = new HashMap<>();
    }


    /**
     * A destructor for starting a new order.
     */
    public void clearCounts(){
        buttonPressCount.clear();
    }


    /**
     * This function is responsible for detecting when a button was pressed and taking the appropriate actions.
     * This will vary fort each type of button.
     * @param e action that occured
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        JButton button = (JButton) e.getSource();

        // increment the press count for this button
        buttonPressCount.put(button, buttonPressCount.getOrDefault(button, 0) + 1);
        int pressCount = buttonPressCount.get(button);

        // update button border color based on press count
        updateMenuBorderButton(button, pressCount);

        // get MenuItem details
        MenuItem item = new MenuItem(button.getText());
        orderManager.addDrink(item);

    }


    /**
     * This function is responsible for updating the drink buttons based on how many time they have been pressed
     * @param button button that was pressed
     * @param pressCount number of times that it has been pressed
     */
    private void updateMenuBorderButton(JButton button, int pressCount) {

        // Create different borders with varying thicknesses and colors
        Border orangeBorder = new LineBorder(Color.ORANGE, 5);
        Border greenBorder = new LineBorder(Color.GREEN, 5);
        Border blueBorder = new LineBorder(Color.BLUE, 5);
        Border resetBorder = new LineBorder(Color.GRAY, 1);

        // Create compound boarders for 2 & 3 clicks
        Border twoClickBorder = new CompoundBorder(greenBorder, blueBorder);
        Border threeClickBorder = new CompoundBorder(orangeBorder, new CompoundBorder(greenBorder, blueBorder));

        if(button.getText().equals("fountain_drink")){
            switch (pressCount) {
                case 1:
                    // first click (small)
                    button.setBorder(blueBorder);
                    orderManager.updateDrinkCount(1);
                    break;
                case 2:
                    // second click (medium)
                    button.setBorder(twoClickBorder);
                    orderManager.updateDrinkCount(1);
                    break;
                case 3:
                    // third click (large)
                    button.setBorder(threeClickBorder);
                    orderManager.updateDrinkCount(1);
                    break;
                case 4:
                    // fourth click (reset)
                    button.setBorder(resetBorder);
                    orderManager.updateDrinkCount(-3);
                    buttonPressCount.put(button, 0);
                    break;
                default:
                    break;
            }
        }
        else{
            switch (pressCount) {
                case 1:
                    // first click (one)
                    button.setBorder(blueBorder);
                    orderManager.updateDrinkCount(1);
                    break;
                case 2:
                    // second click (reset)
                    button.setBorder(resetBorder);
                    orderManager.updateDrinkCount(-1);
                    buttonPressCount.put(button, 0);
                    break;
                default:
                    break;
            }
        }
        
    }
}

