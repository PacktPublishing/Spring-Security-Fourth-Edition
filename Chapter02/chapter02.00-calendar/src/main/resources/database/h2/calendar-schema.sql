create table calendar_users
(
    id         INT AUTO_INCREMENT(1001, 1) PRIMARY KEY,
    email      varchar(256) not null unique,
    password   varchar(256) not null,
    first_name varchar(256) not null,
    last_name  varchar(256) not null
);

create table events
(
    id IDENTITY NOT NULL PRIMARY KEY,
    dateWhen    timestamp    not null,
    summary     varchar(256) not null,
    description varchar(500) not null,
    owner       bigint       not null,
    attendee    bigint       not null,
    FOREIGN KEY (owner) REFERENCES calendar_users (id),
    FOREIGN KEY (attendee) REFERENCES calendar_users (id)
);