CREATE TABLE IF NOT EXISTS app_users (
                                         id SERIAL PRIMARY KEY,
                                         first_name VARCHAR(255),
    last_name VARCHAR(255),
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role SMALLINT NOT NULL,
    is_deleted BOOLEAN NOT NULL
    );

ALTER TABLE app_users
ALTER COLUMN role TYPE SMALLINT USING role::SMALLINT;


CREATE TABLE IF NOT EXISTS categories (
                                          id SERIAL PRIMARY KEY,
                                          name VARCHAR(255) NOT NULL,
    parent_id BIGINT,
    CONSTRAINT fk_parent FOREIGN KEY (parent_id) REFERENCES categories(id) ON DELETE CASCADE
    );


CREATE TABLE IF NOT EXISTS products (
                                        id SERIAL PRIMARY KEY,
                                        name VARCHAR(255) NOT NULL,
    description TEXT,
    is_deleted BOOLEAN,
    category_id BIGINT NOT NULL,
    CONSTRAINT fk_category FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE CASCADE
    );


CREATE TABLE IF NOT EXISTS offers (
                                      id SERIAL PRIMARY KEY,
                                      product_id BIGINT NOT NULL,
                                      price INT NOT NULL,
                                      stock_quantity INT NOT NULL,
                                      is_available BOOLEAN,
                                      is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
                                      CONSTRAINT fk_product FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
    );


CREATE TABLE IF NOT EXISTS attributes (
                                          id SERIAL PRIMARY KEY,
                                          name VARCHAR(255) NOT NULL,
    value VARCHAR(255) NOT NULL,
    CONSTRAINT unique_name_value UNIQUE(name, value)
    );


CREATE TABLE IF NOT EXISTS offer_attributes (
                                                offer_id BIGINT NOT NULL,
                                                attribute_id BIGINT NOT NULL,
                                                CONSTRAINT fk_offer FOREIGN KEY (offer_id) REFERENCES offers(id) ON DELETE CASCADE,
    CONSTRAINT fk_attribute FOREIGN KEY (attribute_id) REFERENCES attributes(id) ON DELETE CASCADE,
    PRIMARY KEY (offer_id, attribute_id)
    );


CREATE TABLE IF NOT EXISTS cart_items (
                                          id SERIAL PRIMARY KEY,
                                          offer_id BIGINT NOT NULL,
                                          quantity INT NOT NULL,
                                          CONSTRAINT fk_offer FOREIGN KEY (offer_id) REFERENCES offers(id) ON DELETE CASCADE
    );


CREATE TABLE IF NOT EXISTS carts (
                                     id SERIAL PRIMARY KEY,
                                     total_amount INT,
                                     total_quantity INT,
                                     user_id BIGINT UNIQUE NOT NULL,
                                     CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES app_users(id) ON DELETE CASCADE
    );


CREATE TABLE IF NOT EXISTS cart_items_list (
                                               cart_id BIGINT NOT NULL,
                                               cart_item_id BIGINT NOT NULL,
                                               CONSTRAINT fk_cart FOREIGN KEY (cart_id) REFERENCES carts(id) ON DELETE CASCADE,
    CONSTRAINT fk_cart_item FOREIGN KEY (cart_item_id) REFERENCES cart_items(id) ON DELETE CASCADE,
    PRIMARY KEY (cart_id, cart_item_id)
    );


CREATE TABLE IF NOT EXISTS payments (
                                        id SERIAL PRIMARY KEY,
                                        name VARCHAR(255) NOT NULL
    );


CREATE TABLE IF NOT EXISTS deliveries (
                                          id SERIAL PRIMARY KEY,
                                          name VARCHAR(255) NOT NULL,
    delivery_price INT NOT NULL
    );


