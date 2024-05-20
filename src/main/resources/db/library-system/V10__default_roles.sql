INSERT INTO adm_role (role_id, name, description)
VALUES (5000, 'Bibliotecario', 'Encargado de manejar la aplicación de la biblioteca'),
(5001, 'Estudiante', 'Persona que puede hacer prestamos en la biblioteca');

INSERT INTO adm_user_role (user_role_id, role_id, user_id) VALUES (5000, 5000, 5000)
