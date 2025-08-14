create table `t_msg_record` (
                                   `id`                  bigint(20)       not null AUTO_INCREMENT comment 'ID',
                                   `msg_id`             varchar(256)      not null                comment '消息ID',
                                   `source_id`             varchar(256)      not null                comment '业务ID',
                                   `channel`                  int(10)   comment '推送渠道，1：邮件，2:短信',
                                   `subject`             varchar(256)      not null                comment '消息主题',
                                   `to`             varchar(256)      not null                comment '发给哪个用户',
                                   `template_id`             varchar(256)      not null                comment '模板ID',
                                   `template_data`             varchar(4096)      not null                comment '模板传入参数',
                                   `status`                  int(10)   comment '状态, 1: 等待中, 2: 成功, 3: 失败',
                                   `retry_count`                  int(10)   comment '重试次数',
                                   `create_time`         datetime     not null DEFAULT CURRENT_TIMESTAMP                             comment '创建时间',
                                   `modify_time`         datetime     not null DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '修改时间',
                                   PRIMARY KEY (`id`),
                                   UNIQUE KEY `idx_msgid` (`msg_id`)
)ENGINE=InnoDB  default CHARSET=utf8mb4 comment '消息记录表' ;


create table `t_msg_template` (
                                `id`                  bigint(20)       not null AUTO_INCREMENT comment 'ID',
                                `template_id`             varchar(256)      not null                comment '模板ID',
                                `rel_template_id`             varchar(256)      not null                comment '关联模板ID',
                                `name`             varchar(256)      not null                comment '模板名字',
                                `sign_name`             varchar(256)      comment '签名',
                                `source_id`             varchar(256)      not null                comment '业务ID',
                                `channel`                  int(10)   comment '推送渠道，1：邮件，2:短信',
                                `subject`             varchar(256)      not null                comment '消息主题',
                                `content`             varchar(4096)      not null                comment '消息文本模板',
                                `status`                  int(10)   comment '状态，未激活还是正常',
                                `create_time`         datetime     not null DEFAULT CURRENT_TIMESTAMP                             comment '创建时间',
                                `modify_time`         datetime     not null DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '修改时间',
                                PRIMARY KEY (`id`),
                                UNIQUE KEY `idx_msgid` (`template_id`)
)ENGINE=InnoDB  default CHARSET=utf8mb4 comment '消息模板表' ;


create table `t_msg_queue_low` (
                                `id`                  bigint(20)       not null AUTO_INCREMENT comment 'ID',
                                `msg_id`             varchar(256)      not null                comment '消息ID',
                                `to`             varchar(256)      not null                comment '发给哪个用户',
                                `subject`             varchar(256)      not null                comment '消息主题',
                                `priority`                  int(10)   comment '优先级',
                                `channel`                  int(10)   comment '推送渠道，1：邮件，2:短信',
                                `template_id`             varchar(256)      not null                comment '模板ID',
                                `template_data`             varchar(4096)      not null                comment '模板传入参数',
                                `status`                  int(10)   comment '状态',
                                `create_time`         datetime     not null DEFAULT CURRENT_TIMESTAMP                             comment '创建时间',
                                `modify_time`         datetime     not null DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '修改时间',
                                PRIMARY KEY (`id`),
                                UNIQUE KEY `idx_msgid` (`msg_id`)
)ENGINE=InnoDB  default CHARSET=utf8mb4 comment '消息队列表' ;

create table `t_msg_queue_middle` (
                                   `id`                  bigint(20)       not null AUTO_INCREMENT comment 'ID',
                                   `msg_id`             varchar(256)      not null                comment '消息ID',
                                   `to`             varchar(256)      not null                comment '发给哪个用户',
                                   `subject`             varchar(256)      not null                comment '消息主题',
                                   `priority`                  int(10)   comment '优先级',
                                   `channel`                  int(10)   comment '推送渠道，1：邮件，2:短信',
                                   `template_id`             varchar(256)      not null                comment '模板ID',
                                   `template_data`             varchar(4096)      not null                comment '模板传入参数',
                                   `status`                  int(10)   comment '状态',
                                   `create_time`         datetime     not null DEFAULT CURRENT_TIMESTAMP                             comment '创建时间',
                                   `modify_time`         datetime     not null DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '修改时间',
                                   PRIMARY KEY (`id`),
                                   UNIQUE KEY `idx_msgid` (`msg_id`)
)ENGINE=InnoDB  default CHARSET=utf8mb4 comment '消息队列表' ;

