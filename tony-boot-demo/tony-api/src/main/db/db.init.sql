drop user IF EXISTS tony_api;
create user tony_api password 'sacd21g#BGdfshj234@%4';

create database tony_api owner tony_api;
grant all on DATABASE tony_api to tony_api;
