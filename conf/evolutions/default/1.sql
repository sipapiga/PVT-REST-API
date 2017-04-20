# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table facebook_data (
  id                            bigint auto_increment not null,
  facebook_user_id              varchar(255) not null,
  email_address                 varchar(255) not null,
  first_name                    varchar(256),
  last_name                     varchar(256),
  gender                        varchar(15),
  locale                        varchar(10),
  timezone                      integer,
  user_id                       bigint,
  constraint uq_facebook_data_facebook_user_id unique (facebook_user_id),
  constraint uq_facebook_data_email_address unique (email_address),
  constraint uq_facebook_data_user_id unique (user_id),
  constraint pk_facebook_data primary key (id)
);

create table swiping_session (
  id                            bigint auto_increment not null,
  initiator_id                  bigint,
  buddy_id                      bigint,
  initialization_date           datetime not null,
  constraint pk_swiping_session primary key (id)
);

create table user (
  id                            bigint auto_increment not null,
  auth_token                    varchar(255),
  email_address                 varchar(255) not null,
  sha_password                  varbinary(64),
  sha_facebook_auth_token       varbinary(255),
  full_name                     varchar(256) not null,
  creation_date                 datetime not null,
  facebook_data_id              bigint,
  constraint uq_user_email_address unique (email_address),
  constraint uq_user_facebook_data_id unique (facebook_data_id),
  constraint pk_user primary key (id)
);

alter table facebook_data add constraint fk_facebook_data_user_id foreign key (user_id) references user (id) on delete restrict on update restrict;

alter table swiping_session add constraint fk_swiping_session_initiator_id foreign key (initiator_id) references user (id) on delete restrict on update restrict;
create index ix_swiping_session_initiator_id on swiping_session (initiator_id);

alter table swiping_session add constraint fk_swiping_session_buddy_id foreign key (buddy_id) references user (id) on delete restrict on update restrict;
create index ix_swiping_session_buddy_id on swiping_session (buddy_id);

alter table user add constraint fk_user_facebook_data_id foreign key (facebook_data_id) references facebook_data (id) on delete restrict on update restrict;


# --- !Downs

alter table facebook_data drop constraint if exists fk_facebook_data_user_id;

alter table swiping_session drop constraint if exists fk_swiping_session_initiator_id;
drop index if exists ix_swiping_session_initiator_id;

alter table swiping_session drop constraint if exists fk_swiping_session_buddy_id;
drop index if exists ix_swiping_session_buddy_id;

alter table user drop constraint if exists fk_user_facebook_data_id;

drop table if exists facebook_data;

drop table if exists swiping_session;

drop table if exists user;