create table `t_msg_queue_high` (
                                      `id`                  bigint(20)       not null AUTO_INCREMENT comment 'ID',
                                      `msg_id`             varchar(256)      not null                comment '消息ID',
                                      `to`             varchar(256)      not null                comment '发给哪个用户',
                                      `subject`             varchar(256)      not null                comment '消息主题',
                                      `priority`                  int(10)   comment '优先级',
                                      `channel`                  int(10)   comment '推送渠道，1：邮件，2:短信',
                                      `template_id`             varchar(256)      not null                comment '模板ID',
                                      `template_data`             varchar(4096)      not null                comment '模板传入参数',
                                      `status`                  int(10)   comment '状态',
                                      `create_time`         datetime     not null DEFAULT CURRENT_TIMESTAMP                             comment '创建时间',
                                      `modify_time`         datetime     not null DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '修改时间',
                                      PRIMARY KEY (`id`),
                                      UNIQUE KEY `idx_msgid` (`msg_id`)
)ENGINE=InnoDB  default CHARSET=utf8mb4 comment '消息队列表' ;

create table `t_msg_queue_retry` (
                                     `id`                  bigint(20)       not null AUTO_INCREMENT comment 'ID',
                                     `msg_id`             varchar(256)      not null                comment '消息ID',
                                     `to`             varchar(256)      not null                comment '发给哪个用户',
                                     `subject`             varchar(256)      not null                comment '消息主题',
                                     `priority`                  int(10)   comment '优先级',
                                     `channel`                  int(10)   comment '推送渠道，1：邮件，2:短信',
                                     `template_id`             varchar(256)      not null                comment '模板ID',
                                     `template_data`             varchar(4096)      not null                comment '模板传入参数',
                                     `status`                  int(10)   comment '状态',
                                     `create_time`         datetime     not null DEFAULT CURRENT_TIMESTAMP                             comment '创建时间',
                                     `modify_time`         datetime     not null DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '修改时间',
                                     PRIMARY KEY (`id`),
                                     UNIQUE KEY `idx_msg_id` (`msg_id`)
)ENGINE=InnoDB  default CHARSET=utf8mb4 comment '消息队列表' ;

create table `t_msg_tmp_queue_timer` (
                                   `id`                  bigint(20)       not null AUTO_INCREMENT comment 'ID',
                                   `msg_id`              varchar(256)      not null                comment '消息ID',
                                   `req`                 varchar(4096)      not null                comment '消息ID',
                                   `send_timestamp`      bigint(10)   comment '定时发送时间',
                                   `status`              int(10)      comment '状态',
                                   `create_time`         datetime     not null DEFAULT CURRENT_TIMESTAMP                             comment '创建时间',
                                   `modify_time`         datetime     not null DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '修改时间',
                                   PRIMARY KEY (`id`),
                                   UNIQUE KEY `idx_msgid` (`msg_id`),
                                   INDEX `idx_send_timestamp_status` (`send_timestamp`,`status`)
)ENGINE=InnoDB  default CHARSET=utf8mb4 comment '定时消息队列表' ;


create table `t_global_quota` (
                           `id`                  bigint(20)       not null AUTO_INCREMENT comment 'ID',
                           `num`                 int(10)      not null                comment '限额',
                           `unit`                 int(10)      not null                comment '限频单位，单位毫秒',
                           `channel`                 int(10)      not null                comment '推送渠道，1：邮件，2:短信',
                           `create_time`         datetime     not null DEFAULT CURRENT_TIMESTAMP                             comment '创建时间',
                           `modify_time`         datetime     not null DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '修改时间',
                           PRIMARY KEY (`id`),
                           UNIQUE KEY `idx_channel` (`channel`)
)ENGINE=InnoDB  default CHARSET=utf8mb4 comment '全局限额表' ;

create table `t_source_quota` (
                                `id`                  bigint(20)       not null AUTO_INCREMENT comment 'ID',
                                `source_id`           varchar(256)       not null comment '渠道ID',
                                `num`                 int(10)      not null                comment '限额',
                                `unit`                 int(10)      not null                comment '限频单位，单位毫秒',
                                `channel`                 int(10)      not null                comment '推送渠道，1：邮件，2:短信',
                                `create_time`         datetime     not null DEFAULT CURRENT_TIMESTAMP                             comment '创建时间',
                                `modify_time`         datetime     not null DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '修改时间',
                                PRIMARY KEY (`id`),
                                KEY `idx_sourceid_channel` (`source_id`, `channel`)
)ENGINE=InnoDB  default CHARSET=utf8mb4 comment '渠道限额表' ;

insert t_global_quota (num, unit, channel) values (1, 1000, 1);
insert t_global_quota (num, unit, channel) values (1, 1000, 2);
insert t_global_quota (num, unit, channel) values (1, 1000, 3);