DROP TABLE IF EXISTS account_t;

CREATE TABLE account_t (
  account_number INT NOT NULL AUTO_INCREMENT,
  balance INT NOT NULL,
  PRIMARY KEY (account_number),
  CHECK (balance >= 0)
);