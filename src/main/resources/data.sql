INSERT INTO role (id, name, group_name) VALUES ((SELECT nextval('role_id_seq')), 'ROLE_USER', 'BUSINESS');
INSERT INTO role (id, name, group_name) VALUES ((SELECT nextval('role_id_seq')), 'ROLE_ADMINISTRATOR', 'ADMINISTRATOR');
INSERT INTO role (id, name, group_name) VALUES ((SELECT nextval('role_id_seq')), 'ROLE_ACCOUNTANT', 'BUSINESS');
