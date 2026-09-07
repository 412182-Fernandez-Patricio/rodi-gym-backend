-- Datos de prueba. Las fechas son relativas al arranque (CURRENT_DATE), no
-- absolutas: si no, envejecen y las pantallas que filtran por hoy o por el
-- mes en curso aparecen vacias a las pocas semanas.

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
INSERT INTO memberships (id, start_date, expiration_date, price) VALUES (27833502, DATEADD('DAY', -200, CURRENT_DATE), DATEADD('DAY', -170, CURRENT_DATE), 5500.0);
INSERT INTO memberships (id, start_date, expiration_date, price) VALUES (11223344, DATEADD('DAY', -160, CURRENT_DATE), DATEADD('DAY', -130, CURRENT_DATE), 6000.0);
INSERT INTO memberships (id, start_date, expiration_date, price) VALUES (12345678, DATEADD('DAY', -125, CURRENT_DATE), DATEADD('DAY', -95, CURRENT_DATE), 5000.0);
INSERT INTO memberships (id, start_date, expiration_date, price) VALUES (87654321, DATEADD('DAY', -90, CURRENT_DATE), DATEADD('DAY', -60, CURRENT_DATE), 5500.0);
INSERT INTO memberships (id, start_date, expiration_date, price) VALUES (35044719, DATEADD('DAY', -78, CURRENT_DATE), DATEADD('DAY', -48, CURRENT_DATE), 6500.0);
INSERT INTO memberships (id, start_date, expiration_date, price) VALUES (31222888, DATEADD('DAY', -50, CURRENT_DATE), DATEADD('DAY', -20, CURRENT_DATE), 6500.0);
INSERT INTO memberships (id, start_date, expiration_date, price) VALUES (26900333, DATEADD('DAY', -21, CURRENT_DATE), DATEADD('DAY', 9, CURRENT_DATE), 7000.0);
INSERT INTO memberships (id, start_date, expiration_date, price) VALUES (33788456, DATEADD('DAY', -20, CURRENT_DATE), DATEADD('DAY', 10, CURRENT_DATE), 7000.0);
INSERT INTO memberships (id, start_date, expiration_date, price) VALUES (28455901, DATEADD('DAY', -16, CURRENT_DATE), DATEADD('DAY', 14, CURRENT_DATE), 7000.0);
INSERT INTO memberships (id, start_date, expiration_date, price) VALUES (29677145, DATEADD('DAY', -12, CURRENT_DATE), DATEADD('DAY', 18, CURRENT_DATE), 7500.0);
INSERT INTO memberships (id, start_date, expiration_date, price) VALUES (30111222, DATEADD('DAY', -6, CURRENT_DATE), DATEADD('DAY', 24, CURRENT_DATE), 7000.0);
INSERT INTO memberships (id, start_date, expiration_date, price) VALUES (31555704, DATEADD('DAY', -4, CURRENT_DATE), DATEADD('DAY', 26, CURRENT_DATE), 7500.0);

