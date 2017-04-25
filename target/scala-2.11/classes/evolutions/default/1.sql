# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table activity (
  place_id 						bigint auto_increment not null,
  name                          varchar(255) not null,
  formatted_address				varchar(255) not null,
  constraint uq_activity_name unique (name),
  constraint pk_activity primary key (place_id)
);

create table activity_choice (
  id                            bigint auto_increment not null,
  user_id                       bigint,
  swiping_session_id            bigint,
  constraint uq_activity_choice_user_id_swiping_session_id unique (user_id,swiping_session_id),
  constraint pk_activity_choice primary key (id)
);

create table activity_choice_activity (
  activity_choice_id            bigint not null,
  activity_id                   bigint not null,
  constraint pk_activity_choice_activity primary key (activity_choice_id,activity_id)
);

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
  initialization_date           datetime not null,
  constraint pk_swiping_session primary key (id)
);

create table swiping_session_user (
  swiping_session_id            bigint not null,
  user_id                       bigint not null,
  constraint pk_swiping_session_user primary key (swiping_session_id,user_id)
);

create table swiping_session_activity (
  swiping_session_id            bigint not null,
  activity_id                   bigint not null,
  constraint pk_swiping_session_activity primary key (swiping_session_id,activity_id)
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
  authorization                 integer,
  constraint ck_user_authorization check (authorization in (0,1)),
  constraint uq_user_email_address unique (email_address),
  constraint uq_user_facebook_data_id unique (facebook_data_id),
  constraint pk_user primary key (id)
);

alter table activity_choice add constraint fk_activity_choice_user_id foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_activity_choice_user_id on activity_choice (user_id);

alter table activity_choice add constraint fk_activity_choice_swiping_session_id foreign key (swiping_session_id) references swiping_session (id) on delete restrict on update restrict;
create index ix_activity_choice_swiping_session_id on activity_choice (swiping_session_id);

alter table activity_choice_activity add constraint fk_activity_choice_activity_activity_choice foreign key (activity_choice_id) references activity_choice (id) on delete restrict on update restrict;
create index ix_activity_choice_activity_activity_choice on activity_choice_activity (activity_choice_id);

alter table activity_choice_activity add constraint fk_activity_choice_activity_activity foreign key (activity_id) references activity (id) on delete restrict on update restrict;
create index ix_activity_choice_activity_activity on activity_choice_activity (activity_id);

alter table facebook_data add constraint fk_facebook_data_user_id foreign key (user_id) references user (id) on delete restrict on update restrict;

alter table swiping_session_user add constraint fk_swiping_session_user_swiping_session foreign key (swiping_session_id) references swiping_session (id) on delete restrict on update restrict;
create index ix_swiping_session_user_swiping_session on swiping_session_user (swiping_session_id);

alter table swiping_session_user add constraint fk_swiping_session_user_user foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_swiping_session_user_user on swiping_session_user (user_id);

alter table swiping_session_activity add constraint fk_swiping_session_activity_swiping_session foreign key (swiping_session_id) references swiping_session (id) on delete restrict on update restrict;
create index ix_swiping_session_activity_swiping_session on swiping_session_activity (swiping_session_id);

alter table swiping_session_activity add constraint fk_swiping_session_activity_activity foreign key (activity_id) references activity (id) on delete restrict on update restrict;
create index ix_swiping_session_activity_activity on swiping_session_activity (activity_id);

alter table user add constraint fk_user_facebook_data_id foreign key (facebook_data_id) references facebook_data (id) on delete restrict on update restrict;


# --- !Downs

alter table activity_choice drop constraint if exists fk_activity_choice_user_id;
drop index if exists ix_activity_choice_user_id;

alter table activity_choice drop constraint if exists fk_activity_choice_swiping_session_id;
drop index if exists ix_activity_choice_swiping_session_id;

alter table activity_choice_activity drop constraint if exists fk_activity_choice_activity_activity_choice;
drop index if exists ix_activity_choice_activity_activity_choice;

alter table activity_choice_activity drop constraint if exists fk_activity_choice_activity_activity;
drop index if exists ix_activity_choice_activity_activity;

alter table facebook_data drop constraint if exists fk_facebook_data_user_id;

alter table swiping_session_user drop constraint if exists fk_swiping_session_user_swiping_session;
drop index if exists ix_swiping_session_user_swiping_session;

alter table swiping_session_user drop constraint if exists fk_swiping_session_user_user;
drop index if exists ix_swiping_session_user_user;

alter table swiping_session_activity drop constraint if exists fk_swiping_session_activity_swiping_session;
drop index if exists ix_swiping_session_activity_swiping_session;

alter table swiping_session_activity drop constraint if exists fk_swiping_session_activity_activity;
drop index if exists ix_swiping_session_activity_activity;

alter table user drop constraint if exists fk_user_facebook_data_id;

drop table if exists activity;

drop table if exists activity_choice;

drop table if exists activity_choice_activity;

drop table if exists facebook_data;

drop table if exists swiping_session;

drop table if exists swiping_session_user;

drop table if exists swiping_session_activity;

drop table if exists user;

