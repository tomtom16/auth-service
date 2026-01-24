CREATE TABLE USERS (
            ID                             UUID NOT NULL PRIMARY KEY,
            CREATION_TIMESTAMP             TIMESTAMP,
            MODIFICATION_TIMESTAMP         TIMESTAMP,
            USERNAME                       VARCHAR(50),
            PASSWORD                       VARCHAR(512)
);

CREATE TABLE REFRESH_TOKEN (
            ID                             UUID NOT NULL PRIMARY KEY,
            CREATION_TIMESTAMP             TIMESTAMP,
            MODIFICATION_TIMESTAMP         TIMESTAMP,
            TOKEN_HASH                     VARCHAR(512),
            EXPIRY_DATE                    TIMESTAMP,
            USER_ID                        UUID NOT NULL,
    FOREIGN KEY (USER_ID) REFERENCES USERS(ID)
);