create table "sequence"
(
    "seq_key"         varchar(127) primary key not null,
    "seq_value"       bigint  default 0        not null,
    "seq_increment"   integer default 1        not null,
    "seq_cache"       integer default 5000     not null,
    "seq_version"     integer default 1        not null,
    "seq_create_time" timestamp                null,
    "seq_update_time" timestamp                null
);
comment on table "sequence" is 'sequence"';
comment on column "sequence"."seq_key" is 'key';
comment on column "sequence"."seq_value" is 'value';
comment on column "sequence"."seq_increment" is 'increment';
comment on column "sequence"."seq_cache" is 'cache size';
comment on column "sequence"."seq_version" is 'version';
comment on column "sequence"."seq_create_time" is 'create time';
comment on column "sequence"."seq_update_time" is 'update time';