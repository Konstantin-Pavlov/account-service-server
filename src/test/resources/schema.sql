CREATE TABLE ACCOUNTS
(
    id           SERIAL PRIMARY KEY,
    balance      NUMERIC(20, 2),
    last_updated TIMESTAMP
);