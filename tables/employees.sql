-- Create the Employees table
CREATE TABLE Employees (
    employee_id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    role VARCHAR(100),
    clock_in TIMESTAMP,
    clock_out TIMESTAMP,
    active_status BOOLEAN
);

-- Insert entries into the Employees table
INSERT INTO Employees (employee_id, name, role, clock_in, clock_out, active_status) VALUES
(1234, 'Harsh Dave', 'Manager', '2023-09-01 10:00:00', '2023-09-01 17:00:00', TRUE),
(5678, 'Robert Lightfoot', 'Manager', '2023-09-01 17:00:00', '2023-09-01 22:30:00', FALSE),
(5389, 'Austin Rummels', 'Employee', '2023-09-01 10:00:00', '2023-09-01 17:00:00', TRUE),
(8974, 'Jack Weltens', 'Employee', '2023-09-01 10:00:00', '2023-09-01 17:00:00', TRUE),
(1024, 'Haresh Raj', 'Employee', '2023-09-01 10:00:00', '2023-09-01 17:00:00', TRUE),
(9034, 'Jarren Tobias', 'Employee', '2023-09-01 10:00:00', '2023-09-01 17:00:00', TRUE),
(6334, 'Donald Duck', 'Employee', '2023-09-01 17:00:00', '2023-09-01 22:30:00', FALSE),
(1536, 'Ronald David', 'Employee', '2023-09-01 17:00:00', '2023-09-01 22:30:00', FALSE),
(8439, 'Jack Snow', 'Employee', '2023-09-01 17:00:00', '2023-09-01 22:30:00', FALSE),
(7456, 'John Dana', 'Employee', '2023-09-01 17:00:00', '2023-09-01 22:30:00', FALSE),
(2358, 'Reagan White', 'Employee', '2023-09-01 17:00:00', '2023-09-01 22:30:00', FALSE),
(4745, 'Daniella Brady', 'Employee', '2023-09-01 17:00:00', '2023-09-01 22:30:00', FALSE), 
(0000, 'Z_Report', 'Z_Report', '2024-10-16 10:00:00', '2024-10-16 22:00:00', FALSE); 
