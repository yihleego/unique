create table `sequence`
(
    `seq_key`       varchar(127) primary key not null comment 'key',
    `seq_value`     bigint(20) default 0     not null comment 'value',
    `seq_increment` int(11)    default 1     not null comment 'increment',
    `seq_cache`     int(11)    default 10000 not null comment 'cache size',
    `seq_version`   int(11)    default 1     not null comment 'version'
) engine = InnoDB
  default charset = utf8mb4
  collate utf8mb4_unicode_ci
    comment 'sequence';