CREATE TABLE IF NOT EXISTS delivery_payments (
                                                 delivery_id BIGINT NOT NULL,
                                                 payment_id BIGINT NOT NULL,
                                                 CONSTRAINT fk_delivery FOREIGN KEY (delivery_id) REFERENCES deliveries(id) ON DELETE CASCADE,
    CONSTRAINT fk_payment FOREIGN KEY (payment_id) REFERENCES payments(id) ON DELETE CASCADE,
    PRIMARY KEY (delivery_id, payment_id)
    );


CREATE TABLE IF NOT EXISTS pickup_locations (
                                                id SERIAL PRIMARY KEY,
                                                name VARCHAR(255),
    address VARCHAR(255) NOT NULL,
    delivery_id BIGINT NOT NULL,
    CONSTRAINT fk_delivery FOREIGN KEY (delivery_id) REFERENCES deliveries(id) ON DELETE CASCADE
    );


CREATE TABLE IF NOT EXISTS orders (
                                      id SERIAL PRIMARY KEY,
                                      user_id BIGINT NOT NULL,
                                      address VARCHAR(255) NOT NULL,
    pickup_location VARCHAR(255),
    delivery_id BIGINT NOT NULL,
    payment_id BIGINT NOT NULL,
    order_create_date_time TIMESTAMP NOT NULL,
    total_quantity INT,
    total_amount INT,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES app_users(id) ON DELETE CASCADE,
    CONSTRAINT fk_delivery FOREIGN KEY (delivery_id) REFERENCES deliveries(id) ON DELETE CASCADE,
    CONSTRAINT fk_payment FOREIGN KEY (payment_id) REFERENCES payments(id) ON DELETE CASCADE
    );


CREATE TABLE IF NOT EXISTS order_details (
                                             id SERIAL PRIMARY KEY,
                                             order_id BIGINT NOT NULL,
                                             offer_id BIGINT NOT NULL,
                                             quantity INT NOT NULL,
                                             total_offer_amount INT NOT NULL,
                                             CONSTRAINT fk_order FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    CONSTRAINT fk_offer FOREIGN KEY (offer_id) REFERENCES offers(id) ON DELETE CASCADE
    );


COPY app_users(id,email,first_name,is_deleted,last_name,password,role)
    FROM '/docker-entrypoint-initdb.d/fulfillment/app_users.csv'
    DELIMITER ','
    CSV HEADER;

COPY categories(id,name,parent_id)
    FROM '/docker-entrypoint-initdb.d/fulfillment/categories.csv'
    DELIMITER ','
    CSV HEADER;

COPY products(id,name,description,category_id,is_deleted)
    FROM '/docker-entrypoint-initdb.d/fulfillment/products.csv'
    DELIMITER ','
    CSV HEADER;

COPY attributes(id,name,value)
    FROM '/docker-entrypoint-initdb.d/fulfillment/attributes.csv'
    DELIMITER ','
    CSV HEADER;

COPY offers(id,is_available,is_deleted,price,stock_quantity,product_id)
    FROM '/docker-entrypoint-initdb.d/fulfillment/offers.csv'
    DELIMITER ','
    CSV HEADER;

COPY offer_attributes(offer_id,attribute_id)
    FROM '/docker-entrypoint-initdb.d/fulfillment/offer_attributes.csv'
    DELIMITER ','
    CSV HEADER;

COPY deliveries(id,delivery_price,name)
    FROM '/docker-entrypoint-initdb.d/fulfillment/deliveries.csv'
    DELIMITER ','
    CSV HEADER;

COPY payments(id,name)
    FROM '/docker-entrypoint-initdb.d/fulfillment/payments.csv'
    DELIMITER ','
    CSV HEADER;

COPY delivery_payments(delivery_id,payment_id)
    FROM '/docker-entrypoint-initdb.d/fulfillment/delivery_payments.csv'
    DELIMITER ','
    CSV HEADER;

COPY pickup_locations(id,address,delivery_id,name)
    FROM '/docker-entrypoint-initdb.d/fulfillment/pickup_locations.csv'
    DELIMITER ','
    CSV HEADER;

