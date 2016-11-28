--liquibase formatted sql

--changeset jayant.mukherji:5
alter  table outbound_messages
add payload varchar(2000);
