create table if not exists  `user_interface_info`
(
    `id` bigint not null primary key auto_increment comment  '主键',
    `userId` bigint not null comment '调用用户 id',
    `interfaceInfoId` bigint not null comment '接口id',
    `totalNum` int default 0 not null comment '总调用次数',
    `leftNum` int default 0 not null comment '剩余调用次数',
    `status` int default 0 not null comment '0-正常,1-禁用',
    `createTime` datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    `updateTime` datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    `isDelete` tinyint default 0 not null comment '是否删除(0-未删除,1-已删除)'
);

INSERT INTO `user_interface_info` (`userId`, `interfaceInfoId`, `totalNum`, `leftNum`, `status`, `createTime`, `updateTime`, `isDelete`)
VALUES
    (1, 1001, 100, 90, 0, NOW(), NOW(), 0),
    (2, 1002, 50, 45, 0, NOW(), NOW(), 0),
    (3, 1003, 200, 185, 0, NOW(), NOW(), 0),
    (4, 1004, 80, 72, 0, NOW(), NOW(), 0),
    (5, 1005, 150, 135, 0, NOW(), NOW(), 0),
    (6, 1006, 30, 27, 0, NOW(), NOW(), 0),
    (7, 1007, 60, 54, 0, NOW(), NOW(), 0),
    (8, 1008, 90, 81, 0, NOW(), NOW(), 0),
    (9, 1009, 45, 40, 0, NOW(), NOW(), 0),
    (10, 1010, 75, 67, 0, NOW(), NOW(), 0);