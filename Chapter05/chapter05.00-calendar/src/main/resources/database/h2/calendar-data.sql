-- Calendar Users
insert into calendar_users(id, email, password, first_name, last_name)
values (0, 'user1@example.com', '{noop}user1', 'User', '1');
insert into calendar_users(id, email, password, first_name, last_name)
values (1, 'admin1@example.com', '{noop}admin1', 'Admin', '1');
insert into calendar_users(id, email, password, first_name, last_name)
values (2, 'user2@example.com', '{noop}user2', 'User', '2');

-- Calendar Events
insert into events (id, dateWhen, summary, description, owner, attendee)
values (100, '2023-07-03 20:30:00', 'Birthday Party',
        'This is going to be a great birthday', 0, 1);
insert into events (id, dateWhen, summary, description, owner, attendee)
values (101, '2023-12-23 13:00:00', 'Conference Call', 'Call with the client', 2, 0);
insert into events (id, dateWhen, summary, description, owner, attendee)
values (102, '2018-01-23 11:30:00', 'Lunch', 'Eating lunch together', 1, 2);
