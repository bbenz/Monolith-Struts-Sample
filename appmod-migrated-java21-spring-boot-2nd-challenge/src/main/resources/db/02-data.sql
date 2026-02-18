-- English (en_US) data with USD Currency
-- Aligned to match 01-schema.sql table definitions

INSERT INTO roles(id, name) VALUES
  ('r-admin', 'ADMIN'),
  ('r-user', 'USER');

INSERT INTO users(id, email, username, password_hash, salt, status, role, created_at, updated_at) VALUES
  ('u-1', 'user@example.com', 'demo', 'hash', 'salt', 'ACTIVE', 'USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Admin user for demo login
INSERT INTO users(id, email, username, password_hash, salt, status, role, created_at, updated_at) VALUES
  ('u-admin', 'admin@example.com', 'admin', 'hash', 'salt', 'ACTIVE', 'ADMIN', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- categories: schema has (id, name, parent_id) only
INSERT INTO categories(id, name, parent_id) VALUES
  ('c-1', 'Snowboards', NULL),
  ('c-2', 'Boots', NULL),
  ('c-3', 'Bindings', NULL),
  ('c-4', 'Wax', NULL),
  ('c-5', 'All-round', 'c-1'),
  ('c-6', 'Freestyle', 'c-1'),
  ('c-7', 'Freeride', 'c-1'),
  ('c-8', 'Carving', 'c-1');

-- products: schema has (id, name, brand, description, category_id, sku, status, created_at, updated_at)
INSERT INTO products(id, name, brand, description, category_id, sku, status, created_at, updated_at) VALUES
  ('p-1', 'Board A', 'SnowPro', 'Advanced men''s for high-speed carving', 'c-8', 'BRD-A-001', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('p-2', 'Board B', 'RideEasy', 'Beginner-friendly all-rounder', 'c-5', 'BRD-B-001', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('p-3', 'Board C', 'SnowPro', 'Intermediate women''s freestyle board', 'c-6', 'BRD-C-001', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('p-4', 'Board D', 'FreestyleCo', 'Advanced freestyle for park and jib', 'c-6', 'BRD-D-001', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('p-5', 'Board E', 'MountainKing', 'Intermediate all-mountain freeride', 'c-7', 'BRD-E-001', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('p-6', 'Board F', 'RideEasy', 'Beginner-friendly soft board', 'c-5', 'BRD-F-001', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('p-7', 'Board G', 'SnowPro', 'Advanced women''s all-rounder', 'c-5', 'BRD-G-001', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('p-8', 'Board H', 'RideEasy', 'Beginner to intermediate men''s all-rounder', 'c-5', 'BRD-H-001', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('p-9', 'Board I', 'SnowPro', 'Intermediate carving board', 'c-8', 'BRD-I-001', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('p-10', 'Board J', 'MountainKing', 'Advanced freeride for deep powder', 'c-7', 'BRD-J-001', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('p-11', 'Board K', 'FreestyleCo', 'Beginner-friendly freestyle board', 'c-6', 'BRD-K-001', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('p-12', 'Board L', 'SnowPro', 'Intermediate to advanced women''s all-rounder', 'c-5', 'BRD-L-001', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('p-13', 'Board M', 'MountainKing', 'Beginner-friendly freeride board', 'c-7', 'BRD-M-001', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('p-14', 'Board N', 'MountainKing', 'Advanced split board', 'c-7', 'BRD-N-001', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('p-15', 'Board O', 'FreestyleCo', 'Intermediate freestyle for tricks', 'c-6', 'BRD-O-001', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('p-16', 'Boots A', 'BootMaster', 'Advanced men''s hard boots', 'c-2', 'BTS-A-001', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('p-17', 'Boots B', 'RideEasy', 'Beginner-friendly soft boots', 'c-2', 'BTS-B-001', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('p-18', 'Boots C', 'BootMaster', 'Intermediate women''s boots', 'c-2', 'BTS-C-001', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('p-19', 'Boots D', 'FreestyleCo', 'Advanced freestyle boots', 'c-2', 'BTS-D-001', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('p-20', 'Boots E', 'RideEasy', 'Intermediate all-round boots', 'c-2', 'BTS-E-001', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('p-21', 'Binding A', 'BindTech', 'Advanced responsive bindings', 'c-3', 'BND-A-001', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('p-22', 'Binding B', 'RideEasy', 'Beginner-friendly flexible bindings', 'c-3', 'BND-B-001', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('p-23', 'Binding C', 'BindTech', 'Intermediate all-round bindings', 'c-3', 'BND-C-001', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('p-24', 'Binding D', 'FreestyleCo', 'Advanced freestyle bindings', 'c-3', 'BND-D-001', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('p-25', 'Binding E', 'MountainKing', 'Intermediate freeride bindings', 'c-3', 'BND-E-001', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('p-26', 'Wax A', 'WaxWorks', 'All-temperature wax', 'c-4', 'WAX-A-001', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('p-27', 'Wax B', 'WaxWorks', 'Cold wax', 'c-4', 'WAX-B-001', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('p-28', 'Wax C', 'WaxWorks', 'Warm wax', 'c-4', 'WAX-C-001', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('p-29', 'Wax D', 'WaxWorks', 'Liquid wax', 'c-4', 'WAX-D-001', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('p-30', 'Wax E', 'WaxWorks', 'Spring wax', 'c-4', 'WAX-E-001', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- prices: schema has (id, product_id, regular_price, sale_price, currency_code, sale_start_date, sale_end_date)
INSERT INTO prices(id, product_id, regular_price, sale_price, currency_code, sale_start_date, sale_end_date) VALUES
  ('pp-1', 'p-1', 832.00, NULL, 'USD', '2024-01-01', '2024-12-31'),
  ('pp-2', 'p-2', 325.00, NULL, 'USD', '2024-01-01', '2024-12-31'),
  ('pp-3', 'p-3', 455.00, NULL, 'USD', '2024-01-01', '2024-12-31'),
  ('pp-4', 'p-4', 715.00, NULL, 'USD', '2024-01-01', '2024-12-31'),
  ('pp-5', 'p-5', 585.00, NULL, 'USD', '2024-01-01', '2024-12-31'),
  ('pp-6', 'p-6', 292.50, NULL, 'USD', '2024-01-01', '2024-12-31'),
  ('pp-7', 'p-7', 650.00, NULL, 'USD', '2024-01-01', '2024-12-31'),
  ('pp-8', 'p-8', 390.00, NULL, 'USD', '2024-01-01', '2024-12-31'),
  ('pp-9', 'p-9', 552.50, NULL, 'USD', '2024-01-01', '2024-12-31'),
  ('pp-10', 'p-10', 910.00, NULL, 'USD', '2024-01-01', '2024-12-31'),
  ('pp-11', 'p-11', 357.50, NULL, 'USD', '2024-01-01', '2024-12-31'),
  ('pp-12', 'p-12', 520.00, NULL, 'USD', '2024-01-01', '2024-12-31'),
  ('pp-13', 'p-13', 422.50, NULL, 'USD', '2024-01-01', '2024-12-31'),
  ('pp-14', 'p-14', 1300.00, NULL, 'USD', '2024-01-01', '2024-12-31'),
  ('pp-15', 'p-15', 487.50, NULL, 'USD', '2024-01-01', '2024-12-31'),
  ('pp-16', 'p-16', 455.00, NULL, 'USD', '2024-01-01', '2024-12-31'),
  ('pp-17', 'p-17', 162.50, NULL, 'USD', '2024-01-01', '2024-12-31'),
  ('pp-18', 'p-18', 292.50, NULL, 'USD', '2024-01-01', '2024-12-31'),
  ('pp-19', 'p-19', 390.00, NULL, 'USD', '2024-01-01', '2024-12-31'),
  ('pp-20', 'p-20', 227.50, NULL, 'USD', '2024-01-01', '2024-12-31'),
  ('pp-21', 'p-21', 325.00, NULL, 'USD', '2024-01-01', '2024-12-31'),
  ('pp-22', 'p-22', 130.00, NULL, 'USD', '2024-01-01', '2024-12-31'),
  ('pp-23', 'p-23', 227.50, NULL, 'USD', '2024-01-01', '2024-12-31'),
  ('pp-24', 'p-24', 292.50, NULL, 'USD', '2024-01-01', '2024-12-31'),
  ('pp-25', 'p-25', 260.00, NULL, 'USD', '2024-01-01', '2024-12-31'),
  ('pp-26', 'p-26', 16.25, NULL, 'USD', '2024-01-01', '2024-12-31'),
  ('pp-27', 'p-27', 19.50, NULL, 'USD', '2024-01-01', '2024-12-31'),
  ('pp-28', 'p-28', 19.50, NULL, 'USD', '2024-01-01', '2024-12-31'),
  ('pp-29', 'p-29', 13.00, NULL, 'USD', '2024-01-01', '2024-12-31'),
  ('pp-30', 'p-30', 22.75, NULL, 'USD', '2024-01-01', '2024-12-31');

-- inventory: schema has (id, product_id, quantity, reserved_quantity, status)
INSERT INTO inventory(id, product_id, quantity, reserved_quantity, status) VALUES
  ('i-1', 'p-1', 15, 0, 'IN_STOCK'),
  ('i-2', 'p-2', 30, 0, 'IN_STOCK'),
  ('i-3', 'p-3', 20, 0, 'IN_STOCK'),
  ('i-4', 'p-4', 18, 0, 'IN_STOCK'),
  ('i-5', 'p-5', 25, 0, 'IN_STOCK'),
  ('i-6', 'p-6', 40, 0, 'IN_STOCK'),
  ('i-7', 'p-7', 12, 0, 'IN_STOCK'),
  ('i-8', 'p-8', 35, 0, 'IN_STOCK'),
  ('i-9', 'p-9', 22, 0, 'IN_STOCK'),
  ('i-10', 'p-10', 10, 0, 'IN_STOCK'),
  ('i-11', 'p-11', 28, 0, 'IN_STOCK'),
  ('i-12', 'p-12', 16, 0, 'IN_STOCK'),
  ('i-13', 'p-13', 24, 0, 'IN_STOCK'),
  ('i-14', 'p-14', 8, 0, 'IN_STOCK'),
  ('i-15', 'p-15', 20, 0, 'IN_STOCK'),
  ('i-16', 'p-16', 14, 0, 'IN_STOCK'),
  ('i-17', 'p-17', 45, 0, 'IN_STOCK'),
  ('i-18', 'p-18', 30, 0, 'IN_STOCK'),
  ('i-19', 'p-19', 18, 0, 'IN_STOCK'),
  ('i-20', 'p-20', 32, 0, 'IN_STOCK'),
  ('i-21', 'p-21', 20, 0, 'IN_STOCK'),
  ('i-22', 'p-22', 50, 0, 'IN_STOCK'),
  ('i-23', 'p-23', 28, 0, 'IN_STOCK'),
  ('i-24', 'p-24', 16, 0, 'IN_STOCK'),
  ('i-25', 'p-25', 22, 0, 'IN_STOCK'),
  ('i-26', 'p-26', 100, 0, 'IN_STOCK'),
  ('i-27', 'p-27', 80, 0, 'IN_STOCK'),
  ('i-28', 'p-28', 75, 0, 'IN_STOCK'),
  ('i-29', 'p-29', 120, 0, 'IN_STOCK'),
  ('i-30', 'p-30', 60, 0, 'IN_STOCK');

-- carts: schema has (id, user_id, session_id, status, expires_at)
INSERT INTO carts(id, user_id, session_id, status, expires_at) VALUES
  ('cart-1', 'u-1', 'sess-demo-001', 'ACTIVE', '2024-12-31 23:59:59');

-- cart_items: schema has (id, cart_id, product_id, quantity, unit_price)
INSERT INTO cart_items(id, cart_id, product_id, quantity, unit_price) VALUES
  ('ci-1', 'cart-1', 'p-2', 1, 325.00),
  ('ci-2', 'cart-1', 'p-17', 1, 162.50),
  ('ci-3', 'cart-1', 'p-22', 1, 130.00);

-- order_shipping: needed before orders that reference shipping
INSERT INTO order_shipping(id, order_id, recipient_name, postal_code, prefecture, address1, address2, phone, shipping_method_code, shipping_fee, requested_delivery_date) VALUES
  ('os-1', 'o-1', 'Duke Johnson', '80435', 'Colorado', '123 Ski Run Blvd', 'Suite 101', '970-555-1234', 'STANDARD', 10.00, NULL),
  ('os-2', 'o-2', 'Duke Johnson', '80435', 'Colorado', '123 Ski Run Blvd', 'Suite 101', '970-555-1234', 'STANDARD', 10.00, NULL),
  ('os-3', 'o-3', 'Sarah Johnson', '84060', 'Utah', '456 Powder Lane', 'Apt 202', '801-555-5678', 'EXPRESS', 15.00, NULL);

-- orders: schema has (id, order_number, user_id, status, payment_status, subtotal, tax, shipping_fee, discount_amount, total_amount, coupon_code, used_points, created_at, updated_at)
INSERT INTO orders(id, order_number, user_id, status, payment_status, subtotal, tax, shipping_fee, discount_amount, total_amount, coupon_code, used_points, created_at, updated_at) VALUES
  ('o-1', 'ORD-2024-0001', 'u-1', 'DELIVERED', 'PAID', 357.50, 35.75, 10.00, 0.00, 403.25, NULL, 0, '2024-01-15 10:30:00', '2024-01-20 15:45:00'),
  ('o-2', 'ORD-2024-0002', 'u-1', 'SHIPPED', 'PAID', 325.00, 32.50, 10.00, 0.00, 367.50, NULL, 0, '2024-02-01 14:20:00', '2024-02-03 09:15:00'),
  ('o-3', 'ORD-2024-0003', 'u-1', 'PROCESSING', 'PENDING', 487.50, 48.75, 15.00, 0.00, 551.25, NULL, 0, '2024-02-10 11:00:00', '2024-02-10 11:00:00');

-- order_items: schema has (id, order_id, product_id, product_name, sku, unit_price, quantity, subtotal)
INSERT INTO order_items(id, order_id, product_id, product_name, sku, unit_price, quantity, subtotal) VALUES
  ('oi-1', 'o-1', 'p-11', 'Board K', 'BRD-K-001', 357.50, 1, 357.50),
  ('oi-2', 'o-2', 'p-2', 'Board B', 'BRD-B-001', 325.00, 1, 325.00),
  ('oi-3', 'o-3', 'p-15', 'Board O', 'BRD-O-001', 487.50, 1, 487.50);

-- shipments: schema has (id, order_id, carrier, tracking_number, status, shipped_at, delivered_at)
INSERT INTO shipments(id, order_id, carrier, tracking_number, status, shipped_at, delivered_at) VALUES
  ('s-1', 'o-1', 'UPS', 'TRACK-001', 'DELIVERED', '2024-01-16 09:00:00', '2024-01-17 14:00:00'),
  ('s-2', 'o-2', 'FedEx', 'TRACK-002', 'IN_TRANSIT', '2024-02-02 10:30:00', NULL);

-- returns: schema has (id, order_id, order_item_id, reason, quantity, refund_amount, status)
INSERT INTO returns(id, order_id, order_item_id, reason, quantity, refund_amount, status) VALUES
  ('ret-1', 'o-1', 'oi-1', 'Size did not fit', 1, 357.50, 'APPROVED');

-- payments: schema has (id, order_id, cart_id, amount, currency, status, payment_intent_id, created_at)
INSERT INTO payments(id, order_id, cart_id, amount, currency, status, payment_intent_id, created_at) VALUES
  ('pay-1', 'o-1', NULL, 403.25, 'USD', 'COMPLETED', 'pi_demo_001', '2024-01-15 10:31:00'),
  ('pay-2', 'o-2', NULL, 367.50, 'USD', 'COMPLETED', 'pi_demo_002', '2024-02-01 14:21:00'),
  ('pay-3', 'o-3', NULL, 551.25, 'USD', 'PENDING', 'pi_demo_003', '2024-02-10 11:01:00');

-- user_addresses: schema has (id, user_id, label, recipient_name, postal_code, prefecture, address1, address2, phone, is_default, created_at, updated_at)
INSERT INTO user_addresses(id, user_id, label, recipient_name, postal_code, prefecture, address1, address2, phone, is_default, created_at, updated_at) VALUES
  ('addr-1', 'u-1', 'Home', 'Duke Johnson', '80435', 'Colorado', '123 Ski Run Blvd', 'Suite 101', '970-555-1234', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('addr-2', 'u-1', 'Office', 'Sarah Johnson', '84060', 'Utah', '456 Powder Lane', 'Apt 202', '801-555-5678', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- point_accounts
INSERT INTO point_accounts(id, user_id, balance, lifetime_earned, lifetime_redeemed) VALUES
  ('pa-1', 'u-1', 100, 200, 100);

-- shipping_methods
INSERT INTO shipping_methods(id, code, name, fee, is_active, sort_order) VALUES
  ('sm-1', 'STANDARD', 'Standard Shipping', 10.00, true, 1),
  ('sm-2', 'EXPRESS', 'Express Shipping', 15.00, true, 2);

-- coupons
INSERT INTO coupons(id, campaign_id, code, coupon_type, discount_value, discount_type, minimum_amount, maximum_discount, usage_limit, used_count, is_active, expires_at) VALUES
  ('coup-1', NULL, 'WINTER2024', 'GENERAL', 10.00, 'PERCENTAGE', 325.00, 100.00, 1000, 45, true, '2024-02-29 23:59:59'),
  ('coup-2', NULL, 'FIRSTBUY', 'GENERAL', 32.50, 'FIXED_AMOUNT', 325.00, 32.50, 500, 120, true, '2024-12-31 23:59:59');
