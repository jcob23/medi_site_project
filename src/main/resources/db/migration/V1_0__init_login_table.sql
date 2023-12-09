CREATE TABLE medisite_role
(
    role_id     SERIAL      NOT NULL,
    role        VARCHAR(20) NOT NULL,
    PRIMARY KEY (role_id)
);

CREATE TABLE medisite_user
(
    user_id     SERIAL          NOT NULL,
    email       VARCHAR(32)     NOT NULL,
    password    VARCHAR(256)    NOT NULL,
    role_id     INT             NOT NULL,
    PRIMARY KEY (user_id),
    UNIQUE(email),
    CONSTRAINT fk_role_user
           FOREIGN KEY(role_id)
               REFERENCES medisite_role(role_id)
);

