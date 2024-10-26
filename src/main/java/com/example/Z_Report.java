package com.example;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.text.DecimalFormat;
import java.time.LocalDateTime;

/**
 * Z_Report class for generating sales reports since the last Z-Report was run.
 * @author Harsh Dave
 */
public class Z_Report extends JPanel {
    /**
     * The database connection
     */
    private Connection conn;
    /**
     * Text area to display the report
     */
    private JTextArea reportArea;
    /**
     * Button to generate the report
     */
    private JButton generateButton;

    /**
     * Constructor initializes the Z_Report panel with a database connection.
     * Set up the report area and generate button with coloring and font. 
     * @param conn Database connection
     * @param startDayButton Button to start the day
     */
    public Z_Report(Connection conn, JButton startDayButton) {
        this.conn = conn;
        setLayout(new BorderLayout());

        reportArea = new JTextArea();
        reportArea.setEditable(false);
        reportArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(reportArea);
        add(scrollPane, BorderLayout.CENTER);

        generateButton = new JButton("Generate Z-Report");
        generateButton.setFont(new Font("Candara", Font.BOLD, 16));
        generateButton.setForeground(Color.WHITE);
        generateButton.setBackground(Color.BLACK);
        generateButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Confirm Z-Report? This will update the last run time.", "Confirm Z-Report", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                generateReport();
                startDayButton.setEnabled(true);
            }
        });
        add(generateButton, BorderLayout.SOUTH);
    }

    /**
     * Displays the Z-Report by getting data from the psql server. 
     * Updates the last run time after generating the report.
     */
    private void generateReport() {
        try {
            // Get the last run time
            String lastRunQuery = "SELECT clock_out FROM Employees WHERE name = 'Z_Report'";
            Statement stmtLastRun = conn.createStatement();
            ResultSet rsLastRun = stmtLastRun.executeQuery(lastRunQuery);
            
            Timestamp lastRunTime;
            if (rsLastRun.next()) {
                lastRunTime = rsLastRun.getTimestamp("clock_out");
                // if previous run time is null, then set it to the previous day at 10:00 AM
                if (lastRunTime == null) {
                    lastRunTime = Timestamp.valueOf(LocalDateTime.now().minusDays(1).withHour(10).withMinute(0).withSecond(0));
                }
            } 
            else {
                lastRunTime = Timestamp.valueOf(LocalDateTime.now().minusDays(1).withHour(10).withMinute(0).withSecond(0));
            }

            String query = "WITH sales_data AS (" +
                "SELECT DATE_TRUNC('hour', time_stamp) AS hour, " +
                "SUM(total) AS total_sales, COUNT(*) AS order_count " +
                "FROM orders WHERE time_stamp > ? " +
                "GROUP BY DATE_TRUNC('hour', time_stamp) ORDER BY hour" +
                ") SELECT TO_CHAR(hour, 'YYYY-MM-DD HH12:MI AM') AS time_period, " +
                "total_sales::NUMERIC(10, 2) AS total_sales, order_count FROM sales_data;";

            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setTimestamp(1, lastRunTime);
            ResultSet rs = pstmt.executeQuery();

            StringBuilder report = new StringBuilder();
            DecimalFormat df = new DecimalFormat("#,##0.00");
            double totalSales = 0;
            int totalOrders = 0;

            report.append(centerAlign("Z-Report: Sales Since Last Run", 60)).append("\n");
            report.append(centerAlign("===============================", 60)).append("\n");
            report.append("Last Run: ").append(lastRunTime).append("\n\n");

            boolean hasData = false;
            while (rs.next()) {
                hasData = true;
                String timePeriod = rs.getString("time_period");
                double salesAmount = rs.getDouble("total_sales");
                int orderCount = rs.getInt("order_count");

                report.append(String.format("%-25s $%-19s %-15d\n",
                    timePeriod, df.format(salesAmount), orderCount));

                totalSales += salesAmount;
                totalOrders += orderCount;
            }

            if (!hasData) {
                report.append("No sales data found for the specified period.\n");
            } 
            else {
                report.append(centerAlign("----------------------------------------------------------------", 60)).append("\n\n");
                report.append(formatLine("Total Sales:", "$" + df.format(totalSales), 60)).append("\n");
                report.append(formatLine("Total Orders:", String.valueOf(totalOrders), 60)).append("\n");
                if (totalOrders > 0) {
                    double averageOrderValue = totalSales / totalOrders;
                    report.append(formatLine("Average Order Value:", "$" + df.format(averageOrderValue), 60)).append("\n");
                }
            }

            reportArea.setText(report.toString());

            LocalDateTime now = LocalDateTime.now();
            String updateQuery = "UPDATE Employees SET clock_out = ? WHERE name = 'Z_Report'";
            PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
            updateStmt.setTimestamp(1, Timestamp.valueOf(now));
            updateStmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Z-Report generated and last run time updated.", "Z-Report Complete", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException e) {
            e.printStackTrace();
            reportArea.setText("Error generating Z-Report: " + e.getMessage());
        }
    }

    /**
     * Centers text within a given width. 
     * @param text Text to center
     * @param width Width for centering
     * @return Centered text
     */
    private String centerAlign(String text, int width) {
        return String.format("%" + (width + text.length()) / 2 + "s", text);
    }

    /**
     * Formats a line with a label and value, right-aligned within a given width.
     * @param label Label of the line
     * @param value Value of the line
     * @param width Total width of the line
     * @return Formatted line
     */
    private String formatLine(String label, String value, int width) {
        int spaces = width - label.length() - value.length();
        return String.format("%s%" + spaces + "s%s", label, "", value);
    }
}