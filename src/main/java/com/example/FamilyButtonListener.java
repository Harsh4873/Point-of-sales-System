package com.example;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;


/**
 * Represents the backbone of the family button that allows it to be pressed and 
 * visually show how many times it has been pressed
 * @author Jack Weltens
 */
public class FamilyButtonListener implements ActionListener{
    private Order orderManager;
    /**
     * keeps track of the number of button presses
     */
    public Integer buttonPressCount;


    /**
     * constructor
     * @param orderManager keeps track of the parent order
     */
    public FamilyButtonListener(Order orderManager) {
        this.orderManager = orderManager;
        this.buttonPressCount = 0;
    }


    /**
     * This is a destructor to reset the family button upon new order creation
     */
    public void clearCounts(){
        buttonPressCount = 0;
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
        buttonPressCount++;

        // update button border color based on press count
        updateMenuBorderButton(button, buttonPressCount);
    }


    /**
     * Responsible for changing the border color of the family button based on button presses
     * @param button that was pressed
     * @param pressCount how many times it was pressed
     */
    private void updateMenuBorderButton(JButton button, int pressCount) {

        // Create different borders with varying thicknesses and colors
        Border blueBorder = new LineBorder(Color.BLUE, 5);
        Border resetBorder = new LineBorder(Color.GRAY, 1);

        switch (pressCount) {
            case 1:
                // first click (true)
                button.setBorder(blueBorder);
                orderManager.updateFamily(true);
                break;
            case 2:
                // second click (false)
                button.setBorder(resetBorder);
                orderManager.updateFamily(false);
                buttonPressCount = 0;
                break;
            default:
                break;
        }
        
    }
}
