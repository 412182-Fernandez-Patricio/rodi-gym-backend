-- Insert initial members
INSERT INTO members (id, name, last_name, phone_number) VALUES (12345678, 'Juan', 'Perez', '1122334455');
INSERT INTO members (id, name, last_name, phone_number) VALUES (87654321, 'Maria', 'Gomez', '1199887766');
INSERT INTO members (id, name, last_name, phone_number) VALUES (11223344, 'Carlos', 'Rodriguez', '1155443322');

-- Insert initial memberships (linked to members by ID)
INSERT INTO memberships (id, start_date, expiration_date, price) VALUES (12345678, '2026-01-01', '2026-02-01', 5000.0);
INSERT INTO memberships (id, start_date, expiration_date, price) VALUES (87654321, '2026-02-15', '2026-03-15', 5500.0);
INSERT INTO memberships (id, start_date, expiration_date, price) VALUES (11223344, '2026-03-01', '2026-04-01', 6000.0);
