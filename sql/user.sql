-- auto-generated definition
create table user
(
    id            bigint auto_increment comment '用户id'
        primary key,
    username      varchar(256)                       null comment '用户昵称',
    user_account  varchar(256)                       null comment '用户账户',
    user_password varchar(512)                       null comment '用户密码',
    avatar_url    varchar(1024)                      null comment '用户头像',
    user_role     int      default 0                 null comment '用户权限 0-普通用户 1-管理员',
    is_delete     int      default 0                 null comment '是否删除 0-未删除 1-删除',
    create_time   datetime default CURRENT_TIMESTAMP null comment '创建时间',
    update_time   datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '修改时间'
);

