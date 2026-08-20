-- Insert initial members
INSERT INTO members (id, name, last_name, phone_number, status) VALUES (12345678, 'Juan', 'Perez', '1122334455', true);
INSERT INTO members (id, name, last_name, phone_number, status) VALUES (87654321, 'Maria', 'Gomez', '1199887766', true);
INSERT INTO members (id, name, last_name, phone_number, status) VALUES (11223344, 'Carlos', 'Rodriguez', '1155443322', true);
INSERT INTO members (id, name, last_name, phone_number, status) VALUES (30111222, 'Ana', 'Garcia', '3512345678', true);
INSERT INTO members (id, name, last_name, phone_number, status) VALUES (28455901, 'Lucia', 'Fernandez', '3514477120', true);
INSERT INTO members (id, name, last_name, phone_number, status) VALUES (26900333, 'Sofia', 'Benitez', '3516622048', true);
INSERT INTO members (id, name, last_name, phone_number, status) VALUES (29677145, 'Valentina', 'Ruiz', '3513390871', true);
INSERT INTO members (id, name, last_name, phone_number, status) VALUES (31555704, 'Federico', 'Luna', '3518801235', true);
INSERT INTO members (id, name, last_name, phone_number, status) VALUES (31222888, 'Diego', 'Sosa', '3512204466', true);
INSERT INTO members (id, name, last_name, phone_number, status) VALUES (35044719, 'Tomas', 'Alvarez', '3517719023', true);
INSERT INTO members (id, name, last_name, phone_number, status) VALUES (34199827, 'Julieta', 'Moyano', '3515510394', true);
INSERT INTO members (id, name, last_name, phone_number, status) VALUES (33788456, 'Martin', 'Ramirez', '3519934120', false);
INSERT INTO members (id, name, last_name, phone_number, status) VALUES (27833502, 'Carla', 'Dominguez', '3512287655', false);

-- Insert initial memberships (linked to members by ID)
INSERT INTO memberships (id, start_date, expiration_date, price) VALUES (12345678, '2026-01-01', '2026-02-01', 5000.0);
INSERT INTO memberships (id, start_date, expiration_date, price) VALUES (87654321, '2026-02-15', '2026-03-15', 5500.0);
INSERT INTO memberships (id, start_date, expiration_date, price) VALUES (11223344, '2026-03-01', '2026-04-01', 6000.0);
INSERT INTO memberships (id, start_date, expiration_date, price) VALUES (30111222, '2026-08-01', '2026-09-01', 7000.0);
INSERT INTO memberships (id, start_date, expiration_date, price) VALUES (28455901, '2026-08-10', '2026-09-10', 7000.0);
INSERT INTO memberships (id, start_date, expiration_date, price) VALUES (26900333, '2026-08-05', '2026-09-05', 7000.0);
INSERT INTO memberships (id, start_date, expiration_date, price) VALUES (29677145, '2026-08-14', '2026-09-14', 7500.0);
INSERT INTO memberships (id, start_date, expiration_date, price) VALUES (31555704, '2026-08-16', '2026-09-16', 7500.0);
INSERT INTO memberships (id, start_date, expiration_date, price) VALUES (31222888, '2026-05-10', '2026-06-10', 6500.0);
INSERT INTO memberships (id, start_date, expiration_date, price) VALUES (35044719, '2026-06-22', '2026-07-22', 6500.0);
INSERT INTO memberships (id, start_date, expiration_date, price) VALUES (33788456, '2026-07-30', '2026-08-30', 7000.0);
INSERT INTO memberships (id, start_date, expiration_date, price) VALUES (27833502, '2026-02-12', '2026-03-12', 5500.0);

