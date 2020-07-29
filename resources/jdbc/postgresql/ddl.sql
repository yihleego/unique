create table "sequence"
(
    "seq_key"       varchar(127) primary key not null,
    "seq_value"     bigint default 0         not null,
    "seq_increment" int    default 1         not null,
    "seq_cache"     int    default 10000     not null,
    "seq_version"   int    default 1         not null
);
comment on table "sequence" is 'sequence"';
comment on column "sequence"."seq_key" is 'key';
comment on column "sequence"."seq_value" is 'value';
comment on column "sequence"."seq_increment" is 'increment';
comment on column "sequence"."seq_cache" is 'cache size';
comment on column "sequence"."seq_version" is 'version';