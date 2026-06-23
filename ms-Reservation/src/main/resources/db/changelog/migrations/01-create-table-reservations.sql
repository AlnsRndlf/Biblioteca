-- liquibase formatted sql
-- changeset alonso:1
CREATE TABLE reservations (
    id_reservation BIGINT AUTO_INCREMENT NOT NULL primary key ,
    user_rut VARCHAR(255) NOT NULL,
    book_isbn VARCHAR(255) NOT NULL,
    reservation_date date NOT NULL,
    status VARCHAR(50) NOT NULL
);

-- changeset alonso:2
RENAME TABLE reservations TO reservation;