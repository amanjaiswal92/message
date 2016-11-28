--liquibase formatted sql

--changeset jayant.mukherji:3

ALTER table clients
ADD grouping_enabled boolean default true;
