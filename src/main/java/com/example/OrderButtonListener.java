package com.example;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * Listens for when the order button has been pressed to initialize the order window and display
 * the content of the current order to the user
 * @author Jack Weltens
 */
public class OrderButtonListener implements ActionListener{
    OrderDetails totalOrder;


    /**
     * constructor
     * @param totalOrder houses the total current order that is being added to
     */
    public OrderButtonListener(OrderDetails totalOrder){
        this.totalOrder = totalOrder;
    }


    /**
     * calls the display contents function on the total order when the button is pressed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        totalOrder.displayContents();
    }

}
