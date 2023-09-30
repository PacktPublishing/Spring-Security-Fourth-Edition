create table users
(
    username varchar(256) not null primary key,
    password varchar(256) not null,
    enabled  boolean      not null
);

create table authorities
(
    username  varchar(256) not null,
    authority varchar(256) not null,
    constraint fk_authorities_users foreign key (username) references users (username)
);
create unique index ix_auth_username on authorities (username, authority);
