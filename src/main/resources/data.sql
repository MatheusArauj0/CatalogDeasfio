INSERT INTO tb_user (first_name, last_name, email, password) VALUES ('Alex', 'Brown', 'alex@gmail.com', '$2a$10$eACCYoNOHEqXve8aIWT8Nu3PkMXWBaOxJ9aORUYzfMQCbVBIhZ8tG');
INSERT INTO tb_user (first_name, last_name, email, password) VALUES ('Maria', 'Green', 'maria@gmail.com', '$2a$10$eACCYoNOHEqXve8aIWT8Nu3PkMXWBaOxJ9aORUYzfMQCbVBIhZ8tG');

INSERT INTO tb_role (authority) VALUES ('ROLE_OPERATOR');
INSERT INTO tb_role (authority) VALUES ('ROLE_ADMIN');

INSERT INTO tb_user_role (user_id, role_id) VALUES (1, 1);
INSERT INTO tb_user_role (user_id, role_id) VALUES (2, 1);
INSERT INTO tb_user_role (user_id, role_id) VALUES (2, 2);


INSERT INTO tb_category (name, active, tipo) VALUES ('Livros', true, 0);
INSERT INTO tb_category (name, active, tipo) VALUES ('Eletrônicos', true, 1);
INSERT INTO tb_category (name, active, tipo) VALUES ('Computadores', true , 2);

INSERT INTO tb_product (name, active, sku, cost_price, icms, sale_price, amount, date, img_url, user_id) VALUES ('The Lord of the Rings', true, 'TLTR', 30.0, '17%', 90.5, 10, TIMESTAMP WITH TIME ZONE '2020-07-13T20:50:07.12345Z','url da imagem', 1);
INSERT INTO tb_product (name, active, sku, cost_price, icms, sale_price, amount, date, img_url, user_id) VALUES ('Smart TV', true, 'SMTV', 1000.0, '17%', 2190.0, 10, TIMESTAMP WITH TIME ZONE '2020-07-14T10:00:00Z', 'url da imagem', 2);
INSERT INTO tb_product (name, active, sku, cost_price, icms, sale_price, amount, date, img_url, user_id) VALUES ('Macbook Pro', true, 'MACPR', 500.0, '17%', 1250.0, 15, TIMESTAMP WITH TIME ZONE '2020-07-14T10:00:00Z', 'url da imagem', 1);
INSERT INTO tb_product (name, active, sku, cost_price, icms, sale_price, amount, date, img_url, user_id) VALUES ('PC Gamer', true, 'PCGM', 500.0, '17%', 1200.0, 5, TIMESTAMP WITH TIME ZONE '2020-07-14T10:00:00Z', 'url da imagem', 2);
INSERT INTO tb_product (name, active, sku, cost_price, icms, sale_price, amount, date, img_url, user_id) VALUES ('Rails for Dummies', true, 'RFDU', 30.0, '17%', 100.99, 20, TIMESTAMP WITH TIME ZONE '2020-07-14T10:00:00Z', 'url da imagem', 1);

INSERT INTO tb_product_category (product_id, category_id) VALUES (1, 2);
INSERT INTO tb_product_category (product_id, category_id) VALUES (2, 1);
INSERT INTO tb_product_category (product_id, category_id) VALUES (2, 3);
INSERT INTO tb_product_category (product_id, category_id) VALUES (3, 3);
INSERT INTO tb_product_category (product_id, category_id) VALUES (4, 3);
INSERT INTO tb_product_category (product_id, category_id) VALUES (5, 2);


INSERT INTO tb_config_product (name, visible) VALUES ('name', true);
INSERT INTO tb_config_product (name, visible) VALUES ('active', true);
INSERT INTO tb_config_product (name, visible) VALUES ('sku', true);
INSERT INTO tb_config_product (name, visible) VALUES ('costPrice', true);
INSERT INTO tb_config_product (name, visible) VALUES ('imgUrl', true);
INSERT INTO tb_config_product (name, visible) VALUES ('icms', true);
INSERT INTO tb_config_product (name, visible) VALUES ('salePrice', true);
INSERT INTO tb_config_product (name, visible) VALUES ('amount', true);
INSERT INTO tb_config_product (name, visible) VALUES ('date', true);