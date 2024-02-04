create table  interface_info (
                                 `id` bigint not null auto_increment  primary key comment '主键',
                                 `name` varchar(256) not null comment '名称',
                                 `description` varchar(256) null comment '描述',
                                 `url` varchar(512) not null comment '接口地址',
                                 `requestHeader` text null comment '请求头',
                                 `responseHeader` text null comment '响应头',
                                 `status` int default 0 not null comment '接口状态(0-关闭,1-开启)',
                                 `method` varchar(256) not null comment '请求类型',
                                 `userId` bigint not null comment '创建人',
                                 `createTime` datetime default CURRENT_TIMESTAMP not null comment '创建时间',
                                 `updateTime` datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
                                 `isDelete` tinyint default 0 not null comment '是否删除(0-未删除,1-已删除)'
);
INSERT INTO interface_info (id, name, description, url, requestHeader, responseHeader, status, method, userId, createTime, updateTime, isDelete)
VALUES
    (1, '接口1', '描述1', 'http://example.com/api1', '请求头1', '响应头1', 1, 'GET', 1001, '2023-07-05 12:00:00', '2023-07-05 12:00:00', 0),
    (2, '接口2', '描述2', 'http://example.com/api2', '请求头2', '响应头2', 1, 'POST', 1002, '2023-07-05 12:05:00', '2023-07-05 12:05:00', 1),
    (3, '接口3', '描述3', 'http://example.com/api3', '请求头3', '响应头3', 0, 'PUT', 1003, '2023-07-05 12:10:00', '2023-07-05 12:10:00', 1),
    (4, '接口4', '描述4', 'http://example.com/api4', '请求头4', '响应头4', 1, 'DELETE', 1004, '2023-07-05 12:15:00', '2023-07-05 12:15:00', 1),
    (5, '接口5', '描述5', 'http://example.com/api5', '请求头5', '响应头5', 1, 'OPTIONS', 1005, '2023-07-05 12:20:00', '2023-07-05 12:20:00', 1),
    (6, '接口6', '描述6', 'http://example.com/api6', '请求头6', '响应头6', 1, 'HEAD', 1006, '2023-07-05 12:25:00', '2023-07-05 12:25:00', 1)
create table if not exists  `user_interface_info`
(
    `id` bigint not null auto_increment comment '主键',
    `userId` bigint not null comment '调用用户 id',
    `interfaceInfoId` bigint not null comment '接口id',
    `totalNum` int default 0 not null comment '总调用次数',
    'leftNum' int default 0 not null comment '剩余调用次数',
    `status` int default 0 not null comment '0-正常,1-禁用',
    `createTime` datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    `updateTime` datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    `isDelete` tinyint default 0 not null comment '是否删除(0-未删除,1-已删除)'
)