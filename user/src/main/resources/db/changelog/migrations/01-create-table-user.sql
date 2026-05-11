-- liquibase formatted sql
-- changeset alonso:1

create table app_user (
    rut varchar(13) primary key,
    full_name varchar(255) not null,
    email varchar(255) unique not null,
    membershiped_date DATE
);

