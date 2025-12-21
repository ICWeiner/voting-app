CREATE TYPE user_role AS ENUM (
    'USER',
    'ADMIN'
);
CREATE DOMAIN vote_choice AS ENUM (
    'V0',
    'V200',
    'V500',
    'V1000',
    'V3000',
    'V10000',
    'V50000'
);
CREATE DOMAIN studies_type AS ENUM (
    'NONE',
    'PRIMARY',
    'SECONDARY',
    'BACHELOR',
    'MASTER',
    'PHD'
);