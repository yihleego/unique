create table `sequence`
(
    `seq_key`         varchar(127) primary key      not null comment 'key',
    `seq_value`       bigint unsigned default 0     not null comment 'value',
    `seq_increment`   int unsigned    default 1     not null comment 'increment',
    `seq_cache`       int unsigned    default 10000 not null comment 'cache size',
    `seq_version`     int unsigned    default 1     not null comment 'version',
    `seq_create_time` timestamp                     null comment 'create time',
    `seq_update_time` timestamp                     null comment 'update time'
) engine = InnoDB
  default charset = utf8mb4
  collate utf8mb4_unicode_ci
    comment 'sequence';
