-- liquibase formatted sql
-- changeset alonso:1

create table penalties (
    id_penalty bigint auto_increment primary key ,
    user_rut varchar(13) not null,
    amount int not null,
    reason varchar(255) not null,
    status varchar(25),
    created_at date not null
);