INSERT INTO users (id, username, email, password)
VALUES
(
    1,
    'awarren',
    'andrew@example.com',
    '$2a$10$dXJ3SW6G7P50lGmMkkmweuJQ7Qv1mV7FQ5hM2F4L5QH7P6WwQJ5hG'
),
(
    2,
    'jsmith',
    'john@example.com',
    '$2a$10$dXJ3SW6G7P50lGmMkkmweuJQ7Qv1mV7FQ5hM2F4L5QH7P6WwQJ5hG'
),
(
    3,
    'mbrown',
    'mary@example.com',
    '$2a$10$dXJ3SW6G7P50lGmMkkmweuJQ7Qv1mV7FQ5hM2F4L5QH7P6WwQJ5hG'
);

-- passwords are all password123 in plain


INSERT INTO retirement_goals
(id, user_id, name, target_retirement_age, target_amount, notes)
VALUES
(
    1,
    1,
    'Early Retirement',
    55,
    1500000.00,
    'Aggressive investing with max 401(k) contributions.'
),
(
    2,
    1,
    'Traditional Retirement',
    65,
    900000.00,
    'Retire at the standard retirement age.'
),
(
    3,
    2,
    'Beach House Retirement',
    60,
    2200000.00,
    'Includes purchasing a beach home after retirement.'
),
(
    4,
    2,
    'Travel the World',
    58,
    1800000.00,
    'Retire early and travel internationally.'
),
(
    5,
    3,
    'Financial Independence',
    50,
    3000000.00,
    'Focus on FIRE strategy with high annual savings.'
),
(
    6,
    3,
    'Comfortable Retirement',
    67,
    1200000.00,
    'Balanced investment portfolio and moderate risk.'
);

