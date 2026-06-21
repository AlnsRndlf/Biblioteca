-- liquibase formatted sql
-- changeset alonso:1

create table book (
    isbn bigint primary key,
    title varchar(255),
    author varchar(255),
    year_publicated int,
    sotck int not null
);

-- changeset alonso:2

ALTER TABLE book
    CHANGE COLUMN sotck stock INT;

-- changeset alonso:3
alter table book
    modify column title varchar(255) not null,
    modify column author varchar(255) not null;

-- changeset alonso:4
-- Poblar tabla BOOK con 25 registros

INSERT INTO book (isbn, title, author, year_publicated, stock) VALUES
    (9780306406157, 'Clean Code', 'Robert C. Martin', 2008, 15);

INSERT INTO book (isbn, title, author, year_publicated, stock) VALUES
    (9780132350884, 'Clean Architecture', 'Robert C. Martin', 2017, 10);

INSERT INTO book (isbn, title, author, year_publicated, stock) VALUES
    (9780201633610, 'Design Patterns', 'Erich Gamma', 1994, 8);

INSERT INTO book (isbn, title, author, year_publicated, stock) VALUES
    (9780131103627, 'The C Programming Language', 'Brian W. Kernighan', 1988, 12);

INSERT INTO book (isbn, title, author, year_publicated, stock) VALUES
    (9780134685991, 'Effective Java', 'Joshua Bloch', 2018, 20);

INSERT INTO book (isbn, title, author, year_publicated, stock) VALUES
    (9781491950357, 'Building Microservices', 'Sam Newman', 2015, 7);

INSERT INTO book (isbn, title, author, year_publicated, stock) VALUES
    (9781617294945, 'Spring in Action', 'Craig Walls', 2018, 9);

INSERT INTO book (isbn, title, author, year_publicated, stock) VALUES
    (9780596009205, 'Head First Java', 'Kathy Sierra', 2005, 14);

INSERT INTO book (isbn, title, author, year_publicated, stock) VALUES
    (9780134494166, 'Core Java Volume I', 'Cay S. Horstmann', 2018, 6);

INSERT INTO book (isbn, title, author, year_publicated, stock) VALUES
    (9781449331818, 'Learning Python', 'Mark Lutz', 2013, 11);

INSERT INTO book (isbn, title, author, year_publicated, stock) VALUES
    (9781492078005, 'Kubernetes Up and Running', 'Brendan Burns', 2022, 5);

INSERT INTO book (isbn, title, author, year_publicated, stock) VALUES
    (9780321356680, 'Effective C++', 'Scott Meyers', 2005, 4);

INSERT INTO book (isbn, title, author, year_publicated, stock) VALUES
    (9780262033848, 'Introduction to Algorithms', 'Thomas H. Cormen', 2009, 13);

INSERT INTO book (isbn, title, author, year_publicated, stock) VALUES
    (9780135974445, 'Artificial Intelligence', 'Stuart Russell', 2021, 3);

INSERT INTO book (isbn, title, author, year_publicated, stock) VALUES
    (9780596517748, 'JavaScript The Good Parts', 'Douglas Crockford', 2008, 16);

INSERT INTO book (isbn, title, author, year_publicated, stock) VALUES
    (9781492056355, 'Fluent Python', 'Luciano Ramalho', 2022, 9);

INSERT INTO book (isbn, title, author, year_publicated, stock) VALUES
    (9781787125933, 'Mastering Spring Boot', 'Dinesh Rajput', 2017, 7);

INSERT INTO book (isbn, title, author, year_publicated, stock) VALUES
    (9780137081073, 'Agile Software Development', 'Robert C. Martin', 2002, 10);

INSERT INTO book (isbn, title, author, year_publicated, stock) VALUES
    (9780321125217, 'Domain Driven Design', 'Eric Evans', 2003, 5);

INSERT INTO book (isbn, title, author, year_publicated, stock) VALUES
    (9780131177055, 'Operating System Concepts', 'Abraham Silberschatz', 2004, 8);

INSERT INTO book (isbn, title, author, year_publicated, stock) VALUES
    (9780078022159, 'Database System Concepts', 'Henry F. Korth', 2019, 6);

INSERT INTO book (isbn, title, author, year_publicated, stock) VALUES
    (9780134093413, 'Computer Networking', 'James F. Kurose', 2016, 12);

INSERT INTO book (isbn, title, author, year_publicated, stock) VALUES
    (9781491910399, 'Docker Deep Dive', 'Nigel Poulton', 2020, 9);

INSERT INTO book (isbn, title, author, year_publicated, stock) VALUES
    (9781098111871, 'Learning SQL', 'Alan Beaulieu', 2020, 17);

INSERT INTO book (isbn, title, author, year_publicated, stock) VALUES
    (9780134757599, 'Refactoring', 'Martin Fowler', 2019, 11);