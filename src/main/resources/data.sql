-- Insert initial members
INSERT INTO members (id, name, last_name, phone_number, status) VALUES (12345678, 'Juan', 'Perez', '1122334455', true);
INSERT INTO members (id, name, last_name, phone_number, status) VALUES (87654321, 'Maria', 'Gomez', '1199887766', true);
INSERT INTO members (id, name, last_name, phone_number, status) VALUES (11223344, 'Carlos', 'Rodriguez', '1155443322', true);

-- Insert initial memberships (linked to members by ID)
INSERT INTO memberships (id, start_date, expiration_date, price) VALUES (12345678, '2026-01-01', '2026-02-01', 5000.0);
INSERT INTO memberships (id, start_date, expiration_date, price) VALUES (87654321, '2026-02-15', '2026-03-15', 5500.0);
INSERT INTO memberships (id, start_date, expiration_date, price) VALUES (11223344, '2026-03-01', '2026-04-01', 6000.0);

-- Insert initial payments
INSERT INTO payments (member_id, amount, payment_date, payment_method) VALUES (12345678, 5000.0, '2026-01-01 10:00:00', 'CASH');
INSERT INTO payments (member_id, amount, payment_date, payment_method) VALUES (12345678, 5000.0, '2026-02-01 11:30:00', 'TRANSFER');
INSERT INTO payments (member_id, amount, payment_date, payment_method) VALUES (87654321, 5500.0, '2026-02-15 09:15:00', 'DEBIT');
INSERT INTO payments (member_id, amount, payment_date, payment_method) VALUES (11223344, 6000.0, '2026-03-01 18:45:00', 'CASH');

-- Insert config settings
INSERT INTO config (config_key, config_value) VALUES ('monthly_price', '5000.0');
