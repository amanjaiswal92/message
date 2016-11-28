--liquibase formatted sql

--changeset jayant.mukherji:1

ALTER table clients
ADD bulk_count integer default 1;

ALTER table clients
ADD status varchar(100);

ALTER table clients
ADD consumer_type varchar(100);
