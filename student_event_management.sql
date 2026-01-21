-- Create database
CREATE DATABASE student_event_db;
USE student_event_db;

-- Create tables
CREATE TABLE STUDENT (
    student_id INT PRIMARY KEY,
    name VARCHAR(50),
    dept VARCHAR(30),
    year INT
);

CREATE TABLE EVENT (
    event_id INT PRIMARY KEY,
    event_name VARCHAR(50),
    event_date DATE,
    venue VARCHAR(50)
);



CREATE TABLE REGISTRATION (
    reg_id INT PRIMARY KEY,
    student_id INT,
    event_id INT,
    reg_date DATE,
    FOREIGN KEY (student_id) REFERENCES STUDENT(student_id),
    FOREIGN KEY (event_id) REFERENCES EVENT(event_id)
);

-- Insert data
INSERT INTO STUDENT VALUES
(101, 'Kavi', 'CSE', 3),
(102, 'Anu', 'ECE', 2),
(103, 'Ravi', 'IT', 4);

INSERT INTO EVENT VALUES
(1, 'Hackathon', '2026-02-07', 'Lab'),
(2, 'Paper Presentation', '2026-02-10', 'Seminar Hall');

INSERT INTO REGISTRATION VALUES
(1, 101, 1, CURDATE()),
(2, 102, 2, CURDATE());

-- Update & Delete
UPDATE STUDENT SET year = 4 WHERE student_id = 101;
DELETE FROM REGISTRATION WHERE reg_id = 2;

-- Select queries
SELECT * FROM STUDENT;

-- Transaction control
SET autocommit = 0;
UPDATE STUDENT SET dept = 'EEE' WHERE student_id = 101;
ROLLBACK;

-- Aggregate functions
SELECT COUNT(*) AS total_students FROM STUDENT;
SELECT dept, COUNT(*) FROM STUDENT GROUP BY dept;
SELECT MAX(event_date) FROM EVENT;
SELECT MIN(event_date) FROM EVENT;

-- Set operations
SELECT student_id FROM STUDENT
UNION
SELECT student_id FROM REGISTRATION;

SELECT student_id FROM STUDENT WHERE dept = 'CSE'
INTERSECT
SELECT student_id FROM REGISTRATION;

SELECT student_id FROM STUDENT
EXCEPT
SELECT student_id FROM REGISTRATION;

-- View
CREATE VIEW Student_Registration_View AS
SELECT * FROM REGISTRATION;

-- Stored procedure
DELIMITER //

CREATE PROCEDURE AddStudent(
    IN sid INT,
    IN sname VARCHAR(50),
    IN sdept VARCHAR(30),
    IN syear INT
)
BEGIN
    INSERT INTO STUDENT VALUES (sid, sname, sdept, syear);
END;
//

DELIMITER ;

CALL AddStudent(104, 'Meena', 'CSE', 1);

-- Function
DELIMITER //

CREATE FUNCTION TotalRegistrations(eid INT)
RETURNS INT
DETERMINISTIC
BEGIN
    DECLARE total INT;
    SELECT COUNT(*) INTO total
    FROM REGISTRATION
    WHERE event_id = eid;
    RETURN total;
END;
//

DELIMITER ;

SELECT TotalRegistrations(1);

-- Loop using procedure
DELIMITER //

CREATE PROCEDURE PrintLoop()
BEGIN
    DECLARE i INT DEFAULT 1;
    WHILE i <= 5 DO
        SELECT i;
        SET i = i + 1;
    END WHILE;
END;
//

DELIMITER ;

CALL PrintLoop();

-- Trigger
DELIMITER //

CREATE TRIGGER default_registration
BEFORE INSERT ON EVENT
FOR EACH ROW
BEGIN
    IF NEW.venue IS NULL THEN
        SET NEW.venue = 'MG Auditorium';
    END IF;
END;
//

DELIMITER ;

-- Test trigger
INSERT INTO EVENT (event_id, event_name, event_date)
VALUES (103, 'Lavaza', '2026-02-27');

-- Exception handling procedure
DELIMITER //

CREATE PROCEDURE InsertStudentSafe()
BEGIN
    DECLARE CONTINUE HANDLER FOR SQLEXCEPTION
        SELECT 'Insertion Failed';

    INSERT INTO STUDENT VALUES (101, 'Kavi', 'CSE', 3);
END;
//

DELIMITER ;

CALL InsertStudentSafe();
ALTER TABLE STUDENT
ADD email VARCHAR(50);
 
ALTER TABLE STUDENT
ADD phone VARCHAR(15);

ALTER TABLE EVENT
ADD organizer VARCHAR(50);

ALTER TABLE STUDENT
ADD address VARCHAR(100),
ADD dob DATE;

ALTER TABLE STUDENT
MODIFY name VARCHAR(100);

ALTER TABLE EVENT
MODIFY venue VARCHAR(100);

ALTER TABLE STUDENT
RENAME COLUMN dept TO department;

ALTER TABLE STUDENT
DROP phone;

ALTER TABLE STUDENT
ADD CONSTRAINT uq_student_email UNIQUE (email);

ALTER TABLE STUDENT
DROP INDEX uq_student_email;

ALTER TABLE EVENT
ALTER venue SET DEFAULT 'MG Auditorium';

GRANT SELECT, INSERT, UPDATE ON student_event_db.* TO 'username'@'localhost';
REVOKE UPDATE ON student_event_db.* FROM 'username'@'localhost';

