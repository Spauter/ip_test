create database ip_test;

use ip_test;

drop table if exists request_entity;

create table request_entity
(
    id               int auto_increment comment '设备id'
        primary key,
    rejected_request int         null comment '被拒绝次数',
    operating_system varchar(25) null comment '操作系统名称',
    browser          varchar(25) null comment '浏览器名称',
    ip_address       varchar(25) null comment 'ipv4地址',
    total_request    int         null comment '总请求次数',
    status           int         null comment '状态'
);

drop table if exists current_request_entity;

create table current_request_entity
(
    id          int         not null,
    fid         int auto_increment
        primary key,
    ip          varchar(25) not null,
    total       int         not null,
    reject      int         not null,
    update_time datetime    null,
    constraint current_request_entity_request_entity_id_fk
        foreign key (id) references request_entity (id)
);
