DROP TABLE IF EXISTS URL;
 
CREATE SEQUENCE IF NOT EXISTS URL_SEQ START WITH 1 INCREMENT BY 1;

CREATE TABLE URL (
  ID IDENTITY PRIMARY KEY NOT NULL,
  ORIGINAL_URL VARCHAR(255) NOT NULL,
  SHORT_URL VARCHAR(255),
  EXPIRES_AT TIMESTAMP NOT NULL
);
 
INSERT INTO URL(ID, ORIGINAL_URL, SHORT_URL, EXPIRES_AT) VALUES 
    (URL_SEQ.NEXTVAL, 'http://original.com', 'XpTo', CURRENT_TIMESTAMP);