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
 * Represents the backbone of the extras buttons that allows them to be pressed and 
 * visually show how many times they have been pressed
 * @author Jack Weltens
 */
public class ExtrasButtonListener implements ActionListener{
    private Order orderManager;
    private Map<JButton, Integer> buttonPressCount;        // track button press counts


    /**
     * constructor
     * @param orderManager keeps track of the parent order
     */
    public ExtrasButtonListener(Order orderManager) {
        this.orderManager = orderManager;
        this.buttonPressCount = new HashMap<>();
    }


    /**
     * This is a destructor that is responsible for clearing the extras upon a new order
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
        orderManager.addExtra(item);
    }


    /**
     * This function is responsible for updating the border colors of the extras buttons
     * @param button that was pressed
     * @param pressCount how many times it was pressed
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
        Border smallToLargerBorder = new CompoundBorder(orangeBorder, blueBorder);

        if(button.getText().equals("apple_pie_roll")){
            switch (pressCount) {
                case 1:
                    // first click (small)
                    button.setBorder(blueBorder);
                    orderManager.updateExtraCount(1);
                    break;
                case 2:
                    // second click (medium)
                    button.setBorder(twoClickBorder);
                    orderManager.updateExtraCount(1);
                    break;
                case 3:
                    // third click (large)
                    button.setBorder(threeClickBorder);
                    orderManager.updateExtraCount(1);
                    break;
                case 4:
                    // fourth click (reset)
                    button.setBorder(resetBorder);
                    orderManager.updateExtraCount(-3);
                    buttonPressCount.put(button, 0);
                    break;
                default:
                    break;
            }
        }
        else{
            switch (pressCount) {
                case 1:
                    // first click (small)
                    button.setBorder(blueBorder);
                    orderManager.updateExtraCount(1);
                    break;
                case 2:
                    // second click (large)
                    button.setBorder(smallToLargerBorder);
                    orderManager.updateExtraCount(1);
                    break;
                case 3:
                    // third click (reset)
                    button.setBorder(resetBorder);
                    orderManager.updateExtraCount(-2);
                    buttonPressCount.put(button, 0);
                    break;
                default:
                    break;
            }
        }
        
    }
}
