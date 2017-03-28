
# USER table taken from https://github.com/jamesward/play-rest-security

# --- !Ups

CREATE TABLE swiping_session (
    id                  bigint AUTO_INCREMENT NOT NULL,
    initiator_email     varchar(256) NOT NULL,
    buddy_email         varchar(256) NOT NULL,
    initialization_date timestamp NOT NULL,
    CONSTRAINT pk_swiping_session PRIMARY KEY (id)
);

CREATE TABLE user (
    id              bigint auto_increment NOT NULL,
    auth_token      varchar(256),
    email_address   varchar(256) NOT NULL,
    sha_password    varbinary(64) NOT NULL,
    full_name       varchar(256) NOT NULL,
    creation_date   timestamp NOT NULL,
    CONSTRAINT uq_user_email_address UNIQUE (email_address),
    CONSTRAINT pk_user PRIMARY KEY (id)
);

ALTER TABLE swiping_session ADD CONSTRAINT fk_swiping_session_emails FOREIGN KEY (initiator_email, buddy_email) REFERENCES user(email_address, email_address) ON DELETE RESTRICT ON UPDATE RESTRICT;
CREATE INDEX ix_swiping_session_emails ON swiping_session (initiator_email, buddy_email);

# --- !Downs

ALTER TABLE swiping_session DROP CONSTRAINT IF EXISTS fk_swiping_session_emails;
DROP INDEX IF EXISTS ix_swiping_session_emails;

DROP TABLE IF EXISTS swiping_session;

DROP TABLE IF EXISTS user;
