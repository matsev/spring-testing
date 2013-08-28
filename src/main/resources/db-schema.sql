DROP TABLE IF EXISTS account_t;

CREATE TABLE account_t (
  account_number BIGINT NOT NULL AUTO_INCREMENT,
  balance BIGINT NOT NULL,
  PRIMARY KEY (account_number)
)
