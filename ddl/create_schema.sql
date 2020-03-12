-- create the challenge schema
create schema if not exists shariz_challenge;

-- table stores signup requests.
CREATE TABLE if not exists shariz_challenge.signup_request
(
    request_id serial PRIMARY KEY,
    first_name VARCHAR(50)                                        NOT NULL,
    last_name  VARCHAR(50)                                        NOT NULL,
    email      VARCHAR(50) UNIQUE                                 NOT NULL, -- emails are enforced to be unique .
    company    VARCHAR(100)                                       NOT NULL,

    dt_created TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL
);

