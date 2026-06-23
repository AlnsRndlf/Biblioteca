-- liquibase formatted sql
-- changeset alonso:1
create table app_user (
    rut varchar(13) primary key,
    full_name varchar(255) not null,
    email varchar(255) unique not null,
    membershiped_date DATE
);

-- changeset alonso:2
INSERT INTO app_user (rut, full_name, email, membershiped_date) VALUES
    ('12345678-k', 'Juan Perez', 'juan.perez@gmail.com', DATE '2024-02-10');

INSERT INTO app_user (rut, full_name, email, membershiped_date) VALUES
    ('87654321-4', 'Maria Gonzalez', 'maria.gonzalez@gmail.com', DATE '2023-08-12');

INSERT INTO app_user (rut, full_name, email, membershiped_date) VALUES
    ('11222333-6', 'Carlos Ramirez', 'carlos.ramirez@gmail.com', DATE '2024-07-15');

INSERT INTO app_user (rut, full_name, email, membershiped_date) VALUES
    ('99888777-K', 'Fernanda Soto', 'fernanda.soto@gmail.com', DATE '2025-01-18');

INSERT INTO app_user (rut, full_name, email, membershiped_date) VALUES
    ('13579246-k', 'Pedro Morales', 'pedro.morales@gmail.com', DATE '2024-04-20');

INSERT INTO app_user (rut, full_name, email, membershiped_date) VALUES
    ('24681357-9', 'Camila Torres', 'camila.torres@gmail.com', DATE '2022-06-22');

INSERT INTO app_user (rut, full_name, email, membershiped_date) VALUES
    ('15975348-2', 'Diego Herrera', 'diego.herrera@gmail.com', DATE '2022-02-25');

INSERT INTO app_user (rut, full_name, email, membershiped_date) VALUES
    ('75315984-7', 'Valentina Rojas', 'valentina.rojas@gmail.com', DATE '2024-01-28');

INSERT INTO app_user (rut, full_name, email, membershiped_date) VALUES
    ('14725836-K', 'Sebastián Diaz', 'sebastian.diaz@gmail.com', DATE '2023-02-01');

INSERT INTO app_user (rut, full_name, email, membershiped_date) VALUES
    ('25836914-3', 'Antonia Castro', 'antonia.castro@gmail.com', DATE '2025-03-03');

INSERT INTO app_user (rut, full_name, email, membershiped_date) VALUES
    ('36925814-0', 'Javier Fuentes', 'javier.fuentes@gmail.com', DATE '2024-08-05');

INSERT INTO app_user (rut, full_name, email, membershiped_date) VALUES
    ('95135746-k', 'Daniela Vega', 'daniela.vega@gmail.com', DATE '2025-05-07');

INSERT INTO app_user (rut, full_name, email, membershiped_date) VALUES
    ('45678912-1', 'Ricardo Silva', 'ricardo.silva@gmail.com', DATE '2026-010-10');

INSERT INTO app_user (rut, full_name, email, membershiped_date) VALUES
    ('56789123-k', 'Paula Martinez', 'paula.martinez@gmail.com', DATE '2026-5-12');

INSERT INTO app_user (rut, full_name, email, membershiped_date) VALUES
    ('67891234-3', 'Felipe Navarro', 'felipe.navarro@gmail.com', DATE '2024-03-14');

INSERT INTO app_user (rut, full_name, email, membershiped_date) VALUES
    ('78912345-4', 'Constanza León', 'constanza.leon@gmail.com', DATE '2022-09-16');

INSERT INTO app_user (rut, full_name, email, membershiped_date) VALUES
    ('89123456-5', 'Matias Ortega', 'matias.ortega@gmail.com', DATE '2026-01-18');

INSERT INTO app_user (rut, full_name, email, membershiped_date) VALUES
    ('91234567-6', 'Francisca Núñez', 'francisca.nunez@gmail.com', DATE '2025-02-20');

INSERT INTO app_user (rut, full_name, email, membershiped_date) VALUES
    ('72345678-k', 'Tomas Reyes', 'tomas.reyes@gmail.com', DATE '2023-07-22');

INSERT INTO app_user (rut, full_name, email, membershiped_date) VALUES
    ('63456789-8', 'Isidora Campos', 'isidora.campos@gmail.com', DATE '2025-12-24');

INSERT INTO app_user (rut, full_name, email, membershiped_date) VALUES
    ('54567891-9', 'Benjamin Vidal', 'benjamin.vidal@gmail.com', DATE '2023-12-26');

INSERT INTO app_user (rut, full_name, email, membershiped_date) VALUES
    ('45678913-0', 'Josefa Paredes', 'josefa.paredes@gmail.com', DATE '2025-05-28');

INSERT INTO app_user (rut, full_name, email, membershiped_date) VALUES
    ('36789124-1', 'Vicente Salazar', 'vicente.salazar@gmail.com', DATE '2023-07-01');

INSERT INTO app_user (rut, full_name, email, membershiped_date) VALUES
    ('27891235-2', 'Catalina Espinoza', 'catalina.espinoza@gmail.com', DATE '2026-05-03');

INSERT INTO app_user (rut, full_name, email, membershiped_date) VALUES
    ('18912346-3', 'Ignacio Araya', 'ignacio.araya@gmail.com', DATE '2024-03-05');
