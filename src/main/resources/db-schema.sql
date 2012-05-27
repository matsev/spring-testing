DROP TABLE account_t IF EXISTS;

CREATE TABLE account_t (
  account_number BIGINT NOT NULL IDENTITY,
  balance BIGINT NOT NULL CHECK (balance >= 0)
);