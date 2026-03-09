# INSERT INTO security_profile(name, description) VALUES ('Super Administrador',
#                                                         'Perfil de seguridad con acceso total a todas las funcionalidades del sistema');
#
# INSERT INTO endpoint(id, description, route, permission_type) VALUES (1, 'Todos los endpoints', '.*', 1),
#                                                                      (2, 'Endpoint para consultar de usuarios', '/secure/api/v1/users/**', 1),
#                                                                      (3, 'Endpoint para cambios de usuarios', '/secure/api/v1/users/**', 2),
#                                                                      (4, 'Endpoint para consultar de perfiles', '/secure/api/v1/security-profiles/**', 1),
#                                                                      (5, 'Endpoint para cambios de perfiles', '/secure/api/v1/security-profiles/**', 2),
#                                                                      (6, 'Endpoint para consultar de vistas', '/secure/api/v1/views/**', 1),
#                                                                      (7, 'Endpoint para cambios de vistas', '/secure/api/v1/views/**', 2),
#                                                                      (8, 'Endpoint para consultar de endpoints', '/secure/api/v1/endpoints/**', 1),
#                                                                      (9, 'Endpoint para cambios de endpoints', '/secure/api/v1/endpoints/**', 2);
# INSERT INTO view(id, description, route) VALUES (1, 'Todas las vistas de la aplicación', '.*');
#
# INSERT INTO view_authorization(id, security_profile_id, view_id) VALUES (1, 1, 1);
# INSERT INTO authorized_endpoint(id, security_profile_id, endpoint_id) VALUES (1, 1, 1);
#
#
# INSERT INTO user (username, password, enabled, account_locked, account_expired, credentials_expired) VALUES ('admin', '$2a$10$X0yjNlL5/Ef2zAv0ePgCIuQYgBLPnKAB8UO2rLFIYYlsLumEWnesC', true, false, false,
#                                                                                                              false);
#
# INSERT INTO user_security_profile(user_id, security_profile_id) VALUES (1, 1);
#
# INSERT INTO employee(id, user_id, first_name, last_name, email, address, document_number,mobile_phone, birth_date, country, gender, identification_type, employee_type) VALUES (1, 1, 'Raul', 'Alfonso Diaz', 'test@gmail.com', 'Lima Peru Mz b4 lt 3', '72700916','913249837', '1990-01-01', 0, 0, 0, 0);
# INSERT INTO seccontrol.employee (birth_date, country, employee_type, gender, identification_type, id, user_id, address, document_number, email, first_name, last_name, mobile_phone) VALUES ('2003-10-12', 0, 3, 0, 0, 2, 1, null, '73627849', 'cesar6201@gmail.com', 'Cesar', 'Calopino', null);

# INSERT INTO seccontrol.turn_template (num_guards, time_from, time_to, turn_type, id, name) VALUES (4, '08:00:00', '18:00:00', 0, 1, 'Turno Mañana - Tarde');
# INSERT INTO seccontrol.turn_template (num_guards, time_from, time_to, turn_type, id, name) VALUES (2, '19:32:00', '05:00:00', 1, 2, 'Turno Noche - Madrugada');
#
# INSERT INTO seccontrol.client (active, created_at, id, updated_at, code, description, direction, name) VALUES (true, '2026-02-21 15:34:52.405248', 1, '2026-02-21 15:35:07.152200', 'SAMSUNG-0001', 'test', 'test', 'Samsungs Electronics');
#
# INSERT INTO seccontrol.unity (id, active, client_id, code, created_at, description, direction, latitude, longitude, name, range_coverage, updated_at) VALUES (1, true, 1, 'SAMSUNG-N', '2026-02-21 15:37:22.252070', 'test', 'test', -12.2384, -77.102938, 'SAMSUNG NORTE', 100, '2026-02-21 15:41:14.881134');
# INSERT INTO seccontrol.unity (id, active, client_id, code, created_at, description, direction, latitude, longitude, name, range_coverage, updated_at) VALUES (2, true, 1, 'SAMSUNG-S', '2026-02-22 01:06:39.612399', 'test', 'test', -13.4385, -23.230948, 'SAMSUNG Sur', 50, '2026-02-22 01:06:39.612399');
#
# INSERT INTO seccontrol.client_contract (id, active, client_id, created_at, description, name, updated_at) VALUES (1, true, 1, '2026-02-21 21:38:54.492183', 'test', 'Contrato 2026', '2026-02-21 21:38:54.492183');
#
# INSERT INTO seccontrol.contract_unity (id, unity_id, client_contract_id) VALUES (3, 2, 1);
#
# INSERT INTO seccontrol.contract_schedule_unit_template (day_of_week, num_of_guards, num_turn_day, num_turn_night, contract_unity_id, id) VALUES (0, 2, 0, 2, 3, 25);
# INSERT INTO seccontrol.contract_schedule_unit_template (day_of_week, num_of_guards, num_turn_day, num_turn_night, contract_unity_id, id) VALUES (6, 2, 0, 2, 3, 26);
#
# INSERT INTO seccontrol.turn_and_hour (contract_schedule_unit_template_id, id, turn_template_id) VALUES (25, 17, 2);
# INSERT INTO seccontrol.turn_and_hour (contract_schedule_unit_template_id, id, turn_template_id) VALUES (26, 18, 2);
#
# INSERT INTO seccontrol.guard (employee_id, id, license_number, photo_url, guard_type) VALUES (1, 1, 'LIC-2384280', null, 0);
# INSERT INTO seccontrol.guard (guard_type, employee_id, id, license_number, photo_url) VALUES (3, 2, 2, 'LIC4384280', null);
