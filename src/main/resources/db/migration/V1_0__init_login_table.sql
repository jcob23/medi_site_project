CREATE TABLE medisite_role
(
    role_id     SERIAL      NOT NULL,
    role        VARCHAR(20) NOT NULL,
    PRIMARY KEY (role_id)
);

CREATE TABLE medisite_user_token
(
    token_id SERIAL NOT NULL,
    token uuid NOT NULL,
    expiration_time TIMESTAMP NOT NULL,
    PRIMARY KEY (token_id),
    UNIQUE(token),
    UNIQUE(token_id)
);

CREATE TABLE medisite_user
(
    user_id     SERIAL          NOT NULL,
    email       VARCHAR(32)     NOT NULL,
    password    VARCHAR(256)    NOT NULL,
    role_id     INT             NOT NULL,
    token_id    INT,
    PRIMARY KEY (user_id),
    UNIQUE(email),
    CONSTRAINT fk_role_user
           FOREIGN KEY(role_id)
               REFERENCES medisite_role(role_id),
    CONSTRAINT fk_user_token
               FOREIGN KEY(token_id)
                   REFERENCES medisite_user_token(token_id) ON DELETE SET NULL
);


