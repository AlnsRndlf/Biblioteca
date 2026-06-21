-- liquibase formatted sql
-- changeset alonso:1

create table loans (
    id_loan bigint auto_increment primary key ,
    book_isbn bigint not null,
    user_rut varchar(13) not null,
    loan_date date not null,
    due_date date not null,
    returned_date date not null
);

-- changeset alonso:2

alter table loans modify returned_date date null;