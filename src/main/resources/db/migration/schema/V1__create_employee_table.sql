-- Create employee table
CREATE TABLE employee (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  first_name VARCHAR(45) NOT NULL,
  last_name VARCHAR(45) NOT NULL,
  email VARCHAR(255) NOT NULL,
  CONSTRAINT uq_employee_email UNIQUE (email)
);
