package com.example;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 * Represents a button for each item in the current order so that these items can be viewed and removed
 * @author Austin Rummel, Jack Weltens
 */
public class CurrentOrderButtonListener implements ActionListener {
    private JFrame frame;
    private JPanel buttonPanel;
    private OrderDetails orderDetails;
    private JButton buttonToRemove;
    private int i;
    private String itemType;


    /**
     * constructor
     * @param frame frame to host the panel
     * @param buttonPanel panel to display information
     * @param orderDetails reference to the OrderDetails instance
     * @param buttonToRemove reference to the button to be removed
     * @param i index to be used to access the vectors from the order
     * @param itemType type of item to be removed
     */
    public CurrentOrderButtonListener(JFrame frame, JPanel buttonPanel, OrderDetails orderDetails, JButton buttonToRemove, int i,
    String itemType) {
        this.frame = frame;
        this.buttonPanel = buttonPanel;
        this.orderDetails = orderDetails;
        this.buttonToRemove = buttonToRemove;
        this.i = i;
        this.itemType = itemType;
    }


    /**
     * This function is responsible for detecting when a button was pressed and taking the appropriate actions.
     * This will vary fort each type of button.
     * @param e action that occured
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // ask user for confirmation to remove the item
        int confirmation = JOptionPane.showConfirmDialog(frame, 
            "Are you sure you want to remove this item?", 
            "Confirm Deletion", JOptionPane.YES_NO_OPTION);

        if (confirmation == JOptionPane.YES_OPTION) {

            // Remove the button from the panel
            buttonToRemove.setVisible(false);
            if (itemType.equals("entreeSide")) {
                orderDetails.removeFromEntreeMap(i);
                orderDetails.removeFromSideMap(i);
                orderDetails.removeFromMealSizesMap(i);
            } 
            else if(itemType.equals("extra")) {            
                orderDetails.removeFromExtrasMap(i);
                
            }
            else{
                orderDetails.removeFromDrinkMap(i);
            }
            
            // revalidate and repaint the panel to reflect the changes
            buttonPanel.revalidate();
            buttonPanel.repaint();

            // Assuming the order panel is a JFrame or JDialog
            JFrame orderFrame = (JFrame) SwingUtilities.getWindowAncestor(buttonPanel);  // Get the parent window (order panel)
            orderFrame.dispose();  // Close the current order panel

            // Reopen the order panel (assuming you have a method like openOrderPanel() that handles this)
            orderDetails.displayContents();
        }
    }
}
