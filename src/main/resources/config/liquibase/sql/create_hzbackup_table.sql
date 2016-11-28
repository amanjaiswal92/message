--liquibase formatted sql

--changeset jayant.mukherji:2

create table hzbackup
(group_name varchar(200),
queue_name varchar(200),
client_name varchar(200),
primary key (group_name)
);
