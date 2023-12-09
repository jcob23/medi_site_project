CREATE TABLE medisite_user
(
    user_id     SERIAL          NOT NULL,
    user_name   VARCHAR(32)     NOT NULL,
    email       VARCHAR(32)     NOT NULL,
    password    VARCHAR(256)    NOT NULL,
    active      BOOLEAN         NOT NULL,
    PRIMARY KEY (user_id),
    UNIQUE (user_name),
    UNIQUE(email)
);

CREATE TABLE medisite_roles
(
    role_id     SERIAL      NOT NULL,
    role        VARCHAR(20) NOT NULL,
    user_id     INT         NOT NULL,
    PRIMARY KEY (role_id),
    CONSTRAINT fk_role_user
        FOREIGN KEY(user_id)
            REFERENCES medisite_user(user_id)

);