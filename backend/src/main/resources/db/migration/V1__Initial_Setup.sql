CREATE TABLE customer(
    id BIGSERIAL PRIMARY KEY,
    name TEXT NOT NULL,
    password TEXT NOT NULL,
    email TEXT NOT NULL UNIQUE ,
    age INT NOT NULL,
    gender TEXT NOT NULL CHECK ( UPPER(gender) IN ('MALE', 'FEMALE') )
);