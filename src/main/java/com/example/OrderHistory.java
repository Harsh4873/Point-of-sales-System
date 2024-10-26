package com.example;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Vector;


/**
 * OrderHistory class for displaying sales history graph.
 * Replaces Python script functionality by querying the database and generating
 * the chart in Java.
 * @author Jarren Tobias
 */
public class OrderHistory extends JPanel {

    /**
     * The database connection
     */
    private Connection conn;
    /**
     * To hold the inventory type selected
     */
    private String inventoryType;
    /**
     * JLabel to display the graph
     */
    private JLabel imageLabel;
    /**
     * Vector to hold the start and stop timestamps
     */
    private Vector<String> startAndStop;

    /**
     * Constructor to initialize OrderHistory with the given database connection
     * and inventory type.
     *
     * @param conn          Database connection.
     * @param inventoryType The inventory type (e.g., "drinks", "sauces").
     * @param startAndStop  Vector containing the start and stop timestamps.
     */
    public OrderHistory(Connection conn, String inventoryType, Vector<String> startAndStop) {
        this.conn = conn;
        this.inventoryType = inventoryType;
        this.startAndStop = startAndStop;
        setLayout(new BorderLayout());
        imageLabel = new JLabel();
        add(new JScrollPane(imageLabel), BorderLayout.CENTER);
        updateGraph();
    }

    /**
     * Updates the graph by fetching data and generating a chart.
     */
    public void updateGraph() {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                generateGraph();
                return null;
            }

            @Override
            protected void done() {
                displayGraph();
            }
        };
        worker.execute();
    }

    /**
     * Fetches sales data from the database and generates the chart.
     */
    private void generateGraph() {
        try {
            
            // create a dataset to hold the sales data for the chart
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();

            // query the database to get sales data for the selected inventory type
            Statement stmt = conn.createStatement();
            String query = "SELECT i.inventory_name, i.inventory_type, SUM(od.quantity * ing.inventory_quantity) AS total_used " +
                       "FROM order_details od " +
                       "JOIN orders o ON od.order_id = o.order_id " + // join with orders to access timestamp
                       "JOIN ingredients ing ON od.product_name = ing.product_name " +
                       "JOIN inventory i ON ing.inventory_name = i.inventory_name " +
                       "WHERE i.inventory_type = '" + inventoryType + "' " + // filter by inventory type
                       "AND o.time_stamp BETWEEN '" + startAndStop.elementAt(0) + "' AND '" + startAndStop.elementAt(1) + "' " + // timestamp filter
                       "GROUP BY i.inventory_name, i.inventory_type " +
                       "ORDER BY total_used DESC;";
            
            ResultSet resultSet = stmt.executeQuery(query);

            // Loop through the result set and populate the dataset
            while (resultSet.next()) {
                String productName = resultSet.getString("inventory_name");
                int totalSold = resultSet.getInt("total_used");

                // Add data to the dataset
                dataset.addValue(totalSold, inventoryType, productName);
            }

            resultSet.close();
            stmt.close();

            // Generate the chart using JFreeChart
            JFreeChart barChart = ChartFactory.createBarChart(
                    "Order History for " + inventoryType, // Chart title
                    "Product Name", // X-axis label
                    "Quantity Sold", // Y-axis label
                    dataset // Data for the chart
            );

            // Create a ChartPanel to hold the chart
            ChartPanel chartPanel = new ChartPanel(barChart);
            chartPanel.setPreferredSize(new Dimension(800, 600));

            // Remove any existing content and add the new chart panel
            removeAll();
            add(chartPanel, BorderLayout.CENTER);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error generating graph: " + e.getMessage(), "Graph Generation Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Displays the generated chart on the JPanel.
     */
    private void displayGraph() {
        revalidate();
        repaint();
    }

}
