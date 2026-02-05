-- Japanese to English (en_US) Translation with USD Currency
-- JPY amounts converted to USD using rate: 0.0065

INSERT INTO roles(id, name) VALUES
  ('r-admin', 'ADMIN'),
  ('r-user', 'USER');

INSERT INTO users(id, email, username, password_hash, salt, status, role, created_at, updated_at) VALUES
  ('u-1', 'user@example.com', 'demo', 'hash', 'salt', 'ACTIVE', 'USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Admin user for demo login
INSERT INTO users(id, email, username, password_hash, salt, status, role, created_at, updated_at) VALUES
  ('u-admin', 'admin@example.com', 'admin', 'hash', 'salt', 'ACTIVE', 'ADMIN', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO categories(id, name, description, parent_id) VALUES
  ('c-1', 'Snowboards', 'All types of snowboards', NULL),
  ('c-2', 'Boots', 'Snowboard boots', NULL),
  ('c-3', 'Bindings', 'Snowboard bindings', NULL),
  ('c-4', 'Wax', 'Snowboard wax', NULL),
  ('c-5', 'All-round', 'All-round snowboards', 'c-1'),
  ('c-6', 'Freestyle', 'Freestyle snowboards', 'c-1'),
  ('c-7', 'Freeride', 'Freeride snowboards', 'c-1'),
  ('c-8', 'Carving', 'Carving snowboards', 'c-1');

INSERT INTO products(id, name, description, category_id, status, created_at, updated_at) VALUES
  ('p-1', 'Board A', 'Advanced men''s for high-speed carving', 'c-8', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('p-2', 'Board B', 'Beginner-friendly all-rounder', 'c-5', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('p-3', 'Board C', 'Intermediate women''s freestyle board', 'c-6', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('p-4', 'Board D', 'Advanced freestyle for park and jib', 'c-6', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('p-5', 'Board E', 'Intermediate all-mountain freeride', 'c-7', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('p-6', 'Board F', 'Beginner-friendly soft board', 'c-5', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('p-7', 'Board G', 'Advanced women''s all-rounder', 'c-5', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('p-8', 'Board H', 'Beginner to intermediate men''s all-rounder', 'c-5', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('p-9', 'Board I', 'Intermediate carving board', 'c-8', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('p-10', 'Board J', 'Advanced freeride for deep powder', 'c-7', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('p-11', 'Board K', 'Beginner-friendly freestyle board', 'c-6', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('p-12', 'Board L', 'Intermediate to advanced women''s all-rounder', 'c-5', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('p-13', 'Board M', 'Beginner-friendly freeride board', 'c-7', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('p-14', 'Board N', 'Advanced split board', 'c-7', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('p-15', 'Board O', 'Intermediate freestyle for tricks', 'c-6', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('p-16', 'Boots A', 'Advanced men''s hard boots', 'c-2', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('p-17', 'Boots B', 'Beginner-friendly soft boots', 'c-2', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('p-18', 'Boots C', 'Intermediate women''s boots', 'c-2', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('p-19', 'Boots D', 'Advanced freestyle boots', 'c-2', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('p-20', 'Boots E', 'Intermediate all-round boots', 'c-2', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('p-21', 'Binding A', 'Advanced responsive bindings', 'c-3', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('p-22', 'Binding B', 'Beginner-friendly flexible bindings', 'c-3', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('p-23', 'Binding C', 'Intermediate all-round bindings', 'c-3', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('p-24', 'Binding D', 'Advanced freestyle bindings', 'c-3', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('p-25', 'Binding E', 'Intermediate freeride bindings', 'c-3', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('p-26', 'Wax A', 'All-temperature wax', 'c-4', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('p-27', 'Wax B', 'Cold wax', 'c-4', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('p-28', 'Wax C', 'Warm wax', 'c-4', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('p-29', 'Wax D', 'Liquid wax', 'c-4', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('p-30', 'Wax E', 'Spring wax', 'c-4', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO product_prices(id, product_id, price, currency_code, valid_from, valid_to) VALUES
  ('pp-1', 'p-1', 832.00, 'USD', '2024-01-01', '2024-12-31'),
  ('pp-2', 'p-2', 325.00, 'USD', '2024-01-01', '2024-12-31'),
  ('pp-3', 'p-3', 455.00, 'USD', '2024-01-01', '2024-12-31'),
  ('pp-4', 'p-4', 715.00, 'USD', '2024-01-01', '2024-12-31'),
  ('pp-5', 'p-5', 585.00, 'USD', '2024-01-01', '2024-12-31'),
  ('pp-6', 'p-6', 292.50, 'USD', '2024-01-01', '2024-12-31'),
  ('pp-7', 'p-7', 650.00, 'USD', '2024-01-01', '2024-12-31'),
  ('pp-8', 'p-8', 390.00, 'USD', '2024-01-01', '2024-12-31'),
  ('pp-9', 'p-9', 552.50, 'USD', '2024-01-01', '2024-12-31'),
  ('pp-10', 'p-10', 910.00, 'USD', '2024-01-01', '2024-12-31'),
  ('pp-11', 'p-11', 357.50, 'USD', '2024-01-01', '2024-12-31'),
  ('pp-12', 'p-12', 520.00, 'USD', '2024-01-01', '2024-12-31'),
  ('pp-13', 'p-13', 422.50, 'USD', '2024-01-01', '2024-12-31'),
  ('pp-14', 'p-14', 1300.00, 'USD', '2024-01-01', '2024-12-31'),
  ('pp-15', 'p-15', 487.50, 'USD', '2024-01-01', '2024-12-31'),
  ('pp-16', 'p-16', 455.00, 'USD', '2024-01-01', '2024-12-31'),
  ('pp-17', 'p-17', 162.50, 'USD', '2024-01-01', '2024-12-31'),
  ('pp-18', 'p-18', 292.50, 'USD', '2024-01-01', '2024-12-31'),
  ('pp-19', 'p-19', 390.00, 'USD', '2024-01-01', '2024-12-31'),
  ('pp-20', 'p-20', 227.50, 'USD', '2024-01-01', '2024-12-31'),
  ('pp-21', 'p-21', 325.00, 'USD', '2024-01-01', '2024-12-31'),
  ('pp-22', 'p-22', 130.00, 'USD', '2024-01-01', '2024-12-31'),
  ('pp-23', 'p-23', 227.50, 'USD', '2024-01-01', '2024-12-31'),
  ('pp-24', 'p-24', 292.50, 'USD', '2024-01-01', '2024-12-31'),
  ('pp-25', 'p-25', 260.00, 'USD', '2024-01-01', '2024-12-31'),
  ('pp-26', 'p-26', 16.25, 'USD', '2024-01-01', '2024-12-31'),
  ('pp-27', 'p-27', 19.50, 'USD', '2024-01-01', '2024-12-31'),
  ('pp-28', 'p-28', 19.50, 'USD', '2024-01-01', '2024-12-31'),
  ('pp-29', 'p-29', 13.00, 'USD', '2024-01-01', '2024-12-31'),
  ('pp-30', 'p-30', 22.75, 'USD', '2024-01-01', '2024-12-31');

INSERT INTO inventory(id, product_id, quantity, location, last_updated) VALUES
  ('i-1', 'p-1', 15, 'Warehouse A', CURRENT_TIMESTAMP),
  ('i-2', 'p-2', 30, 'Warehouse A', CURRENT_TIMESTAMP),
  ('i-3', 'p-3', 20, 'Warehouse A', CURRENT_TIMESTAMP),
  ('i-4', 'p-4', 18, 'Warehouse A', CURRENT_TIMESTAMP),
  ('i-5', 'p-5', 25, 'Warehouse A', CURRENT_TIMESTAMP),
  ('i-6', 'p-6', 40, 'Warehouse A', CURRENT_TIMESTAMP),
  ('i-7', 'p-7', 12, 'Warehouse B', CURRENT_TIMESTAMP),
  ('i-8', 'p-8', 35, 'Warehouse B', CURRENT_TIMESTAMP),
  ('i-9', 'p-9', 22, 'Warehouse B', CURRENT_TIMESTAMP),
  ('i-10', 'p-10', 10, 'Warehouse B', CURRENT_TIMESTAMP),
  ('i-11', 'p-11', 28, 'Warehouse B', CURRENT_TIMESTAMP),
  ('i-12', 'p-12', 16, 'Warehouse B', CURRENT_TIMESTAMP),
  ('i-13', 'p-13', 24, 'Warehouse C', CURRENT_TIMESTAMP),
  ('i-14', 'p-14', 8, 'Warehouse C', CURRENT_TIMESTAMP),
  ('i-15', 'p-15', 20, 'Warehouse C', CURRENT_TIMESTAMP),
  ('i-16', 'p-16', 14, 'Warehouse C', CURRENT_TIMESTAMP),
  ('i-17', 'p-17', 45, 'Warehouse C', CURRENT_TIMESTAMP),
  ('i-18', 'p-18', 30, 'Warehouse C', CURRENT_TIMESTAMP),
  ('i-19', 'p-19', 18, 'Warehouse D', CURRENT_TIMESTAMP),
  ('i-20', 'p-20', 32, 'Warehouse D', CURRENT_TIMESTAMP),
  ('i-21', 'p-21', 20, 'Warehouse D', CURRENT_TIMESTAMP),
  ('i-22', 'p-22', 50, 'Warehouse D', CURRENT_TIMESTAMP),
  ('i-23', 'p-23', 28, 'Warehouse D', CURRENT_TIMESTAMP),
  ('i-24', 'p-24', 16, 'Warehouse D', CURRENT_TIMESTAMP),
  ('i-25', 'p-25', 22, 'Warehouse E', CURRENT_TIMESTAMP),
  ('i-26', 'p-26', 100, 'Warehouse E', CURRENT_TIMESTAMP),
  ('i-27', 'p-27', 80, 'Warehouse E', CURRENT_TIMESTAMP),
  ('i-28', 'p-28', 75, 'Warehouse E', CURRENT_TIMESTAMP),
  ('i-29', 'p-29', 120, 'Warehouse E', CURRENT_TIMESTAMP),
  ('i-30', 'p-30', 60, 'Warehouse E', CURRENT_TIMESTAMP);

INSERT INTO carts(id, user_id, created_at, updated_at) VALUES
  ('cart-1', 'u-1', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO cart_items(id, cart_id, product_id, quantity, added_at) VALUES
  ('ci-1', 'cart-1', 'p-2', 1, CURRENT_TIMESTAMP),
  ('ci-2', 'cart-1', 'p-17', 1, CURRENT_TIMESTAMP),
  ('ci-3', 'cart-1', 'p-22', 1, CURRENT_TIMESTAMP);

INSERT INTO addresses(id, user_id, recipient_name, postal_code, prefecture, city, address_line1, address_line2, address_type, is_default, created_at) VALUES
  ('addr-1', 'u-1', 'Taro Yamada', '160-0022', 'Tokyo', 'Shinjuku-ku', '1-1-1 Shinjuku', 'Apartment 101', 'Home', true, CURRENT_TIMESTAMP),
  ('addr-2', 'u-1', 'Hanako Yamada', '150-0001', 'Tokyo', 'Shibuya-ku', '1-2-3 Shibuya', 'Building 202', 'Office', false, CURRENT_TIMESTAMP);

INSERT INTO payment_methods(id, user_id, method_type, provider, account_number, expiry_date, is_default, created_at) VALUES
  ('pm-1', 'u-1', 'CREDIT_CARD', 'VISA', '****1234', '2025-12-31', true, CURRENT_TIMESTAMP),
  ('pm-2', 'u-1', 'CREDIT_CARD', 'MASTER', '****5678', '2026-06-30', false, CURRENT_TIMESTAMP);

INSERT INTO orders(id, user_id, order_number, status, total_amount, currency_code, payment_method_id, shipping_address_id, ordered_at, updated_at) VALUES
  ('o-1', 'u-1', 'ORD-2024-0001', 'DELIVERED', 357.50, 'USD', 'pm-1', 'addr-1', '2024-01-15 10:30:00', '2024-01-20 15:45:00'),
  ('o-2', 'u-1', 'ORD-2024-0002', 'SHIPPED', 325.00, 'USD', 'pm-1', 'addr-1', '2024-02-01 14:20:00', '2024-02-03 09:15:00'),
  ('o-3', 'u-1', 'ORD-2024-0003', 'PROCESSING', 487.50, 'USD', 'pm-2', 'addr-2', '2024-02-10 11:00:00', '2024-02-10 11:00:00');

INSERT INTO order_items(id, order_id, product_id, quantity, unit_price, currency_code, subtotal) VALUES
  ('oi-1', 'o-1', 'p-11', 1, 357.50, 'USD', 357.50),
  ('oi-2', 'o-2', 'p-2', 1, 325.00, 'USD', 325.00),
  ('oi-3', 'o-3', 'p-15', 1, 487.50, 'USD', 487.50);

INSERT INTO shipments(id, order_id, tracking_number, carrier, shipped_at, estimated_delivery, actual_delivery, status) VALUES
  ('s-1', 'o-1', 'TRACK-001', 'Yamato Transport', '2024-01-16 09:00:00', '2024-01-18', '2024-01-17', 'DELIVERED'),
  ('s-2', 'o-2', 'TRACK-002', 'Sagawa Express', '2024-02-02 10:30:00', '2024-02-05', NULL, 'IN_TRANSIT');

INSERT INTO reviews(id, product_id, user_id, rating, title, comment, created_at, updated_at) VALUES
  ('r-1', 'p-2', 'u-1', 5, 'Perfect for beginners!', 'Very easy to control. Recommended for first-time buyers.', '2024-01-25 16:30:00', '2024-01-25 16:30:00'),
  ('r-2', 'p-11', 'u-1', 4, 'Good product', 'Great for the price. A bit soft.', '2024-01-22 13:15:00', '2024-01-22 13:15:00');

INSERT INTO returns(id, order_id, order_item_id, reason, status, requested_at, approved_at, refund_amount, currency_code) VALUES
  ('ret-1', 'o-1', 'oi-1', 'Size did not fit', 'APPROVED', '2024-01-19 10:00:00', '2024-01-20 15:00:00', 357.50, 'USD');

INSERT INTO wishlists(id, user_id, product_id, added_at) VALUES
  ('w-1', 'u-1', 'p-1', '2024-01-10 12:00:00'),
  ('w-2', 'u-1', 'p-4', '2024-01-15 18:30:00'),
  ('w-3', 'u-1', 'p-10', '2024-02-01 09:45:00');

INSERT INTO notifications(id, user_id, type, title, message, is_read, created_at) VALUES
  ('n-1', 'u-1', 'ORDER_SHIPPED', 'Your order has been shipped', 'Order ORD-2024-0001 has been shipped.', true, '2024-01-16 09:30:00'),
  ('n-2', 'u-1', 'ORDER_DELIVERED', 'Your order has been delivered', 'Order ORD-2024-0001 has been delivered.', true, '2024-01-17 14:20:00'),
  ('n-3', 'u-1', 'PRICE_DROP', 'Price drop alert', 'Board A in your wishlist is on sale!', false, '2024-02-05 10:00:00');

INSERT INTO product_images(id, product_id, image_url, display_order, is_primary) VALUES
  ('img-1', 'p-1', '/images/products/board-a-1.jpg', 1, true),
  ('img-2', 'p-1', '/images/products/board-a-2.jpg', 2, false),
  ('img-3', 'p-2', '/images/products/board-b-1.jpg', 1, true),
  ('img-4', 'p-3', '/images/products/board-c-1.jpg', 1, true);

INSERT INTO promotions(id, code, description, discount_type, discount_value, min_purchase_amount, currency_code, valid_from, valid_to, max_uses, current_uses) VALUES
  ('promo-1', 'WINTER2024', '10% off winter campaign', 'PERCENTAGE', 10, 325.00, 'USD', '2024-01-01', '2024-02-29', 1000, 45),
  ('promo-2', 'FIRSTBUY', '$32.50 off first purchase', 'FIXED_AMOUNT', 32.50, 325.00, 'USD', '2024-01-01', '2024-12-31', 500, 120);

INSERT INTO promotion_usage(id, promotion_id, user_id, order_id, used_at) VALUES
  ('pu-1', 'promo-1', 'u-1', 'o-1', '2024-01-15 10:30:00');