-- Insert initial payments
-- Ordenados por fecha: payments.id es IDENTITY, asi el id sigue la cronologia.
INSERT INTO payments (member_id, amount, payment_date, payment_method) VALUES (30111222, 5000.0, DATEADD('MINUTE', 632, DATEADD('DAY', -336, CURRENT_DATE)), 'DEBIT');
INSERT INTO payments (member_id, amount, payment_date, payment_method) VALUES (30111222, 5000.0, DATEADD('MINUTE', 625, DATEADD('DAY', -306, CURRENT_DATE)), 'TRANSFER');
INSERT INTO payments (member_id, amount, payment_date, payment_method) VALUES (30111222, 5000.0, DATEADD('MINUTE', 618, DATEADD('DAY', -276, CURRENT_DATE)), 'CASH');
INSERT INTO payments (member_id, amount, payment_date, payment_method) VALUES (30111222, 5000.0, DATEADD('MINUTE', 611, DATEADD('DAY', -246, CURRENT_DATE)), 'DEBIT');
INSERT INTO payments (member_id, amount, payment_date, payment_method) VALUES (30111222, 5000.0, DATEADD('MINUTE', 654, DATEADD('DAY', -216, CURRENT_DATE)), 'TRANSFER');
INSERT INTO payments (member_id, amount, payment_date, payment_method) VALUES (27833502, 5500.0, DATEADD('MINUTE', 605, DATEADD('DAY', -200, CURRENT_DATE)), 'CASH');
INSERT INTO payments (member_id, amount, payment_date, payment_method) VALUES (30111222, 5500.0, DATEADD('MINUTE', 647, DATEADD('DAY', -186, CURRENT_DATE)), 'CASH');
INSERT INTO payments (member_id, amount, payment_date, payment_method) VALUES (28455901, 5500.0, DATEADD('MINUTE', 640, DATEADD('DAY', -166, CURRENT_DATE)), 'CASH');
INSERT INTO payments (member_id, amount, payment_date, payment_method) VALUES (11223344, 6000.0, DATEADD('MINUTE', 605, DATEADD('DAY', -160, CURRENT_DATE)), 'DEBIT');
INSERT INTO payments (member_id, amount, payment_date, payment_method) VALUES (30111222, 5500.0, DATEADD('MINUTE', 640, DATEADD('DAY', -156, CURRENT_DATE)), 'DEBIT');
INSERT INTO payments (member_id, amount, payment_date, payment_method) VALUES (12345678, 5000.0, DATEADD('MINUTE', 612, DATEADD('DAY', -155, CURRENT_DATE)), 'TRANSFER');
INSERT INTO payments (member_id, amount, payment_date, payment_method) VALUES (28455901, 6000.0, DATEADD('MINUTE', 633, DATEADD('DAY', -136, CURRENT_DATE)), 'DEBIT');
INSERT INTO payments (member_id, amount, payment_date, payment_method) VALUES (30111222, 6000.0, DATEADD('MINUTE', 633, DATEADD('DAY', -126, CURRENT_DATE)), 'TRANSFER');
INSERT INTO payments (member_id, amount, payment_date, payment_method) VALUES (12345678, 5000.0, DATEADD('MINUTE', 605, DATEADD('DAY', -125, CURRENT_DATE)), 'CASH');
INSERT INTO payments (member_id, amount, payment_date, payment_method) VALUES (35044719, 6000.0, DATEADD('MINUTE', 612, DATEADD('DAY', -108, CURRENT_DATE)), 'TRANSFER');
INSERT INTO payments (member_id, amount, payment_date, payment_method) VALUES (28455901, 6000.0, DATEADD('MINUTE', 626, DATEADD('DAY', -106, CURRENT_DATE)), 'TRANSFER');
INSERT INTO payments (member_id, amount, payment_date, payment_method) VALUES (30111222, 6000.0, DATEADD('MINUTE', 626, DATEADD('DAY', -96, CURRENT_DATE)), 'CASH');
INSERT INTO payments (member_id, amount, payment_date, payment_method) VALUES (87654321, 5500.0, DATEADD('MINUTE', 605, DATEADD('DAY', -90, CURRENT_DATE)), 'CASH');
INSERT INTO payments (member_id, amount, payment_date, payment_method) VALUES (26900333, 6500.0, DATEADD('MINUTE', 619, DATEADD('DAY', -81, CURRENT_DATE)), 'TRANSFER');
INSERT INTO payments (member_id, amount, payment_date, payment_method) VALUES (31222888, 6000.0, DATEADD('MINUTE', 612, DATEADD('DAY', -80, CURRENT_DATE)), 'DEBIT');
INSERT INTO payments (member_id, amount, payment_date, payment_method) VALUES (35044719, 6500.0, DATEADD('MINUTE', 605, DATEADD('DAY', -78, CURRENT_DATE)), 'CASH');
INSERT INTO payments (member_id, amount, payment_date, payment_method) VALUES (28455901, 6500.0, DATEADD('MINUTE', 619, DATEADD('DAY', -76, CURRENT_DATE)), 'CASH');
INSERT INTO payments (member_id, amount, payment_date, payment_method) VALUES (30111222, 6500.0, DATEADD('MINUTE', 619, DATEADD('DAY', -66, CURRENT_DATE)), 'DEBIT');
INSERT INTO payments (member_id, amount, payment_date, payment_method) VALUES (26900333, 6500.0, DATEADD('MINUTE', 612, DATEADD('DAY', -51, CURRENT_DATE)), 'CASH');
INSERT INTO payments (member_id, amount, payment_date, payment_method) VALUES (31222888, 6500.0, DATEADD('MINUTE', 605, DATEADD('DAY', -50, CURRENT_DATE)), 'TRANSFER');
INSERT INTO payments (member_id, amount, payment_date, payment_method) VALUES (28455901, 6500.0, DATEADD('MINUTE', 612, DATEADD('DAY', -46, CURRENT_DATE)), 'DEBIT');
INSERT INTO payments (member_id, amount, payment_date, payment_method) VALUES (29677145, 7000.0, DATEADD('MINUTE', 612, DATEADD('DAY', -42, CURRENT_DATE)), 'CASH');
INSERT INTO payments (member_id, amount, payment_date, payment_method) VALUES (30111222, 6500.0, DATEADD('MINUTE', 612, DATEADD('DAY', -36, CURRENT_DATE)), 'TRANSFER');
INSERT INTO payments (member_id, amount, payment_date, payment_method) VALUES (26900333, 7000.0, DATEADD('MINUTE', 605, DATEADD('DAY', -21, CURRENT_DATE)), 'DEBIT');
INSERT INTO payments (member_id, amount, payment_date, payment_method) VALUES (33788456, 7000.0, DATEADD('MINUTE', 605, DATEADD('DAY', -20, CURRENT_DATE)), 'DEBIT');
INSERT INTO payments (member_id, amount, payment_date, payment_method) VALUES (28455901, 7000.0, DATEADD('MINUTE', 605, DATEADD('DAY', -16, CURRENT_DATE)), 'TRANSFER');
INSERT INTO payments (member_id, amount, payment_date, payment_method) VALUES (29677145, 7500.0, DATEADD('MINUTE', 605, DATEADD('DAY', -12, CURRENT_DATE)), 'DEBIT');
INSERT INTO payments (member_id, amount, payment_date, payment_method) VALUES (30111222, 7000.0, DATEADD('MINUTE', 605, DATEADD('DAY', -6, CURRENT_DATE)), 'CASH');
INSERT INTO payments (member_id, amount, payment_date, payment_method) VALUES (31555704, 7500.0, DATEADD('MINUTE', 605, DATEADD('DAY', -4, CURRENT_DATE)), 'CASH');