-- Insert initial payments
-- Ordenados por fecha: payments.id es IDENTITY, asi el id sigue la cronologia.
INSERT INTO payments (member_id, amount, payment_date, payment_method) VALUES (30111222, 5000.0, '2025-09-01 10:15:00', 'CASH');
INSERT INTO payments (member_id, amount, payment_date, payment_method) VALUES (30111222, 5000.0, '2025-10-01 10:20:00', 'CASH');
INSERT INTO payments (member_id, amount, payment_date, payment_method) VALUES (30111222, 5000.0, '2025-11-01 09:40:00', 'TRANSFER');
INSERT INTO payments (member_id, amount, payment_date, payment_method) VALUES (30111222, 5000.0, '2025-12-01 18:05:00', 'CASH');
INSERT INTO payments (member_id, amount, payment_date, payment_method) VALUES (12345678, 5000.0, '2026-01-01 10:00:00', 'CASH');
INSERT INTO payments (member_id, amount, payment_date, payment_method) VALUES (30111222, 5500.0, '2026-01-01 11:10:00', 'TRANSFER');
INSERT INTO payments (member_id, amount, payment_date, payment_method) VALUES (30111222, 5500.0, '2026-02-01 09:25:00', 'DEBIT');
INSERT INTO payments (member_id, amount, payment_date, payment_method) VALUES (12345678, 5000.0, '2026-02-01 11:30:00', 'TRANSFER');
INSERT INTO payments (member_id, amount, payment_date, payment_method) VALUES (27833502, 5500.0, '2026-02-12 12:50:00', 'TRANSFER');
INSERT INTO payments (member_id, amount, payment_date, payment_method) VALUES (87654321, 5500.0, '2026-02-15 09:15:00', 'DEBIT');
INSERT INTO payments (member_id, amount, payment_date, payment_method) VALUES (30111222, 5500.0, '2026-03-01 10:05:00', 'CASH');
INSERT INTO payments (member_id, amount, payment_date, payment_method) VALUES (11223344, 6000.0, '2026-03-01 18:45:00', 'CASH');
INSERT INTO payments (member_id, amount, payment_date, payment_method) VALUES (28455901, 5500.0, '2026-03-10 17:30:00', 'DEBIT');
INSERT INTO payments (member_id, amount, payment_date, payment_method) VALUES (30111222, 6500.0, '2026-04-01 09:50:00', 'TRANSFER');
INSERT INTO payments (member_id, amount, payment_date, payment_method) VALUES (31222888, 6500.0, '2026-04-10 17:00:00', 'CASH');
INSERT INTO payments (member_id, amount, payment_date, payment_method) VALUES (28455901, 6500.0, '2026-04-10 18:20:00', 'CASH');
INSERT INTO payments (member_id, amount, payment_date, payment_method) VALUES (30111222, 6500.0, '2026-05-01 10:30:00', 'CASH');
INSERT INTO payments (member_id, amount, payment_date, payment_method) VALUES (31222888, 6500.0, '2026-05-10 17:20:00', 'CASH');
INSERT INTO payments (member_id, amount, payment_date, payment_method) VALUES (28455901, 6500.0, '2026-05-10 19:00:00', 'TRANSFER');
INSERT INTO payments (member_id, amount, payment_date, payment_method) VALUES (35044719, 6500.0, '2026-05-22 16:15:00', 'DEBIT');
INSERT INTO payments (member_id, amount, payment_date, payment_method) VALUES (30111222, 6500.0, '2026-06-01 11:05:00', 'DEBIT');
INSERT INTO payments (member_id, amount, payment_date, payment_method) VALUES (26900333, 6500.0, '2026-06-05 09:45:00', 'TRANSFER');
INSERT INTO payments (member_id, amount, payment_date, payment_method) VALUES (28455901, 6500.0, '2026-06-10 18:40:00', 'DEBIT');
INSERT INTO payments (member_id, amount, payment_date, payment_method) VALUES (35044719, 6500.0, '2026-06-22 16:40:00', 'DEBIT');
INSERT INTO payments (member_id, amount, payment_date, payment_method) VALUES (30111222, 7000.0, '2026-07-01 10:15:00', 'CASH');
INSERT INTO payments (member_id, amount, payment_date, payment_method) VALUES (26900333, 7000.0, '2026-07-05 08:55:00', 'CASH');
INSERT INTO payments (member_id, amount, payment_date, payment_method) VALUES (28455901, 7000.0, '2026-07-10 19:10:00', 'TRANSFER');
INSERT INTO payments (member_id, amount, payment_date, payment_method) VALUES (29677145, 7000.0, '2026-07-14 20:00:00', 'CASH');
INSERT INTO payments (member_id, amount, payment_date, payment_method) VALUES (33788456, 7000.0, '2026-07-30 11:25:00', 'CASH');
INSERT INTO payments (member_id, amount, payment_date, payment_method) VALUES (30111222, 7000.0, '2026-08-01 10:05:00', 'TRANSFER');
INSERT INTO payments (member_id, amount, payment_date, payment_method) VALUES (26900333, 7000.0, '2026-08-05 09:45:00', 'TRANSFER');
INSERT INTO payments (member_id, amount, payment_date, payment_method) VALUES (28455901, 7000.0, '2026-08-10 18:30:00', 'DEBIT');
INSERT INTO payments (member_id, amount, payment_date, payment_method) VALUES (29677145, 7500.0, '2026-08-14 19:20:00', 'CASH');
INSERT INTO payments (member_id, amount, payment_date, payment_method) VALUES (31555704, 7500.0, '2026-08-16 08:10:00', 'TRANSFER');

-- Insert config settings
INSERT INTO config (config_key, config_value) VALUES ('monthly_price', '5000.0');
