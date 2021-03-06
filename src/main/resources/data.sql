DROP TABLE IF EXISTS SFI_SAFEBOX;
DROP TABLE IF EXISTS SFI_SAFEBOX_ITEMS;

CREATE TABLE SFI_SAFEBOX (
  SBX_ID INT AUTO_INCREMENT PRIMARY KEY,
  SBX_UUID VARCHAR UNIQUE NOT NULL,
  SBX_NAME VARCHAR(250) NOT NULL,
  SBX_PASSWORD VARCHAR(250) NOT NULL,
  SBX_OPEN_ATTEMPTS NUMBER NOT NULL DEFAULT 0,
  SBX_BLOCKED BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE SFI_SAFEBOX_ITEMS (
  SBC_ITEM_ID INT AUTO_INCREMENT  PRIMARY KEY,
  SBC_SAFEBOX_UUID VARCHAR(250) NOT NULL,
  SBC_ITEM_VALUE VARCHAR(250) NOT NULL
);

CREATE SEQUENCE IF NOT EXISTS SEQ_ID_SAFEBOX
  START WITH 1
  MINVALUE 1
  INCREMENT BY 1;
  
CREATE SEQUENCE IF NOT EXISTS SEQ_ID_SAFEBOX_ITEM
  START WITH 1
  MINVALUE 1
  INCREMENT BY 1;

INSERT INTO SFI_SAFEBOX (SBX_ID, SBX_UUID, SBX_NAME, SBX_PASSWORD, SBX_OPEN_ATTEMPTS, SBX_BLOCKED) 
VALUES (nextval('SEQ_ID_SAFEBOX'), 'c0c9dcc7-1bf3-4c93-bc94-e45002edd7ab', 'Test secure safe box for testing purposes', '$2a$10$HNNvZiGG1TjMDv0O9GCEn..1O0JqtPWa51d1QO/A9GCHAwmxYBCQG', 0, FALSE);

INSERT INTO SFI_SAFEBOX_ITEMS (SBC_ITEM_ID, SBC_SAFEBOX_UUID, SBC_ITEM_VALUE) 
VALUES (nextval('SEQ_ID_SAFEBOX_ITEM'), 'c0c9dcc7-1bf3-4c93-bc94-e45002edd7ab', 'It2xgDcJUixr0kzsuTekZAguhXKEcOg/el340DLRgRaxwp5u3XEglN7ojDk6LXZFD4DYV0A7pA=='); --Item 1 for testing purposes

INSERT INTO SFI_SAFEBOX_ITEMS (SBC_ITEM_ID, SBC_SAFEBOX_UUID, SBC_ITEM_VALUE) 
VALUES (nextval('SEQ_ID_SAFEBOX_ITEM'), 'c0c9dcc7-1bf3-4c93-bc94-e45002edd7ab', 'Ag6M4ZLBTMKJqrPXP8En188HS1ioSQ9peW6gOb1ol2xqkfjcj3e2m9pIxEDn3V4iEorDwPfEOA=='); --Item 2 for testing purposes