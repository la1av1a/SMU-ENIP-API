create table users
(
    users_id      bigint auto_increment
        primary key,
    login_id      varchar(255) null,
    email         varchar(255) null,
    score         varchar(255) null,
    created_date  datetime     null,
    modified_date datetime     null
);

create table users_auth
(
    id            bigint auto_increment
        primary key,
    user_id       bigint                            null,
    provider      enum ('KAKAO', 'GOOGLE', 'LOCAL') null,
    provider_id   varchar(255)                      null,
    password      varchar(255)                      null,
    phone_number  varchar(11)                       null,
    created_date  datetime                          null,
    modified_date datetime                          null
);


create table role
(
    role_id bigint auto_increment
        primary key,
    name    varchar(255) not null
);

create table user_role
(
    user_id bigint not null
        primary key,
    role_id bigint not null
);
