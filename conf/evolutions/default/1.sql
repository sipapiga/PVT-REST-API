# --- !Ups

create table user (
    id              bigint auto_increment not null,
    auth_token      varchar(256),
    email_address   varchar(256) not null,
    sha_password    varbinary(64) not null,
    full_name       varchar(256) not null,
    creation_date     timestamp not null,
    constraint uq_user_email_address unique (email_address),
    constraint pk_user primary key (id)
);

# --- !Downs

drop table if exists user;
