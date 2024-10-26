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
 * Represents the backbone of the sides buttons that allows them to be pressed and 
 * visually show how many times they have been pressed
 * @author Jack Weltens
 */
public class SideButtonListener implements ActionListener{
    private Order orderManager;                             // tracks current order
    private Map<JButton, Integer> buttonPressCount;         // track button press counts


    /**
     * constructor
     * @param orderManager keeps track of the current order
     */
    public SideButtonListener(Order orderManager) {
        this.orderManager = orderManager;
        this.buttonPressCount = new HashMap<>();
    }


    /**
     * Destructor for order reset
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
        orderManager.addSide(item);
    }


    /**
     * Responsible for changing the border colors based on what was just pressed
     * @param button the pressed button
     * @param pressCount the number of presses
     */
    private void updateMenuBorderButton(JButton button, int pressCount) {

        // Create different borders with varying thicknesses and colors
        Border orangeBorder = new LineBorder(Color.ORANGE, 5);
        Border greenBorder = new LineBorder(Color.GREEN, 5);
        Border resetBorder = new LineBorder(Color.GRAY, 1);

        // Create compound boarders for 2 & 3 clicks
        Border twoClickBorder = new CompoundBorder(orangeBorder, greenBorder);

        switch (pressCount) {
            case 1:
                // first click (medium)
                button.setBorder(greenBorder);
                orderManager.updateSideCount(1);
                break;
            case 2:
                // second click (large)
                button.setBorder(twoClickBorder);
                orderManager.updateSideCount(1);
                break;
            case 3:
                // third click (reset)
                button.setBorder(resetBorder);
                orderManager.updateSideCount(-2);
                buttonPressCount.put(button, 0);
                break;
            default:
                break;
        }
    }
}
