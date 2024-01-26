create database if not exists atm_db;
use atm_db;

CREATE TABLE IF NOT EXISTS atms(
	id SERIAL,
    city VARCHAR(45) NOT NULL,
	PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS credentials (
	id SERIAL,
    pin VARCHAR(10) NOT NULL,
    account_number VARCHAR(50) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS users (
	id SERIAL,
    name VARCHAR(50) NOT NULL,
    PRIMARY KEY (id),
    atm_id BIGINT unsigned,
    credential_id BIGINT UNSIGNED,
    
    CONSTRAINT fk_user_atm FOREIGN KEY (atm_id) REFERENCES 
    atms (id) ON DELETE CASCADE ON UPDATE NO ACTION,
    
    CONSTRAINT fk_user_credential FOREIGN KEY (credential_id) REFERENCES 
    credentials (id) ON DELETE CASCADE ON UPDATE NO ACTION
);

CREATE TABLE IF NOT EXISTS accounts (
	id SERIAL,
    balance DOUBLE NOT NULL,
    type ENUM("Checking", "Saving"),
    interest_rate DOUBLE,
    PRIMARY KEY (id),
    user_id BIGINT unsigned,
    
    CONSTRAINT fk_account_user FOREIGN KEY (user_id) REFERENCES 
    users (id) ON DELETE CASCADE ON UPDATE NO ACTION
);


CREATE TABLE IF NOT EXISTS transactions (
	id SERIAL,
    date TIMESTAMP,
    PRIMARY KEY (id),
    amount DOUBLE,
    type ENUM("WithDrawal", "Deposit", "Transfer"),
    user_id BIGINT unsigned,

    CONSTRAINT fk_transaction_user FOREIGN KEY (user_id) REFERENCES 
    users (id) ON DELETE CASCADE ON UPDATE NO ACTION,

);

