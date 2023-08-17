create table member
(
    id           int unsigned auto_increment comment 'auto_increment'
        primary key,
    name         varchar(50)                    not null,
    email        varchar(100)                   not null,
    password     text                           not null,
    status       varchar(30) default 'ACTIVATE' not null,
    user_role    varchar(30)                    not null,
    career      smallint unsigned default 0     not null,
    created_time datetime default CURRENT_TIMESTAMP not null,
    updated_time datetime ON UPDATE CURRENT_TIMESTAMP
);

create table rally_place
(
    id                int unsigned auto_increment
        primary key,
    name              varchar(100) not null,
    city              varchar(50)  null comment '시',
    district          varchar(50)  null comment '구',
    road_name_address varchar(200) null,
    member_id         int unsigned not null,
    created_time datetime default CURRENT_TIMESTAMP not null,
    updated_time datetime ON UPDATE CURRENT_TIMESTAMP,
    constraint fk_member_rally_place
        foreign key (member_id) references member (id)
);

create table rally_schedule
(
    id            int unsigned auto_increment
        primary key,
    member_id     int unsigned null,
    rally_place_id int unsigned null,
    play_time     smallint     not null,
    start_time    datetime     not null,
    created_time datetime default CURRENT_TIMESTAMP not null,
    updated_time datetime ON UPDATE CURRENT_TIMESTAMP,
    constraint fk_member_rally_schedule
        foreign key (member_id) references member (id),
    constraint fk_rally_place_rally_schedule
        foreign key (rally_place_id) references rally_place (id)
);