-- Insert initial check-ins
-- Ordenados por fecha: check_ins.id es IDENTITY, asi el id sigue la cronologia.
INSERT INTO check_ins (member_id, checkin_time, success, reason, message) VALUES (30111222, DATEADD('MINUTE', 1115, DATEADD('DAY', -42, CURRENT_DATE)), true, 'ACCESS_GRANTED', 'Access granted');
INSERT INTO check_ins (member_id, checkin_time, success, reason, message) VALUES (30111222, DATEADD('MINUTE', 1100, DATEADD('DAY', -38, CURRENT_DATE)), true, 'ACCESS_GRANTED', 'Access granted');
INSERT INTO check_ins (member_id, checkin_time, success, reason, message) VALUES (30111222, DATEADD('MINUTE', 1120, DATEADD('DAY', -33, CURRENT_DATE)), true, 'ACCESS_GRANTED', 'Access granted');
INSERT INTO check_ins (member_id, checkin_time, success, reason, message) VALUES (30111222, DATEADD('MINUTE', 1110, DATEADD('DAY', -28, CURRENT_DATE)), true, 'ACCESS_GRANTED', 'Access granted');
INSERT INTO check_ins (member_id, checkin_time, success, reason, message) VALUES (28455901, DATEADD('MINUTE', 1130, DATEADD('DAY', -24, CURRENT_DATE)), true, 'ACCESS_GRANTED', 'Access granted');
INSERT INTO check_ins (member_id, checkin_time, success, reason, message) VALUES (30111222, DATEADD('MINUTE', 1100, DATEADD('DAY', -21, CURRENT_DATE)), true, 'ACCESS_GRANTED', 'Access granted');
INSERT INTO check_ins (member_id, checkin_time, success, reason, message) VALUES (29677145, DATEADD('MINUTE', 1140, DATEADD('DAY', -19, CURRENT_DATE)), true, 'ACCESS_GRANTED', 'Access granted');
INSERT INTO check_ins (member_id, checkin_time, success, reason, message) VALUES (30111222, DATEADD('MINUTE', 1115, DATEADD('DAY', -16, CURRENT_DATE)), true, 'ACCESS_GRANTED', 'Access granted');
INSERT INTO check_ins (member_id, checkin_time, success, reason, message) VALUES (28455901, DATEADD('MINUTE', 1105, DATEADD('DAY', -14, CURRENT_DATE)), true, 'ACCESS_GRANTED', 'Access granted');
INSERT INTO check_ins (member_id, checkin_time, success, reason, message) VALUES (30111222, DATEADD('MINUTE', 1125, DATEADD('DAY', -12, CURRENT_DATE)), true, 'ACCESS_GRANTED', 'Access granted');
INSERT INTO check_ins (member_id, checkin_time, success, reason, message) VALUES (26900333, DATEADD('MINUTE', 1155, DATEADD('DAY', -12, CURRENT_DATE)), true, 'ACCESS_GRANTED', 'Access granted');
INSERT INTO check_ins (member_id, checkin_time, success, reason, message) VALUES (30111222, DATEADD('MINUTE', 1090, DATEADD('DAY', -9, CURRENT_DATE)), true, 'ACCESS_GRANTED', 'Access granted');
INSERT INTO check_ins (member_id, checkin_time, success, reason, message) VALUES (28455901, DATEADD('MINUTE', 1110, DATEADD('DAY', -7, CURRENT_DATE)), true, 'ACCESS_GRANTED', 'Access granted');
INSERT INTO check_ins (member_id, checkin_time, success, reason, message) VALUES (30111222, DATEADD('MINUTE', 552, DATEADD('DAY', -6, CURRENT_DATE)), false, 'MEMBERSHIP_EXPIRED', 'Membership expired or not found');
INSERT INTO check_ins (member_id, checkin_time, success, reason, message) VALUES (30111222, DATEADD('MINUTE', 620, DATEADD('DAY', -6, CURRENT_DATE)), true, 'ACCESS_GRANTED', 'Access granted');
INSERT INTO check_ins (member_id, checkin_time, success, reason, message) VALUES (30111222, DATEADD('MINUTE', 1095, DATEADD('DAY', -5, CURRENT_DATE)), true, 'ACCESS_GRANTED', 'Access granted');
INSERT INTO check_ins (member_id, checkin_time, success, reason, message) VALUES (26900333, DATEADD('MINUTE', 1165, DATEADD('DAY', -5, CURRENT_DATE)), true, 'ACCESS_GRANTED', 'Access granted');
INSERT INTO check_ins (member_id, checkin_time, success, reason, message) VALUES (31555704, DATEADD('MINUTE', 470, DATEADD('DAY', -3, CURRENT_DATE)), true, 'ACCESS_GRANTED', 'Access granted');
INSERT INTO check_ins (member_id, checkin_time, success, reason, message) VALUES (28455901, DATEADD('MINUTE', 1120, DATEADD('DAY', -3, CURRENT_DATE)), true, 'ACCESS_GRANTED', 'Access granted');
INSERT INTO check_ins (member_id, checkin_time, success, reason, message) VALUES (30111222, DATEADD('MINUTE', 1100, DATEADD('DAY', -2, CURRENT_DATE)), true, 'ACCESS_GRANTED', 'Access granted');
INSERT INTO check_ins (member_id, checkin_time, success, reason, message) VALUES (29677145, DATEADD('MINUTE', 1150, DATEADD('DAY', -2, CURRENT_DATE)), true, 'ACCESS_GRANTED', 'Access granted');
INSERT INTO check_ins (member_id, checkin_time, success, reason, message) VALUES (30111222, DATEADD('MINUTE', 480, DATEADD('DAY', -1, CURRENT_DATE)), true, 'ACCESS_GRANTED', 'Access granted');
INSERT INTO check_ins (member_id, checkin_time, success, reason, message) VALUES (28455901, DATEADD('MINUTE', 1110, DATEADD('DAY', -1, CURRENT_DATE)), true, 'ACCESS_GRANTED', 'Access granted');
INSERT INTO check_ins (member_id, checkin_time, success, reason, message) VALUES (26900333, DATEADD('MINUTE', 1140, DATEADD('DAY', -1, CURRENT_DATE)), true, 'ACCESS_GRANTED', 'Access granted');
INSERT INTO check_ins (member_id, checkin_time, success, reason, message) VALUES (31555704, DATEADD('MINUTE', 425, DATEADD('DAY', 0, CURRENT_DATE)), true, 'ACCESS_GRANTED', 'Access granted');
INSERT INTO check_ins (member_id, checkin_time, success, reason, message) VALUES (28455901, DATEADD('MINUTE', 460, DATEADD('DAY', 0, CURRENT_DATE)), true, 'ACCESS_GRANTED', 'Access granted');
INSERT INTO check_ins (member_id, checkin_time, success, reason, message) VALUES (31222888, DATEADD('MINUTE', 495, DATEADD('DAY', 0, CURRENT_DATE)), false, 'MEMBERSHIP_EXPIRED', 'Membership expired or not found');
INSERT INTO check_ins (member_id, checkin_time, success, reason, message) VALUES (30111222, DATEADD('MINUTE', 530, DATEADD('DAY', 0, CURRENT_DATE)), true, 'ACCESS_GRANTED', 'Access granted');
INSERT INTO check_ins (member_id, checkin_time, success, reason, message) VALUES (26900333, DATEADD('MINUTE', 570, DATEADD('DAY', 0, CURRENT_DATE)), true, 'ACCESS_GRANTED', 'Access granted');
INSERT INTO check_ins (member_id, checkin_time, success, reason, message) VALUES (33788456, DATEADD('MINUTE', 610, DATEADD('DAY', 0, CURRENT_DATE)), false, 'MEMBER_INACTIVE', 'Member is not active');
INSERT INTO check_ins (member_id, checkin_time, success, reason, message) VALUES (29677145, DATEADD('MINUTE', 685, DATEADD('DAY', 0, CURRENT_DATE)), true, 'ACCESS_GRANTED', 'Access granted');
INSERT INTO check_ins (member_id, checkin_time, success, reason, message) VALUES (34199827, DATEADD('MINUTE', 760, DATEADD('DAY', 0, CURRENT_DATE)), false, 'MEMBERSHIP_EXPIRED', 'Membership expired or not found');

-- Insert config settings
INSERT INTO config (config_key, config_value) VALUES ('monthly_price', '7000.0');
