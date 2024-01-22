

use atm_db;

INSERT INTO atms (city) VALUES
    ('CityA'),
    ('CityB'),
    ('CityC'),
    ('CityD'),
    ('CityE');
    
    
INSERT INTO credentials (pin, account_number) VALUES
    ('1234', '1234567890'),
    ('5678', '0987654321'),
    ('4321', '9876543210'),
    ('8765', '0123456789'),
    ('1111', '1111222233');
    
    
INSERT INTO users (name, atm_id, credential_id) VALUES
    ('User1', 1, 1),
    ('User2', 2, 2),
    ('User3', 3, 3),
    ('User4', 4, 4),
    ('User5', 5, 5);


INSERT INTO accounts (balance, type, interest_rate, user_id) VALUES
    (1000.00, 'Checking', 0, 1),
    (5000.00, 'Saving', 0.2, 2),
    (200.00, 'Checking', 0, 3),
    (8000.00, 'Saving', 0.3, 4),
    (1500.00, 'Checking', 0, 5);
    
    
INSERT INTO transaction_details (amount, pre_balance, post_balance, transfer_to, type) VALUES
    (100.00, 1000.00, 900.00, NULL, 'Withdrawal'),
    (500.00, 5000.00, 5500.00, NULL, 'Deposit'),
    (50.00, 200.00, 150.00, NULL, 'Withdrawal'),
    (1000.00, 8000.00, 7000.00, '9876543210', 'Transfer'),
    (200.00, 1500.00, 1300.00, NULL, 'Withdrawal');


INSERT INTO transactions (date, user_id, transaction_detail_id) VALUES
    ('2024-01-19 10:00:00', 1, 1),
    ('2024-01-19 11:30:00', 2, 2),
    ('2024-01-19 12:45:00', 3, 3),
    ('2024-01-19 14:15:00', 4, 4),
    ('2024-01-19 15:30:00', 5, 5);
