package com.example;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.text.DecimalFormat;

/**
 * X_report class for generating total sales reports since the last Z-Report.
 * @author Harsh Dave
 */
public class X_report extends JPanel {
    /**
     * The database connection
     */
    private Connection conn;
    /**
     * Text area to display the report
     */
    private JTextArea textArea;

    /**
     * Constructor initializes the report panel.
     * @param conn Database connection
     */
    public X_report(Connection conn) {
        this.conn = conn;
        setLayout(new BorderLayout());

        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(textArea);

        add(scrollPane, BorderLayout.CENTER);

        updateReport();
    }

    /**
     * Display the total sales since the last Z-Report run by using the psql. 
     * Shows total sales since the last Z-Report run.
     */
    public void updateReport() {
        try {
            String lastZReportQuery = "SELECT clock_out FROM Employees WHERE name = 'Z_Report'";
            Statement stmtLastZReport = conn.createStatement();
            ResultSet rsLastZReport = stmtLastZReport.executeQuery(lastZReportQuery);

            Timestamp lastZReportTime;
            if (rsLastZReport.next() && rsLastZReport.getTimestamp("clock_out") != null) {
                lastZReportTime = rsLastZReport.getTimestamp("clock_out");
            } else {
                lastZReportTime = Timestamp.valueOf("2024-10-16 10:00:00");
            }

            String query = "WITH daily_sales AS (" +
                "SELECT DATE(time_stamp) AS sale_date, " +
                "DATE_TRUNC('hour', time_stamp) AS hour, " +
                "SUM(total) AS total_sales, COUNT(*) AS order_count " +
                "FROM orders WHERE time_stamp > ? " +
                "GROUP BY DATE(time_stamp), DATE_TRUNC('hour', time_stamp) ORDER BY sale_date, hour" +
                ") SELECT TO_CHAR(sale_date, 'YYYY-MM-DD') AS date, " +
                "TO_CHAR(hour, 'HH12:MI AM') AS time_period, " +
                "total_sales::NUMERIC(10, 2) AS total_sales, order_count FROM daily_sales;";

            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setTimestamp(1, lastZReportTime);
            ResultSet rs = pstmt.executeQuery();

            StringBuilder report = new StringBuilder();
            DecimalFormat df = new DecimalFormat("#,##0.00");
            String currentDate = "";

            report.append(centerAlign("X-Report: Total Sales Since Last Z-Report", 70)).append("\n");
            report.append(centerAlign("===========================================", 70)).append("\n");
            report.append("Last Z-Report Run: ").append(lastZReportTime).append("\n\n");

            report.append(String.format("%-12s %-15s %-20s %-15s\n", "Date", "Time Period", "Total Sales ($)", "Order Count"));
            report.append(String.format("%-12s %-15s %-20s %-15s\n", "------------", "---------------", "--------------------", "---------------"));

            while (rs.next()) {
                String date = rs.getString("date");
                String timePeriod = rs.getString("time_period");
                double salesAmount = rs.getDouble("total_sales");
                int orderCount = rs.getInt("order_count");

                if (!date.equals(currentDate)) {
                    if (!currentDate.isEmpty()) {
                        report.append("\n");
                    }
                    currentDate = date;
                }

                report.append(String.format("%-12s %-15s $%-19s %-15d\n",
                    date, timePeriod, df.format(salesAmount), orderCount));
            }

            textArea.setText(report.toString());

        } catch (Exception e) {
            e.printStackTrace();
            textArea.setText("Error generating X-Report: " + e.getMessage());
        }
    }

    /**
     * Resets the X-Report by updating the last Z-Report run time.
     * method called when a new Z-Report is generated.
     */
    public void resetReport() {
        try {
            String updateQuery = "UPDATE Employees SET clock_out = CURRENT_TIMESTAMP WHERE name = 'Z_Report'";
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(updateQuery);
            updateReport();
        } catch (SQLException e) {
            e.printStackTrace();
            textArea.setText("Error resetting X-Report: " + e.getMessage());
        }
    }

    /**
     * Centers text within a given width.
     * @param text Text to center
     * @param width Total width to use for centering
     * @return Centered text
     */
    private String centerAlign(String text, int width) {
        return String.format("%" + (width + text.length()) / 2 + "s", text);
    }
}