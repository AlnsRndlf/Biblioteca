-- liquibase formatted sql
-- changeset alonso:1

create table reviews (
    id_review bigint auto_increment primary key ,
    user_rut varchar(13) not null ,
    book_isbn bigint not null ,
    rating int not null ,
    comment varchar(500),
    created_at date
);