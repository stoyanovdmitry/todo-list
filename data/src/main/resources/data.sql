insert into users (username, password) values ('user', 'password'), ('admin', 'password');
insert into todo (user_id, text, completed) values
  (1, 'todo1', false), (1, 'todo2', false), (1, 'todo3', true), (1, 'todo4', true), (1, 'todo5', true),
  (2, 'todo1', false), (2, 'todo2', false), (2, 'todo3', true), (2, 'todo4', true), (2, 'todo5', true);