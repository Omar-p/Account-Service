create sequence app_user_id_seq start with 1 increment by 1;
create sequence permission_id_seq start with 1 increment by 1;
create sequence role_id_seq start with 1 increment by 1;
create sequence security_user_id_seq start with 1 increment by 1;


create table app_user (
                          id bigint not null,
                          email varchar(255),
                          lastname varchar(255),
                          name varchar(255),
                          primary key (id)
);

create table permission (
                            id bigint not null,
                            name varchar(255),
                            primary key (id)
);

create table role (
                      id bigint not null,
                      group_name varchar(255),
                      name varchar(255),
                      primary key (id)
);

create table role_permission (
                                 role_id bigint not null,
                                 permission_id bigint not null,
                                 primary key (role_id, permission_id)
);

create table security_user (
                               id bigint not null,
                               enabled boolean not null,
                               password varchar(255),
                               app_user_id bigint,
                               primary key (id)
);

create table user_role (
                           user_id bigint not null,
                           role_id bigint not null,
                           primary key (user_id, role_id)
);

alter table if exists app_user
    add constraint app_user_email_key unique (email);

alter table if exists permission
    add constraint permission_name_key unique ("name");

alter table if exists role
    add constraint role_name_key unique ("name");

alter table if exists role_permission
    add constraint role_permission_permission_id_fkey
    foreign key (permission_id)
    references permission;

alter table if exists role_permission
    add constraint role_permission_role_id_fkey
    foreign key (role_id)
    references role;

alter table if exists security_user
    add constraint security_user_app_user_id_fkey
    foreign key (app_user_id)
    references app_user;

alter table if exists user_role
    add constraint user_role_role_id_fkey
    foreign key (role_id)
    references role;

alter table if exists user_role
    add constraint user_role_user_id_fkey
    foreign key (user_id)
    references security_user;