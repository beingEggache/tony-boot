#!/bin/sh
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
    CREATE USER tony_api superuser password 'tony_api';
    CREATE DATABASE tony_api;
    GRANT ALL PRIVILEGES ON DATABASE tony_api TO tony_api;
EOSQL