/* Inserer the roles in table role*/
insert into role (id, is_enabled, version, created_by, created_at, updated_by, updated_at, name) values (1 , 1 , 0 , 'ROOT', CURRENT_TIMESTAMP, 'ROOT', CURRENT_TIMESTAMP, 'ADMIN');
insert into role (id, is_enabled, version, created_by, created_at, updated_by, updated_at, name) values (2 , 1, 0 , 'ROOT', CURRENT_TIMESTAMP, 'ROOT', CURRENT_TIMESTAMP, 'USER');

/* Inserer the user in table user*/
insert into user(id, is_enabled, version, created_by, created_at, updated_by, updated_at, first_name, last_name, email, password, is_activated_account)
 values (1, 1, 0, 'ROOT', CURRENT_TIMESTAMP, 'ROOT', CURRENT_TIMESTAMP , 'ayoub', 'boublil', 'ayoub@gmail.com','$2a$10$9Z5FCExtQvylFZInargLoOOycJLzGKSqmyFMT4jYT0oUAKUOz8B7G', true);

/* Affect the roles to user*/
insert into user_roles (user_id, role_id) values (1, 1);
insert into user_roles (user_id, role_id) values (1, 2);
