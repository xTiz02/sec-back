INSERT INTO security_profile(name, description)
VALUES ('Super Administrador',
        'Perfil de seguridad con acceso total a todas las funcionalidades del sistema');

INSERT INTO endpoint(id, description, route, permission_type)
VALUES (1, 'Todos los endpoints', '.*', 1),
       (2, 'Endpoint para consultar de usuarios', '/secure/api/v1/users/**', 1),
       (3, 'Endpoint para cambios de usuarios', '/secure/api/v1/users/**', 2),
       (4, 'Endpoint para consultar de perfiles', '/secure/api/v1/security-profiles/**', 1),
       (5, 'Endpoint para cambios de perfiles', '/secure/api/v1/security-profiles/**', 2),
       (6, 'Endpoint para consultar de vistas', '/secure/api/v1/views/**', 1),
       (7, 'Endpoint para cambios de vistas', '/secure/api/v1/views/**', 2),
       (8, 'Endpoint para consultar de endpoints', '/secure/api/v1/endpoints/**', 1),
       (9, 'Endpoint para cambios de endpoints', '/secure/api/v1/endpoints/**', 2);
INSERT INTO view(id, description, route)
VALUES (1, 'Todas las vistas de la aplicación', '.*');

INSERT INTO view_authorization(id, security_profile_id, view_id)
VALUES (1, 1, 1);
INSERT INTO authorized_endpoint(id, security_profile_id, endpoint_id)
VALUES (1, 1, 1);


INSERT INTO user (username, password, enabled, account_locked, account_expired, credentials_expired)
VALUES ('admin', '$2a$10$X0yjNlL5/Ef2zAv0ePgCIuQYgBLPnKAB8UO2rLFIYYlsLumEWnesC', true, false, false,
        false);

INSERT INTO user_security_profile(user_id, security_profile_id)
VALUES (1, 1);

INSERT INTO employee(id, user_id, first_name, last_name, email, address, document_number,
                     mobile_phone, birth_date, country, gender, identification_type, employee_type)
VALUES (1, 1, 'Raul', 'Alfonso Diaz', 'test@gmail.com', 'Lima Peru Mz b4 lt 3', '72700916',
        '913249837', '1990-01-01', 0, 0, 0, 0);







