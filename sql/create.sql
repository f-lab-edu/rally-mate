create table member
(
    id           int unsigned auto_increment  primary key not null,
    name         varchar(50)                    not null,
    email        varchar(100)                   not null,
    password     text                           not null,
    career       smallint unsigned default 0 not null,
    status       varchar(30) default 'ACTIVATE' not null,
    created_at   datetime                       not null default CURRENT_TIMESTAMP,
    updated_at    datetime                       null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
    user_role    varchar(30)                    not null
);

create table rally_place
(
    id                int unsigned auto_increment primary key not null,
    name              varchar(100) not null,
    city              varchar(50)  null comment '시',
    district          varchar(50)  null comment '구',
    road_name_address varchar(200) null,
    member_id         int unsigned not null,
    created_at   datetime                       not null default CURRENT_TIMESTAMP,
    updated_at    datetime                       null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
    constraint fk_member_rally_place
        foreign key (member_id) references member (id)
);

create table rally_schedule
(
    id            int unsigned auto_increment primary key not null,
    member_id     int unsigned null,
    rally_place_id int unsigned null,
    play_time     smallint     not null,
    start_time    datetime     not null,
    created_at   datetime                       not null default CURRENT_TIMESTAMP,
    updated_at    datetime                       null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
    constraint fk_member_rally_schedule
        foreign key (member_id) references member(id),
    constraint fk_rally_place_rally_schedule
        foreign key (rally_place_id) references rally_place(id)
);

create table applicant
(
    id            int unsigned auto_increment primary key not null,
    member_id     int unsigned null,
    rally_schedule_id       int unsigned null,
    status        varchar(30) default 'REQUESTED' not null,
    created_at   datetime                       not null default CURRENT_TIMESTAMP,
    updated_at    datetime                       null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
    constraint fk_member_applicant
        foreign key (member_id) references member(id),
    constraint fk_rally_schedule_applicant
        foreign key (rally_schedule_id) references rally_schedule (id)
);
