
# Modified from https://github.com/jamesward/play-rest-security

# --- !Ups

create table swiping_session (
    id                  bigint auto_increment not null,
    initiator_email     varchar(256) not null,
    buddy_email         varchar(256) not null,
    initialization_date timestamp not null,
    constraint pk_swiping_session primary key (id)
);

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

alter table swiping_session add constraint fk_swiping_session_emails foreign key (initiator_email, buddy_email) references user(email_address, email_address) on delete restrict on update restrict;
create index ix_swiping_session_emails on swiping_session (initiator_email, buddy_email);

# --- !Downs

alter table swiping_session drop constraint if exists fk_swiping_session_emails;
drop index if exists ix_swiping_session_emails;

drop table if exists swiping_session;

drop table if exists user;
