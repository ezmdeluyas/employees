-- Users: john (EMPLOYEE), mary (MANAGER), susan (ADMIN)
-- Passwords (for now):
--   john / john123
--   mary / mary123
--   susan / susan123
--
-- BCrypt hashes below. You can change passwords later by updating these hashes in a NEW migration.

INSERT INTO system_users (user_id, password, enabled) VALUES
                                                          ('john',  '$2a$10$zCr3z6qgUN2OAwvw/Z4w8OaDOrMODX8N/rDU7wrUj3.DzydnpxRF6', true),
                                                          ('mary',  '$2a$10$Rd/dxWgLc/PdLtS8TfkYw.ysE28owtOseKl.kqYhxfuO8azQV5Ldq', true),
                                                          ('susan', '$2a$10$ZF9CUFzQilrIYGIaaoXnU.LxrIJKSYP2W8FMIWi81dJPEcqyJ0B9.', true);

-- Roles must be ROLE_* because you are using hasRole("EMPLOYEE"/"MANAGER"/"ADMIN")
INSERT INTO roles (user_id, role) VALUES
                                      ('john',  'ROLE_EMPLOYEE'),
                                      ('mary',  'ROLE_MANAGER'),
                                      ('susan', 'ROLE_ADMIN'),

-- optional: give admin everything
                                      ('susan', 'ROLE_MANAGER'),
                                      ('susan', 'ROLE_EMPLOYEE');
