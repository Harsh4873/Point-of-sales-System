package com.example;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;


/**
 * Represents the backbone of the kid button that allows it to be pressed and 
 * visually show how many times it has been pressed
 * @author Jack Weltens
 */
public class KidButtonListener implements ActionListener{
    private Order orderManager;
    /**
     * keeps track of the number of button presses
     */
    public Integer buttonPressCount;


    /**
     * constructor
     * @param orderManager keeps track of the parent order
     */
    public KidButtonListener(Order orderManager) {
        this.orderManager = orderManager;
        this.buttonPressCount = 0;
    }


    /**
     * destructor to reset the class when a new order is started
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
     * This function is responsible for updating the color of the button border based on presses
     * @param button
     * @param pressCount
     */
    private void updateMenuBorderButton(JButton button, int pressCount) {

        // Create different borders with varying thicknesses and colors
        Border blueBorder = new LineBorder(Color.BLUE, 5);
        Border resetBorder = new LineBorder(Color.GRAY, 1);

        switch (pressCount) {
            case 1:
                // first click (true)
                button.setBorder(blueBorder);
                orderManager.updateKid(true);
                break;
            case 2:
                // second click (false)
                button.setBorder(resetBorder);
                orderManager.updateKid(false);
                buttonPressCount = 0;
                break;
            default:
                break;
        }
    }
}