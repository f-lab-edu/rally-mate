create table member
(
    id           int unsigned auto_increment comment 'auto_increment'
        primary key,
    name         varchar(50)                    not null,
    email        varchar(100)                   not null,
    password     text                           not null,
    status       varchar(30) default 'ACTIVATE' not null,
    created_time timestamp                      not null,
    user_role    varchar(30)                    not null
);

create table playground
(
    id                int unsigned auto_increment
        primary key,
    name              varchar(100) not null,
    city              varchar(50)  null comment '시',
    district          varchar(50)  null comment '구',
    road_name_address varchar(200) null,
    created_time      timestamp    not null,
    member_id         int unsigned not null,
    constraint fk_member_playground
        foreign key (member_id) references member (id)
);

create table post
(
    id            int unsigned auto_increment
        primary key,
    member_id     int unsigned null,
    playground_id int unsigned null,
    play_time     smallint     not null,
    start_time    datetime     not null,
    created_time  timestamp    not null,
    constraint fk_member_post
        foreign key (member_id) references member (id),
    constraint fk_playground_post
        foreign key (playground_id) references playground (id)
);

