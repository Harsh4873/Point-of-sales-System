package com.example;
import java.sql.Timestamp;

/** 
 * Represents an employee with time tracking capabilities.
 * @author Harsh Dave
 */
public class Employee {
    private int employeeId;
    private String name;
    private String role;
    private String clockIn;
    private String clockOut;
    private boolean activeStatus;

    /**
     * Constructs an Employee with specified details and timestamps.
     * @param employeeId The unique identifier for the employee
     * @param name The name of the employee
     * @param role The role of the employee within the organization
     * @param clockIn The timestamp marking the start of the work period
     * @param clockOut The timestamp marking the end of the work period
     * @param activeStatus The current active status of the employee
     */
    public Employee(int employeeId, String name, String role, Timestamp clockIn, Timestamp clockOut, boolean activeStatus) {
        this.employeeId = employeeId;
        this.name = name;
        this.role = role;
        this.clockIn = clockIn != null ? clockIn.toString() : null;
        this.clockOut = clockOut != null ? clockOut.toString() : null;
        this.activeStatus = activeStatus;
    }

    /**
     * Gets employee ID
     * @return employeeId
     */
    public int getEmployeeId() {
        return employeeId;
    }

    /**
     * Gets employee name
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets employee role
     * @return role
     */
    public String getRole() {
        return role;
    }

    /**
     * Gets employee clock in time
     * @return clockIn
     */
    public String getClockIn() {
        return clockIn;
    }

    /**
     * Gets employee clock out time
     * @return clockOut
     */
    public String getClockOut() {
        return clockOut;
    }

    /**
     * Checks employee active status
     * @return activeStatus
     */
    public boolean isActiveStatus() {
        return activeStatus;
    }

    /**
     * Sets employee active status
     * @param activeStatus The new active status
     */
    public void setActiveStatus(boolean activeStatus) {
        this.activeStatus = activeStatus;
    }

    /**
     * Sets employee name
     * @param name The new employee name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets employee role
     * @param role The new employee role
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * Updates the clock-in and clock-out times for the employee.
     * @param clockIn The new clock-in timestamp
     * @param clockOut The new clock-out timestamp
     */
    public void updateClockTimes(Timestamp clockIn, Timestamp clockOut) {
        this.clockIn = clockIn != null ? clockIn.toString() : null;
        this.clockOut = clockOut != null ? clockOut.toString() : null;
    }
}