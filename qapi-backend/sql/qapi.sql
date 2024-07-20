-- 创建库
create database if not exists qapi_db;

-- 切换库
use qapi_db;

-- 创建表

-- 角色表
drop table if exists `user_role`;
create table if not exists `user_role`
(
    `id`    bigint auto_increment comment 'id' primary key comment 'id',
    `role`  varchar(256) not null comment '角色',
    `name`  varchar(256) not null comment '名称',
    `desc`  text         null comment '描述',
    `value` int          not null comment '角色权限值：越小越高'
) comment '用户角色表' collate = utf8mb4_unicode_ci;
-- 等级表
drop table if exists `user_level`;
create table if not exists `user_level`
(
    `id`    bigint auto_increment comment 'id' primary key comment 'id',
    `level` varchar(256) not null comment '等级',
    `name`  varchar(256) not null comment '名称',
    `desc`  text         null comment '描述',
    `value` int          not null comment '角色等级值：越小越高'
) comment '用户等级表' collate = utf8mb4_unicode_ci;
-- 用户表
drop table if exists user;
create table if not exists user
(
    `id`          bigint auto_increment comment 'id' primary key comment 'id',
    `username`    varchar(256) unique                    not null comment '账号',
    `password`    varchar(256)                           not null comment '密码',
    `email`       varchar(256)                           null comment '邮箱',
    `nickname`    varchar(256)                           null comment '昵称',
    `avatar`      varchar(512)                           null comment '头像',
    `gender`      tinyint      default 0                 null comment '性别：0-未知，1-男，2-女',
    `birthday`    date                                   null comment '生日',
    `phone`       varchar(128)                           null comment '手机号',
    `address`     varchar(512)                           null comment '地址',
    `role`        varchar(256) default 'user'            not null comment '角色权限',
    `level`       varchar(256) default 'v1'              not null comment '角色等级',
    `access_key`  varchar(256)                           not null comment 'accessKey',
    `secret_key`  varchar(256)                           not null comment 'secretKey',
    `is_banned`   tinyint      default 0                 not null comment '是否封禁：0-未封禁，1-封禁',
    `is_delete`   tinyint      default 0                 not null comment '是否注销：0-未注销，1-注销',
    `create_time` datetime     default current_timestamp not null comment '创建时间',
    `update_time` datetime     default current_timestamp not null on update current_timestamp comment '更新时间',
    index idx_id (id)
) comment '用户表' collate = utf8mb4_unicode_ci;
-- 接口信息表
drop table if exists `interface_info`;
create table if not exists `interface_info`
(
    `id`              bigint auto_increment              not null primary key comment 'id',
    `name`            varchar(256)                       not null comment '名称',
    `url`             varchar(512)                       not null comment '地址',
    `method`          varchar(16)                        not null comment '请求方法',
    `request_params`  text                               null comment '请求参数',
    `request_header`  text                               null comment '请求头',
    `response_header` text                               null comment '响应头',
    `mock_params`     text                               null comment '模拟参数',
    `description`     varchar(256)                       null comment '描述',
    `call_count`      int      default 0                 not null comment '调用次数',
    `status`          tinyint  default 0                 not null comment '状态：0-未启用，1-启用',
    `user_id`         bigint                             not null comment '创建人id',
    `create_time`     datetime default current_timestamp not null comment '创建时间',
    `update_time`     datetime default current_timestamp not null on update current_timestamp comment '更新时间',
    `is_delete`       tinyint  default 0                 not null comment '是否删除：0-未删除，1-删除',
    index idx_id (id),
    constraint fk_interface_info_user_id foreign key (user_id) references user (id)
) comment '接口信息表' collate = utf8mb4_unicode_ci;
-- 用户接口表
drop table if exists `user_interface_info`;
create table if not exists `user_interface_info`
(
    `id`                bigint auto_increment              not null primary key comment 'id',
    `userId`            bigint                             not null comment '用户id',
    `interface_info_id` bigint                             not null comment '接口id',
    `total_num`         int      default 0                 null comment '总调用次数',
    `remain_num`        int      default 0                 null comment '剩余调用次数',
    `status`            tinyint  default 0                 not null comment '状态：0-正常，1-禁用',
    `create_time`       datetime default current_timestamp not null comment '创建时间',
    `update_time`       datetime default current_timestamp not null on update current_timestamp comment '更新时间',
    `is_delete`         tinyint  default 0                 not null comment '是否删除：0-未删除，1-删除',
    index idx_id (id),
    index idx_user_id (userId),
    index idx_interface_info_id (interface_info_id),
    constraint fk_user_interface_info_user_id foreign key (userId) references user (id)
) comment '用户接口表' collate = utf8mb4_unicode_ci;
-- 插入表

insert into user_role (role, name, value)
values ('admin', '管理员', 0);
insert into user_role (role, name, value)
values ('svip', 'SVIP', 10);
insert into user_role (role, name, value)
values ('vip', 'VIP', 20);
insert into user_role (role, name, value)
values ('user', '用户', 30);

insert into user_level (level, name, value)
values ('v1', '新手', 100);
insert into user_level (level, name, value)
values ('v2', '初级', 80);
insert into user_level (level, name, value)
values ('v3', '中级', 60);
insert into user_level (level, name, value)
values ('v4', '高级', 40);
insert into user_level (level, name, value)
values ('v5', '超级', 20);
insert into user_level (level, name, value)
values ('v6', '至尊', 10);
insert into user_level (level, name, value)
values ('v7', '神级', 5);

