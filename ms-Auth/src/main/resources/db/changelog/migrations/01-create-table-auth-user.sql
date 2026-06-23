-- liquibase formatted sql
-- changeset Alonso:1
create table auth_user (
    rut varchar(13) primary key,
    password varchar(255) not null,
    role varchar(50) not null
);

-- changeset Alonso:2
insert into auth_user (rut, password, role) values
    ('21006259-9', 'contraseña', 'ADMIN');