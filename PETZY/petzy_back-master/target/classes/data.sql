-- Precarga de categorías base para productos
INSERT INTO product_categories (name, description) VALUES ('Alimentos', 'Concentrados, snacks y comida húmeda para mascotas');
INSERT INTO product_categories (name, description) VALUES ('Juguetes', 'Juguetes interactivos, mordedores y pelotas');
INSERT INTO product_categories (name, description) VALUES ('Higiene', 'Shampoos, cepillos y productos de aseo');

-- Precarga de roles
INSERT INTO roles (name) VALUES ('ROLE_CLIENT');
INSERT INTO roles (name) VALUES ('ROLE_ADMIN');
INSERT INTO roles (name) VALUES ('ROLE_VET')

-- Precarga de Typo de documento
INSERT INTO document_types (name, code) VALUES ('Cédula de Ciudadanía', 'CC');
INSERT INTO document_types (name, code) VALUES ('Cédula de Extranjería', 'CE');
INSERT INTO document_types (name, code) VALUES ('Pasaporte', 'PAS');