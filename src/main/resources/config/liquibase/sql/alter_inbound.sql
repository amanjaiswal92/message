--liquibase formatted sql

--changeset jayant.mukherji:4
alter  table inbound_messages
add created_at datetime default NOW();

alter table inbound_messages rename      to outbound_messages;
