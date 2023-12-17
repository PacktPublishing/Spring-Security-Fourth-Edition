
insert into calendar_users(id,email,first_name,last_name) values (0, 'user1@example.com','User','1');
insert into calendar_users(id,email,first_name,last_name) values (1,'calendarjbcp@gmail.com','Admin','1');

-- ROLES --
insert into role(id, name) values (0, 'ROLE_USER');
insert into role(id, name) values (1, 'ROLE_ADMIN');


-- User have one role
insert into user_role(user_id,role_id) values (0, 0);

-- Admin has two roles
insert into user_role(user_id,role_id) values (1, 0);
insert into user_role(user_id,role_id) values (1, 1);

-- Events --
insert into events (id,date_when,summary,description,owner,attendee) values (100,'2023-07-03 20:30:00','Birthday Party','This is going to be a great birthday',0,1);
insert into events (id,date_when,summary,description,owner,attendee) values (101,'2023-12-23 13:00:00','Conference Call','Call with the client',2,0);
insert into events (id,date_when,summary,description,owner,attendee) values (102,'2023-09-14 11:30:00','Vacation','Paragliding in Greece',1,2);

