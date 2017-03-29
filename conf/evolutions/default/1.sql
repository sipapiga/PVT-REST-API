# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table swiping_session (
  id                            bigint auto_increment not null,
  initiator_email               varchar(256) not null,
  buddy_email                   varchar(256) not null,
  initialization_date           timestamp not null,
  constraint uq_swiping_session_initiator_email unique (initiator_email),
  constraint uq_swiping_session_buddy_email unique (buddy_email),
  constraint pk_swiping_session primary key (id)
);

create table user (
  id                            bigint auto_increment not null,
  auth_token                    varchar(255),
  email_address                 varchar(256) not null,
  sha_password                  varbinary(64) not null,
  full_name                     varchar(256) not null,
  creation_date                 timestamp not null,
  constraint uq_user_email_address unique (email_address),
  constraint pk_user primary key (id)
);


# --- !Downs

drop table if exists swiping_session;

drop table if exists user;

