create table if not exists ms_article
(
    id
    bigint
    auto_increment
    primary
    key,
    comment_counts
    int
    null
    comment
    'comment count',
    create_date
    bigint
    null
    comment
    'create datetime',
    summary
    varchar
(
    255
) null comment 'description',
    title varchar
(
    64
) null comment 'title',
    view_counts int null comment 'view count',
    weight int not null comment 'set on top',
    author_id bigint null comment 'author id',
    body_id bigint null comment 'article body id',
    category_id int null comment 'category id',
    deleted bit default b'0' not null comment 'logical delete'
    )
    charset = utf8mb3;

create table if not exists ms_article_body
(
    id
    bigint
    auto_increment
    primary
    key,
    content
    longtext
    null,
    content_html
    longtext
    null,
    article_id
    bigint
    not
    null
)
    charset = utf8mb3;

create index article_id
    on ms_article_body (article_id);

create table if not exists ms_article_tag
(
    id
    bigint
    auto_increment
    primary
    key,
    article_id
    bigint
    not
    null,
    tag_id
    bigint
    not
    null
)
    charset = utf8mb3;

create index article_id
    on ms_article_tag (article_id);

create index tag_id
    on ms_article_tag (tag_id);

create table if not exists ms_category
(
    id
    bigint
    auto_increment
    primary
    key,
    avatar
    varchar
(
    255
) collate utf8mb4_unicode_ci null,
    category_name varchar
(
    255
) collate utf8mb4_unicode_ci null,
    description varchar
(
    255
) collate utf8mb4_unicode_ci null
    )
    charset = utf8mb3;

create table if not exists ms_comment
(
    id
    bigint
    auto_increment
    primary
    key,
    content
    varchar
(
    255
) collate utf8mb4_unicode_ci not null,
    create_date bigint not null,
    article_id int not null,
    author_id bigint not null,
    parent_id bigint null,
    to_uid bigint not null,
    level int not null,
    deleted bit default b'0' not null comment 'logical delete'
    )
    charset = utf8mb3;

create index article_id
    on ms_comment (article_id);

create table if not exists ms_sys_log
(
    id
    bigint
    auto_increment
    primary
    key,
    create_date
    bigint
    null,
    ip
    varchar
(
    15
) collate utf8mb3_bin null,
    method varchar
(
    100
) collate utf8mb3_bin null,
    module varchar
(
    10
) collate utf8mb3_bin null,
    nickname varchar
(
    10
) collate utf8mb4_unicode_ci null,
    operation varchar
(
    25
) collate utf8mb3_bin null,
    params varchar
(
    255
) collate utf8mb3_bin null,
    time bigint null,
    userid bigint null
    )
    collate = utf8mb3_unicode_ci;

create table if not exists ms_sys_user
(
    id
    bigint
    auto_increment
    primary
    key,
    account
    varchar
(
    64
) null comment 'account',
    admin bit null comment 'admin',
    avatar varchar
(
    255
) null comment 'avatar',
    create_date bigint null comment 'register datetime',
    deleted bit null comment 'logical delete',
    email varchar
(
    128
) null comment 'email',
    last_login bigint null comment 'last login datetime',
    nickname varchar
(
    255
) null comment 'display name',
    password varchar
(
    64
) null comment 'password',
    status varchar
(
    255
) null comment 'status'
    )
    charset = utf8mb3;

create table if not exists ms_tag
(
    id
    bigint
    auto_increment
    primary
    key,
    avatar
    varchar
(
    255
) collate utf8mb4_unicode_ci null,
    tag_name varchar
(
    255
) collate utf8mb4_unicode_ci null
    )
    charset = utf8mb3;

